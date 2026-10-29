// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3

package net.serverpackswitcher.test;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.server.Bootstrap;
import net.serverpackswitcher.ServerPackSwitcherMod;
import net.serverpackswitcher.config.PackConfig;
import net.serverpackswitcher.config.PlayerPreferences;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 4: GameTest State Machine Simulation & In-Engine Protocol Suite (1.20.1).
 * Simulates the lifecycle transitions of player join handlers, single-pack resource pack
 * networking packets (ClientboundResourcePackPacket), dynamic pack swap sequences, and preference state mutations.
 */
@DisplayName("Pillar 4: GameTest State Machine Simulation & In-Engine Protocol Suite (1.20.1)")
public class ServerPackSwitcherGameTest {

    @TempDir
    Path tempDir;

    private Path customConfigPath;
    private Path customPrefsPath;
    private Map<String, PackConfig.PackEntry> originalPacks;

    @BeforeAll
    static void initMinecraft() {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    @BeforeEach
    void setUp() {
        customConfigPath = tempDir.resolve("serverpackswitcher.json");
        customPrefsPath = tempDir.resolve("serverpackswitcher_preferences.json");

        PackConfig.setCustomConfigPath(customConfigPath);
        PlayerPreferences.setCustomPrefsPath(customPrefsPath);

        originalPacks = new HashMap<>(ServerPackSwitcherMod.CONFIG.getPacks());
        ServerPackSwitcherMod.CONFIG.getPacks().clear();
        ServerPackSwitcherMod.PREFERENCES.load();
    }

    @AfterEach
    void tearDown() {
        ServerPackSwitcherMod.CONFIG.getPacks().clear();
        ServerPackSwitcherMod.CONFIG.getPacks().putAll(originalPacks);

        PackConfig.setCustomConfigPath(null);
        PlayerPreferences.setCustomPrefsPath(null);
    }

    /**
     * Simulated protocol session modeling ServerPlayConnectionEvents and command handlers.
     */
    static class SimulatedProtocolSession {
        private final UUID playerUuid;
        private final PackConfig config;
        private final PlayerPreferences preferences;
        private final List<Object> sentPackets = new ArrayList<>();

        public SimulatedProtocolSession(UUID playerUuid, PackConfig config, PlayerPreferences preferences) {
            this.playerUuid = playerUuid;
            this.config = config;
            this.preferences = preferences;
        }

        public List<Object> getSentPackets() {
            return sentPackets;
        }

        public void clearPackets() {
            sentPackets.clear();
        }

        /**
         * Simulates the ServerPlayConnectionEvents.JOIN handler logic in ServerPackSwitcherMod.
         */
        public void simulateJoin() {
            String preferredPackName = preferences.getPreference(playerUuid);
            if (preferredPackName != null) {
                PackConfig.PackEntry pack = config.getPacks().get(preferredPackName);
                if (pack != null && pack.getUrl() != null && !pack.getUrl().isEmpty()) {
                    Component prompt = pack.getPrompt() != null && !pack.getPrompt().isEmpty()
                            ? Component.literal(pack.getPrompt()) : null;
                    ClientboundResourcePackPacket packet = new ClientboundResourcePackPacket(
                            pack.getUrl(),
                            pack.getHash() != null ? pack.getHash() : "",
                            pack.isRequired(),
                            prompt
                    );
                    sentPackets.add(packet);
                }
            }
        }

        /**
         * Simulates the /pack apply <packName> command execution logic in ServerPackSwitcherMod.
         */
        public boolean simulateApply(String packNameInput) {
            final String packName = packNameInput.toLowerCase();
            PackConfig.PackEntry pack = config.getPacks().get(packName);
            if (pack == null) {
                return false;
            }

            if (pack.getUrl() == null || pack.getUrl().isEmpty()) {
                return false;
            }

            // In 1.20.1, sending ClientboundResourcePackPacket directly applies the pack.
            Component prompt = pack.getPrompt() != null && !pack.getPrompt().isEmpty()
                    ? Component.literal(pack.getPrompt()) : null;
            ClientboundResourcePackPacket packet = new ClientboundResourcePackPacket(
                    pack.getUrl(),
                    pack.getHash() != null ? pack.getHash() : "",
                    pack.isRequired(),
                    prompt
            );
            sentPackets.add(packet);

            preferences.setPreference(playerUuid, packName);
            return true;
        }

        /**
         * Simulates the /pack pop or /pack clear command execution logic in ServerPackSwitcherMod.
         */
        public boolean simulatePop() {
            String currentPackName = preferences.getPreference(playerUuid);
            if (currentPackName == null) {
                return false;
            }

            preferences.setPreference(playerUuid, null);
            return true;
        }
    }

    @Test
    @DisplayName("a) testPlayerJoinStateWithPreference - Verifies automatic packet construction on join")
    void testPlayerJoinStateWithPreference() {
        UUID packId = UUID.randomUUID();
        String packUrl = "https://cdn.example.org/packs/faithful_x64.zip";
        String packHash = "0123456789abcdef0123456789abcdef01234567";
        boolean required = true;
        String promptText = "Please download the server resource pack.";

        PackConfig.PackEntry entry = new PackConfig.PackEntry(packId, packUrl, packHash, required, promptText, null);
        ServerPackSwitcherMod.CONFIG.getPacks().put("faithful", entry);

        UUID playerUuid = UUID.randomUUID();
        ServerPackSwitcherMod.PREFERENCES.setPreference(playerUuid, "faithful");

        SimulatedProtocolSession session = new SimulatedProtocolSession(
                playerUuid,
                ServerPackSwitcherMod.CONFIG,
                ServerPackSwitcherMod.PREFERENCES
        );

        session.simulateJoin();

        assertEquals(1, session.getSentPackets().size(), "Exactly one resource pack packet must be sent on join with preference");
        Object packetObj = session.getSentPackets().get(0);
        assertInstanceOf(ClientboundResourcePackPacket.class, packetObj);

        ClientboundResourcePackPacket packet = (ClientboundResourcePackPacket) packetObj;
        assertEquals(packUrl, packet.getUrl(), "Packet url must match pack entry URL");
        assertEquals(packHash, packet.getHash(), "Packet hash must match pack entry hash");
        assertEquals(required, packet.isRequired(), "Packet required flag must match pack entry");
        assertNotNull(packet.getPrompt(), "Packet prompt must not be null when configured");
        assertEquals(promptText, packet.getPrompt().getString(), "Packet prompt text must match");
    }

    @Test
    @DisplayName("b) testPlayerJoinStateWithoutPreference - Verifies no packet is sent when player has no preference")
    void testPlayerJoinStateWithoutPreference() {
        UUID packId = UUID.randomUUID();
        PackConfig.PackEntry entry = new PackConfig.PackEntry(packId, "https://example.com/pack.zip", "abc", false, null, null);
        ServerPackSwitcherMod.CONFIG.getPacks().put("defaultpack", entry);

        UUID playerUuid = UUID.randomUUID();
        assertNull(ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid), "Initial player preference must be null");

        SimulatedProtocolSession session = new SimulatedProtocolSession(
                playerUuid,
                ServerPackSwitcherMod.CONFIG,
                ServerPackSwitcherMod.PREFERENCES
        );

        session.simulateJoin();

        assertTrue(session.getSentPackets().isEmpty(), "No packet should be constructed or sent when preference is null");
    }

    @Test
    @DisplayName("c) testPackSwapStateSequence - Verifies single-pack swap sequence and preference update")
    void testPackSwapStateSequence() {
        UUID packAId = UUID.randomUUID();
        PackConfig.PackEntry packA = new PackConfig.PackEntry(packAId, "https://example.com/packA.zip", "hashA", false, "Prompt A", null);
        ServerPackSwitcherMod.CONFIG.getPacks().put("packa", packA);

        UUID packBId = UUID.randomUUID();
        PackConfig.PackEntry packB = new PackConfig.PackEntry(packBId, "https://example.com/packB.zip", "hashB", true, "Prompt B", null);
        ServerPackSwitcherMod.CONFIG.getPacks().put("packb", packB);

        UUID playerUuid = UUID.randomUUID();
        ServerPackSwitcherMod.PREFERENCES.setPreference(playerUuid, "packa");
        assertEquals("packa", ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid));

        SimulatedProtocolSession session = new SimulatedProtocolSession(
                playerUuid,
                ServerPackSwitcherMod.CONFIG,
                ServerPackSwitcherMod.PREFERENCES
        );

        boolean applied = session.simulateApply("packB");
        assertTrue(applied, "Applying packB should succeed");

        List<Object> packets = session.getSentPackets();
        assertEquals(1, packets.size(), "1.20.1 swap sequence sends 1 packet to apply the new pack");

        assertInstanceOf(ClientboundResourcePackPacket.class, packets.get(0));
        ClientboundResourcePackPacket pushPacket = (ClientboundResourcePackPacket) packets.get(0);
        assertEquals("https://example.com/packB.zip", pushPacket.getUrl());
        assertEquals("hashB", pushPacket.getHash());
        assertTrue(pushPacket.isRequired());
        assertNotNull(pushPacket.getPrompt());
        assertEquals("Prompt B", pushPacket.getPrompt().getString());

        // Preference updated
        assertEquals("packb", ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid), "Preference must be updated to packB");
    }

    @Test
    @DisplayName("d) testPackPopLifecycle - Verifies pack pop lifecycle clears preference to null")
    void testPackPopLifecycle() {
        UUID packAId = UUID.randomUUID();
        PackConfig.PackEntry packA = new PackConfig.PackEntry(packAId, "https://example.com/packA.zip", "hashA", false, "Prompt A", null);
        ServerPackSwitcherMod.CONFIG.getPacks().put("packa", packA);

        UUID playerUuid = UUID.randomUUID();
        ServerPackSwitcherMod.PREFERENCES.setPreference(playerUuid, "packa");
        assertEquals("packa", ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid));

        SimulatedProtocolSession session = new SimulatedProtocolSession(
                playerUuid,
                ServerPackSwitcherMod.CONFIG,
                ServerPackSwitcherMod.PREFERENCES
        );

        boolean popped = session.simulatePop();
        assertTrue(popped, "Pop command simulation must return true when active pack exists");

        assertNull(ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid), "Player preference must become null after pop");
    }

    @Test
    @DisplayName("e) testUnknownPackHandling - Verifies unconfigured pack does not push packets and does not mutate preference")
    void testUnknownPackHandling() {
        UUID packAId = UUID.randomUUID();
        PackConfig.PackEntry packA = new PackConfig.PackEntry(packAId, "https://example.com/packA.zip", "hashA", false, "Prompt A", null);
        ServerPackSwitcherMod.CONFIG.getPacks().put("packa", packA);

        UUID playerUuid = UUID.randomUUID();
        ServerPackSwitcherMod.PREFERENCES.setPreference(playerUuid, "packa");
        assertEquals("packa", ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid));

        SimulatedProtocolSession session = new SimulatedProtocolSession(
                playerUuid,
                ServerPackSwitcherMod.CONFIG,
                ServerPackSwitcherMod.PREFERENCES
        );

        boolean applied = session.simulateApply("ghost_pack_that_does_not_exist");
        assertFalse(applied, "Applying unknown pack must return false");
        assertTrue(session.getSentPackets().isEmpty(), "No network packets must be constructed or sent for unknown pack");
        assertEquals("packa", ServerPackSwitcherMod.PREFERENCES.getPreference(playerUuid), "Existing player preference must remain unchanged");

        // Test with player having no initial preference
        UUID freshUuid = UUID.randomUUID();
        SimulatedProtocolSession freshSession = new SimulatedProtocolSession(
                freshUuid,
                ServerPackSwitcherMod.CONFIG,
                ServerPackSwitcherMod.PREFERENCES
        );

        boolean freshApplied = freshSession.simulateApply("unknown_pack");
        assertFalse(freshApplied, "Applying unknown pack to unconfigured player must return false");
        assertTrue(freshSession.getSentPackets().isEmpty(), "No network packets must be constructed or sent");
        assertNull(ServerPackSwitcherMod.PREFERENCES.getPreference(freshUuid), "Preference must remain null");
    }
}

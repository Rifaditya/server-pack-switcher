// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3

package net.serverpackswitcher.test;

import net.serverpackswitcher.config.PackConfig;
import net.serverpackswitcher.config.PlayerPreferences;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 1: Chaos Property & Config Fuzzing Suite for Server Pack Switcher.
 * Executes extensive property-based and adversarial chaos fuzzing against PackConfig
 * and PlayerPreferences to verify crash-resilience, deterministic UUID generation,
 * and lossless persistence across edge cases.
 */
@DisplayName("Pillar 1: Chaos Property & Config Fuzzing Suite")
public class ServerPackSwitcherStressFuzzTest {

    @TempDir
    Path tempDir;

    private Path customConfigPath;
    private Path customPrefsPath;

    @BeforeEach
    void setUp() {
        customConfigPath = tempDir.resolve("serverpackswitcher.json");
        customPrefsPath = tempDir.resolve("serverpackswitcher_preferences.json");
        PackConfig.setCustomConfigPath(customConfigPath);
        PlayerPreferences.setCustomPrefsPath(customPrefsPath);
    }

    @AfterEach
    void tearDown() {
        PackConfig.setCustomConfigPath(null);
        PlayerPreferences.setCustomPrefsPath(null);
    }

    @Test
    @DisplayName("a) testChaosJsonPayloadFuzzing - 20,000 iterations of adversarial JSON / corrupted byte payloads")
    void testChaosJsonPayloadFuzzing() {
        final int iterations = 20_000;
        final Random random = new Random(0xDA51C);

        // Mute console error output during adversarial fuzzing to avoid console I/O bottlenecks
        PrintStream originalErr = System.err;
        try {
            Configurator.setLevel("serverpackswitcher", Level.OFF);
            System.setErr(new PrintStream(OutputStream.nullOutputStream()));

            for (int i = 0; i < iterations; i++) {
                byte[] payload = generateChaosPayload(i, random);

                // Fuzz PackConfig
                assertDoesNotThrow(() -> {
                    Files.write(customConfigPath, payload,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.WRITE);
                    PackConfig config = new PackConfig();
                    config.load();
                    assertNotNull(config.getPacks(), "PackConfig packs map must not be null after load");
                }, "PackConfig.load() threw unhandled exception on iteration " + i);

                // Fuzz PlayerPreferences
                assertDoesNotThrow(() -> {
                    Files.write(customPrefsPath, payload,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.TRUNCATE_EXISTING,
                            StandardOpenOption.WRITE);
                    PlayerPreferences prefs = new PlayerPreferences();
                    prefs.load();
                }, "PlayerPreferences.load() threw unhandled exception on iteration " + i);
            }
        } finally {
            System.setErr(originalErr);
            Configurator.setLevel("serverpackswitcher", Level.INFO);
        }
    }

    @Test
    @DisplayName("b) testPackEntryDeterministicUuidFuzzing - 10,000 iterations testing deterministic UUID computation")
    void testPackEntryDeterministicUuidFuzzing() {
        final int iterations = 10_000;
        final Random random = new Random(0xDA51C + 1);

        String[] urlProtocols = {"https://", "http://", "ftp://", ""};
        String[] sampleDomains = {
                "example.com", "cdn.modrinth.com", "resourcepacks.org",
                "서버팩.한국", "리소스.рф", "example.org/nested/path", "localhost:8080"
        };
        String[] unicodeSegments = {
                "plastic_pack", "текстуры_1.21", "リソースパック_HD", "🎮_pack_🔥",
                "custom-build#1", "spaces in name", "url_with_special!$&'()*+,;="
        };

        for (int i = 0; i < iterations; i++) {
            String url;
            int urlType = random.nextInt(6);
            if (urlType == 0) {
                url = null;
            } else if (urlType == 1) {
                url = "";
            } else if (urlType == 2) {
                url = "   ";
            } else {
                String protocol = urlProtocols[random.nextInt(urlProtocols.length)];
                String domain = sampleDomains[random.nextInt(sampleDomains.length)];
                String segment = unicodeSegments[random.nextInt(unicodeSegments.length)];
                url = protocol + domain + "/" + segment + "_" + i + ".zip";
            }

            String hash = (random.nextBoolean()) ? UUID.randomUUID().toString().replace("-", "") : null;
            String prompt = (random.nextBoolean()) ? "Apply pack " + i + " \uD83D\uDE80?" : null;
            boolean required = random.nextBoolean();
            String modrinth = (random.nextInt(4) == 0) ? "slug-" + i : "";

            PackConfig.PackEntry entry = new PackConfig.PackEntry(null, url, hash, required, prompt, modrinth);

            // 1. Assert getId() never throws NPE
            UUID computedId1 = assertDoesNotThrow(entry::getId,
                    "PackEntry.getId() threw exception for url: " + url);

            // 2. Assert getId() is idempotent
            UUID computedId2 = entry.getId();
            assertEquals(computedId1, computedId2, "PackEntry.getId() must be idempotent");

            // 3. Assert deterministic calculation against UUID.nameUUIDFromBytes(url.getBytes(UTF_8))
            if (url == null || url.isEmpty()) {
                assertNull(computedId1,
                        "When url is null or empty and id is null, getId() must return null without NPE");
            } else {
                UUID expected = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8));
                assertEquals(expected, computedId1,
                        "PackEntry.getId() must deterministically match UUID.nameUUIDFromBytes(url UTF-8 bytes)");
            }

            // 4. Test explicit ID preservation
            UUID explicitId = UUID.randomUUID();
            entry.setId(explicitId);
            assertEquals(explicitId, entry.getId(), "PackEntry.getId() must return explicit ID when set");
        }
    }

    @Test
    @DisplayName("c) testPlayerPreferencesRapidChaosFuzzing - 10,000 iterations of rapid preference mutations")
    void testPlayerPreferencesRapidChaosFuzzing() {
        final int iterations = 10_000;
        final Random random = new Random(0xDA51C + 2);

        // Maintain a pool of 64 persistent player UUIDs to stress repeated mutations & unsettings
        List<UUID> playerPool = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            playerPool.add(UUID.randomUUID());
        }

        String[] unicodePackNames = {
                "Plastic", "BAREBONES", "Faithful32x", "リソースパック_日本語",
                "Текстуры_HD", "🎮GamerPack🔥", "Pack with Spaces & (Symbols)!",
                "Élégance_Moderne", "🌟Starlight#42", "\uD83D\uDE00_Emoji_Pack_\uD83D\uDE80",
                "null_string_literal", "a", "Z".repeat(128)
        };

        PlayerPreferences prefs = new PlayerPreferences();
        prefs.load();

        Map<UUID, String> expectedState = new HashMap<>();

        for (int i = 0; i < iterations; i++) {
            UUID targetPlayer;
            if (random.nextInt(10) < 8) {
                targetPlayer = playerPool.get(random.nextInt(playerPool.size()));
            } else {
                targetPlayer = UUID.randomUUID();
                playerPool.add(targetPlayer);
            }

            String chosenPack;
            int actionType = random.nextInt(4);
            if (actionType == 0) {
                // 25% chance: Unset preference
                chosenPack = null;
            } else {
                chosenPack = unicodePackNames[random.nextInt(unicodePackNames.length)];
            }

            prefs.setPreference(targetPlayer, chosenPack);

            if (chosenPack == null) {
                expectedState.remove(targetPlayer);
                assertNull(prefs.getPreference(targetPlayer),
                        "Preference must be null after setting null (unsetting) on iteration " + i);
            } else {
                String expectedStored = chosenPack.toLowerCase();
                expectedState.put(targetPlayer, expectedStored);
                assertEquals(expectedStored, prefs.getPreference(targetPlayer),
                        "Preference must match lowercased pack name on iteration " + i);
            }

            // Verify persistence integrity across round-trip save and load every 500 iterations and on final iteration
            if (i % 500 == 0 || i == iterations - 1) {
                PlayerPreferences freshLoad = new PlayerPreferences();
                freshLoad.load();

                for (Map.Entry<UUID, String> entry : expectedState.entrySet()) {
                    assertEquals(entry.getValue(), freshLoad.getPreference(entry.getKey()),
                            "Persisted preference mismatch for player " + entry.getKey() + " at checkpoint " + i);
                }
            }
        }
    }

    /**
     * Generates diverse adversarial, corrupted, and edge-case byte payloads.
     */
    private byte[] generateChaosPayload(int index, Random random) {
        int strategy = index % 12;
        switch (strategy) {
            case 0:
                // Completely empty payload
                return new byte[0];

            case 1:
                // Pure random byte corruption of variable length
                byte[] raw = new byte[random.nextInt(256)];
                random.nextBytes(raw);
                return raw;

            case 2:
                // Corrupted UTF-8 leading / orphan bytes
                byte[][] badUtf8 = {
                        {(byte) 0x80},
                        {(byte) 0xC3},
                        {(byte) 0xE0, (byte) 0xA0},
                        {(byte) 0xF0, (byte) 0x90, (byte) 0x80},
                        {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x01},
                        {(byte) 0xC0, (byte) 0x80} // Overlong NUL
                };
                return badUtf8[random.nextInt(badUtf8.length)];

            case 3:
                // Truncated JSON tokens
                String[] truncated = {
                        "{\"packs\":",
                        "{\"packs\": {",
                        "{\"packs\": {\"test\":",
                        "{\"packs\": {\"test\": {\"url\": \"http",
                        "{\"packs\": [",
                        "{\"packs\": {\"test\": 12,",
                        "{\"preferences\": {\"",
                        "{",
                        "[",
                        "{\"key\": \"unclosed string"
                };
                return truncated[random.nextInt(truncated.length)].getBytes(StandardCharsets.UTF_8);

            case 4:
                // Structural syntax violations
                String[] syntaxViolations = {
                        "{,}",
                        "{:}",
                        "{{{{}}}}",
                        "[[[[]]]]",
                        "{\"packs\": ::::}",
                        "{\"packs\": {\"a\": 1,, \"b\": 2}}",
                        "{\"packs\": { 1234: \"numeric key\" }}",
                        "/* C-style comments only */",
                        "<xml><packs></packs></xml>",
                        "YAML: \n  packs:\n    true"
                };
                return syntaxViolations[random.nextInt(syntaxViolations.length)].getBytes(StandardCharsets.UTF_8);

            case 5:
                // Type mismatch payloads (primitives / arrays instead of JSON objects)
                String[] typeMismatches = {
                        "[]",
                        "[1, 2, 3, 4]",
                        "[\"a\", \"b\", \"c\"]",
                        "\"plain string instead of json object\"",
                        "123456789",
                        "-9999999999999999999999999999999999999999999999999999999999999",
                        "true",
                        "false",
                        "null",
                        "{\"packs\": null}",
                        "{\"packs\": 1234}",
                        "{\"packs\": \"not-a-map\"}",
                        "{\"packs\": []}",
                        "{\"packs\": [\"item1\", \"item2\"]}"
                };
                return typeMismatches[random.nextInt(typeMismatches.length)].getBytes(StandardCharsets.UTF_8);

            case 6:
                // Unexpected PackEntry / Preference structures
                String[] invalidStructures = {
                        "{\"packs\": {\"bad\": null}}",
                        "{\"packs\": {\"bad\": 42}}",
                        "{\"packs\": {\"bad\": true}}",
                        "{\"packs\": {\"bad\": []}}",
                        "{\"packs\": {\"bad\": \"string\"}}",
                        "{\"packs\": {\"bad\": {\"id\": 12345, \"url\": false, \"required\": \"not_boolean\"}}}",
                        "{\"packs\": {\"bad\": {\"id\": \"not-a-uuid\", \"url\": 999}}}",
                        "{\"not-a-uuid-key\": \"plastic\"}",
                        "{\"00000000-0000-0000-0000-000000000000\": 12345}",
                        "{\"00000000-0000-0000-0000-000000000000\": []}",
                        "{\"00000000-0000-0000-0000-000000000000\": null}"
                };
                return invalidStructures[random.nextInt(invalidStructures.length)].getBytes(StandardCharsets.UTF_8);

            case 7:
                // Extreme numeric & floating point payloads
                String[] numericPayloads = {
                        "{\"packs\": 1e999999}",
                        "{\"packs\": -1e999999}",
                        "{\"packs\": NaN}",
                        "{\"packs\": Infinity}",
                        "{\"packs\": -Infinity}",
                        "{\"packs\": 0000123}",
                        "{\"packs\": 0.000000000000000000000000000000000000001}"
                };
                return numericPayloads[random.nextInt(numericPayloads.length)].getBytes(StandardCharsets.UTF_8);

            case 8:
                // NUL bytes and control characters
                return new byte[]{
                        0x00, 0x00, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04,
                        0x1B, 0x7F, 0x0A, 0x0D
                };

            case 9:
                // Whitespace only
                String[] whitespaces = {" ", "  ", "\t", "\n", "\r\n", "   \t\r\n   "};
                return whitespaces[random.nextInt(whitespaces.length)].getBytes(StandardCharsets.UTF_8);

            case 10:
                // Deeply nested objects / arrays
                StringBuilder nested = new StringBuilder();
                for (int d = 0; d < 50; d++) {
                    nested.append("{\"d\":");
                }
                nested.append("1");
                for (int d = 0; d < 50; d++) {
                    nested.append("}");
                }
                return nested.toString().getBytes(StandardCharsets.UTF_8);

            case 11:
            default:
                // Adversarial unicode & injection edge cases
                String[] injectionPayloads = {
                        "{\"packs\": {\"'; DROP TABLE packs; --\": {\"url\": \"http://example.com\"}}}",
                        "{\"packs\": {\"<script>alert(1)</script>\": {\"url\": \"javascript:alert(1)\"}}}",
                        "{\"packs\": {\"👾🔥🚀\": {\"prompt\": \"\uD83D\uDE00\uFFFF\u0000\"}}}",
                        "{\"\uD83D\uDE80\": \"\uD83D\uDE80\"}"
                };
                return injectionPayloads[random.nextInt(injectionPayloads.length)].getBytes(StandardCharsets.UTF_8);
        }
    }
}

package net.serverpackswitcher;

// Verified against: ModInitializer.java (Fabric API)
// Verified against: CommandRegistrationCallback.java (Fabric API)
// Verified against: ServerPlayConnectionEvents.java (Fabric API)
// Verified against: ServerPlayer.java (26.2 Release)
// Verified against: ServerPackCommand.java (26.2 Release)

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.server.level.ServerPlayer;
import net.serverpackswitcher.config.PackConfig;
import net.serverpackswitcher.config.PlayerPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ServerPackSwitcherMod implements ModInitializer {
    public static final String MOD_ID = "serverpackswitcher";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final PackConfig CONFIG = new PackConfig();
    public static final PlayerPreferences PREFERENCES = new PlayerPreferences();

    private static final SuggestionProvider<CommandSourceStack> PACK_SUGGESTIONS = (context, builder) -> {
        return SharedSuggestionProvider.suggest(CONFIG.getPacks().keySet(), builder);
    };

    @Override
    public void onInitialize() {
        LOGGER.info("Server Pack Switcher Initializing...");

        // Load configs
        CONFIG.load();
        PREFERENCES.load();

        // Register commands
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(dispatcher);
        });

        // Register join handler
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            ServerPlayer player = listener.player;
            if (player != null) {
                String preferredPackName = PREFERENCES.getPreference(player.getUUID());
                if (preferredPackName != null) {
                    PackConfig.PackEntry pack = CONFIG.getPacks().get(preferredPackName);
                    if (pack != null) {
                        Component prompt = pack.prompt() != null && !pack.prompt().isEmpty() ? Component.literal(pack.prompt()) : null;
                        ClientboundResourcePackPushPacket pushPacket = new ClientboundResourcePackPushPacket(
                            pack.id(),
                            pack.url(),
                            pack.hash(),
                            pack.required(),
                            Optional.ofNullable(prompt)
                        );
                        listener.send(pushPacket);
                        LOGGER.info("Automatically sent preferred pack '{}' to player {}", preferredPackName, player.getScoreboardName());
                    }
                }
            }
        });

        LOGGER.info("Server Pack Switcher Initialized successfully!");
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pack")
            .then(Commands.literal("list")
                .executes(context -> listPacks(context.getSource()))
            )
            .then(Commands.literal("pop")
                .executes(context -> popPackSelf(context.getSource()))
                .then(Commands.argument("targets", EntityArgument.players())
                    .requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
                    .executes(context -> popPackMultiple(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                )
            )
            .then(Commands.literal("clear")
                .executes(context -> popPackSelf(context.getSource()))
                .then(Commands.argument("targets", EntityArgument.players())
                    .requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
                    .executes(context -> popPackMultiple(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                )
            )
            .then(Commands.literal("reload")
                .requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
                .executes(context -> reloadConfig(context.getSource()))
            )
            .then(Commands.literal("apply")
                .then(Commands.argument("pack_name", StringArgumentType.word())
                    .suggests(PACK_SUGGESTIONS)
                    .executes(context -> applyPackSelf(context.getSource(), StringArgumentType.getString(context, "pack_name")))
                    .then(Commands.argument("targets", EntityArgument.players())
                        .requires(source -> Commands.LEVEL_GAMEMASTERS.check(source.permissions()))
                        .executes(context -> applyPackMultiple(context.getSource(), EntityArgument.getPlayers(context, "targets"), StringArgumentType.getString(context, "pack_name")))
                    )
                )
            )
            // Legacy / direct syntax: /pack <pack_name>
            .then(Commands.argument("pack_name", StringArgumentType.word())
                .suggests(PACK_SUGGESTIONS)
                .executes(context -> applyPackSelf(context.getSource(), StringArgumentType.getString(context, "pack_name")))
            )
        );
    }

    private static int applyPack(CommandSourceStack source, ServerPlayer player, String packNameInput) {
        final String packName = packNameInput.toLowerCase();
        PackConfig.PackEntry pack = CONFIG.getPacks().get(packName);
        if (pack == null) {
            source.sendFailure(Component.literal("Unknown resource pack: " + packName));
            return 0;
        }

        // Pop current pack if they have one
        String currentPackName = PREFERENCES.getPreference(player.getUUID());
        if (currentPackName != null) {
            PackConfig.PackEntry currentPack = CONFIG.getPacks().get(currentPackName);
            if (currentPack != null) {
                player.connection.send(new ClientboundResourcePackPopPacket(Optional.of(currentPack.id())));
            }
        }

        // Push new pack
        Component prompt = pack.prompt() != null && !pack.prompt().isEmpty() ? Component.literal(pack.prompt()) : null;
        ClientboundResourcePackPushPacket pushPacket = new ClientboundResourcePackPushPacket(
            pack.id(),
            pack.url(),
            pack.hash(),
            pack.required(),
            Optional.ofNullable(prompt)
        );
        player.connection.send(pushPacket);

        PREFERENCES.setPreference(player.getUUID(), packName);
        source.sendSuccess(() -> Component.literal("Sent resource pack '" + packName + "' to " + player.getScoreboardName()), true);
        return 1;
    }

    private static int applyPackSelf(CommandSourceStack source, String packName) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return applyPack(source, player, packName);
        } else {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return 0;
        }
    }

    private static int applyPackMultiple(CommandSourceStack source, Collection<ServerPlayer> targets, String packName) {
        int count = 0;
        for (ServerPlayer player : targets) {
            count += applyPack(source, player, packName);
        }
        return count;
    }

    private static int popPack(CommandSourceStack source, ServerPlayer player) {
        String currentPackName = PREFERENCES.getPreference(player.getUUID());
        if (currentPackName == null) {
            source.sendFailure(Component.literal(player.getScoreboardName() + " does not have any active server pack."));
            return 0;
        }

        PackConfig.PackEntry pack = CONFIG.getPacks().get(currentPackName);
        if (pack != null) {
            player.connection.send(new ClientboundResourcePackPopPacket(Optional.of(pack.id())));
        } else {
            player.connection.send(new ClientboundResourcePackPopPacket(Optional.empty()));
        }

        PREFERENCES.setPreference(player.getUUID(), null);
        source.sendSuccess(() -> Component.literal("Removed server resource pack from " + player.getScoreboardName()), true);
        return 1;
    }

    private static int popPackSelf(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            return popPack(source, player);
        } else {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return 0;
        }
    }

    private static int popPackMultiple(CommandSourceStack source, Collection<ServerPlayer> targets) {
        int count = 0;
        for (ServerPlayer player : targets) {
            count += popPack(source, player);
        }
        return count;
    }

    private static int listPacks(CommandSourceStack source) {
        Map<String, PackConfig.PackEntry> packs = CONFIG.getPacks();
        if (packs.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No server resource packs are configured."), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Available server resource packs:"), false);
        for (String name : packs.keySet()) {
            source.sendSuccess(() -> Component.literal("- " + name), false);
        }
        return packs.size();
    }

    private static int reloadConfig(CommandSourceStack source) {
        CONFIG.load();
        source.sendSuccess(() -> Component.literal("Reloaded server pack configurations. (Loaded " + CONFIG.getPacks().size() + " packs)"), true);
        return 1;
    }
}

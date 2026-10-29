// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3

package net.serverpackswitcher.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.serverpackswitcher.ServerPackSwitcherMod;
import net.serverpackswitcher.config.PackConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 3: Brigadier AST & Command Hierarchy Validation Suite for Server Pack Switcher (1.21.1).
 * Validates root command registration, subcommand hierarchy, argument node typing,
 * operator permission gating (source.hasPermission(2)), and dynamic tab-completion suggestions.
 */
@DisplayName("Pillar 3: Brigadier AST & Command Hierarchy Validation Suite (1.21.1)")
public class ServerPackSwitcherCommandTest {

    private CommandDispatcher<CommandSourceStack> dispatcher;
    private CommandSourceStack adminSource;
    private CommandSourceStack unprivilegedSource;
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
        dispatcher = new CommandDispatcher<>();
        ServerPackSwitcherMod.registerCommands(dispatcher);

        originalPacks = new HashMap<>(ServerPackSwitcherMod.CONFIG.getPacks());

        adminSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                2,
                "AdminTester",
                Component.literal("AdminTester"),
                null,
                null
        );

        unprivilegedSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                0,
                "GuestTester",
                Component.literal("GuestTester"),
                null,
                null
        );
    }

    @AfterEach
    void tearDown() {
        ServerPackSwitcherMod.CONFIG.getPacks().clear();
        ServerPackSwitcherMod.CONFIG.getPacks().putAll(originalPacks);
    }

    @Test
    @DisplayName("a) Verify Command Tree Structure and Subcommand AST Hierarchy")
    void testCommandTreeStructure() {
        CommandNode<CommandSourceStack> rootNode = dispatcher.getRoot().getChild("pack");
        assertNotNull(rootNode, "Root /pack command node must be registered in dispatcher");
        assertInstanceOf(LiteralCommandNode.class, rootNode, "Root node must be a literal command node");

        // Inspect direct children: list, pop, clear, reload, apply, and direct pack_name argument
        CommandNode<CommandSourceStack> listNode = rootNode.getChild("list");
        assertNotNull(listNode, "Subcommand /pack list must exist in command hierarchy");
        assertInstanceOf(LiteralCommandNode.class, listNode, "Subcommand /pack list must be a literal node");

        CommandNode<CommandSourceStack> popNode = rootNode.getChild("pop");
        assertNotNull(popNode, "Subcommand /pack pop must exist in command hierarchy");
        assertInstanceOf(LiteralCommandNode.class, popNode, "Subcommand /pack pop must be a literal node");

        CommandNode<CommandSourceStack> clearNode = rootNode.getChild("clear");
        assertNotNull(clearNode, "Subcommand /pack clear must exist in command hierarchy");
        assertInstanceOf(LiteralCommandNode.class, clearNode, "Subcommand /pack clear must be a literal node");

        CommandNode<CommandSourceStack> reloadNode = rootNode.getChild("reload");
        assertNotNull(reloadNode, "Subcommand /pack reload must exist in command hierarchy");
        assertInstanceOf(LiteralCommandNode.class, reloadNode, "Subcommand /pack reload must be a literal node");

        CommandNode<CommandSourceStack> applyNode = rootNode.getChild("apply");
        assertNotNull(applyNode, "Subcommand /pack apply must exist in command hierarchy");
        assertInstanceOf(LiteralCommandNode.class, applyNode, "Subcommand /pack apply must be a literal node");

        CommandNode<CommandSourceStack> legacyPackNameNode = rootNode.getChild("pack_name");
        assertNotNull(legacyPackNameNode, "Direct argument /pack <pack_name> must exist in command hierarchy");
        assertInstanceOf(ArgumentCommandNode.class, legacyPackNameNode, "Direct /pack <pack_name> must be an argument node");

        // Assert apply has child argument pack_name
        CommandNode<CommandSourceStack> applyPackNameNode = applyNode.getChild("pack_name");
        assertNotNull(applyPackNameNode, "Subcommand /pack apply must have child argument 'pack_name'");
        assertInstanceOf(ArgumentCommandNode.class, applyPackNameNode, "'pack_name' under apply must be an argument node");

        // Assert that pop, clear, and apply -> pack_name have child argument targets
        CommandNode<CommandSourceStack> popTargetsNode = popNode.getChild("targets");
        assertNotNull(popTargetsNode, "Subcommand /pack pop must have child argument 'targets'");
        assertInstanceOf(ArgumentCommandNode.class, popTargetsNode, "'targets' under pop must be an argument node");

        CommandNode<CommandSourceStack> clearTargetsNode = clearNode.getChild("targets");
        assertNotNull(clearTargetsNode, "Subcommand /pack clear must have child argument 'targets'");
        assertInstanceOf(ArgumentCommandNode.class, clearTargetsNode, "'targets' under clear must be an argument node");

        CommandNode<CommandSourceStack> applyTargetsNode = applyPackNameNode.getChild("targets");
        assertNotNull(applyTargetsNode, "Subcommand /pack apply <pack_name> must have child argument 'targets'");
        assertInstanceOf(ArgumentCommandNode.class, applyTargetsNode, "'targets' under apply <pack_name> must be an argument node");
    }

    @Test
    @DisplayName("b) Verify Operator Permission Requirements (source.hasPermission(2))")
    void testOperatorPermissionRequirements() {
        CommandNode<CommandSourceStack> rootNode = dispatcher.getRoot().getChild("pack");
        assertNotNull(rootNode, "Root /pack command node must be registered");

        CommandNode<CommandSourceStack> reloadNode = rootNode.getChild("reload");
        CommandNode<CommandSourceStack> popTargetsNode = rootNode.getChild("pop").getChild("targets");
        CommandNode<CommandSourceStack> clearTargetsNode = rootNode.getChild("clear").getChild("targets");
        CommandNode<CommandSourceStack> applyTargetsNode = rootNode.getChild("apply").getChild("pack_name").getChild("targets");

        // Operator-only nodes: must pass for admin, fail for unprivileged
        List<CommandNode<CommandSourceStack>> opNodes = List.of(
                reloadNode,
                popTargetsNode,
                clearTargetsNode,
                applyTargetsNode
        );

        for (CommandNode<CommandSourceStack> opNode : opNodes) {
            assertNotNull(opNode, "Operator node under test must exist");
            assertTrue(opNode.getRequirement().test(adminSource),
                    "Operator node '" + opNode.getName() + "' must be permitted for adminSource");
            assertFalse(opNode.getRequirement().test(unprivilegedSource),
                    "Operator node '" + opNode.getName() + "' must be rejected for unprivilegedSource");
        }

        // Public player nodes: must pass for unprivileged players
        CommandNode<CommandSourceStack> listNode = rootNode.getChild("list");
        CommandNode<CommandSourceStack> popNode = rootNode.getChild("pop");
        CommandNode<CommandSourceStack> clearNode = rootNode.getChild("clear");
        CommandNode<CommandSourceStack> applyNode = rootNode.getChild("apply");
        CommandNode<CommandSourceStack> applyPackNameNode = applyNode.getChild("pack_name");
        CommandNode<CommandSourceStack> legacyPackNameNode = rootNode.getChild("pack_name");

        List<CommandNode<CommandSourceStack>> playerNodes = List.of(
                rootNode,
                listNode,
                popNode,
                clearNode,
                applyNode,
                applyPackNameNode,
                legacyPackNameNode
        );

        for (CommandNode<CommandSourceStack> playerNode : playerNodes) {
            assertNotNull(playerNode, "Player node under test must exist");
            assertTrue(playerNode.getRequirement().test(unprivilegedSource),
                    "Player accessible node '" + playerNode.getName() + "' must be permitted for unprivilegedSource");
        }
    }

    @Test
    @DisplayName("c) Verify Tab Completion Suggestions with PACK_SUGGESTIONS and Dispatcher")
    void testSuggestionsProvider() throws Exception {
        // Prepare test packs in configuration
        ServerPackSwitcherMod.CONFIG.getPacks().clear();
        ServerPackSwitcherMod.CONFIG.getPacks().put("faithful_64x", new PackConfig.PackEntry(
                UUID.randomUUID(), "https://example.com/faithful.zip", "abc123hash", false, "Faithful 64x", null
        ));
        ServerPackSwitcherMod.CONFIG.getPacks().put("bare_bones", new PackConfig.PackEntry(
                UUID.randomUUID(), "https://example.com/barebones.zip", "def456hash", false, "Bare Bones", null
        ));
        ServerPackSwitcherMod.CONFIG.getPacks().put("stay_true", new PackConfig.PackEntry(
                UUID.randomUUID(), "https://example.com/staytrue.zip", "ghi789hash", false, "Stay True", null
        ));

        // 1. Direct invocation of PACK_SUGGESTIONS provider
        SuggestionsBuilder directBuilder = new SuggestionsBuilder("pack apply ", 11);
        CompletableFuture<Suggestions> directFuture = ServerPackSwitcherMod.PACK_SUGGESTIONS.getSuggestions(null, directBuilder);
        assertNotNull(directFuture, "Suggestions future must not be null");

        Suggestions directSuggestions = directFuture.get();
        List<String> directList = directSuggestions.getList().stream().map(Suggestion::getText).toList();

        assertEquals(3, directList.size(), "Direct suggestions must return all 3 configured packs");
        assertTrue(directList.contains("faithful_64x"), "Suggestions must include faithful_64x");
        assertTrue(directList.contains("bare_bones"), "Suggestions must include bare_bones");
        assertTrue(directList.contains("stay_true"), "Suggestions must include stay_true");

        // 2. Dispatcher completion suggestions for /pack apply <tab>
        ParseResults<CommandSourceStack> applyParse = dispatcher.parse("pack apply ", adminSource);
        CompletableFuture<Suggestions> applyFuture = dispatcher.getCompletionSuggestions(applyParse);
        Suggestions applySuggestions = applyFuture.get();
        List<String> applyList = applySuggestions.getList().stream().map(Suggestion::getText).toList();

        assertTrue(applyList.contains("faithful_64x"), "Dispatcher /pack apply suggestions must contain faithful_64x");
        assertTrue(applyList.contains("bare_bones"), "Dispatcher /pack apply suggestions must contain bare_bones");
        assertTrue(applyList.contains("stay_true"), "Dispatcher /pack apply suggestions must contain stay_true");

        // 3. Dispatcher completion suggestions for direct /pack <tab>
        ParseResults<CommandSourceStack> directParse = dispatcher.parse("pack ", adminSource);
        CompletableFuture<Suggestions> directParseFuture = dispatcher.getCompletionSuggestions(directParse);
        Suggestions directParseSuggestions = directParseFuture.get();
        List<String> rootList = directParseSuggestions.getList().stream().map(Suggestion::getText).toList();

        // Root completions include both subcommands and direct pack names
        assertTrue(rootList.contains("list"), "Root suggestions must contain 'list'");
        assertTrue(rootList.contains("apply"), "Root suggestions must contain 'apply'");
        assertTrue(rootList.contains("faithful_64x"), "Root suggestions must contain 'faithful_64x'");
        assertTrue(rootList.contains("bare_bones"), "Root suggestions must contain 'bare_bones'");
    }

    @Test
    @DisplayName("d) Verify Command Parsing & Permission Gating execution AST")
    void testCommandParsingAndPermissions() {
        // Operator command parse checks
        ParseResults<CommandSourceStack> adminReload = dispatcher.parse("pack reload", adminSource);
        assertFalse(adminReload.getReader().canRead(), "Admin must fully parse 'pack reload'");
        assertNotNull(adminReload.getContext().getCommand(), "Admin 'pack reload' must resolve to an executable command");

        ParseResults<CommandSourceStack> guestReload = dispatcher.parse("pack reload", unprivilegedSource);
        assertTrue(guestReload.getContext().getCommand() == null
                        || guestReload.getReader().canRead()
                        || !guestReload.getExceptions().isEmpty(),
                "Unprivileged user must be denied executable access to 'pack reload'");

        // General player commands
        ParseResults<CommandSourceStack> guestList = dispatcher.parse("pack list", unprivilegedSource);
        assertFalse(guestList.getReader().canRead(), "Guest must fully parse 'pack list'");
        assertNotNull(guestList.getContext().getCommand(), "Guest 'pack list' must resolve to an executable command");

        ParseResults<CommandSourceStack> guestPop = dispatcher.parse("pack pop", unprivilegedSource);
        assertFalse(guestPop.getReader().canRead(), "Guest must fully parse 'pack pop'");
        assertNotNull(guestPop.getContext().getCommand(), "Guest 'pack pop' must resolve to an executable command");

        ParseResults<CommandSourceStack> guestClear = dispatcher.parse("pack clear", unprivilegedSource);
        assertFalse(guestClear.getReader().canRead(), "Guest must fully parse 'pack clear'");
        assertNotNull(guestClear.getContext().getCommand(), "Guest 'pack clear' must resolve to an executable command");

        ParseResults<CommandSourceStack> guestApply = dispatcher.parse("pack apply testpack", unprivilegedSource);
        assertFalse(guestApply.getReader().canRead(), "Guest must fully parse 'pack apply testpack'");
        assertNotNull(guestApply.getContext().getCommand(), "Guest 'pack apply testpack' must resolve to an executable command");

        ParseResults<CommandSourceStack> guestDirect = dispatcher.parse("pack testpack", unprivilegedSource);
        assertFalse(guestDirect.getReader().canRead(), "Guest must fully parse direct 'pack testpack'");
        assertNotNull(guestDirect.getContext().getCommand(), "Guest direct 'pack testpack' must resolve to an executable command");
    }
}

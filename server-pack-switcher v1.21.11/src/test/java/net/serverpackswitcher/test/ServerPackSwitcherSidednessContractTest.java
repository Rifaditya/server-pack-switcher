// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3

package net.serverpackswitcher.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 5: Sidedness Contract & License Hygiene Test Suite (1.21.11).
 * Asserts strict server-side safety (zero client package leakage),
 * universal GNU GPLv3 license header enforcement, and fabric.mod.json manifest hygiene.
 */
@DisplayName("Pillar 5: Server Pack Switcher Sidedness & License Contract Tests (1.21.11)")
public class ServerPackSwitcherSidednessContractTest {

    private static final String EXPECTED_LICENSE_HEADER = "// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3";

    // Split forbidden client package constants to avoid constant-pool self-matching
    private static final String CLIENT_FORBIDDEN = new String(new byte[]{
            110, 101, 116, 46, 109, 105, 110, 101, 99, 114, 97, 102, 116, 46, 99, 108, 105, 101, 110, 116
    }); // "net.minecraft.client"

    private static final String BLAZE3D_FORBIDDEN = new String(new byte[]{
            99, 111, 109, 46, 109, 111, 106, 97, 110, 103, 46, 98, 108, 97, 122, 101, 51, 100
    }); // "com.mojang.blaze3d"

    private static Path resolvePath(String relativePath) {
        Path cwd = Paths.get(System.getProperty("user.dir", "."));
        Path direct = cwd.resolve(relativePath);
        if (Files.exists(direct)) {
            return direct;
        }
        Path nested = cwd.resolve("server-pack-switcher v1.21.11").resolve(relativePath);
        if (Files.exists(nested)) {
            return nested;
        }
        Path current = cwd;
        while (current != null) {
            Path candidate = current.resolve(relativePath);
            if (Files.exists(candidate)) {
                return candidate;
            }
            Path subCandidate = current.resolve("server-pack-switcher v1.21.11").resolve(relativePath);
            if (Files.exists(subCandidate)) {
                return subCandidate;
            }
            Path parentCandidate = current.resolve("server-pack-switcher").resolve("server-pack-switcher v1.21.11").resolve(relativePath);
            if (Files.exists(parentCandidate)) {
                return parentCandidate;
            }
            current = current.getParent();
        }
        return direct;
    }

    @Test
    @DisplayName("a) Assert zero client or Blaze3D references in common code")
    void testNoClientReferencesInCommonCode() throws IOException {
        Path mainJavaDir = resolvePath("src/main/java");
        assertTrue(Files.exists(mainJavaDir), "src/main/java must exist at: " + mainJavaDir);

        List<Path> javaFiles;
        try (Stream<Path> stream = Files.walk(mainJavaDir)) {
            javaFiles = stream.filter(p -> p.toString().endsWith(".java")).toList();
        }

        assertFalse(javaFiles.isEmpty(), "No Java files discovered to audit for client package leakage");

        for (Path file : javaFiles) {
            String content = Files.readString(file, StandardCharsets.UTF_8);
            String normalized = file.toString().replace('\\', '/');

            assertFalse(content.contains(CLIENT_FORBIDDEN),
                    "Server-side code must NOT reference client classes (" + CLIENT_FORBIDDEN + "): " + normalized);
            assertFalse(content.contains(BLAZE3D_FORBIDDEN),
                    "Server-side code must NOT reference Blaze3D classes (" + BLAZE3D_FORBIDDEN + "): " + normalized);
        }
    }

    @Test
    @DisplayName("b) Assert every Java file in main and test has exact GPLv3 Line 1 header")
    void testGplv3LicenseHeadersPresent() throws IOException {
        Path mainJavaDir = resolvePath("src/main/java");
        Path testJavaDir = resolvePath("src/test/java");

        assertTrue(Files.exists(mainJavaDir), "src/main/java must exist at: " + mainJavaDir);
        assertTrue(Files.exists(testJavaDir), "src/test/java must exist at: " + testJavaDir);

        List<Path> allJavaFiles;
        try (Stream<Path> mainStream = Files.walk(mainJavaDir);
             Stream<Path> testStream = Files.walk(testJavaDir)) {
            allJavaFiles = Stream.concat(mainStream, testStream)
                    .filter(p -> p.toString().endsWith(".java"))
                    .toList();
        }

        assertFalse(allJavaFiles.isEmpty(), "No Java files discovered to audit for license headers");

        for (Path file : allJavaFiles) {
            String normalized = file.toString().replace('\\', '/');
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                String firstLine = reader.readLine();
                assertNotNull(firstLine, "File is empty: " + normalized);
                assertEquals(EXPECTED_LICENSE_HEADER, firstLine.trim(),
                        "Missing or malformed GNU GPLv3 license header on Line 1 of: " + normalized);
            }
        }
    }

    @Test
    @DisplayName("c) Assert fabric.mod.json valid JSON, id, environment, and entrypoints")
    void testFabricModJsonHygiene() throws IOException {
        Path modJsonPath = resolvePath("src/main/resources/fabric.mod.json");
        assertTrue(Files.exists(modJsonPath), "fabric.mod.json must exist at: " + modJsonPath);

        JsonObject rootJson;
        try (BufferedReader reader = Files.newBufferedReader(modJsonPath, StandardCharsets.UTF_8)) {
            rootJson = JsonParser.parseReader(reader).getAsJsonObject();
        }
        assertNotNull(rootJson, "fabric.mod.json parsed JSON object must not be null");

        // Verify id
        assertTrue(rootJson.has("id"), "fabric.mod.json must have 'id' field");
        assertEquals("serverpackswitcher", rootJson.get("id").getAsString(), "Mod ID must be 'serverpackswitcher'");

        // Verify environment
        assertTrue(rootJson.has("environment"), "fabric.mod.json must have 'environment' field");
        assertEquals("*", rootJson.get("environment").getAsString(), "Mod environment must be '*'");

        // Verify entrypoints contain net.serverpackswitcher.ServerPackSwitcherMod
        assertTrue(rootJson.has("entrypoints"), "fabric.mod.json must have 'entrypoints' field");
        JsonObject entrypoints = rootJson.getAsJsonObject("entrypoints");

        boolean foundMainMod = false;
        for (String key : entrypoints.keySet()) {
            JsonElement el = entrypoints.get(key);
            if (el.isJsonArray()) {
                JsonArray arr = el.getAsJsonArray();
                for (JsonElement item : arr) {
                    if (item.isJsonPrimitive() && "net.serverpackswitcher.ServerPackSwitcherMod".equals(item.getAsString())) {
                        foundMainMod = true;
                        break;
                    }
                }
            }
        }
        assertTrue(foundMainMod, "Entrypoints must contain net.serverpackswitcher.ServerPackSwitcherMod");
    }
}

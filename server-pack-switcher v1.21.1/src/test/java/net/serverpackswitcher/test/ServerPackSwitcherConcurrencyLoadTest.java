// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3

package net.serverpackswitcher.test;

import net.serverpackswitcher.config.PackConfig;
import net.serverpackswitcher.config.PlayerPreferences;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 2: Virtual Concurrency & High-Contention Stress Suite for Server Pack Switcher.
 * Simulates extreme multithreaded load across player preference mutations, pack configuration lookups,
 * and concurrent disk persistence to assert zero race conditions, deadlocks, or corruption.
 */
@DisplayName("Pillar 2: Virtual Concurrency & High-Contention Stress Suite")
public class ServerPackSwitcherConcurrencyLoadTest {

    private static final int THREAD_COUNT = 50;
    private static final int ITERATIONS_PER_THREAD = 100;
    private static final int TIMEOUT_SECONDS = 30;

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
    @DisplayName("a) testHighContentionPreferencesAccess - 50 threads mutating, querying, unsetting, saving, and reloading preferences")
    void testHighContentionPreferencesAccess() throws InterruptedException {
        PlayerPreferences preferences = new PlayerPreferences();
        preferences.load();

        UUID sharedUuid = new UUID(0xDA51CL, 9999L);
        UUID globalToggleUuid = new UUID(0xDA51CL, 8888L);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);
        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            final UUID uniqueUuid = new UUID(0xDA51CL, 1000L + threadId);

            executor.submit(() -> {
                try {
                    startLatch.await();

                    for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
                        // 1. Mutate unique preference
                        String expectedPack = "pack_" + threadId + "_" + i;
                        preferences.setPreference(uniqueUuid, expectedPack);

                        // 2. Query unique preference
                        String current = preferences.getPreference(uniqueUuid);
                        if (current != null && !current.startsWith("pack_" + threadId)) {
                            throw new IllegalStateException("Corrupted preference for thread " + threadId + ": " + current);
                        }

                        // 3. Mutate shared UUID preference under heavy contention
                        preferences.setPreference(sharedUuid, "shared_pack_" + (i % 5));
                        String sharedVal = preferences.getPreference(sharedUuid);
                        if (sharedVal != null && !sharedVal.startsWith("shared_pack_")) {
                            throw new IllegalStateException("Corrupted shared preference: " + sharedVal);
                        }

                        // 4. Periodically unset and reset a toggle UUID
                        if (i % 10 == 0) {
                            preferences.setPreference(globalToggleUuid, null);
                        } else if (i % 10 == 1) {
                            preferences.setPreference(globalToggleUuid, "toggled_pack");
                        }

                        // 5. Concurrent manual saves & reloads
                        if (i % 25 == 0) {
                            preferences.save();
                        }
                        if (i % 30 == 0) {
                            preferences.load();
                        }
                    }
                } catch (Throwable th) {
                    exceptions.add(th);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean completed = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Executor did not terminate cleanly");

        assertTrue(completed, "testHighContentionPreferencesAccess timed out after " + TIMEOUT_SECONDS + " seconds");
        assertTrue(exceptions.isEmpty(), "Encountered exceptions during preferences contention: " + exceptions);

        // Verify preference store maintains integrity
        preferences.save();
        PlayerPreferences verificationPrefs = new PlayerPreferences();
        verificationPrefs.load();

        for (int t = 0; t < THREAD_COUNT; t++) {
            UUID uniqueUuid = new UUID(0xDA51CL, 1000L + t);
            String pref = verificationPrefs.getPreference(uniqueUuid);
            assertNotNull(pref, "Preference store lost unique entry for thread " + t);
            assertTrue(pref.startsWith("pack_" + t), "Preference store entry corrupted for thread " + t + ": " + pref);
        }
        assertNotNull(verificationPrefs.getPreference(sharedUuid), "Shared UUID preference must be present");
        assertTrue(verificationPrefs.getPreferences().size() >= THREAD_COUNT, "Preference map size must contain all thread entries");
    }

    @Test
    @DisplayName("b) testHighContentionPackConfigAccess - 50 threads reading pack maps, querying pack entries, calling load() and save()")
    void testHighContentionPackConfigAccess() throws InterruptedException {
        // Pre-populate pack configuration with 10 local pack entries
        PackConfig initialConfig = new PackConfig();
        for (int p = 0; p < 10; p++) {
            String packName = "pack_" + p;
            String url = "https://example.com/packs/" + packName + ".zip";
            initialConfig.getPacks().put(packName, new PackConfig.PackEntry(
                UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)),
                url,
                "sha1_hash_" + p,
                p % 2 == 0,
                "Prompt for " + packName,
                ""
            ));
        }
        initialConfig.save();

        PackConfig config = new PackConfig();
        config.load();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);
        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        for (int t = 0; t < THREAD_COUNT; t++) {
            executor.submit(() -> {
                try {
                    startLatch.await();

                    for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
                        // 1. Concurrently read pack maps
                        Map<String, PackConfig.PackEntry> packs = config.getPacks();
                        assertNotNull(packs, "getPacks() must never return null");

                        // 2. Query pack entries
                        String targetPack = "pack_" + (i % 10);
                        PackConfig.PackEntry entry = packs.get(targetPack);
                        if (entry != null) {
                            assertNotNull(entry.getId(), "PackEntry id must not be null");
                            assertNotNull(entry.getUrl(), "PackEntry url must not be null");
                            assertNotNull(entry.getHash(), "PackEntry hash must not be null");
                            assertNotNull(entry.getPrompt(), "PackEntry prompt must not be null");
                        }

                        // 3. Concurrently trigger load() and save()
                        if (i % 20 == 0) {
                            config.load();
                        }
                        if (i % 25 == 0) {
                            config.save();
                        }
                    }
                } catch (Throwable th) {
                    exceptions.add(th);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean completed = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Executor did not terminate cleanly");

        assertTrue(completed, "testHighContentionPackConfigAccess timed out after " + TIMEOUT_SECONDS + " seconds");
        assertTrue(exceptions.isEmpty(), "Encountered exceptions during PackConfig contention: " + exceptions);

        // Clean completion assertions
        PackConfig verificationConfig = new PackConfig();
        verificationConfig.load();
        assertEquals(10, verificationConfig.getPacks().size(), "Configuration must retain all 10 pack entries after heavy contention");
        for (int p = 0; p < 10; p++) {
            assertTrue(verificationConfig.getPacks().containsKey("pack_" + p), "Missing pack_" + p);
        }
    }

    @Test
    @DisplayName("c) testSimultaneousMixedWorkload - 50 threads interleaving preference writes, pack lookups, saves, and reloads")
    void testSimultaneousMixedWorkload() throws InterruptedException {
        // Pre-populate PackConfig with 5 packs
        PackConfig config = new PackConfig();
        for (int p = 0; p < 5; p++) {
            String name = "theme_" + p;
            String url = "https://example.com/" + name + ".zip";
            config.getPacks().put(name, new PackConfig.PackEntry(
                UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)),
                url,
                "hash_" + p,
                false,
                "Prompt " + p,
                ""
            ));
        }
        config.save();

        PlayerPreferences preferences = new PlayerPreferences();
        preferences.load();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);
        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            final UUID playerUuid = new UUID(0xDA51CL, 5000L + threadId);

            executor.submit(() -> {
                try {
                    startLatch.await();

                    for (int i = 0; i < ITERATIONS_PER_THREAD; i++) {
                        String packChoice = "theme_" + (i % 5);

                        // 1. Preference write
                        preferences.setPreference(playerUuid, packChoice);

                        // 2. Pack lookup
                        PackConfig.PackEntry pack = config.getPacks().get(packChoice);
                        assertNotNull(pack, "Pack " + packChoice + " must exist in config");
                        assertEquals(packChoice, pack.getUrl().substring(pack.getUrl().lastIndexOf('/') + 1, pack.getUrl().lastIndexOf('.')));

                        // 3. Preference lookup
                        String currentPref = preferences.getPreference(playerUuid);
                        assertNotNull(currentPref, "Player preference should not be null");

                        // 4. Interleaved saves and reloads
                        if (i % 20 == 0) {
                            preferences.save();
                        }
                        if (i % 25 == 0) {
                            config.save();
                        }
                        if (i % 30 == 0) {
                            preferences.load();
                        }
                        if (i % 35 == 0) {
                            config.load();
                        }
                    }
                } catch (Throwable th) {
                    exceptions.add(th);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean completed = doneLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Executor did not terminate cleanly");

        assertTrue(completed, "testSimultaneousMixedWorkload timed out after " + TIMEOUT_SECONDS + " seconds");
        assertTrue(exceptions.isEmpty(), "Encountered exceptions during mixed workload contention: " + exceptions);

        // Verification of state integrity
        assertDoesNotThrow(() -> config.load(), "PackConfig load failed after mixed workload");
        assertDoesNotThrow(() -> preferences.load(), "PlayerPreferences load failed after mixed workload");
        assertEquals(5, config.getPacks().size(), "Config must retain 5 themes");
        assertTrue(preferences.getPreferences().size() >= THREAD_COUNT, "Preferences must contain entries for all threads");
    }
}

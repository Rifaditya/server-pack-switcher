package net.serverpackswitcher.config;

// Verified against: FabricLoader.java (Fabric Loader API)

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PackConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("serverpackswitcher");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("serverpackswitcher.json");

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    private Map<String, PackEntry> packs = new HashMap<>();

    public static class PackEntry {
        private UUID id;
        private String url;
        private String hash;
        private boolean required;
        private String prompt;
        private String modrinth;

        public PackEntry() {}

        public PackEntry(UUID id, String url, String hash, boolean required, String prompt, String modrinth) {
            this.id = id;
            this.url = url;
            this.hash = hash;
            this.required = required;
            this.prompt = prompt;
            this.modrinth = modrinth;
        }

        public UUID getId() {
            if (id == null && url != null && !url.isEmpty()) {
                id = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8));
            }
            return id;
        }

        public void setId(UUID id) { this.id = id; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getHash() { return hash; }
        public void setHash(String hash) { this.hash = hash; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
        public String getModrinth() { return modrinth; }
        public void setModrinth(String modrinth) { this.modrinth = modrinth; }
    }

    public Map<String, PackEntry> getPacks() {
        return packs;
    }

    public void load() {
        if (!Files.exists(CONFIG_PATH)) {
            LOGGER.info("Config file not found, creating default at {}", CONFIG_PATH);
            createDefaultConfig();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root != null && root.has("packs")) {
                Map<String, PackEntry> loadedPacks = GSON.fromJson(
                    root.get("packs"),
                    new TypeToken<Map<String, PackEntry>>() {}.getType()
                );
                if (loadedPacks != null) {
                    this.packs = new HashMap<>();
                    for (Map.Entry<String, PackEntry> entry : loadedPacks.entrySet()) {
                        String name = entry.getKey().toLowerCase();
                        PackEntry pack = entry.getValue();
                        this.packs.put(name, pack);
                        
                        // If Modrinth is specified, resolve it asynchronously
                        if (pack.getModrinth() != null && !pack.getModrinth().isEmpty()) {
                            resolveModrinthPack(name, pack);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load pack config", e);
        }
    }

    private void resolveModrinthPack(String name, PackEntry pack) {
        CompletableFuture.runAsync(() -> {
            try {
                String input = pack.getModrinth().trim();
                LOGGER.info("Resolving Modrinth pack '{}' from: {}", name, input);
                
                String target = input;
                if (target.startsWith("http://") || target.startsWith("https://")) {
                    target = parseModrinthUrl(target);
                }

                JsonObject versionJson = null;
                if (isModrinthVersionId(target)) {
                    versionJson = fetchModrinthVersion(target);
                }

                if (versionJson == null) {
                    versionJson = fetchModrinthLatestVersion(target);
                }

                if (versionJson != null && versionJson.has("files")) {
                    JsonArray files = versionJson.getAsJsonArray("files");
                    JsonObject targetFile = null;
                    for (int i = 0; i < files.size(); i++) {
                        JsonObject file = files.get(i).getAsJsonObject();
                        if (file.has("primary") && file.get("primary").getAsBoolean()) {
                            targetFile = file;
                            break;
                        }
                    }
                    if (targetFile == null && files.size() > 0) {
                        targetFile = files.get(0).getAsJsonObject();
                    }

                    if (targetFile != null) {
                        String downloadUrl = targetFile.get("url").getAsString();
                        String sha1 = "";
                        if (targetFile.has("hashes")) {
                            JsonObject hashes = targetFile.getAsJsonObject("hashes");
                            if (hashes.has("sha1")) {
                                sha1 = hashes.get("sha1").getAsString();
                            }
                        }
                        pack.setUrl(downloadUrl);
                        pack.setHash(sha1);
                        pack.setId(UUID.nameUUIDFromBytes(downloadUrl.getBytes(StandardCharsets.UTF_8)));
                        LOGGER.info("Successfully resolved Modrinth pack '{}' -> URL: {}, Hash: {}", name, downloadUrl, sha1);
                    } else {
                        LOGGER.warn("Failed to find files for Modrinth pack '{}'", name);
                    }
                } else {
                    LOGGER.warn("Failed to retrieve version details from Modrinth for '{}'", name);
                }
            } catch (Exception e) {
                LOGGER.error("Error resolving Modrinth pack '" + name + "'", e);
            }
        });
    }

    private String parseModrinthUrl(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        if (url.contains("/version/")) {
            int idx = url.lastIndexOf("/version/");
            return url.substring(idx + "/version/".length());
        }
        int idx = url.lastIndexOf('/');
        return url.substring(idx + 1);
    }

    private boolean isModrinthVersionId(String target) {
        return target.length() == 8 && target.matches("^[a-zA-Z0-9]+$");
    }

    private JsonObject fetchModrinthVersion(String versionId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.modrinth.com/v2/version/" + versionId))
            .header("User-Agent", "Rifaditya/server-pack-switcher/1.0.0 (minecraft mod)")
            .GET()
            .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return GSON.fromJson(response.body(), JsonObject.class);
        }
        return null;
    }

    private JsonObject fetchModrinthLatestVersion(String projectIdOrSlug) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.modrinth.com/v2/project/" + projectIdOrSlug + "/version"))
            .header("User-Agent", "Rifaditya/server-pack-switcher/1.0.0 (minecraft mod)")
            .GET()
            .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonArray versions = GSON.fromJson(response.body(), JsonArray.class);
            if (versions.size() > 0) {
                return versions.get(0).getAsJsonObject();
            }
        }
        return null;
    }

    private void createDefaultConfig() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            packs = new HashMap<>();
            packs.put("plastic", new PackEntry(
                null,
                "https://example.com/plastic_pack.zip",
                "da39a3ee5e6b4b0d3255bfef95601890afd80709",
                false,
                "Would you like to apply the Plastic Resource Pack?",
                ""
            ));
            packs.put("glassdoors", new PackEntry(
                null,
                "",
                "",
                false,
                "Would you like to apply the Glass Doors resource pack from Modrinth?",
                "https://modrinth.com/resourcepack/glass-doors"
            ));
            save();
        } catch (Exception e) {
            LOGGER.error("Failed to create default config", e);
        }
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
            Map<String, Object> root = new HashMap<>();
            root.put("packs", packs);
            GSON.toJson(root, writer);
        } catch (Exception e) {
            LOGGER.error("Failed to save pack config", e);
        }
    }
}

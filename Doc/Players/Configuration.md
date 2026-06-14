# Server Configuration

The mod generates its configuration file at `config/serverpackswitcher.json` inside your server folder. You can configure packs using either **Direct URLs** or **Modrinth links**.

---

## 🔧 JSON Template
Here is an example config template showing both Modrinth and direct URL configs:

```json
{
  "packs": {
    "glassdoors": {
      "modrinth": "https://modrinth.com/resourcepack/glass-doors",
      "required": false,
      "prompt": "Would you like to apply the Glass Doors resource pack?"
    },
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to apply the Fresh Animations resource pack?"
    },
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to use the Plastic look?"
    }
  }
}
```

---

## 📋 Configuration Fields
For each pack entry under `"packs"`, you can define:

* **`modrinth`** (Optional String): A Modrinth URL (e.g. `https://modrinth.com/resourcepack/glass-doors`), project slug (e.g. `glass-doors`), or specific version URL. If specified, the server will asynchronously retrieve the direct download link and SHA-1 hash from the Modrinth API automatically at startup.
* **`url`** (Required if `modrinth` is omitted): The direct download link to the resource pack `.zip` file.
* **`hash`** (Optional String): The SHA-1 hash of the zip file (40 hex characters). Recommended if not using Modrinth to enable client-side caching.
* **`required`** (Optional Boolean, Default: `false`): If `true`, the client is prompted with a mandatory dialog to accept the pack.
* **`prompt`** (Optional String): A message displayed on the client's screen when asking to download the pack.
* **`id`** (Optional UUID String): A unique identifier for the pack. If omitted, it is generated automatically.

---

## 🔍 How to Find URLs & Generate Hashes (For Direct Links)

If you are not using Modrinth and want to configure direct links:

### 1. Obtaining a Direct Download URL
The URL must download the `.zip` file immediately when clicked, without loading a website.
- **Discord**: Upload the resource pack to a channel, right-click the download arrow, and click **Copy Link**.
- **Dropbox**: Copy the sharing link and change the end from `?dl=0` to `?raw=1`.
- **GitHub**: Use the direct URL to a release asset.

### 2. Generating the SHA-1 Hash
The hash is a unique 40-character fingerprint that lets Minecraft cache the pack.
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
- **Online Tools**: Drag and drop your `.zip` into an online SHA-1 checksum calculator.

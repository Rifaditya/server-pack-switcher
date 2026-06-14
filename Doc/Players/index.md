# Wiki: Server Pack Switcher User Guide

Welcome to the **Server Pack Switcher** Wiki! This guide explains how to configure, use, and manage this server-side mod.

---

## 📖 Table of Contents
1. [Introduction](#-introduction)
2. [Commands Reference](#-commands-reference)
3. [Server Configuration](#-server-configuration)
4. [Persistent Preferences](#-persistent-preferences)
5. [Permissions & Administration](#-permissions--administration)

---

## 🚀 Introduction
**Server Pack Switcher** is a lightweight, 100% server-side Fabric mod that enables players to choose, download, and apply resource packs using commands. Since it runs completely on the server, players can join with unmodified vanilla clients and still dynamically switch their resource packs.

---

## 💻 Commands Reference

All commands start with the `/pack` base prefix.

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/pack list` | Everyone | Displays a list of all configured server resource packs. |
| `/pack <pack_name>` | Everyone | Applies the specified resource pack to the caller. |
| `/pack apply <pack_name>` | Everyone | Alias for `/pack <pack_name>`. |
| `/pack pop` | Everyone | Removes the active server pack from the caller's client. |
| `/pack clear` | Everyone | Alias for `/pack pop`. |
| `/pack apply <pack_name> <targets>` | Gamemaster (Level 2+) | Forces the specified resource pack onto the target player(s). |
| `/pack pop <targets>` | Gamemaster (Level 2+) | Forces removal of the active server pack from the target player(s). |
| `/pack reload` | Gamemaster (Level 2+) | Reloads the configuration file from disk. |

*Note: Command arguments support autocomplete (TAB suggestions) for all configured pack names.*

---

## 🔧 Server Configuration

The mod generates its configuration file at `config/serverpackswitcher.json` inside your server folder.

### Default Template
```json
{
  "packs": {
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to apply the Plastic Resource Pack?"
    },
    "default": {
      "url": "https://example.com/default_server_pack.zip",
      "hash": "",
      "required": false
    }
  }
}
```

### Configuration Fields
For each pack entry, you can specify:

* **`id`** (Optional UUID String): The unique identifier for this pack. If omitted, the mod generates one deterministically based on the `url`.
* **`url`** (Required String): The download link to the resource pack zip file.
* **`hash`** (Optional String): The SHA-1 hash of the zip file (40 hex characters). Highly recommended for client-side caching so players don't have to re-download the pack every time.
* **`required`** (Optional Boolean, Default: `false`): If `true`, the client is prompted with a mandatory dialog to accept the pack.
* **`prompt`** (Optional String): A message displayed on the client's screen when asking to download the pack.

### 🔍 How to Find URLs & Generate Hashes

#### 1. Obtaining a Direct Download URL
The URL must be a **direct link** to the `.zip` file. When clicked, it should download the file immediately rather than opening a web page or file preview.
- **Discord**: Upload the resource pack to a Discord channel, right-click the file, and select **Copy Link**.
- **GitHub Releases**: Upload the `.zip` as a release asset, right-click the asset link, and select **Copy Link Address**.
- **Dropbox**: Copy the share link and change the suffix from `?dl=0` to `?dl=1` or `?raw=1`.
- **Google Drive**: Use a direct download link generator tool.

#### 2. Generating the SHA-1 Hash
The hash is a unique 40-character hex string representing the exact content of the file. Minecraft uses this to check if a client has already downloaded the pack (caching).
- **Windows (PowerShell)**:
  ```powershell
  Get-FileHash -Algorithm SHA1 .\my_resource_pack.zip
  ```
- **macOS / Linux (Terminal)**:
  ```bash
  sha1sum my_resource_pack.zip
  ```
  *(or `shasum my_resource_pack.zip`)*
- **Online Tools**: Search for "SHA1 file calculator", upload your `.zip` file, and copy the resulting SHA-1 hex string.

---

## 💾 Persistent Preferences

When a player selects a pack via `/pack <name>`, the server records this choice in `config/serverpackswitcher_preferences.json`.
- When the player logs out and connects again later, the server automatically reads their preference and pushes the correct resource pack packet.
- If a player runs `/pack pop`, their preference is cleared, and no pack will be sent on their next join.

---

## 🛡️ Permissions & Administration

Administrators can force packs on players using targeted commands:
- `/pack apply plastic @a` - Sends the `plastic` pack to all online players.
- `/pack pop @a[distance=..50]` - Clears server packs from all players within 50 blocks.

To add new packs, edit `config/serverpackswitcher.json` and run `/pack reload`. The changes take effect instantly without restarting the server.

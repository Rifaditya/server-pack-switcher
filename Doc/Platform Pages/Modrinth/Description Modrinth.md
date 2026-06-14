<div align="center">

# ⚒️ Server Pack Switcher

**Dynamic, player-driven server resource pack switching on demand.**

<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Environment-Server-orange?style=for-the-badge" alt="Server Side Only">
    <img src="https://img.shields.io/badge/Language-Java_25-red?style=for-the-badge" alt="Java 25">
    <img src="https://img.shields.io/badge/Minecraft-26.2+-brightgreen?style=for-the-badge" alt="Minecraft 26.2+">
</p>

</div>

---

**Server Pack Switcher** is a server-side mod that allows players to switch between server-provided resource packs dynamically using simple chat commands. 

Since it runs completely on the server, players can join with unmodified vanilla clients and still customize their gameplay looks on demand!

## ✨ Features

- 🔄 **Player-Driven Swapping:** Players can change their own game look instantly by typing a command (e.g. `/pack freshanimations`).
- 💾 **Persistent Preferences:** The server remembers each player's chosen pack. When they reconnect, the correct resource pack is automatically sent to them.
- 🦊 **Direct Modrinth Integration:** You don't need to manually find file URLs or calculate SHA-1 hashes! Just paste the Modrinth version URL, project slug, or version ID, and the mod resolves it asynchronously.
- 🗑️ **Clean Pack Swapping:** Automatically pops (removes) the player's previously active pack before pushing a new one, ensuring no stacked resource packs from this mod.
- 🛡️ **Admin Commands:** Operators can force packs on specific players or groups (e.g. `/pack apply plastic @a`) and reload configuration files live using `/pack reload`.
- 🔌 **100% Server-Side:** Unmodified vanilla clients can connect and benefit from this mod.

---

## 🔧 Configuration Guide

The mod automatically generates `config/serverpackswitcher.json` upon start.

### JSON Template
```json
{
  "packs": {
    "freshanimations": {
      "modrinth": "https://modrinth.com/resourcepack/fresh-animations",
      "required": false,
      "prompt": "Would you like to apply the Fresh Animations resource pack from Modrinth?"
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

### Config Field Details
- **`modrinth`** (Optional String): Modrinth project URL, version URL, or project slug. If specified, the mod queries the Modrinth API at launch to find the download link and hash code automatically.
- **`url`** (Required if `modrinth` is omitted): Direct download link to the `.zip` file.
- **`hash`** (Optional String): The SHA-1 hash of the zip file (40 hex characters) for client caching.
- **`required`** (Optional Boolean, Default: `false`): If true, players cannot easily dismiss the download prompt.
- **`prompt`** (Optional String): Custom text shown to the player when downloading.

---

## 💻 Commands Reference

| Command | Permission | Description |
| :--- | :--- | :--- |
| `/pack list` | Everyone | Lists all available configured packs. |
| `/pack <pack_name>` | Everyone | Applies the specified pack to yourself. |
| `/pack apply <pack_name>` | Everyone | Alias for `/pack <pack_name>`. |
| `/pack pop` / `/pack clear` | Everyone | Removes your active server resource pack. |
| `/pack apply <pack_name> <targets>` | Operators | Forces a pack onto target player(s). |
| `/pack pop <targets>` | Operators | Clears packs from target player(s). |
| `/pack reload` | Operators | Reloads configuration and refreshes Modrinth links. |

---

## 📦 Install & Setup
1. Download `server-pack-switcher-1.0.2+A-26.2.jar` and place it in your server's `mods` folder.
2. Start the server to generate the default configuration.
3. Edit `config/serverpackswitcher.json` to configure your packs.
4. Run `/pack reload` to apply your new configuration live.

---

## 📜 Modpack Permissions
You are free to include this mod in any modpack, provided it is hosted on the same platform (e.g., Modrinth). Cross-platform redistribution is not permitted.

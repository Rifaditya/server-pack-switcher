# Admin Guide

This guide details administration features, live reloading, and persistent player configurations.

---

## 💾 Persistent Preferences

When a player selects a pack via `/pack <name>`, the server automatically records this choice in `config/serverpackswitcher_preferences.json`.
- **Automatic Re-apply**: When the player logs out and connects again later, the server automatically reads their preference and sends the correct pack.
- **Clearing Preferences**: If a player runs `/pack pop` or `/pack clear`, their preference is removed, and they will not receive any pack the next time they join.

---

## 🔄 Live Configuration Reloading

If you edit the configuration file `config/serverpackswitcher.json` while the server is running, you do not need to restart!
1. Make sure you are an administrator (have operator permissions).
2. Type **`/pack reload`** in the game chat or server console.
3. The server will reload the configurations live. Any packs using Modrinth links will re-resolve asynchronously on a background thread.

---

## 🛡️ Targeted Admin Commands

Administrators (operators with permission level 2+) can force resource packs on other players or clear them.

### Force Apply a Pack
- `/pack apply plastic Bob` - Pushes the `plastic` pack to player `Bob`.
- `/pack apply plastic @a` - Pushes the `plastic` pack to all online players.

### Force Remove a Pack
- `/pack pop Bob` - Clears the active pack from player `Bob`.
- `/pack pop @a` - Clears active packs from all online players.

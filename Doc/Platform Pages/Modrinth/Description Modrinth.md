# Server Pack Switcher

**Server Pack Switcher** is a server-side mod that allows players to switch between server-provided resource packs using simple commands.

## Features
- **Player-driven Swapping**: Players can use `/pack <name>` or `/pack apply <name>` to download and apply any configured resource pack.
- **Persistent Preferences**: Player choices are saved. When a player logs out and returns, their chosen pack is re-applied automatically.
- **Pack List**: `/pack list` displays all available configured resource packs.
- **Pop/Clear Packs**: `/pack pop` or `/pack clear` removes the active server resource pack.
- **Admin Control**: Operators can apply/remove packs for other players and run `/pack reload` to update configurations without restarting the server.
- **100% Server-Side**: Unmodified vanilla clients can connect and use this mod!

## Configuration
The mod generates a configuration file at `config/serverpackswitcher.json`:
```json
{
  "packs": {
    "plastic": {
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to install the Plastic resource pack?"
    }
  }
}
```
- **url**: Link to the zip file.
- **hash**: (Optional) SHA-1 hash of the zip file for client caching.
- **required**: (Optional) If true, the client must accept the pack.
- **prompt**: (Optional) Custom text shown on the download prompt.

# Concept: Server Pack Switcher

## Overview
A server-side utility mod that allows players to switch server resource packs using commands (e.g. `/pack plastic`).

## Mechanics
- Packs are configured server-side in `config/serverpackswitcher.json`.
- Players can type `/pack <name>` to apply a specific pack.
- A player's choice is saved persistently to `config/serverpackswitcher_preferences.json`.
- When they rejoin, their chosen pack is automatically sent to them.
- Players can run `/pack pop` or `/pack clear` to remove their active server resource pack.
- Operators can reload the configurations live using `/pack reload`.

## Configuration Syntax
```json
{
  "packs": {
    "plastic": {
      "id": "optional-uuid-string-here",
      "url": "https://example.com/plastic_pack.zip",
      "hash": "da39a3ee5e6b4b0d3255bfef95601890afd80709",
      "required": false,
      "prompt": "Would you like to install the Plastic resource pack?"
    }
  }
}
```

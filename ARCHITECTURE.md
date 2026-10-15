# Architecture & Symbol Index: Server Pack Switcher

## 1. Mod Metadata & Entrypoint
- **Mod ID**: `serverpackswitcher`
- **Main Entrypoint**: `net.serverpackswitcher.ServerPackSwitcherMod` (`net.fabricmc.api.ModInitializer`)
- **Client Entrypoint**: `None`

## 2. Bytecode Mixin Target Registry
| Target Vanilla Class | Mixin Class | Purpose |
| :--- | :--- | :--- |
| `Minecraft` | `Mixin` | Core bytecode hook |

## 3. Core Mechanics & Subsystems
- **Source Root**: `src/main/java/`
- **Resource Root**: `src/main/resources/`

## 4. Dynamic GameRules & Commands
- **GameRules / Commands**: Configured dynamically via namespaced keys (`serverpackswitcher:*`).

## 5. Configuration & Sidedness Isolation
- **Sidedness**: Server-safe logic in main, client isolated in `src/client/java` or client entrypoint.

# 🎛️ Master Release Queue: Server Pack Switcher

> **Mod Project Master Ground-Truth Document**  
> *Last Synchronized: 2026-10-29*  
> **Modrinth ID**: *Unregistered* | **CurseForge ID**: *Unregistered* | **Lead SemVer**: `1.0.3`

---

## 📊 Multi-Version Release Matrix & Queue Status

| Target MC | Generational Era | Live on Platforms | Next Queued Version | Status & Cadence Action | Feature Highlights / Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MC 26.2-rc-1** | Modern Standard | *(Unreleased)* | `1.0.3` | 🛠️ **Local Development** | Initial workspace build ready for first public deployment. |
| **MC 1.21.11** | Winter Drop | *(Unreleased)* | `1.0.0+1.21.11` | 🛠️ **Local Development** | Initial 1.21.11 release with multi-pack push/pop adapter. |
| **MC 1.21.1** | Transitional Early | *(Unreleased)* | `1.0.0+1.21.1` | 🛠️ **Local Development** | Initial 1.21.1 release with multi-pack push/pop adapter. |
| **MC 1.20.1** | Legacy Long-Term Support | *(Unreleased)* | `1.0.0+1.20.1` | 🛠️ **Local Development** | Initial 1.20.1 LTS release with single-pack adapter. |

---

## 🏛️ Project Operating Rules & Architectural Invariants

1. **🔢 Universal Direct SemVer Inheritance**:
   - Modern subprojects share unified SemVer milestone lineage targeting `1.0.3`.
   - Each Minecraft version anchor manages its own organic progression to ensure 100% clean, verified parity.

2. **📅 Daily Update Guard**:
   - Strict maximum of 1 release per day per targeted Minecraft version anchor across Modrinth and CurseForge.

---

## 🛠️ CLI Publisher Commands for Server Pack Switcher

```powershell
# 1. Check current status across all targeted Minecraft versions
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Server Pack Switcher" --status

# 2. Publish next sequential batch across all active versions
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Server Pack Switcher" --publish-next --yes

```

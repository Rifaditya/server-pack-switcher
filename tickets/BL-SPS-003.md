# 🎫 [BL-SPS-003] Onboard Server Pack Switcher to Release Hub and Discord Bot Registries

- **Ticket ID**: `[BL-SPS-003]`
- **Category**: `[TECH_DEBT]`
- **Priority**: `[LOW]`
- **Scope**: `Downstream Registries & Toolchain`
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `server-pack-switcher`

---

## 🎯 1. Problem Statement & Player Impact
Currently, `server-pack-switcher` is not registered in `minecraft-mod-release-hub/config/platform_projects.json` or `dasik-mod-sync/watched_projects.json`. As a result, automated multi-platform release publishing (Modrinth/CurseForge) and Discord early-access build synchronization cannot discover or route builds for Server Pack Switcher.

## 🔬 2. Architectural Blueprint & Target Classes
- Registry Files to Update:
  - `minecraft-mod-release-hub/config/platform_projects.json`: Add `Server Pack Switcher` entry with Modrinth/CurseForge project slugs/IDs and root directory mapping.
  - `dasik-mod-sync/watched_projects.json`: Add project metadata, icon path, drive folder ID, and forum routing.
  - `server-pack-switcher/MASTER_RELEASE_QUEUE.md`: Populate active project IDs once registered on storefronts.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- Registers all active Minecraft version anchors (`26.2-rc-1`, expanding to `1.20.1`, `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3` upon backport completion).

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- Missing Storefront IDs: If storefront projects are not yet created on Modrinth/CurseForge, set `modrinth_id: ""` and `curseforge_id: ""` with placeholder/offline status.
- Discord Icon Path Resolution: Must point to verified on-disk asset `src/main/resources/assets/serverpackswitcher/icon.png`.

## 🚀 5. Implementation Steps (Code-Level Plan)
1. Add `server-pack-switcher` to `platform_projects.json`.
2. Add project entry to `watched_projects.json`.
3. Verify icon path and root directory resolution via `project-onboarder` validation.
4. Run downstream registry checks to assert 100% health.

## 🏁 6. 5-Pillar Verification Checklist
- [ ] **Pillar 1 (Chaos Fuzzing)**: N/A (Registry metadata).
- [ ] **Pillar 2 (Concurrency Load)**: N/A.
- [ ] **Pillar 3 (Brigadier AST)**: N/A.
- [ ] **Pillar 4 (GameTest)**: N/A.
- [ ] **Pillar 5 (Sidedness & Hygiene)**: Registry JSON syntax valid; zero broken paths.
- [ ] **Downstream Discovery**: Successfully recognized by `platform_publisher.py --status` and `sync_builds.py`.

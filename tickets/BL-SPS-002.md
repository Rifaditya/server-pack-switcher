# 🎫 [BL-SPS-002] Multi-Era Parity Backport to Minecraft 1.20.1 and 1.21.x Anchors

- **Ticket ID**: `[BL-SPS-002]`
- **Category**: `[FEATURE]`
- **Priority**: `[MEDIUM]`
- **Scope**: `Legacy & Transitional Anchors` (`1.20.1`, `1.21.1`, `1.21.11`)
- **Status**: `✅ RESOLVED`
- **Target Repository / Root**: `server-pack-switcher`

---

## 🎯 1. Problem Statement & Player Impact
Currently, `server-pack-switcher` exists exclusively on modern Minecraft 26.2-rc-1. Server owners and modpack developers operating on legacy long-term support versions (1.20.1) and transitional anchors (1.21.1, 1.21.11) cannot utilize server-side resource pack switching. Backporting `server-pack-switcher` across all studio version anchors enforces the Multi-Era Anchor Parity Law and expands server compatibility.

## 🔬 2. Architectural Blueprint & Target Classes
- Subprojects to Scaffold:
  - `server-pack-switcher v1.20.1/` (Java 17, Fabric Loader >=0.15.0)
  - `server-pack-switcher v1.21.1/` (Java 21, Fabric Loader >=0.16.0)
  - `server-pack-switcher v1.21.11/` (Java 21, Fabric Loader >=0.16.5)
- Target Classes:
  - `net.serverpackswitcher.ServerPackSwitcherMod` (Adapted packet structures across network changes)
  - `net.serverpackswitcher.config.PackConfig` (File I/O and Modrinth async resolver)
  - `net.serverpackswitcher.config.PlayerPreferences` (UUID preference store)

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 1.20.1 (Java 17)**: Uses legacy `ClientboundCustomPayloadPacket` / `ClientboundResourcePackPacket` (pre-push/pop packet API), `net.minecraft.network.chat.Component`.
- **MC 1.21.1 / 1.21.11 (Java 21)**: Uses `ClientboundResourcePackPushPacket` and `ClientboundResourcePackPopPacket` with Java 21 standard library.
- **Modern Sovereign (Java 25)**: Modern reference codebase intact.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- Protocol Packet Divergence: 1.20.1 lacks native multi-pack push/pop UUID packets (introduced in 1.20.3+); requires legacy single-pack replacement emulation.
- Dedicated Server Sidedness: Zero client classloading in dedicated server runtime environments.
- Loom Toolchain Mappings: Mappings adapt from Yarn / Mojang mappings across 1.20.1 vs 1.21.x.

## 🚀 5. Implementation Steps (Code-Level Plan)
1. Scaffold 1.20.1, 1.21.1, and 1.21.11 subprojects using project-scaffolder / backporter patterns.
2. Adapt packet dispatch for 1.20.1 single-pack API vs 1.20.3+ multi-pack UUID packets.
3. Wire 5-Pillar testing harness across all newly scaffolded subprojects.
4. Execute `./gradlew test build` across all anchors and verify parity.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Chaos Fuzzing)**: Config and preference fuzzing passes across all anchors.
- [x] **Pillar 2 (Concurrency Load)**: 50 concurrent virtual threads verified per anchor.
- [x] **Pillar 3 (Brigadier AST)**: Brigadier commands `/pack` registered cleanly across all subprojects.
- [x] **Pillar 4 (GameTest)**: Headless packet dispatch state machine validated.
- [x] **Pillar 5 (Sidedness & Hygiene)**: 0 client references in common code; single-line GPLv3 headers on all files.
- [x] **Anchor Compilation**: Clean `./gradlew test` and `./gradlew build` across all 6 anchors.

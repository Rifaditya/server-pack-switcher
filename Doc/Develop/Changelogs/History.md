# 📜 Server Pack Switcher Technical Development History

## [BL-SPS-002] Multi-Era Parity Backport to Minecraft 1.20.1 and 1.21.x Anchors

- **Date**: 2026-10-29
- **Scope**: Multi-Era Anchor Parity (`1.20.1`, `1.21.1`, `1.21.11`, `26.2-rc-1`)
- **Status**: ✅ RESOLVED
- **Backlog Reference**: `[BL-SPS-002]`

### 1. Architectural Scaffolding & Subprojects
Established dedicated independent subprojects per version anchor adhering to the **1 Jar 1 Version Policy** and **Multi-Era Anchor Parity Law**:
- **`server-pack-switcher v1.20.1/`**: Targets Minecraft `1.20.1` (Java 17, Loom `1.10.2`, Fabric Loader `0.15.11`, DasikLibrary `1.4.0+1.20.1`, SemVer `1.0.0+1.20.1`).
- **`server-pack-switcher v1.21.1/`**: Targets Minecraft `1.21.1` (Java 21, Loom `1.10.2`, Fabric Loader `0.16.0`, DasikLibrary `1.4.0+1.21.1`, SemVer `1.0.0+1.21.1`).
- **`server-pack-switcher v1.21.11/`**: Targets Minecraft `1.21.11` Winter Drop (Java 21, Loom-Remap `1.15-SNAPSHOT`, Fabric Loader `0.16.5`, DasikLibrary `1.4.0+1.21.11`, SemVer `1.0.0+1.21.11`).

### 2. Network Protocol Adaptations
- **Legacy Single-Pack Emulation (`1.20.1`)**:
  - Replaced modern multi-pack push/pop packets with `net.minecraft.network.protocol.game.ClientboundResourcePackPacket`.
  - Adapted prompt formatting via `Component.literal(pack.getPrompt())`.
  - Guarded hash serialization with empty string `""` fallback to eliminate `NullPointerException` risks.
- **Modern Push/Pop Packets (`1.21.1` & `1.21.11`)**:
  - Wired `net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket` and `ClientboundResourcePackPopPacket`.
  - Enforced deterministic UUID derivation via `UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8))` when explicit pack ID is absent.
- **Universal Permission Verification**:
  - Standardized operator check `.requires(source -> source.hasPermission(2))` across all anchors for admin subcommands.

### 3. 5-Pillar Autonomous Headless Testing Harness
Integrated and verified the complete 5-Pillar testing suite across all scaffolded anchors (18/18 passing tests per anchor):
- **Pillar 1 (Chaos Fuzzing)**: Fuzzed `PackConfig` and `PlayerPreferences` under 15,000+ pseudo-random permutations with seed `0xDA51C263L`, asserting zero uncaught crashes on malformed JSON, empty hashes, or abnormal URLs.
- **Pillar 2 (Virtual Concurrency Load)**: Verified lock-free concurrent reads and updates across 50 virtual worker threads without race conditions or memory corruption.
- **Pillar 3 (Brigadier Command AST)**: Executed headless syntax tree validation for `/pack` commands (`list`, `select`, `clear`, `reload`), verifying argument parse trees and suggestion providers.
- **Pillar 4 (GameTest / Packet State Machine)**: Verified packet dispatch sequences and player preference persistence state transitions.
- **Pillar 5 (Sidedness & Hygiene Contract)**: Bytecode audit asserting zero client imports (`net.minecraft.client.*`) in common server classes, 100% single-line GPLv3 license headers, and clean manifest descriptors.

### 4. Build & Distribution Parity
- All 3 subprojects successfully compiled and remapped via `.\gradlew.bat build remapJar`.
- Jars archived and indexed into `MASTER_RELEASE_QUEUE.md`.

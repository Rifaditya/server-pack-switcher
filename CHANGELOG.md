<!-- Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3 -->
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.3] - 2026-10-28

### Added
- Implemented complete Studio 5-Pillar Testing Harness:
  - Pillar 1: Chaos Property & Config Fuzzing (`ServerPackSwitcherStressFuzzTest`) running 20,000+ iterations under Freedom Over Anti-Crash.
  - Pillar 2: Virtual Concurrency Load Test (`ServerPackSwitcherConcurrencyLoadTest`) stressing 50 concurrent worker threads.
  - Pillar 3: Brigadier AST & Command Hierarchy Validation (`ServerPackSwitcherCommandTest`) testing node syntax, suggestions, and operator permissions headlessly.
  - Pillar 4: State Machine Simulation & In-Engine Protocol Suite (`ServerPackSwitcherGameTest`) verifying pack push, swap, pop, and player preference state transitions.
  - Pillar 5: Sidedness & License Hygiene Contract (`ServerPackSwitcherSidednessContractTest`) verifying 0 client references in common code and 100% single-line GPLv3 license headers.

### Changed
- Hardened `PlayerPreferences` and `PackConfig` with `ConcurrentHashMap` and thread-safe file locking guards to guarantee deadlock-free concurrent read/write safety.
- Decoupled configuration paths with `setCustomConfigPath` and `setCustomPrefsPath` supporting isolated unit test runners.

## [1.0.2] - 2026-08-01

### Changed
- Updated default Modrinth configuration example to Fresh Animations.
- Modernized JDK configuration and dependency toolchain for Java 25.
- Aligned SemVer versioning to standard 1.0.2.

## [1.0.0+A-26.2] - 2026-06-14

### Added
- Initial implementation of Server Pack Switcher.
- Added player `/pack` command tree: `apply`, `list`, `pop`, `clear`, and `reload`.
- Dynamic, player-driven server resource pack switching on demand.
- Persistent player pack preferences saved to `config/serverpackswitcher_preferences.json`.
- Direct Modrinth integration for resolving download URLs and SHA-1 hashes dynamically.
- Clean pack swapping mechanism automatically unprompting previous packs.
- Operator administrative commands for forced pack application and live configuration reloading.

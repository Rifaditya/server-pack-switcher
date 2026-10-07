<!-- Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3 -->
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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

# Changelog

All notable changes to the Server Pack Switcher mod for Minecraft 1.20.1 will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0+1.20.1] - 2026-10-29

### Added
- Initial LTS release of Server Pack Switcher for Minecraft 1.20.1 on Fabric.
- Full server-side resource pack switching using in-game commands (`/pack apply`, `/pack list`, `/pack clear`, `/pack reload`).
- Legacy packet adapter using `ClientboundResourcePackPacket` for seamless single-pack switching.
- Persistent player pack preferences across server reconnects and restarts.
- Direct Modrinth CDN auto-resolution and configuration caching.
- Operator permission level 2 gating for administrative and target operations.

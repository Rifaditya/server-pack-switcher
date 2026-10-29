# Changelog

All notable changes to the Server Pack Switcher mod for Minecraft 1.21.11 will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0+1.21.11] - 2026-10-29

### Added
- Initial release of Server Pack Switcher for Minecraft 1.21.11 (Winter Drop) on Fabric.
- Multi-pack push/pop adapter utilizing native `ClientboundResourcePackPushPacket` and `ClientboundResourcePackPopPacket`.
- Full server-side resource pack switching using in-game commands (`/pack apply`, `/pack list`, `/pack clear`, `/pack reload`).
- Persistent player pack preferences across server reconnects and restarts.
- Direct Modrinth CDN auto-resolution and configuration caching.
- Operator permission level 2 gating for administrative and target operations.

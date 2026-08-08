# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [3.0.0-SNAPSHOT-8-8-2026] – 2026-08-08

### Changed
- Ponder is now developed AI-first. Day-to-day feature work, grooming, review and maintenance run through AI agents working directly against this repository, with the maintainers setting direction and approving what lands. The major version bump marks that change in how the project is built — it is not a break in behaviour, configuration or stored data, and existing installations can upgrade in place. Released as `3.0.0-SNAPSHOT-8-8-2026`: the AI-first line has not yet been verified in live operation, and the dated snapshot designation stays until it has.

## [2.0.0]

### Changed

- Split project into three modules: `ponder-bukkit`, `ponder-cache`, `ponder-commands`.
- Generified types of maps, lists, and sets across the project.
- Introduced unit tests.

### Added

- `ponder-bukkit`: `Distance` value class and extension functions for distance-based player lookup.
- `ponder-bukkit`: `Plugin.registerListeners` extension function.
- `ponder-cache`: `Cache`, `CacheManager`, `CacheConfiguration`, `DefaultCache`, `DefaultCacheManager`.
- `ponder-commands`: `Command`, `CommandSender`, `Args`, `CommandService`, `DefaultCommandService`, `DelegatingCommand`, `CommandResult`, `IncorrectUsageFailure`.
- Published artifacts to DansPlugins Maven repository and GitHub Packages.

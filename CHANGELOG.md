# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

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

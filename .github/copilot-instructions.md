# Copilot Instructions

This repository follows the DPC (Dans Plugins Community) conventions defined at
https://github.com/Dans-Plugins/dpc-conventions. Read those conventions before
making any changes.

## Technology Stack

- Language: Java and Kotlin
- Build tool: Gradle (Groovy DSL), multi-module project
- Target platform: Spigot / Paper (Bukkit API)
- Test framework: JUnit 5, MockK

## Project Structure

- `ponder-bukkit/src/main/kotlin/` – Kotlin extension functions for Bukkit (distance utilities, plugin helpers)
- `ponder-cache/src/main/java/` – Generic in-memory cache (`Cache`, `CacheManager`, `CacheConfiguration`)
- `ponder-commands/src/main/java/` – Platform-agnostic command abstraction (`Command`, `CommandService`, `DelegatingCommand`)
- `*/src/test/java/` – Unit tests for each module

## Coding Conventions

- Follow the existing package structure (`preponderous.ponder.*`) when adding new classes.
- Prefer Kotlin for new code in `ponder-bukkit`; use Java for `ponder-cache` and `ponder-commands` to match the existing style.
- Do not hard-code user-facing strings; surface them through the calling plugin's own language files.
- Keep each module focused: `ponder-bukkit` for Bukkit-specific code, `ponder-cache` for caching, `ponder-commands` for command abstractions.

## Contribution Workflow

- Branch from `develop` for all changes.
- Open a pull request against `develop`, not `main`.
- Reference the related GitHub issue in every pull request description.

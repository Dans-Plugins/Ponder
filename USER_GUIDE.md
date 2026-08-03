# User Guide

Ponder is a Java/Kotlin library providing shared utilities for Minecraft (Bukkit/Spigot) plugin development. This guide describes how to add it to your project and start using each module.

## Prerequisites

- Java 17 or later
- A Gradle or Maven build system
- (For `ponder-bukkit`) A Spigot/Paper server project

## First Steps

Add the desired Ponder module(s) to your project by following the [Installation](README.md#installation) section of the README. Then rebuild your project to confirm the dependency resolves correctly.

## Common Scenarios

### Using ponder-cache

`ponder-cache` provides a generic in-memory key/value cache with configurable capacity.

1. Create a `CacheConfiguration` with a name and optional capacity (default 20):

   ```java
   CacheConfiguration<String, MyObject> config = new CacheConfiguration<>("my-cache", 100);
   ```

2. Create a `Cache` from a `CacheManager`, passing in that configuration:

   ```java
   CacheManager manager = new DefaultCacheManager();
   Cache<String, MyObject> cache = manager.createCache(config);
   ```

3. Store and retrieve values:

   ```java
   cache.set("key", myObject);
   MyObject value = cache.get("key");
   ```

4. Create additional caches from the same manager as needed:

   ```java
   Cache<String, OtherObject> otherCache = manager.createCache(new CacheConfiguration<>("other-cache"));
   ```

### Using ponder-commands

`ponder-commands` provides an abstraction layer for dispatching commands without coupling to a specific platform.

1. Implement the `Command` interface for each command.
2. Register commands with a `DefaultCommandService` via `addCommand`.
3. Look up a command by name with `getCommand` and call its `execute` method to run it.
4. Use `DelegatingCommand` to route to sub-commands automatically based on the first argument.

See the [API Reference](COMMANDS.md) for details on every interface and class.

### Using ponder-bukkit

`ponder-bukkit` provides Kotlin extension functions for common Bukkit operations.

**Distance utilities** – check whether locations or players are within a given distance:

```kotlin
val nearbyPlayers = player.findPlayersWithin(10.blocks)
```

**Plugin helpers** – register multiple listeners with a single call:

```kotlin
plugin.registerListeners(myListener, anotherListener)
```

## Permissions

Ponder is a library; it does not register any commands or permission nodes on a server. Permissions are the responsibility of the plugin that depends on Ponder.

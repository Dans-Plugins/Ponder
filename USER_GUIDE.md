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

2. Create a `DefaultCache` backed by that configuration:

   ```java
   Cache<String, MyObject> cache = new DefaultCache<>(config);
   ```

3. Store and retrieve values:

   ```java
   cache.set("key", myObject);
   MyObject value = cache.get("key");
   ```

4. Manage multiple caches with `DefaultCacheManager`:

   ```java
   CacheManager manager = new DefaultCacheManager();
   manager.addCache(cache);
   ```

### Using ponder-commands

`ponder-commands` provides an abstraction layer for dispatching commands without coupling to a specific platform.

1. Implement the `Command` interface for each command.
2. Register commands with a `DefaultCommandService`.
3. Dispatch incoming input through the service's `handle` method.
4. Use `DelegatingCommand` to forward commands to sub-handlers.

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

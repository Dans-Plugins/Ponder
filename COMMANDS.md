# API Reference

Ponder is a library, not a plugin, so it does not add any in-game commands. This document describes the public API exposed by each module.

## ponder-cache

### `Cache<K, V>` (interface)

A generic key/value cache.

| Method | Description |
|--------|-------------|
| `get(K key)` | Returns the value associated with the key, or `null`. |
| `set(K key, V value)` | Stores a value under the given key. |
| `containsKey(K key)` | Returns `true` if the cache holds an entry for the key. |
| `remove(K key)` | Removes the entry for the key. |
| `removeMatching(Predicate<V> predicate)` | Removes all entries whose value matches the predicate. |
| `keys()` | Returns the set of all keys currently in the cache. |
| `clear()` | Removes all entries from the cache. |

### `CacheConfiguration<K, V>`

Immutable configuration for a `Cache` instance.

| Constructor | Description |
|-------------|-------------|
| `CacheConfiguration(String name)` | Creates a configuration with the given name and default capacity (20). |
| `CacheConfiguration(String name, long capacity)` | Creates a configuration with explicit capacity. |

| Method | Description |
|--------|-------------|
| `getName()` | Returns the cache name. |
| `getCapacity()` | Returns the maximum capacity. |

### `DefaultCache<K, V>`

The standard `Cache` implementation backed by a `LinkedHashMap`. Accepts a `CacheConfiguration` at construction time.

### `CacheManager` (interface)

Manages a collection of named caches.

### `DefaultCacheManager`

The standard `CacheManager` implementation.

---

## ponder-commands

### `Command` (interface)

Represents a single executable command.

### `CommandSender` (interface)

Abstraction for the entity that sends a command (e.g. a player or console).

### `Args`

Wrapper around the raw argument string passed to a command. Provides helpers for parsing positional arguments.

### `CommandResult` (sealed class / interface)

Represents the outcome of executing a command.

### `IncorrectUsageFailure`

A `CommandResult` subtype indicating that the command was invoked with invalid arguments.

### `CommandService` (interface)

Accepts raw command input and dispatches it to the appropriate `Command` implementation.

### `DefaultCommandService`

The standard `CommandService` implementation.

### `DelegatingCommand`

A `Command` that forwards execution to a child command based on the first argument (sub-command routing).

---

## ponder-bukkit

All members are Kotlin extension functions/properties in the `preponderous.ponder.minecraft.bukkit` packages.

### Distance utilities (`distance` package)

| Member | Description |
|--------|-------------|
| `Distance` (value class) | Wraps a `Double` representing a block distance. |
| `Int.blocks` | Converts an `Int` to a `Distance`. |
| `Double.blocks` | Converts a `Double` to a `Distance`. |
| `location within distance` (infix) | Returns a `Predicate<Location>` that matches locations within the given distance. Usage: `location within 10.blocks`. |
| `player within distance` (infix) | Returns a `Predicate<Player>` that matches players within the given distance. Usage: `player within 10.blocks`. |
| `Player.findPlayersWithin(distance)` | Returns all players in the same world within the given distance, excluding the receiver. |

### Plugin utilities (`plugin` package)

| Member | Description |
|--------|-------------|
| `Plugin.registerListeners(vararg listeners)` | Registers one or more `Listener` instances with the server's plugin manager. |

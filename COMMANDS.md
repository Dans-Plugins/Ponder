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

Creates `Cache` instances from a name/type or a `CacheConfiguration`.

| Method | Description |
|--------|-------------|
| `createCache(String name, Class<K> keyType, Class<V> valueType, long capacity)` | Creates a new cache with the given name, key/value types, and capacity. |
| `createCache(CacheConfiguration<K, V> config)` | Creates a new cache using the given configuration. |

### `DefaultCacheManager`

The standard `CacheManager` implementation.

---

## ponder-commands

### `Command` (interface)

Represents a single executable command.

### `CommandSender` (interface)

Abstraction for the entity that sends a command (e.g. a player or console).

### `Args`

Extension functions on `Array<out String>` for working with raw command argument arrays (there is no `Args` wrapper class).

| Function | Description |
|----------|-------------|
| `Array<out String>.dropFirst()` | Returns a new array with the first element removed. |
| `Array<out String>.unquote()` | Merges quoted, space-separated arguments (e.g. `"hello world"`) back into single elements. |

### `CommandResult` (sealed class / interface)

Represents the outcome of executing a command.

### `IncorrectUsageFailure`

A `CommandResult` subtype indicating that the command was invoked with invalid arguments.

### `CommandService` (interface)

A registry of named `Command` implementations. It does not dispatch commands itself — callers look up a command by name and call its `execute` method (see `DelegatingCommand` for automatic sub-command routing).

| Method | Description |
|--------|-------------|
| `addCommand(String name, Command command)` | Registers a command under the given name. |
| `getCommand(String name)` | Returns the command registered under the given name, or `null`. |

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

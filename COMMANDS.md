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

The standard `Cache` implementation, backed by a `ConcurrentHashMap`. Its constructor takes a `long capacity` rather than a `CacheConfiguration` — `DefaultCacheManager` reads the capacity off a configuration and passes it through. Once the entry count exceeds the capacity, the least recently accessed entry is evicted on each `set`.

| Constructor | Description |
|-------------|-------------|
| `DefaultCache(long capacity)` | Creates a cache holding at most `capacity` entries. |

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

### `Command` (functional interface)

Represents a single executable command. Being a Kotlin `fun interface`, it can be implemented with a lambda.

| Method | Description |
|--------|-------------|
| `suspend execute(CommandSender sender, vararg String args)` | Runs the command on behalf of the sender and returns a `CommandResult`. Suspending, so it must be called from a coroutine. |

### `CommandSender` (interface)

Abstraction for the entity that sends a command (e.g. a player or console).

### `Args`

Extension functions on `Array<out String>` for working with raw command argument arrays (there is no `Args` wrapper class).

| Function | Description |
|----------|-------------|
| `Array<out String>.dropFirst()` | Returns a new array with the first element removed. |
| `Array<out String>.unquote()` | Merges quoted, space-separated arguments (e.g. `"hello world"`) back into single elements. |

### `CommandResult` (sealed interface)

Represents the outcome of executing a command. Two implementations ship in the same file.

| Member | Description |
|--------|-------------|
| `CommandSuccess` (object) | Singleton indicating the command completed successfully. |
| `CommandFailure` (open class) | Base type for failures. Being `open`, it can be subclassed to describe a specific failure. |

### `IncorrectUsageFailure`

A `CommandFailure` subtype indicating that the command was invoked with invalid arguments.

### `CommandService` (interface)

A registry of named `Command` implementations. It does not dispatch commands itself — callers look up a command by name and call its `execute` method (see `DelegatingCommand` for automatic sub-command routing).

| Method | Description |
|--------|-------------|
| `addCommand(String name, Command command)` | Registers a command under the given name. |
| `getCommand(String name)` | Returns the command registered under the given name, or `null`. |

### `DefaultCommandService`

The standard `CommandService` implementation.

### `DelegatingCommand`

A `Command` that forwards execution to a sub-command selected by the first argument (sub-command routing). It also implements `CommandService`, so its sub-commands can be added and looked up after construction.

| Member | Description |
|--------|-------------|
| `DelegatingCommand(Map<String, Command> subcommands, String usageMessage)` | Creates a delegating command over a copy of the given sub-command map. |
| `suspend execute(CommandSender sender, vararg String args)` | Strips the first argument and runs the matching sub-command with the remainder. When the arguments are empty or no sub-command matches, `usageMessage` is sent to the sender and an `IncorrectUsageFailure` is returned. |
| `addCommand(String name, Command command)` | Registers a sub-command under the given name. |
| `getCommand(String name)` | Returns the sub-command registered under the given name, or `null`. |

---

## ponder-bukkit

Members live in the `preponderous.ponder.minecraft.bukkit` packages and are, apart from the `Distance` value class itself, Kotlin extension functions and properties.

### Distance utilities (`distance` package)

| Member | Description |
|--------|-------------|
| `Distance` (value class) | Wraps a `Double` representing a block distance. |
| `Distance.valueSquared` | The wrapped value multiplied by itself, so comparisons can avoid a square root. |
| `Int.blocks` | Converts an `Int` to a `Distance`. |
| `Double.blocks` | Converts a `Double` to a `Distance`. |
| `location within distance` (infix) | Returns a `Predicate<Location>` matching locations within the given distance of the receiver. |
| `player within distance` (infix) | Returns a `Predicate<Player>` matching players within the given distance of the receiver. |
| `predicate of value` (infix) | Applies a `Predicate<T>` to a value and returns the `Boolean` result. |
| `Player.findPlayersWithin(distance)` | Returns all players in the same world within the given distance, excluding the receiver. |

The `within` functions return a `Predicate` rather than a `Boolean`, so a comparison is written by applying that predicate with `of`. Both infix functions have the same precedence and associate to the left, so no parentheses are needed:

```kotlin
if (playerA within 10.blocks of playerB) {
    // ...
}

if (someLocation within 5.blocks of anotherLocation) {
    // ...
}
```

### Plugin utilities (`plugin` package)

| Member | Description |
|--------|-------------|
| `Plugin.registerListeners(vararg listeners)` | Registers one or more `Listener` instances with the server's plugin manager. |

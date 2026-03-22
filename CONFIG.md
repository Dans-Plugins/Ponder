# Configuration Guide

Ponder is a library and does not ship a `config.yml` of its own. Configuration is handled programmatically via the `CacheConfiguration` class in the `ponder-cache` module.

## ponder-cache Configuration

### `CacheConfiguration`

Use `CacheConfiguration` to configure a `Cache` instance before constructing it.

---

## name

**Type:** `String`  
**Required:** Yes  
**Description:** A human-readable identifier for the cache. Used for logging and lookup via `CacheManager`.

**Example:**

```java
new CacheConfiguration<>("player-data");
```

---

## capacity

**Type:** `long`  
**Default:** `20`  
**Description:** The maximum number of entries the cache will hold. When capacity is reached, the oldest entry is evicted automatically.

**Example:**

```java
new CacheConfiguration<>("player-data", 500);
```

---

## Full Example

```java
// Cache with default capacity (20)
CacheConfiguration<UUID, PlayerData> smallConfig =
    new CacheConfiguration<>("session-cache");

// Cache with explicit capacity
CacheConfiguration<UUID, PlayerData> largeConfig =
    new CacheConfiguration<>("player-data", 1000);

Cache<UUID, PlayerData> cache = new DefaultCache<>(largeConfig);
```

# Configuration Guide

Ponder is a library and does not ship a `config.yml` of its own. Configuration is handled programmatically via the `CacheConfiguration` class in the `ponder-cache` module.

## ponder-cache Configuration

### `CacheConfiguration`

Use `CacheConfiguration` to configure a `Cache` instance before constructing it.

---

## name

**Type:** `String`  
**Required:** Yes  
**Description:** A human-readable identifier for the cache. It is stored on the configuration but is not currently read by `DefaultCache` or `DefaultCacheManager` (no logging or lookup-by-name behavior yet).

**Example:**

```java
new CacheConfiguration<>("player-data");
```

---

## capacity

**Type:** `long`  
**Default:** `20`  
**Description:** The maximum number of entries the cache will hold. When capacity is exceeded, the least recently accessed entry is evicted automatically (an LRU policy — calling `get()` on an entry refreshes it and protects it from being the next eviction).

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

CacheManager cacheManager = new DefaultCacheManager();
Cache<UUID, PlayerData> cache = cacheManager.createCache(largeConfig);
```

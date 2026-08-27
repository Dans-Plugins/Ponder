package preponderous.ponder.cache;

import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public final class DefaultCache<K, V> implements Cache<K, V> {

    private final Map<K, CacheRecord<V>> records = new ConcurrentHashMap<>();
    private final long capacity;

    public DefaultCache(long capacity) {
        this.capacity = capacity;
    }

    private static final class CacheRecord<V> {
        private final V value;
        private Instant lastAccess;

        private CacheRecord(V value, Instant lastAccess) {
            this.value = value;
            this.lastAccess = lastAccess;
        }

        private CacheRecord(V value) {
            this(value, Instant.now());
        }

        public V value() {
            return value;
        }

        public Instant lastAccess() {
            return lastAccess;
        }

        public void setLastAccess(Instant lastAccess) {
            this.lastAccess = lastAccess;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (CacheRecord) obj;
            return Objects.equals(this.value, that.value) &&
                    Objects.equals(this.lastAccess, that.lastAccess);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, lastAccess);
        }

        @Override
        public String toString() {
            return "CacheRecord[" +
                    "value=" + value + ", " +
                    "lastAccess=" + lastAccess + ']';
        }
    }

    @Override
    public V get(K key) {
        CacheRecord<V> record = records.get(key);
        if (record == null) return null;
        record.lastAccess = Instant.now();
        return record.value;
    }

    @Override
    public void set(K key, V value) {
        records.put(key, new CacheRecord<>(value));
        while (records.size() > capacity) {
            Map.Entry<K, CacheRecord<V>> oldestEntry = records.entrySet().stream().min(Comparator.comparing(entry -> entry.getValue().lastAccess())).orElse(null);
            if (oldestEntry != null) {
                records.remove(oldestEntry.getKey());
            }
        }
    }

    @Override
    public boolean containsKey(K key) {
        return records.containsKey(key);
    }

    @Override
    public void remove(K key) {
        records.remove(key);
    }

    @Override
    public void removeMatching(Predicate<V> predicate) {
        records.entrySet().stream()
                .filter(entry -> predicate.test(entry.getValue().value()))
                .forEach(entry -> remove(entry.getKey()));
    }

    @Override
    public Set<K> keys() {
        // Copied rather than returned directly: records.keySet() is a live, mutable view of
        // the backing map, through which a caller could evict entries without going through
        // the Cache interface.
        return Set.copyOf(records.keySet());
    }

    @Override
    public void clear() {
        records.clear();
    }
}

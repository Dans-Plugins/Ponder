package preponderous.ponder.cache;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public final class DefaultCache<K, V> implements Cache<K, V> {

    private final Map<K, CacheRecord<V>> records = new ConcurrentHashMap<>();
    private final long capacity;
    // Orders accesses by a strictly increasing sequence number rather than the wall clock:
    // two Instant.now() calls can return the same value, and a tie let eviction pick the
    // entry that was just set instead of the least recently accessed one.
    private final AtomicLong accessSequence = new AtomicLong();

    public DefaultCache(long capacity) {
        this.capacity = capacity;
    }

    private static final class CacheRecord<V> {
        private final V value;
        private long lastAccess;

        private CacheRecord(V value, long lastAccess) {
            this.value = value;
            this.lastAccess = lastAccess;
        }

        public V value() {
            return value;
        }

        public long lastAccess() {
            return lastAccess;
        }

        public void setLastAccess(long lastAccess) {
            this.lastAccess = lastAccess;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (CacheRecord) obj;
            return Objects.equals(this.value, that.value) &&
                    this.lastAccess == that.lastAccess;
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
        record.lastAccess = accessSequence.incrementAndGet();
        return record.value;
    }

    @Override
    public void set(K key, V value) {
        records.put(key, new CacheRecord<>(value, accessSequence.incrementAndGet()));
        while (records.size() > capacity) {
            Map.Entry<K, CacheRecord<V>> oldestEntry = records.entrySet().stream().min(Comparator.comparingLong(entry -> entry.getValue().lastAccess())).orElse(null);
            // With a negative capacity the map empties while still over capacity, so the
            // loop must stop once there is nothing left to evict.
            if (oldestEntry == null) break;
            records.remove(oldestEntry.getKey());
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

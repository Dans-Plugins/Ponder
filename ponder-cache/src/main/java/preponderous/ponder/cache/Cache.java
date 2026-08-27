package preponderous.ponder.cache;

import java.util.Set;
import java.util.function.Predicate;

public interface Cache<K, V> {

    V get(K key);
    void set(K key, V value);
    boolean containsKey(K key);
    void remove(K key);
    void removeMatching(Predicate<V> predicate);

    /**
     * Returns an immutable snapshot of the keys held at the moment of the call. Later changes
     * to the cache are not reflected in the returned set, and the returned set cannot be used
     * to modify the cache.
     */
    Set<K> keys();

    void clear();

}

package preponderous.ponder.cache;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public final class DefaultCacheTests {

    @Test
    public void initializesAtEmptyState() {
        Cache<String, String> underTest = new DefaultCache<>(1);
        assertTrue(underTest.keys().isEmpty());
    }

    @Test
    public void insertedValueCanBeRetrieved() {
        Cache<String, String> underTest = new DefaultCache<>(1);
        underTest.set("key", "value");
        assertEquals("value", underTest.get("key"));
    }

    @Test
    public void oldestEntryRemovedWhenCapacitySurpassed() {
        Cache<String, String> underTest = new DefaultCache<>(1);
        underTest.set("key1", "value1");
        underTest.set("key2", "value2");
        assertTrue(underTest.containsKey("key2"));
        assertFalse(underTest.containsKey("key1"));
    }

    @Test
    public void missingKeyIsRetrievedAsNull() {
        Cache<String, String> underTest = new DefaultCache<>(1);
        assertNull(underTest.get("absent"));
    }

    @Test
    public void missingKeyIsNotContained() {
        Cache<String, String> underTest = new DefaultCache<>(1);
        underTest.set("key", "value");
        assertFalse(underTest.containsKey("absent"));
    }

    @Test
    public void repeatedInsertOfSameKeyOverwritesValue() {
        Cache<String, String> underTest = new DefaultCache<>(2);
        underTest.set("key", "first");
        underTest.set("key", "second");
        assertEquals("second", underTest.get("key"));
        assertEquals(1, underTest.keys().size());
    }

    @Test
    public void removedEntryIsNoLongerContained() {
        Cache<String, String> underTest = new DefaultCache<>(2);
        underTest.set("key1", "value1");
        underTest.set("key2", "value2");
        underTest.remove("key1");
        assertFalse(underTest.containsKey("key1"));
        assertTrue(underTest.containsKey("key2"));
    }

    @Test
    public void removalOfAbsentKeyLeavesCacheUnchanged() {
        Cache<String, String> underTest = new DefaultCache<>(2);
        underTest.set("key", "value");
        underTest.remove("absent");
        assertEquals(Set.of("key"), underTest.keys());
    }

    @Test
    public void onlyEntriesMatchingPredicateAreRemoved() {
        Cache<String, String> underTest = new DefaultCache<>(3);
        underTest.set("key1", "keep");
        underTest.set("key2", "discard");
        underTest.set("key3", "discard");
        underTest.removeMatching(value -> value.equals("discard"));
        assertEquals(Set.of("key1"), underTest.keys());
    }

    @Test
    public void clearRemovesEveryEntry() {
        Cache<String, String> underTest = new DefaultCache<>(3);
        underTest.set("key1", "value1");
        underTest.set("key2", "value2");
        underTest.clear();
        assertTrue(underTest.keys().isEmpty());
    }

    @Test
    public void keysReportEveryInsertedKey() {
        Cache<String, String> underTest = new DefaultCache<>(3);
        underTest.set("key1", "value1");
        underTest.set("key2", "value2");
        assertEquals(Set.of("key1", "key2"), underTest.keys());
    }

    @Test
    public void retrievalProtectsEntryFromBeingEvictedNext() throws InterruptedException {
        Cache<String, String> underTest = new DefaultCache<>(2);
        underTest.set("key1", "value1");
        // Eviction orders entries by their last-access Instant, so the operations under
        // test are separated far enough apart for the system clock to distinguish them.
        Thread.sleep(10);
        underTest.set("key2", "value2");
        Thread.sleep(10);
        underTest.get("key1");
        Thread.sleep(10);
        underTest.set("key3", "value3");
        assertTrue(underTest.containsKey("key1"));
        assertFalse(underTest.containsKey("key2"));
        assertTrue(underTest.containsKey("key3"));
    }

    @Test
    public void insertionEvictsDownToCapacityWhenOverfilled() {
        Cache<String, String> underTest = new DefaultCache<>(2);
        underTest.set("key1", "value1");
        underTest.set("key2", "value2");
        underTest.set("key3", "value3");
        assertEquals(2, underTest.keys().size());
        assertTrue(underTest.containsKey("key3"));
    }

}

package preponderous.ponder.cache;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class DefaultCacheManagerTests {

    @Test
    public void createsCacheGivenParameters() {
        CacheManager underTest = new DefaultCacheManager();
        Cache<String, String> result = underTest.createCache("cache", String.class, String.class, 20);
        assertNotNull(result);
    }

    @Test
    public void createsCacheGivenConfiguration() {
        CacheManager underTest = new DefaultCacheManager();
        Cache<String, String> result = underTest.createCache(new CacheConfiguration<>("cache", 20));
        assertNotNull(result);
    }

    @Test
    public void appliesCapacityGivenAsParameter() {
        CacheManager underTest = new DefaultCacheManager();
        Cache<String, String> result = underTest.createCache("cache", String.class, String.class, 1);
        result.set("key1", "value1");
        result.set("key2", "value2");
        assertEquals(Set.of("key2"), result.keys());
    }

    @Test
    public void appliesCapacityGivenByConfiguration() {
        CacheManager underTest = new DefaultCacheManager();
        Cache<String, String> result = underTest.createCache(new CacheConfiguration<>("cache", 1));
        result.set("key1", "value1");
        result.set("key2", "value2");
        assertEquals(Set.of("key2"), result.keys());
    }

    @Test
    public void createsIndependentCaches() {
        CacheManager underTest = new DefaultCacheManager();
        Cache<String, String> first = underTest.createCache(new CacheConfiguration<>("cache", 20));
        Cache<String, String> second = underTest.createCache(new CacheConfiguration<>("cache", 20));
        first.set("key", "value");
        assertTrue(second.keys().isEmpty());
    }

}

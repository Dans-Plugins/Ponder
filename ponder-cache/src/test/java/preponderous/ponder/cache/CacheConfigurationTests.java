package preponderous.ponder.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CacheConfigurationTests {

    @Test
    public void retainsGivenNameAndCapacity() {
        CacheConfiguration<String, String> underTest = new CacheConfiguration<>("cache", 500);
        assertEquals("cache", underTest.getName());
        assertEquals(500L, underTest.getCapacity());
    }

    @Test
    public void defaultsCapacityWhenOnlyNameIsGiven() {
        CacheConfiguration<String, String> underTest = new CacheConfiguration<>("cache");
        assertEquals("cache", underTest.getName());
        assertEquals(20L, underTest.getCapacity());
    }

}

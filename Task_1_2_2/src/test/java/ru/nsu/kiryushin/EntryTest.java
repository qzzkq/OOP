package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

public class EntryTest {

    @Test
    void constructorAndGetters() {
        Entry<String, Integer> e = new Entry<>("key", 42);
        assertEquals("key", e.getKey());
        assertEquals(42, e.getValue());
    }

    @Test
    void setValueUpdatesValue() {
        Entry<String, Integer> e = new Entry<>("key", 1);
        e.setValue(10);
        assertEquals(10, e.getValue());
    }

    @Test
    void equalsAndHashCodeForSameKeyAndValue() {
        Entry<String, Integer> e1 = new Entry<>("key", 42);
        Entry<String, Integer> e2 = new Entry<>("key", 42);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentKeyOrValue() {
        Entry<String, Integer> e1 = new Entry<>("key", 42);
        Entry<String, Integer> e2 = new Entry<>("other", 42);
        Entry<String, Integer> e3 = new Entry<>("key", 43);

        assertNotEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertNotEquals(e2, e3);
    }

    @Test
    void equalsWithSelfNullAndOtherType() {
        Entry<String, Integer> e = new Entry<>("key", 42);

        assertEquals(e, e);
        assertNotEquals(e, null);
        assertNotEquals(e, "some string");
    }

    @Test
    void toStringFormat() {
        Entry<String, Integer> e = new Entry<>("k", 7);
        assertEquals("[KEY = k, VALUE = 7]", e.toString());
    }
}

package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

class HashTableTest {

    @Test
    void addGetContainsAndDuplicate() {
        HashTable<Integer, String> table = new HashTable<>();

        assertTrue(table.addPair(1, "one"));
        assertTrue(table.addPair(2, "two"));
        assertEquals("one", table.getValue(1));
        assertEquals("two", table.getValue(2));

        assertTrue(table.containsKey(1));
        assertTrue(table.containsKey(2));
        assertFalse(table.containsKey(3));
        assertNull(table.getValue(3));

        int before = 0;
        for (Entry<Integer, String> ignored : table) {
            before++;
        }

        assertFalse(table.addPair(1, "newOne"));

        int after = 0;
        for (Entry<Integer, String> ignored : table) {
            after++;
        }

        assertEquals(before, after);
        assertEquals("one", table.getValue(1));
    }

    @Test
    void updatePairExistingAndMissing() {
        HashTable<String, String> table = new HashTable<>();
        table.addPair("k1", "v1");

        assertTrue(table.updatePair("k1", "v1_updated"));
        assertEquals("v1_updated", table.getValue("k1"));

        assertFalse(table.updatePair("k2", "v2"));
        assertFalse(table.containsKey("k2"));
    }

    @Test
    void removePairExistingAndMissing() {
        HashTable<String, String> table = new HashTable<>();
        table.addPair("a", "1");
        table.addPair("b", "2");

        String removed = table.removePair("a");
        assertEquals("1", removed);
        assertFalse(table.containsKey("a"));
        assertTrue(table.containsKey("b"));

        assertNull(table.removePair("not-exists"));
        assertTrue(table.containsKey("b"));
    }

    @Test
    void resizeKeepsAllEntries() {
        HashTable<Integer, String> table = new HashTable<>();

        for (int i = 0; i < 20; i++) {
            assertTrue(table.addPair(i, "v" + i));
        }

        for (int i = 0; i < 20; i++) {
            assertTrue(table.containsKey(i));
            assertEquals("v" + i, table.getValue(i));
        }

        int cnt = 0;
        for (Entry<Integer, String> ignored : table) {
            cnt++;
        }
        assertEquals(20, cnt);
    }

    @Test
    void iteratorTraversesAllEntries() {
        HashTable<Integer, String> table = new HashTable<>();
        table.addPair(1, "one");
        table.addPair(2, "two");
        table.addPair(3, "three");

        Set<Integer> keys = new HashSet<>();
        Set<String> values = new HashSet<>();

        for (Entry<Integer, String> e : table) {
            keys.add(e.getKey());
            values.add(e.getValue());
        }

        assertEquals(Set.of(1, 2, 3), keys);
        assertEquals(Set.of("one", "two", "three"), values);
    }

    @Test
    void iteratorOnEmptyTable() {
        HashTable<Integer, String> table = new HashTable<>();
        Iterator<Entry<Integer, String>> it = table.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorFailFastOnModification() {
        HashTable<Integer, String> table = new HashTable<>();
        table.addPair(1, "one");
        table.addPair(2, "two");

        Iterator<Entry<Integer, String>> it = table.iterator();
        assertTrue(it.hasNext());

        table.addPair(3, "three");

        assertThrows(ConcurrentModificationException.class, it::hasNext);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void equalsAndHashCode() {
        HashTable<Integer, String> a = new HashTable<>();
        HashTable<Integer, String> b = new HashTable<>();
        HashTable<Integer, String> c = new HashTable<>();

        a.addPair(1, "one");
        a.addPair(2, "two");

        b.addPair(1, "one");
        b.addPair(2, "two");

        c.addPair(1, "one");
        c.addPair(2, "TWO");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

    @Test
    void toStringContainsBasicInfo() {
        HashTable<String, String> table = new HashTable<>();
        table.addPair("k1", "v1");
        table.addPair("k2", "v2");

        String s = table.toString();
        assertTrue(s.contains("HashTable"));
        assertTrue(s.contains("size = 2"));
        assertTrue(s.contains("capacity ="));
        assertTrue(s.contains("KEY = k1"));
        assertTrue(s.contains("VALUE = v1"));
    }
}
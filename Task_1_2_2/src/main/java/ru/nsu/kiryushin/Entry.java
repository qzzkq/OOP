package ru.nsu.kiryushin;

import java.util.Objects;

/**
 * Implementation of entry in HashTable.
 *
 * @param <K> type of key
 * @param <V> type of value
 */
public class Entry<K, V>  {
    private final K key;
    private V value;

    /**
     * Constructor for entry
     * @param key key
     * @param value value
     */
    Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V newValue) {
        this.value = newValue;
    }

    @Override
    public String toString(){
        return "[KEY = " + this.key + ", VALUE = " + this.value + ']';
    }

    @Override
    public boolean equals(Object object){
        if (this == object) {
            return true;
        }
        if (!(object instanceof Entry<?, ?> other)) {
            return false;
        }
        return Objects.equals(key, other.key) &&
                Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}

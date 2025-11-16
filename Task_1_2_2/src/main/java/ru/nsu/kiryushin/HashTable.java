package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Generic hash table implementation based on separate chaining
 * (array of buckets with linked lists).
 * Supports basic operations by key: add, remove, update, lookup,
 * containment check, iteration with fail-fast behavior
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public class HashTable<K, V> implements Iterable<Entry<K, V>> {
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private int capacity;
    private int modCount = 0;

    private List<List<Entry<K, V>>> table;

    /**
     * Hashtable's constructor;
     */
    public HashTable(){
        this.capacity = 16;
        this.size = 0;
        this.table = new ArrayList<>(this.capacity);
        for (int i = 0; i < this.capacity; i++){
            table.add(new LinkedList<>());
        }
    }

    /**
     * Adds a new key-value pair to the hash table.
     *
     * @param key key
     * @param value value
     * @return {@code true} if if a new pair was added,
     *         {@code false} if a pair with the same key already exists
     */
    public boolean addPair(K key, V value){
        int index = hashCompute(key, this.capacity);
        List<Entry<K, V>> entries = table.get(index);
        for (Entry<K, V> entry : entries){
            if (entry.getKey().equals(key)){
                return false;
            }
        }
        entries.add(new Entry<K, V>(key, value));
        this.size++;
        this.modCount++;
        if (this.size >= this.capacity * LOAD_FACTOR){
            resize();
        }
        return true;
    }

    /**
     * Removes the key–value pair associated with the key from the hash table.
     * @param key key
     * @return the value that was associated with the specified key,
     *         or {@code null} if there was no mapping for the key
     */
    public V removePair(K key){
        int index = hashCompute(key, this.capacity);
        List<Entry<K, V>> entries = table.get(index);

        Iterator<Entry<K, V>> it = entries.iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            if (Objects.equals(entry.getKey(), key)) {
                it.remove();
                this.size--;
                this.modCount++;
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Updates the value associated with the specified key.
     * @param key key
     * @param value value
     * @return {@code true} if the value was updated,
     *         {@code false} if there is no entry with the given key
     */
    public boolean updatePair(K key, V value){
        int index = hashCompute(key, this.capacity);
        List<Entry<K, V>> entries = table.get(index);
        for (Entry<K, V> entry : entries){
            if (entry.getKey().equals(key)){
                entry.setValue(value);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value associated with the specified key.
     * @param key key
     * @return the value associated with the specified key,
     *         or {@code null} if there is no entry with the given key
     */
    public V getValue(K key){
        int index = hashCompute(key, this.capacity);
        List<Entry<K, V>> entries = table.get(index);

        for (Entry<K, V> entry : entries){
            if (entry.getKey().equals(key)){
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * Checks whether the table contains the specified key.
     * @param key key
     * @return {@code true} if an entry with this key exists,
     *         {@code false} otherwise
     */
    public boolean containsKey(K key){
        return getValue(key) != null;
    }

    /**
     * Doubles the capacity of the table and rehashes all entries.
     * This is an internal method that redistributes all existing
     * key–value pairs according to the new capacity.
     */
    private void resize(){
        int newCapacity = this.capacity * 2;

        List<List<Entry<K, V>>> newTable = new ArrayList<>(newCapacity);

        for (int i = 0; i < newCapacity; i++){
            newTable.add(new LinkedList<>());
        }

        for (List<Entry<K, V>> entries : table){
            for (Entry<K, V> entry : entries){
                K key = entry.getKey();
                int newIndex = hashCompute(key, newCapacity);
                newTable.get(newIndex).add(entry);
            }
        }
        this.capacity = newCapacity;
        this.table = newTable;
        modCount++;
    }

    /**
     * Computes bucket index for the {@code key}.
     * Mixes hash bits to reduce collisions, makes it non-negative
     * and maps it into {@code [0, capacity)}.
     *
     * @param key key
     * @param capacity capacity
     * @return index of the bucket for this key in range {@code [0, capacity)}
     */
    private int hashCompute(K key, int capacity){
        int hash = key.hashCode();
        hash ^= (hash >>> 16);
        return (hash & 0x7fffffff) % capacity;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<Entry<K, V>> {

        private int bucketIndex = 0;
        private int entryIndex = 0;
        private final int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            while (bucketIndex < capacity) {
                List<Entry<K, V>> bucket = table.get(bucketIndex);
                if (entryIndex < bucket.size()) {
                    return true;
                }
                else {
                    bucketIndex++;
                    entryIndex = 0;
                }
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Entry<K, V> res = table.get(bucketIndex).get(entryIndex);
            entryIndex++;
            return res;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Entry<K, V> e : this) {
            hash += e.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        HashTable<K, V> other = (HashTable<K, V>) o;

        if (this.size != other.size) {
            return false;
        }

        for (Entry<K, V> entry : this) {
            K key = entry.getKey();
            V value = entry.getValue();
            V otherValue = other.getValue(key);

            if (!Objects.equals(value, otherValue)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashTable").append('\n');
        sb.append("size = ").append(size).append('\n');
        sb.append("capacity = ").append(capacity).append('\n');

        for (int i = 0; i < capacity; i++) {
            sb.append(i).append(" -> ");
            List<Entry<K, V>> bucket = table.get(i);

            if (bucket.isEmpty()) {
                sb.append("empty");
            } else {
                for (int j = 0; j < bucket.size(); j++) {
                    sb.append(bucket.get(j).toString());
                    if (j < bucket.size() - 1) {
                        sb.append(" - ");
                    }
                }
            }
            sb.append('\n');
        }

        return sb.toString();
    }

}

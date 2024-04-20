package ch.zhaw.ads;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class MyHashtable<K, V> implements Map<K, V> {
    private K[] keys;
    private V[] values;

    private int size;
    private final double LOAD_FACTOR = 0.75;

    private int hash(Object k) {
        int h = Math.abs(k.hashCode());
        return h % keys.length;
    }

    public MyHashtable(int size) {
        keys = (K[]) new Object[size];
        values = (V[]) new Object[size];
    }

    // Removes all mappings from this map (optional operation).
    public void clear() {
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
    }

    // Associates the specified value with the specified key in this map (optional operation).
    public V put(K key, V value) {
        if (size >= keys.length * LOAD_FACTOR) {
            resize();
        }
        int h = findPos(key);
        if (values[h] == null) {
            size++;
        }
        keys[h] = key;
        values[h] = value;
        return value;
    }

    private void resize() {
        int newSize = keys.length * 2;
        K[] oldKeys = keys;
        V[] oldValues = values;
        keys = (K[]) new Object[newSize];
        values = (V[]) new Object[newSize];
        size = 0;

        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }

    // Returns the value to which this map maps the specified key.
    public V get(Object key) {
        int h = findPos(key);
        if (keys[h] != null) return values[h];
        return null;
    }

    // Removes the mapping for this key from this map if present (optional operation).
    public V remove(Object key) {
        int h = findPos(key);
        if (values[h] == null) {
            return null;
        }
        var toRemove = values[h];
        values[h] = null;
        size--;
        return toRemove;
    }

    private int findPos(Object key) {
        int collisionNum = 0;
        int h = hash(key);
        while (h > 0 && keys[h] != null && !keys[h].equals(key)) {
            h += 2 * ++collisionNum -1;
            h = h % keys.length;
        }
        return h;
    }

    // Returns the number of key-value mappings in this map.
    public int size() {
        return size;
    }

    // UnsupportedOperationException ===================================================================
    // Returns a collection view of the values contained in this map.
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    // Returns true if this map contains a mapping for the specified key.
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    // Returns true if this map maps one or more keys to the specified value.
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    // Returns a set view of the mappings contained in this map.
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    // Compares the specified object with this map for equality.
    public boolean equals(Object o) {
        throw new UnsupportedOperationException();
    }

    // Copies all of the mappings from the specified map to this map (optional operation).
    public void putAll(Map<? extends K, ? extends V> t) {
        throw new UnsupportedOperationException();
    }

    // Returns the hash code value for this map.
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    // Returns true if this map contains no key-value mappings.
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    // Returns a set view of the keys contained in this map.
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }
}

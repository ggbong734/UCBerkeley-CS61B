import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;

/** @author Gerry Bong
 * Codes are adapted from yngz github
 * https://github.com/yngz/cs61b/blob/master/lab8/MyHashMap.java
 *
 *  This is for Lab 8 of CS 61B of Spring 2019
 */

public class MyHashMap<K, V> implements Map61B<K, V> {
    private int size;
    private int hashBins;
    private double loadFactor;
    private ArrayList<Entry>[] bins;
    private HashSet<K> keySet;

    public MyHashMap() {
        this(16);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        if (initialSize < 1 || loadFactor <= 0.0) {
            throw new IllegalArgumentException();
        }

        size = 0;
        hashBins = initialSize;
        this.loadFactor = loadFactor;
        keySet = new HashSet<>();
        bins = (ArrayList<Entry>[]) new ArrayList[hashBins];

        for (int i = 0; i < hashBins; i++) {
            bins[i] = new ArrayList<>();
        }
    }

    public class Entry {
        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int hash(K key, int numOfBins) {
        // the bitwise operation forces the number to be positive
        // Integer is represented by 32 bits in java or 32 ones.
        // the left most bit which indicates if a number is positive
        // or negative is forced to zero to indicate positive
        // number. 7fffffff represents 31 ones ('1').
        // f = 1111 while 7 = 111

        return (key.hashCode() & 0x7fffffff) % numOfBins;
    }

    @Override
    public void clear() {
        keySet = new HashSet<>();
        bins = (ArrayList<Entry>[]) new ArrayList[hashBins];
        hashBins = 0;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return keySet.contains(key);
    }

    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        int index = hash(key, hashBins);
        for (Entry entry : bins[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {

        if (containsKey(key)) {
            // loop through each Entry in the bin to
            // update value of the correct Entry object
            int index = hash(key, hashBins);
            for (Entry entry: bins[index]) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                }
            }
        } else {
            if (loadFactor <= 1.0 * size / hashBins) {
                resize(hashBins * 2);
            }
            int index = hash(key, hashBins);
            keySet.add(key);
            bins[index].add(new Entry(key, value));
            size += 1;
        }
    }


    // resize the hash table to have the given number of chains,
    // rehashing all of the keys
    private void resize(int newCapacity) {
        ArrayList<Entry>[] newBins = (ArrayList<Entry>[]) new ArrayList[newCapacity];

        for (int i = 0; i < newCapacity; i++) {
            newBins[i] = new ArrayList<>();
        }

        for (K key : keySet) {
            int index = hash(key, newCapacity);
            newBins[index].add(new Entry(key, get(key)));
        }
        this.hashBins = newCapacity;
        this.bins = newBins;
    }

    @Override
    public Set<K> keySet() {
        return keySet;

    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }

    @Override
    public V remove(K key) {
        int index = hash(key, hashBins);
        int indexToRemove = -999;
        // check if key exists in hash map
        for (int i = 0; i < bins[index].size(); i++) {
            if (bins[index].get(i).key.equals(key)) {
                indexToRemove = i;
            }
        }

        // remove item from hash map and return removed item
        if (indexToRemove >= 0) {
            V toRemove = bins[index].get(indexToRemove).value;
            System.out.println(bins[index].get(indexToRemove).key);
            bins[index].remove(indexToRemove);
            keySet.remove(key);    // item from keyset
            size -= 1;
            return toRemove;
        }
        //if key does not exist in has hmap, return null
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return remove(key);
    }
}

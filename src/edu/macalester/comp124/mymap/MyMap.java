package edu.macalester.comp124.mymap;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple implementation of a hashtable.
 *
 * @author shilad
 *
 * @param <K> Class for keys in the table.
 * @param <V> Class for values in the table.
 */
public class MyMap <K, V> {
	private static final int INITIAL_SIZE = 4;
	
	/** The table is package-protected so that the unit test can examine it. */
	List<MyEntry<K, V>> [] buckets;
	
	/** Number of unique entries (e.g. keys) in the table */
	private int numEntries = 0;
	
	/** Threshold that determines when the table should expand */
	private double loadFactor = 0.75;
	
	/**
	 * Initializes the data structures associated with a new hashmap.
	 */
	public MyMap() {
		buckets = newArrayOfEntries(INITIAL_SIZE);
	}
	
	/**
	 * Returns the number of unique entries (e.g. keys) in the table.
	 * @return the number of entries.
	 */
	public int size() {
		return numEntries;
	}
	
	/**
	 * Adds a new key, value pair to the table.
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		expandIfNecessary();
        int code = Math.abs(key.hashCode());
        int index = code%buckets.length;
        List<MyEntry<K,V>> bucket = buckets[index];
        boolean in = false;
        for (int i = 0; i<bucket.size(); i++){
            MyEntry<K,V> entry = bucket.get(i);
            if (key.equals(entry.getKey())){
                in = true;
                entry.setValue(value);
                break;
            }
        }
        if (!in){
            MyEntry<K,V> pair = new MyEntry<>(key, value);
            bucket.add(pair);
            numEntries++;
        }
	}
	
	/**
	 * Returns the value associated with the specified key, or null if it
	 * doesn't exist.
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key) {
        int code = Math.abs(key.hashCode());
        int index = code%buckets.length;
        List<MyEntry<K,V>> bucket = buckets[index];
        for (int i = 0; i<bucket.size(); i++){
            MyEntry<K,V> entry = bucket.get(i);
            if (key.equals(entry.getKey())){
                V value = entry.getValue();
                return value;
            }
        }
		return null;
	}
	
	/**
	 * Expands the table to double the size, if necessary.
	 */
	private void expandIfNecessary() {
        int curSize = buckets.length;
        double entries = numEntries;
        double percentFull = entries/curSize;
        if (percentFull>loadFactor){
            List<MyEntry<K, V>> [] old = buckets;
            buckets = newArrayOfEntries(curSize*2);
            int transferred = 0;
            for (int i = 0; i<curSize; i++){
                List<MyEntry<K,V>> bucket = old[i];
                if (!bucket.isEmpty()){
                    for (int j = 0; j<bucket.size(); j++){
                        MyEntry<K,V> data = bucket.get(j);
                        K key = data.getKey();
                        int code = Math.abs(key.hashCode());
                        int index = code%buckets.length;
                        List<MyEntry<K,V>> newHome = buckets[index];
                        newHome.add(data);
                        transferred++;
                    }
                }
                if(transferred == numEntries){
                    break;
                }
            }
        }
	}


	/**
	 * Returns an array of the specified size, each
	 * containing an empty linked list that can be
	 * filled with MyEntry objects.
	 * 
	 * @param capacity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<MyEntry<K,V>>[] newArrayOfEntries(int capacity) {
		List<MyEntry<K, V>> [] entries = (List<MyEntry<K,V>> []) (
				java.lang.reflect.Array.newInstance(LinkedList.class, capacity));
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new LinkedList();
		}
		return entries;
	}
	
}

package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private final double loadFactor;
    private int arraySize;
    private Collection<Node>[] buckets;
    private int size;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this.arraySize = 16;
        this.loadFactor = 0.75;
        this.buckets = createTable(arraySize);
        this.size = 0;
    }

    public MyHashMap(int initialSize) {
        this.arraySize = initialSize;
        this.loadFactor = 0.75;
        this.buckets = createTable(initialSize);
        this.size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *z
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.loadFactor = maxLoad;
        this.arraySize = initialSize;
        this.buckets = createTable(initialSize);
        this.size = 0;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] newBuckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            newBuckets[i] = createBucket();
        }
        return newBuckets;
    }

    private void resize(int newArraySize) {
        Collection<Node>[] newBuckets = createTable(newArraySize);
        Iterator<K> keypos = iterator();
        while(keypos.hasNext()) {
            Node nextNode = new Node(keypos.next(), get(keypos.next()));
            //Get which bucket to put into
            int bucket = bucketNumber(nextNode.key, newArraySize);
            //Make the key and val into a node
            //Put it into that bucket
            newBuckets[bucket].add(nextNode);
        }
        buckets = newBuckets;
        arraySize = newArraySize;
    }

    private int bucketNumber(K key, int numbuckets) {
        int code = key.hashCode();
        int bucket = Math.floorMod(code, numbuckets);
        return bucket;
    }

    @Override
    public void clear() {
        for (int i = 0; i < arraySize; i++) {
            buckets[i].clear();
        }
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < arraySize; i++) {
            for (Node n : buckets[i]) {
                if (n.key.equals(key)) {
                    return n.value;
                }
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
        //Get which bucket to put into
        int bucket = bucketNumber(key, arraySize);
        //Make the key and val into a node
        Node n = new Node(key, value);
        //Put it into that bucket
        buckets[bucket].add(n);
        size += 1;
        //Check to resize if load is > loadFactor
        if ((double) (size/arraySize) > loadFactor) {
            resize(arraySize*3);
        }
    }

    //Returns a set of keys keys contained in the map
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIterator();
    }

    private class HashMapIterator implements Iterator<K> {
        private int bucketpos;
        public HashMapIterator() { bucketpos = 0; }
        public Iterator<Node> bucketIterator = buckets[bucketpos].iterator();
        public boolean hasNext() {
            return bucketpos < arraySize && bucketIterator.hasNext();
        }
        public K next() {
            if (bucketpos < arraySize && !bucketIterator.hasNext()) {
                bucketpos += 1;
                bucketIterator = buckets[bucketpos].iterator();
                return next();
            } else {
                return bucketIterator.next().key;
            }
        }
    }


    //Don't need these --------------------------------------------------
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}

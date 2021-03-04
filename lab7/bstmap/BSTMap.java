package bstmap;

/* A map that implements Map61B except
* remove, iterator, and keySet
 */
import java.util.Iterator;
import java.util.Set;

//@Source: Everything is essentially the same as the Princeton implementation given in the spec.
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;

    /* Private nested class to facilitate creation of BST*/
    private class BSTNode{
        private K key;
        private V value;
        private BSTNode left, right;
        private int size;

        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.value = val;
            this.size = size;
        }
    }


    @Override
    /** Removes all of the mappings from this map. */
    public void clear(){
        root = null;
    }

    @Override
    /* Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key){
       if (key == null) throw new IllegalArgumentException("argument containsKey is null");
       return containsKey(root, key);
    }

    private boolean containsKey(BSTNode x, K key) {
        if (x == null) return false;
        int cmp = key.compareTo(x.key);
        if (cmp < 0 ) return containsKey(x.left, key);
        else if (cmp > 0) return containsKey(x.right, key);
        else return true;
    }

    @Override
    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.*/
    public V get(K key){
        return get(root, key);
    }

    private V get(BSTNode x, K key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0 ) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.value;
    }

    @Override
    /* Returns the number of key-value mappings in this map.*/
    public int size(){
        return size(root);
    }

    private int size(BSTNode x) {
        if (x == null) return 0;
        return x.size;
    }

    @Override
    /* Associates the specified value with the specified key in this map. */
    public void put(K key, V value){
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode x, K key, V val) {
        if (x == null) {
            return new BSTNode(key, val, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        }
        else if (cmp > 0) {
            x.right = put(x.right, key, val);
        }
        else {
            x.value = val;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    public void printInOrder() {
    }

    //### Don't need to implement the following functions for full credit###


    /* Returns a Set view of the keys contained in this map. Not required for Lab 7.
     * If you don't implement this, throw an UnsupportedOperationException. */
    public Set<K> keySet(){
        throw new UnsupportedOperationException("Unsupported Operand");
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 7. If you don't implement this, throw an
     * UnsupportedOperationException. */
    public V remove(K key) {
        throw new UnsupportedOperationException("Unsupported Operand");
    }

    public V remove(K key, V val) {
        throw new UnsupportedOperationException("Unsupported Operand");
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}

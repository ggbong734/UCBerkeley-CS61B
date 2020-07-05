import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private Node root;

    private class Node {
        private K key;               // sorted by key
        private V val;               // associated data
        private Node left, right;    // left and right subtrees
        private int size;

        private Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    public BSTMap() {
    }

    // Checks if key exists in the BSTMap
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(key) != null;
    }

    // Removes all mapping from this map
    @Override
    public void clear() {
        root = null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.val;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.size;
        }
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (val == null) {
            throw new IllegalArgumentException();
        }
        root = put(root, key, val);
    }

    private Node put (Node x, K key, V val) {
        if (x == null) {
            return new Node(key, val, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
        } else {
            x.val = val;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    public Iterator<K> iterator() {
        Set setOfKeys;
        setOfKeys = keySet();
        return setOfKeys.iterator();
    }


    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> newSet = new HashSet<>();
        keySet(root, newSet);
        return newSet;
    }

    private void keySet(Node x, Set theSet){
        if (x.left == null && x.right == null) {
            theSet.add(x.key);
        } else if (x.right == null) {
            keySet(x.left, theSet);
            theSet.add(x.key);
        } else if (x.left == null) {
            keySet(x.right, theSet);
            theSet.add(x.key);
        } else {
            keySet(x.left, theSet);
            theSet.add(x.key);
            keySet(x.right, theSet);
        }
    }


    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key){
        throw new UnsupportedOperationException();
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value){
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node x) {
        if (x.left == null && x.right == null) {
            printNode(x);
        } else if (x.right == null) {
            printInOrder(x.left);
            printNode(x);
        } else if (x.left == null) {
            printNode(x);
            printInOrder(x.right);
        } else {
            printInOrder(x.left);
            printNode(x);
            printInOrder(x.right);
        }
    }

    private void printNode(Node x) {
        System.out.print(x.key);
        System.out.println(" " + x.val);
    }

}

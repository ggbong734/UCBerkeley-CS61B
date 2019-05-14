import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**  @author Gerry Bong
 *   With help from Josh Hug lecture notes (cs 61b 2019 lecture 21)
 *   and Algorithm textbook by Robert Sedgewick and Kevin Wayne
 *   https://algs4.cs.princeton.edu/52trie/TrieST.java.html
 *
 */

public class MyTrieSet implements TrieSet61B {
    private Node root;
    private int size;

    private class Node {
        private boolean isKey;
        private Hashtable<Character, Node> hTable;

        private Node() {
            isKey = false;      // false by default
            hTable = new Hashtable<>();
        }
    }

    public MyTrieSet() {
        root = new Node();
        size = 0;
    }

    /** Clears all items out of Trie */
    public void clear() {
        root = new Node();
        size = 0;
    }

    /** Returns true if the Trie contains KEY, false otherwise */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return contains(key, root, 0);
    }

    /* Helper method for contains. Starting from root, check if
        the Trie has every character in key, returning a false
        if the character is not found. At the end, the loop ends
        when the full String is built and isKey is true in the
        next node.
     */
    private boolean contains(String key, Node n, int d) {
        if (d == key.length()) {
            return n.isKey;
        }
        char c = key.charAt(d);
        if (n.hTable.containsKey(c)) {
            return contains(key, n.hTable.get(c), d + 1);
        }
        return false;
    }


    /** Inserts string KEY into Trie */
    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        add(root, key, 0);
    }

    /* Helper method for add. For every character in key, if the Trie
       does not have the character, add it as a new key in the node's
       hash table using put(key, value). The value of each entry is
       the link to a new Node. Then recursively checks the next node
       until the end of the String key is reached, where the isKey
       variable of the next node is marked with true.

     */
    private void add(Node n, String key, int d) {
        if (d == key.length()) {
            n.isKey = true;
            size += 1;
            return;
        }
        char c = key.charAt(d);
        if (!n.hTable.containsKey(c)) {
            n.hTable.put(c, new Node());
        }
        add(n.hTable.get(c), key, d + 1);
    }


    /** Returns a list of all words that start with PREFIX */
    public List<String> keysWithPrefix(String prefix) {
        List<String> container = new ArrayList<>();
        StringBuilder toAdd = new StringBuilder();
        toAdd.append(prefix);
        keysWithPrefix(prefix, toAdd, container, root, 0);
        return container;
    }

    /*  Helper method for keysWithPrefix. Build the toAdd string
        character by character until prefix is complete. Then
        add each new character and traverse each branch of the tree,
        adding any word found to container List.
     */
    private void keysWithPrefix(String prefix, StringBuilder toAdd,
                                List<String> container, Node n, int d) {
        if (d >= prefix.length()) {
            if (n.isKey) {
                container.add(toAdd.toString());
            }
            if (n.hTable.isEmpty()) {
                return;
            }
            for (char c : n.hTable.keySet()) {
                toAdd.append(c);
                keysWithPrefix(prefix, toAdd, container, n.hTable.get(c), d + 1);
                toAdd.setLength(toAdd.length() - 1);
            }
            return;
        }
        char c = prefix.charAt(d);
        keysWithPrefix(prefix, toAdd, container, n.hTable.get(c), d + 1);
    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public String longestPrefixOf(String key) {
        StringBuilder container = new StringBuilder();
        String result = "";
        return longestPrefixOf(key, result, container, root, 0);
    }

    /*  Helper method for longestPrefixOf. Build the container string
    character by character until String key is completed. Update the String
    result when a new word is found. End the recursion before checking if the
    last node is a key (isKey).
 */
    private String longestPrefixOf(String key, String result,
                                   StringBuilder container, Node n, int d) {
        if (d == key.length()) {
            return result;
        }
        if (n.isKey) {
            result = container.toString();
        }
        char c = key.charAt(d);
        container.append(c);
        return longestPrefixOf(key, result, container, n.hTable.get(c), d + 1);
    }

    /* Test to visualize myTrieSet and check correctness
     */
//    public static void main(String[] args) {
//        MyTrieSet t = new MyTrieSet();
//        t.add("hello");
//        t.add("hi");
//        t.add("help");
//        t.add("zebra");
//    }
}

/** Implementation of Weighted Union Find (Disjoint Set) with Path Compression
 *  Implementation tracks size of root node in parent array (does not create a
 *  separate array to track size)
 *  The code for this class is sourced from Algorithms textbook by Robert
 *  Sedgewick and Kevin Wayne page 228.
 *
 */

public class UnionFind {

    private int[] id;  // parent link (site indexed)
    private int[] sz;  // size of component for roots (site indexed)
    private int count; // number of components

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        count = n;
        id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        sz = new int[n];
        for (int i = 0; i < n; i++) {
            sz[i] = 1;  //all nodes start with size 1
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        if (vertex >= count || vertex < 0) {
            throw new IllegalArgumentException();
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        return sz[find(v1)]; // root node has the size in sz[]
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        if (v1 == id[v1]) {
            return -sz[v1];
        }
        return id[v1];
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        int root1 = find(v1);
        int root2 = find(v2);
        if (root1 == root2) {
            return;
        }
        if (sz[root1] <= sz[root2]) {
            id[root1] = root2;
            sz[root2] += sz[root1];
        } else {
            id[root2] = root1;
            sz[root1] += sz[root2];
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        validate(vertex);
        int root = vertex;
        while (root != id[root]) {
            root = id[root];
        }

        //Path compression by setting the id[] entry of each node along the way
        //directly to the root
        while (vertex != id[vertex]) {
            int temp = vertex;
            vertex = id[vertex];
            id[temp] = root;
        }
        return root;
    }

}

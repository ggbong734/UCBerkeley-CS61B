/** HW2 of UC Berkeley CS61 B Algorithm class
 * @author Gerry Bong
 */
package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int length;
    private int [][] grid;
    private int totalOpen;
    private WeightedQuickUnionUF gridDisjointSet;
    private WeightedQuickUnionUF extraDS;
    private int topVirtualNodeIndex;
    private int bottomVirtualNodeIndex;

    // create N-by-N grid, with all sites initially blocked
    // Fill up the new grid with zeroes to indicate all
    // cells are initially closed
    // Add 2 to the number of elements in disjoint set to
    // create two virtual nodes above and below the 2D array
    // grid
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        length = N;
        gridDisjointSet = new WeightedQuickUnionUF(N * N + 2);
        // id N * N is for virtual node above the grid
        // id N * N + 1 is for virtual node below the grid
        topVirtualNodeIndex = N * N;
        bottomVirtualNodeIndex = N * N + 1;
        totalOpen = 0;
        grid = new int [N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = 0;
            }
        }
        // creates an extra disjoint set to solve backwash issue
        // he extra DisjointSet only has the top extra virtual node
        // above the grid array. This acts as a check to avoid
        // backwash issues
        extraDS = new WeightedQuickUnionUF(N * N + 1);
    }

    // converts (row, column) into a unique integer
    private int xyTo1D(int row, int col) {
        return (row * length) + col;
    }

    // checks that the given row and col numbers are valid
    private void checkRowCol(int row, int col) {
        if ((row >= length) || (col >= length)) {
            throw new IndexOutOfBoundsException();
        }
    }

    // open the site (row, col) if it is not open
    // Use number 1 to indicate that the grid is open
    public void open(int row, int col) {
        checkRowCol(row, col);
        if (grid[row][col] == 1) {
            return;
        }
        grid[row][col] = 1;
        totalOpen += 1;
        int currIndex = xyTo1D(row, col);
        // loop through each of 4 adjacent cells and check if
        // any is open, if so union neighbor cell with current
        // open cell
        int[] dR = new int[]{-1, 0, 1, 0};
        int[] dC = new int[]{0, -1, 0, 1};
        for (int i = 0; i < 4; i++) {
            int nR = row + dR[i];
            int nC = col + dC[i];
            if ((0 <= nR) && (nR < length)
                    && (0 <= nC) && (nC < length)) {
                if (isOpen(nR, nC)) {
                    gridDisjointSet.union(currIndex, xyTo1D(nR, nC));
                    extraDS.union(currIndex, xyTo1D(nR, nC));
                }
            }
        }
        // if the cell is on the top row, connect if with the top
        // virtual node, but if the cell is on the bottom row,
        // connect if with the bottom virtual node.
        if (row == 0) {
            gridDisjointSet.union(currIndex, topVirtualNodeIndex);
            extraDS.union(currIndex, topVirtualNodeIndex);
        }
        if (row == (length - 1)) {
            gridDisjointSet.union(currIndex, bottomVirtualNodeIndex);
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return grid[row][col] == 1;
    }

    // is the site (row, col) full?
    // A row is full only if the cell in both original and extra disjoint sets
    // are connected to the top virtual node, to avoid backwash.
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);
        int currIndex = xyTo1D(row, col);
        return gridDisjointSet.connected(currIndex, topVirtualNodeIndex)
                && extraDS.connected(currIndex, topVirtualNodeIndex);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return totalOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return gridDisjointSet.connected(topVirtualNodeIndex,
                bottomVirtualNodeIndex);
    }

    // use for unit testing (not required, but keep for autograder)
    public static void main(String[] args) {
    }
}

/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */
/* NOTE: Backwash
 * Backwash is a problem caused by the fact that we have virtual sites at the bottom and the
 * top, and because all the sites at the bottom row are connected to the virtual bottom.
 * When the system percolates, the virtual bottom will be connected to the virtual top.
 * As a consequence, all the sites in the bottom row will also be connected to the virtual
 * top through this bottom, and will be marked as Full even if they aren't actually connected
 * to the top
 * To prevent this, we create a second grid data-structure without virtual bottom. We'll use
 * this second grid only to check if the elements are full; as it doesn't have bottom, a site
 * will only be full if there is a real path to the top. */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF grid, gridWithoutVirtualBotom;
    private final boolean[][] state;
    private final int n;
    private int opensites;
    private final int virtualtop, virtualbottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException("Invalid argument");
        this.n = n;
        this.state = new boolean[n][n];
        this.grid = new WeightedQuickUnionUF(n * n + 2);
        // gridWithoutBottom is used to prevent backwash
        this.gridWithoutVirtualBotom = new WeightedQuickUnionUF(n * n + 1);
        this.virtualtop = 0;
        this.virtualbottom = n * n + 1;
        this.opensites = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                state[i][j] = false;
            }
        }
        connectTopToVirtualTop();
        connectBottomToVirtualBottom();
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Invalid index");
        if (!isOpen(row, col)) {
            opensites++;
            state[row - 1][col - 1] = true;
            connect(row, col, row + 1, col); // up
            connect(row, col, row - 1, col); // down
            connect(row, col, row, col + 1); // right
            connect(row, col, row, col - 1); // left
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Invalid index");
        if (state[row - 1][col - 1]) return true;
        return false;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("Invalid index");
        if (isOpen(row, col) && (gridWithoutVirtualBotom.find((row - 1) * n + col)
                == gridWithoutVirtualBotom.find(virtualtop)))
            return true;
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        // corner case: when grid is (1x1 and site is not open)
        if (n == 1 && !isOpen(n, n)) return false;
        if (grid.find(virtualbottom) == grid.find(virtualtop)) return true;
        return false;
    }

    private void connectTopToVirtualTop() {
        for (int i = 1; i <= n; i++) {
            // top is the first row
            grid.union(virtualtop, i);
            gridWithoutVirtualBotom.union(virtualtop, i);
        }
    }

    private void connectBottomToVirtualBottom() {
        for (int i = 1; i <= n; i++) {
            // bottom is the nth row
            int gridindex = (n - 1) * n + i; // calculate 1d array index from 2d
            grid.union(virtualbottom, gridindex);
        }
    }

    private void connect(int row1, int col1, int row2, int col2) {
        // check if row and col is out of bound
        if (row2 < 1 || row2 > n || col2 < 1 || col2 > n) return;
        // check if the neighbor is open
        if (!isOpen(row2, col2)) return;
        // change gridindex to 1d array index
        int gridindex1 = (row1 - 1) * n + col1;
        int gridindex2 = (row2 - 1) * n + col2;
        grid.union(gridindex1, gridindex2);
        gridWithoutVirtualBotom.union(gridindex1, gridindex2);
    }

}

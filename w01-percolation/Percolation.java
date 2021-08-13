/* *****************************************************************************
 *  Name:              J
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF grid;
    private int[] openGrid;
    private final int n;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be a positive integer");
        }
        grid = new WeightedQuickUnionUF(n * n + 2);

        this.n = n;

        // 0: blocked, 1: open
        openGrid = new int[n * n + 2];
        for (int i = 0; i < n * n + 2; i++) {
            openGrid[i] = 0;
        }
        openGrid[0] = 1;
        openGrid[n * n + 1] = 1;

        openSites = 0;
    }

    // check if indices are valid
    private void checkIndices(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("The row and column indices are integers between 1 and n");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkIndices(row, col);
        int idx = (row - 1) * n + col;
        if (!isOpen(row, col)) {
            openGrid[idx] = 1;
            openSites++;
        }

        if (row == 1) {
            grid.union(idx, 0);  // connect top
        }

        if (row == n) {
             grid.union(idx, n * n + 1);  // connect bottom
         }

        if (row != 1) {
            if (openGrid[idx - n] == 1) grid.union(idx, idx - n);
        }
        if (col != 1) {
            if (openGrid[idx - 1] == 1) grid.union(idx, idx - 1);
        }
        if (col != n) {
            if (openGrid[idx + 1] == 1) grid.union(idx, idx + 1);
        }
        if (row != n) {
            if (openGrid[idx + n] == 1) grid.union(idx, idx + n);
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIndices(row, col);
        int idx = (row - 1) * n + col;
        return openGrid[idx] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkIndices(row, col);
        int idx = (row - 1) * n + col;
        if (isOpen(row, col)) {
            return grid.find(idx) == grid.find(0);
        } else {
            return false;
        }

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (numberOfOpenSites() < n) {
            return false;
        }
        // would fail in test 18 ~ 21 for fake isFull() in situation like
        //  (0) | (0) | (1)
        //  (0) | (0) | (1)
        //  (1) | (0) | (1)
        // (3, 1) should not be full, but I use a bottom node here so
        // (3, 1) is regarded connected to top as it percolates XD.
        // GREAT. I NEED HELP

        return grid.find(0) == grid.find(n * n + 1);

        // another solution in O(n) time
        // could do check for the whole bottom line but time tests would fail
        // for (int i = 0; i < n; i++) {
        //     if (grid.find(0) == grid.find(n * n - i)) {
        //         return true;
        //     }
        // }
        // return false

    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 3);
        p.open(3, 1);

        System.out.println(p.isFull(3, 1));

        System.out.println(p.percolates());
    }
}

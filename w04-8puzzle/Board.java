/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Board {
    private final int n;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = copy(tiles);  // use a deep copy to pass test11 (immutable)
    }

    // string representation of this board
    public String toString() {
        StringBuilder buf = new StringBuilder();  // checkstyle would be happy
        buf.append(n + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                buf.append(" " + tiles[row][col]);
            }
            if (row != 2) {
                buf.append("\n");
            }
        }
        return buf.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int res = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if ((row != n - 1) || (col != n - 1)) {  // don't count last one
                    if (tiles[row][col] != row * n + col + 1) {
                        res++;
                    }
                }
            }
        }
        return res;
    }

    // get delta of two integers
    private int delta(int a, int b) {
        if (a > b) {
            return (a - b);
        } else {
            return (b - a);
        }
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int res = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int t = tiles[row][col];
                if (t != 0) {  // don't count 0
                    int r = (t - 1) / n;
                    int c = (t - 1) % n;
                    res = res + delta(r, row) + delta(c, col);
                }
            }
        }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) { return true; }
        if (y == null) { return false; }
        if (y.getClass() != this.getClass()) { return false; }

        Board other = (Board) y;
        if (other.dimension() != n) { return false; }
        return (Arrays.deepEquals(other.tiles, this.tiles));  // faster than string
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {  // should not use an inner class
        Stack<Board> neighbors = new Stack<>();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) {
                    if ((row - 1) >= 0) {
                        int[][] copyT = copy(tiles);
                        swapTiles(copyT, row - 1, col, row, col);
                        neighbors.push(new Board(copyT));
                    }
                    if ((row + 1) < n) {
                        int[][] copyT = copy(tiles);
                        swapTiles(copyT, row + 1, col, row, col);
                        neighbors.push(new Board(copyT));
                    }
                    if ((col - 1) >= 0) {
                        int[][] copyT = copy(tiles);
                        swapTiles(copyT, row, col - 1, row, col);
                        neighbors.push(new Board(copyT));
                    }
                    if ((col + 1) < n) {
                        int[][] copyT = copy(tiles);
                        swapTiles(copyT, row, col + 1, row, col);
                        neighbors.push(new Board(copyT));
                    }
                }
            }
        }
        return neighbors;
    }

    private int[][] copy(int[][] target) {
        int length = target.length;
        int[][] res = new int[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                res[i][j] = target[i][j];
            }
        }
        return res;
    }

    // swap two tiles on the board
    private void swapTiles(int[][] target, int x1, int y1, int x2, int y2) {
        if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
            throw new IllegalArgumentException("row or col less than 0.");
        }
        int k = target.length;
        if (x1 >= k || y1 >= k || x2 >= k || y2 >= k) {
            throw new IllegalArgumentException("row or col out of bound.");
        }
        int temp = target[x1][y1];
        target[x1][y1] = target[x2][y2];
        target[x2][y2] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copy(tiles);
        int i = 0;
        int j = 0;
        while (twinTiles[i][j] == 0 || twinTiles[i][j+1] == 0) {
            j++;
            if (j >= n-1) {
                i++;
                j = 0;
            }
        }
        swapTiles(twinTiles, i, j, i, j+1);
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
                {8, 1, 3},
                {4, 0, 2},
                {7, 6, 5}
        };
        Board b = new Board(tiles);

        StdOut.println(b);
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
        StdOut.println(b.isGoal());

        int[][] goal = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board g = new Board(goal);
        StdOut.println(g.isGoal());

        int[][] goal2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        Board g2 = new Board(goal2);

        StdOut.println(g);
        StdOut.println(g2);

        StdOut.println(g.equals(g2));
        StdOut.println(g.equals(b));

        StdOut.println(g.twin());
        StdOut.println("*****************");


        StdOut.println("b: " + b);

        Iterable<Board> n = b.neighbors();
        for (Board board : n) {
            StdOut.println(board);
        }
        

    }



}
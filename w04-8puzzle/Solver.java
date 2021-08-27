/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean solvable = false;
    private int movesToGoal = -1;
    private Stack<Board> solutionSteps;

    private class SNode implements Comparable<SNode> {

        private final Board board;
        private final int moves;
        private final SNode prev;
        private final int manhattan;

        public SNode(Board initial) {  // search node
            board = initial;
            moves = 0;  // moves so far
            prev = null;  // node with last board state
            manhattan = board.manhattan();
        }

        public SNode(Board initial, int moves, SNode prev) {
            board = initial;
            this.moves = moves;
            this.prev = prev;
            manhattan = board.manhattan();
        }

        public int compareTo(SNode that) {
            int thisPrioity = this.moves + this.manhattan;
            int thatPrioity = that.moves + that.manhattan;
            return thisPrioity - thatPrioity;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("null input.");
        }

        MinPQ<SNode> pq = new MinPQ<SNode>();
        MinPQ<SNode> twinPq = new MinPQ<SNode>();
        pq.insert(new SNode(initial));
        twinPq.insert(new SNode(initial.twin()));

        while (!pq.isEmpty() && !twinPq.isEmpty()) {
            SNode currNode = pq.delMin();
            SNode currTwinNode = twinPq.delMin();

            // should use isGoal(), checkstyle not happy if hamming is called
            if (currTwinNode.board.isGoal()) { break; } // unsolvable

            if (currNode.board.isGoal()) {
                solvable = true;
                movesToGoal = currNode.moves;

                solutionSteps = new Stack<Board>();
                solutionSteps.push(currNode.board);
                SNode prevPointer = currNode.prev;
                while (prevPointer != null) {
                    solutionSteps.push(prevPointer.board);
                    prevPointer = prevPointer.prev;
                }
                break;  // get out of the outer loop
            }

            for (Board neighbor : currNode.board.neighbors()) {
                if (currNode.prev == null || !neighbor.equals(currNode.prev.board)) {
                    pq.insert(new SNode(neighbor, currNode.moves + 1, currNode));
                }
            }

            for (Board twinNeighbor : currTwinNode.board.neighbors()) {
                if (currTwinNode.prev == null || !twinNeighbor.equals(currTwinNode.prev.board)) {
                    twinPq.insert(new SNode(twinNeighbor, currTwinNode.moves + 1, currTwinNode));
                }
            }
        }
    }



    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;

    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) { return -1; }
        return movesToGoal;

    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) { return null; }
        return solutionSteps;
    }


    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
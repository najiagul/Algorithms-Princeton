/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Solver {
    private final Board initial;
    private int steps;
    private List<Board> path;
    private boolean isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previous;
        private final int moves;
        private final int manhattan;
        private final boolean isGoal;

        public SearchNode(Board b, int m, SearchNode p) {
            this.board = b;
            this.moves = m;
            this.previous = p;
            this.manhattan = board.manhattan();
            this.isGoal = board.isGoal();
        }

        public int priority() {
            return manhattan + moves;
        }

        public Iterable<Board> neighbors() {
            List<Board> neighbours = new ArrayList<Board>();
            for (Board b : board.neighbors()) {
                if (previous == null) {
                    neighbours.add(b);
                    continue;
                }
                if (b.equals(previous.board)) continue;
                neighbours.add(b);
            }
            return neighbours;
        }

        public int compareTo(SearchNode that) {
            if (this.priority() < that.priority()) return -1;
            if (this.priority() > that.priority()) return 1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        // check input
        if (initial == null) throw new IllegalArgumentException("Null Argument");

        this.initial = initial;
        this.isSolvable = false;

        SearchNode goal = aStar();
        if (!isSolvable()) return;

        path = new ArrayList<Board>();
        this.steps = goal.moves;

        // chase pointers to the optimal path
        while (goal.previous != null) {
            path.add(goal.board);
            goal = goal.previous;
        }
        path.add(initial);
        Collections.reverse(path);
    }

    private SearchNode aStar() {

        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        SearchNode searchNode = new SearchNode(initial, 0, null);
        SearchNode twinNode = new SearchNode(initial.twin(), 0, null);

        if (searchNode.isGoal) {
            isSolvable = true;
            return searchNode;
        }
        priorityQueue.insert(searchNode);
        twinPQ.insert(twinNode);

        while (true) {
            if (priorityQueue.isEmpty()) {
                isSolvable = false;
                return searchNode;
            }

            searchNode = priorityQueue.delMin();
            twinNode = twinPQ.delMin();

            if (searchNode.isGoal) {
                isSolvable = true;
                return searchNode;
            }

            if (twinNode.isGoal) {
                isSolvable = false;
                return twinNode;
            }

            for (Board n : searchNode.neighbors()) {
                priorityQueue.insert(new SearchNode(n, searchNode.moves + 1,
                                                    searchNode));
            }

            for (Board t : twinNode.neighbors()) {
                twinPQ.insert(new SearchNode(t, twinNode.moves + 1,
                                             twinNode));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return steps;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return path;
    }

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

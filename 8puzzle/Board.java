/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public final class Board {
    private final int n;
    private final int[][] tiles;
    private int hamming;
    private int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
        this.hamming = hamming();
        this.manhattan = manhattan();
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension() + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++)
                str.append(String.format("%2d ", tiles[row][col]));
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) continue;
                if (!validateTiles(i, j)) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        manhattan = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) continue;
                if (!validateTiles(row, col)) {
                    // int onedIndex = n * row + col; // 1d index of 2d array
                    int actualIndex_1D = tiles[row][col];
                    int actualRow = (actualIndex_1D - 1) / n;
                    int actualCol = actualIndex_1D - actualRow * n - 1;
                    manhattan += Math.abs(row - actualRow) + Math.abs(col - actualCol);
                }
            }
        }
        return manhattan;
    }

    private boolean validateTiles(int row, int col) {
        if (row == n - 1 && col == n - 1) {
            if (tiles[row][col] == 0) return true;
            return false;
        }
        if (tiles[row][col] == (row * n) + col + 1) return true;
        return false;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (!validateTiles(row, col)) return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (this.tiles[row][col] != that.tiles[row][col])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbours = new ArrayList<Board>();
        int zeroRow = 0;
        int zeroCol = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] == 0) {
                    zeroRow = row;
                    zeroCol = col;
                    break;
                }
            }
        }
        // Finding top neighbour
        if (zeroRow > 0) {
            Board topNeighbour = new Board(tiles);
            topNeighbour.swapTiles(zeroRow, zeroCol, zeroRow - 1, zeroCol);
            neighbours.add(topNeighbour);
        }
        // Finding left neighbour
        if (zeroCol > 0) {
            Board leftNeighbour = new Board(tiles);
            leftNeighbour.swapTiles(zeroRow, zeroCol, zeroRow, zeroCol - 1);
            neighbours.add(leftNeighbour);
        }
        // Finding bottom neighbour
        if (zeroRow < n - 1) {
            Board bottomNeighbour = new Board(tiles);
            bottomNeighbour.swapTiles(zeroRow, zeroCol, zeroRow + 1, zeroCol);
            neighbours.add(bottomNeighbour);
        }
        // Finding right neighbour
        if (zeroCol < n - 1) {
            Board rightNeighbour = new Board(tiles);
            rightNeighbour.swapTiles(zeroRow, zeroCol, zeroRow, zeroCol + 1);
            neighbours.add(rightNeighbour);
        }

        return neighbours;
    }

    private void swapTiles(int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[][] twinBlocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twinBlocks[i][j] = tiles[i][j];
            }
        }
        int i = 0;
        int j = 0;
        while (twinBlocks[i][j] == 0 || twinBlocks[i][j + 1] == 0) {
            j++;
            if (j >= twinBlocks.length - 1) {
                i++;
                j = 0;
            }
        }
        int temp = twinBlocks[i][j];
        twinBlocks[i][j] = twinBlocks[i][j + 1];
        twinBlocks[i][j + 1] = temp;
        return new Board(twinBlocks);
    }

    public static void main(String[] args) {
        int tiles[][] = new int[3][3];
        tiles[0][0] = 3;
        tiles[0][1] = 6;
        tiles[0][2] = 0;

        tiles[1][0] = 8;
        tiles[1][1] = 7;
        tiles[1][2] = 5;

        tiles[2][0] = 4;
        tiles[2][1] = 2;
        tiles[2][2] = 1;

        Board initial = new Board(tiles);
        System.out.println(initial);
        System.out.println("===================");

        for (Board neighbor : initial.neighbors()) {
            System.out.println(neighbor);
        }

        Board twin = initial.twin();
        System.out.println(initial);
        System.out.println("twin");
        System.out.println(twin);

        System.out.println(initial.dimension());
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());

    }
}

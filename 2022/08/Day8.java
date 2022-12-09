import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2022/day/8
 */
public class Day8 {

    public static void main(String[] args) throws Exception {
        int[][] grid = getGrid();
        part1(grid);
    }

    private static void part1(int[][] grid) {
        Set<Tree> visibleInterior = new HashSet<>();
        int n = grid.length;

        // left to right
        for (int row = 1; row < n - 1; row++) {
            int tallest = grid[row][0];
            for (int col = 1; col < n - 1; col++) {
                int h = grid[row][col];
                if (h > tallest) {
                    visibleInterior.add(new Tree(row, col));
                    tallest = h;
                }
            }
        }

        // right to left
        for (int row = 1; row < n - 1; row++) {
            int tallest = grid[row][n - 1];
            for (int col = n - 2; col > 0; col--) {
                int h = grid[row][col];
                if (h > tallest) {
                    visibleInterior.add(new Tree(row, col));
                    tallest = h;
                }
            }
        }

        // top to bottom
        for (int col = 1; col < n - 1; col++) {
            int tallest = grid[0][col];
            for (int row = 1; row < n - 1; row++) {
                int h = grid[row][col];
                if (h > tallest) {
                    visibleInterior.add(new Tree(row, col));
                    tallest = h;
                }
            }
        }

        // bottom to top
        for (int col = 1; col < n - 1; col++) {
            int tallest = grid[n - 1][col];
            for (int row = n - 2; row > 0; row--) {
                int h = grid[row][col];
                if (h > tallest) {
                    visibleInterior.add(new Tree(row, col));
                    tallest = h;
                }
            }
        }

        int visibleExterior = n * n - (n - 2) * (n - 2);
        System.out.println("Part 1: " + (visibleInterior.size() + visibleExterior));
    }

    private static int[][] getGrid() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int[][] grid = null;
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (grid == null) {
                    grid = new int[line.length()][line.length()];
                }

                for (int col = 0; col < line.length(); col++) {
                    grid[row][col] = line.charAt(col) - '0';
                }

                row++;
            }

            return grid;
        }
    }

    private static class Tree {

        final int row;
        final int col;

        Tree(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Tree other = (Tree) obj;
            return row == other.row && col == other.col;
        }
    }
}

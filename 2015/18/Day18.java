import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2015/day/18
 */
public class Day18 {

    public static void main(String[] args) {
        Grid grid = getGrid();
        part1(grid);
        part2(grid);
    }

    private static void part1(Grid grid) {
        Grid nextGrid = grid;
        for (int i = 0; i < 100; i++) {
            nextGrid = step(nextGrid, Collections.<Location>emptyList());
        }

        System.out.println(nextGrid.countOn());
    }

    private static void part2(Grid grid) {
        Grid nextGrid = grid;
        for (int i = 0; i < 100; i++) {
            nextGrid = step(
                nextGrid,
                Arrays.<Location>asList(
                    new Location(0, 0),
                    new Location(0, 99),
                    new Location(99, 0),
                    new Location(99, 99)));
        }

        System.out.println(nextGrid.countOn());
    }

    private static Grid getGrid() {
        Grid grid = new Grid(100);

        int row = 0;
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine() && row < grid.size()) {
            String line = scanner.nextLine();
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '#') {
                    grid.on(row, col);
                } else if (line.charAt(col) == '.') {
                    // no-op
                } else {
                    throw new IllegalArgumentException("Unknown state: " + line.charAt(col));
                }
            }

            row++;
        }

        return grid;
    }

    private static Grid step(Grid grid, List<Location> alwaysOn) {
        Grid newGrid = new Grid(grid.size());
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.size(); col++) {
                if (alwaysOn.contains(new Location(row, col))) {
                    newGrid.on(row, col);
                } else {
                    int neighboursOn = grid.countNeighboursOn(row, col);
                    if (grid.isOn(row, col) && neighboursOn == 2 || neighboursOn == 3) {
                        newGrid.on(row, col);
                    } else if (!grid.isOn(row, col) && neighboursOn == 3) {
                        newGrid.on(row, col);
                    }
                }
            }
        }

        return newGrid;
    }

    private static class Grid {

        private boolean[][] lights;

        Grid(int size) {
            lights = new boolean[size][size];
        }

        void on(int row, int col) {
            lights[row][col] = true;
        }

        boolean isOn(int row, int col) {
            return lights[row][col];
        }

        int countNeighboursOn(int row, int col) {
            int count = 0;

            // north-west
            if (row > 0 && col > 0 && isOn(row - 1, col - 1)) {
                count++;
            }

            // north
            if (row > 0 && isOn(row - 1, col)) {
                count++;
            }

            // north-east
            if (row > 0 && col < size() - 1 && isOn(row - 1, col + 1)) {
                count++;
            }

            // east
            if (col < size() - 1 && isOn(row, col + 1)) {
                count++;
            }

            // south-east
            if (row < size() - 1 && col < size() - 1 && isOn(row + 1, col + 1)) {
                count++;
            }

            // south
            if (row < size() - 1 && isOn(row + 1, col)) {
                count++;
            }

            // south-west
            if (row < size() - 1 && col > 0 && isOn(row + 1, col - 1)) {
                count++;
            }

            // west
            if (col > 0 && isOn(row, col - 1)) {
                count++;
            }

            return count;
        }

        int countOn() {
            int count = 0;
            for (int row = 0; row < size(); row++) {
                for (int col = 0; col < size(); col++) {
                    if (isOn(row, col)) {
                        count++;
                    }
                }
            }

            return count;
        }

        int size() {
            return lights.length;
        }
    }

    private static class Location {

        private final int row;
        private final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return other.row == row && other.col == col;
        }
    }
}

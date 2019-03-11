import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2016/day/24
 */
public class Day24 {

    public static void main(String[] args) {
        Grid grid = getGrid();
        System.out.println(shortestPath(grid, false));
        System.out.println(shortestPath(grid, true));
    }

    private static int shortestPath(Grid grid, boolean returnToStart) {
        State initialState = new State(grid, 0);

        Set<State> seen = new HashSet<State>();
        seen.add(initialState);

        LinkedList<State> queue = new LinkedList<State>();
        queue.add(initialState);

        while (queue.size() > 0) {
            State currentState = queue.poll();
            //System.out.println(currentState);
            if (!returnToStart && currentState.numbers.size() == grid.numbersCount) {
                //System.out.println(currentState);
                return currentState.steps;
            }

            if (returnToStart && currentState.numbers.size() == grid.numbersCount
                    && grid.isNumber(currentState.location) && grid.numberAt(currentState.location) == 0) {
                return currentState.steps;
            }

            for (Location location : Arrays.asList(
                        new Location(currentState.location.row - 1, currentState.location.col),
                        new Location(currentState.location.row, currentState.location.col + 1),
                        new Location(currentState.location.row + 1, currentState.location.col),
                        new Location(currentState.location.row, currentState.location.col - 1))) {
                State nextState = currentState.transition(location);
                if (nextState != null) {
                    if (!seen.contains(nextState)) {
                        queue.offer(nextState);
                        seen.add(nextState);
                    }
                }
            }
        }

        throw new IllegalStateException();
    }

    private static Grid getGrid() {
        List<String> lines = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        return new Grid(lines);
    }

    private static class State {

        private Grid grid;
        private Location location;
        private int steps;
        private ArrayList<Integer> numbers;

        private State() { }

        State(Grid grid, int initialNumber) {
            this.location = grid.locationOf(initialNumber);
            this.steps = steps;
            this.grid = grid;
            this.numbers = new ArrayList<Integer>();
            this.numbers.add(initialNumber);
        }

        State transition(Location location) {
            if (location.row >= 0 && location.row < grid.height
                    && location.col >= 0 && location.col < grid.width
                    && !grid.isWall(location)) {
                State copy = new State();
                copy.grid = grid;
                copy.location = location;
                copy.steps = steps + 1;
                copy.numbers = numbers;

                if (grid.isNumber(location)) {
                    int number = grid.numberAt(location);
                    if (!numbers.contains(number)) {
                        copy.numbers = new ArrayList<Integer>(numbers);
                        copy.numbers.add(number);
                    }
                }

                return copy;
            }
                        
            return null;
        }

        @Override
        public int hashCode() {
            return 31 * location.hashCode() + numbers.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            State other = (State) obj;
            return location.equals(other.location) && numbers.equals(other.numbers);
        }

        @Override
        public String toString() {
            return "State: location=" + location + ", steps=" + steps + ", numbers=" + numbers;
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
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public String toString() {
            return "Location: row=" + row + ", col=" + col;
        }
    }

    private static class Grid {

        private final int width;
        private final int height;
        private final int numbersCount;
        private final char[][] grid;

        Grid(List<String> lines) {
            width = lines.get(0).length();
            height = lines.size();
            grid = new char[height][width];

            int numbersCount = 0;
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    grid[row][col] = lines.get(row).charAt(col);
                    if (Character.isDigit(grid[row][col])) {
                        numbersCount++;
                    }
                }
            }
            this.numbersCount = numbersCount;
        }

        boolean isWall(Location location) {
            return grid[location.row][location.col] == '#';
        }

        boolean isNumber(Location location) {
            return Character.isDigit(grid[location.row][location.col]);
        }

        int numberAt(Location location) {
            return Integer.parseInt(grid[location.row][location.col] + "");
        }

        Location locationOf(int number) {
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Location location = new Location(row, col);
                    if (isNumber(location) && numberAt(location) == number) {
                        return new Location(row, col);
                    }
                }
            }

            throw new IllegalStateException();
        }
    }
}

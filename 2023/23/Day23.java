import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2023/day/23
 */
public class Day23 {

    public static void main(String[] args) throws Exception {
        Input map = getInput();
        part1(map);
    }

    private static void part1(Input map) {
        int longestPath = -1;

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(new Step(0, map.startLocation));
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();
            if (currentStep.location.equals(map.endLocation)) {
                longestPath = Math.max(currentStep.num, longestPath);
                continue;
            }

            List<Location> nextLocations = new ArrayList<>();

            Slope slope = map.slopes.get(currentStep.location);
            if (slope != null) {
                if (slope == Slope.UP) {
                    nextLocations.add(currentStep.location.up());
                } else if (slope == Slope.DOWN) {
                    nextLocations.add(currentStep.location.down());
                } else if (slope == Slope.LEFT) {
                    nextLocations.add(currentStep.location.left());
                } else if (slope == Slope.RIGHT) {
                    nextLocations.add(currentStep.location.right());
                } else {
                    throw new IllegalStateException();
                }
            } else {
                nextLocations.add(currentStep.location.up());
                nextLocations.add(currentStep.location.down());
                nextLocations.add(currentStep.location.left());
                nextLocations.add(currentStep.location.right());
            }

            for (Location nextLocation : nextLocations) {
                if (!map.paths.contains(nextLocation)) {
                    continue;
                }

                if (!currentStep.visitedPaths.contains(nextLocation)) {
                    Step step = new Step(currentStep.num + 1, nextLocation);
                    step.visitedPaths.addAll(currentStep.visitedPaths);
                    step.visitedPaths.add(nextLocation);
                    queue.addLast(step);
                }
            }
        }

        System.out.println("Part 1: " + longestPath);
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Input input = new Input();

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line.trim();
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    Location location = new Location(row, col);
                    if (ch == '.') {
                        if (input.startLocation == null) {
                            input.startLocation = location;
                        }
                        input.endLocation = location;
                        input.paths.add(location);
                    } else if (ch == '#') {
                        // skip
                    } else if (ch == '^') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.UP);
                    } else if (ch == 'v') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.DOWN);
                    } else if (ch == '<') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.LEFT);
                    } else if (ch == '>') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.RIGHT);
                    } else {
                        throw new IllegalStateException("Unknown symbol: " + ch);
                    }
                }

                row++;
            }

            return input;
        }
    }

    private static class Input {

        Location startLocation;
        Location endLocation;
        Set<Location> paths = new HashSet<>();
        Map<Location, Slope> slopes = new HashMap<>();
    }

    private enum Slope {

        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    private static class Step {

        final int num;
        final Location location;
        final Set<Location> visitedPaths = new HashSet<>();

        Step(int num, Location location) {
            this.num = num;
            this.location = location;
        }
    }

    private static class Location {

        final int row;
        final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Location up() {
            return new Location(row - 1, col);
        }

        Location down() {
            return new Location(row + 1, col);
        }

        Location left() {
            return new Location(row, col - 1);
        }

        Location right() {
            return new Location(row, col + 1);
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
    }
}

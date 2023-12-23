import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2023/day/21
 */
public class Day21 {

    public static void main(String[] args) throws Exception {
        Input map = getInput();
        part1(map);
    }

    private static void part1(Input input) {
        Map<Location, Integer> stepsToPlots = new HashMap<>();

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(new Step(0, input.startingLocation));
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();
            if (currentStep.num < stepsToPlots.getOrDefault(currentStep.location, Integer.MAX_VALUE)) {
                stepsToPlots.put(currentStep.location, currentStep.num);
            } else {
                continue;
            }

            for (Location nextLocation : List.of(
                    currentStep.location.up(),
                    currentStep.location.down(),
                    currentStep.location.left(),
                    currentStep.location.right())) {
                if (!input.plots.contains(nextLocation)) {
                    continue;
                }

                if (currentStep.num + 1 < stepsToPlots.getOrDefault(nextLocation, Integer.MAX_VALUE)) {
                    queue.addLast(new Step(currentStep.num + 1, nextLocation));
                }
            }
        }

        int count = 0;
        for (Integer steps : stepsToPlots.values()) {
            if (steps <= 64 && steps % 2 == 0) {
                count++;
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Set<Location> plots = new HashSet<>();
            Location startingLocation = null;

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    if (ch == 'S') {
                        startingLocation = new Location(row, col);
                        plots.add(startingLocation);
                    } else if (ch == '.') {
                        plots.add(new Location(row, col));
                    } else if (ch == '#') {
                        // skip
                    } else {
                        throw new IllegalStateException("Uknown symbol: " + ch);
                    }
                }

                row++;
            }

            return new Input(startingLocation, plots);
        }
    }

    private static class Input {

        final Location startingLocation;
        final Set<Location> plots;

        Input(Location startingLocation, Set<Location> plots) {
            this.startingLocation = startingLocation;
            this.plots = plots;
        }
    }

    private static class Step {

        final int num;
        final Location location;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2023/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        List<Pattern> patterns = getInput();
        part1(patterns);
        part2(patterns);
    }

    private static void part1(List<Pattern> patterns) {
        int sum = 0;
        for (Pattern pattern : patterns) {
            List<Reflection> reflections = getReflections(pattern);
            if (reflections.size() != 1) {
                throw new IllegalStateException("Expected only one reflection!");
            }

            Reflection reflection = reflections.get(0);
            if (reflection.type == ReflectionType.VERTICAL) {
                sum += reflection.axis;
            } else if (reflection.type == ReflectionType.HORIZONTAL) {
                sum += reflection.axis * 100;
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<Pattern> patterns) {
        int sum = 0;
        for (Pattern pattern : patterns) {
            List<Reflection> reflections = getReflections(pattern);
            if (reflections.size() != 1) {
                throw new IllegalStateException("Expected only one reflection!");
            }

            Reflection reflection = reflections.get(0);

            smudge:
            for (int row = 0; row < pattern.height; row++) {
                for (int col = 0; col < pattern.width; col++) {
                    Location location = new Location(row, col);
                    LocationType type = pattern.locations.get(location);
                    if (type == LocationType.ROCK) {
                        pattern.locations.put(location, LocationType.ASH);
                    } else if (type == LocationType.ASH) {
                        pattern.locations.put(location, LocationType.ROCK);
                    }

                    List<Reflection> smudgeReflections = getReflections(pattern);
                    pattern.locations.put(location, type);
                    for (Reflection sr : smudgeReflections) {
                        if (sr.type != reflection.type || sr.axis != reflection.axis) {
                            reflection = sr;
                            break smudge;
                        }
                    }
                }
            }

            if (reflection.type == ReflectionType.VERTICAL) {
                sum += reflection.axis;
            } else if (reflection.type == ReflectionType.HORIZONTAL) {
                sum += reflection.axis * 100;
            }
        }

        System.out.println("Part 2: " + sum);
    }

    private static List<Reflection> getReflections(Pattern pattern) {
        List<Reflection> reflections = new ArrayList<>();
        for (int d = pattern.width / 2; d > 0; d--) {
            nextCol:
            for (int col = d - 1; col < pattern.width; col++) {
                for (int row = 0; row < pattern.height; row++) {
                    for (int i = 0; i < d; i++) {
                        if (col - i < 0 || col + i + 1 >= pattern.width) {
                            continue nextCol;
                        }

                        if (pattern.locations.get(new Location(row, col - i))
                                != pattern.locations.get(new Location(row, col + i + 1))) {
                            continue nextCol;
                        }
                    }
                }

                if (col - d + 1 == 0 || col + d == pattern.width - 1) {
                    reflections.add(new Reflection(ReflectionType.VERTICAL, col + 1));
                }
            }
        }

        for (int d = pattern.height / 2; d > 0; d--) {
            nextRow:
            for (int row = d - 1; row < pattern.height; row++) {
                for (int col = 0; col < pattern.width; col++) {
                    for (int i = 0; i < d; i++) {
                        if (row - i < 0 || row + i + 1 >= pattern.height) {
                            continue nextRow;
                        }

                        if (pattern.locations.get(new Location(row - i, col))
                                != pattern.locations.get(new Location(row + i + 1, col))) {
                            continue nextRow;
                        }
                    }
                }

                if (row - d + 1 == 0 || row + d == pattern.height - 1) {
                    reflections.add(new Reflection(ReflectionType.HORIZONTAL, row + 1));
                }
            }
        }

        return reflections;
    }

    private static List<Pattern> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Pattern> patterns = new ArrayList<>();

            String line;
            do {
                int row = 0;
                int col = 0;
                Map<Location, LocationType> locations = new HashMap<>();
                while ((line = reader.readLine()) != null && !line.isBlank()) {
                    line = line.trim();
                    for (col = 0; col < line.length(); col++) {
                        if (line.charAt(col) == '#') {
                            locations.put(new Location(row, col), LocationType.ROCK);
                        } else if (line.charAt(col) == '.') {
                            locations.put(new Location(row, col), LocationType.ASH);
                        } else {
                            throw new IllegalStateException("Unknown location type: " + line.charAt(col));
                        }
                    }

                    row++;
                }

                patterns.add(new Pattern(col, row, locations));
            } while (line != null);
            

            return patterns;
        }
    }

    private static class Pattern {

        final int width;
        final int height;
        final Map<Location, LocationType> locations;

        Pattern(int width, int height, Map<Location, LocationType> locations) {
            this.width = width;
            this.height = height;
            this.locations = locations;
        }
    }

    private enum LocationType { ROCK, ASH; }

    private static class Location {

        final int row;
        final int col;

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
    }

    private enum ReflectionType { HORIZONTAL, VERTICAL; }

    private static class Reflection {

        final ReflectionType type;
        final int axis;

        Reflection(ReflectionType type, int axis) {
            this.type = type;
            this.axis = axis;
        }
    }
}

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2020/day/24
 */
public class Day24 {

    public static void main(String[] args) throws Exception {
        List<String> instructions = getInstructions();
        part1(instructions);
        part2(instructions);
    }

    private static void part1(List<String> instructions) {
        Set<Point> black = getBlack(instructions);
        System.out.println("Part 1: " + black.size());
    }

    private static void part2(List<String> instructions) {
        Set<Point> black = getBlack(instructions);
        for (int i = 0; i < 100; i++) {
            Set<Point> tiles = new HashSet<>(black);
            for (Point b : black) {
                tiles.add(b.east());
                tiles.add(b.west());
                tiles.add(b.southEast());
                tiles.add(b.southWest());
                tiles.add(b.northEast());
                tiles.add(b.northWest());
            }

            Set<Point> blackNext = new HashSet<>();
            for (Point t : tiles) {
                int nc = 0;
                if (black.contains(t.east())) nc++;
                if (black.contains(t.west())) nc++;
                if (black.contains(t.southEast())) nc++;
                if (black.contains(t.southWest())) nc++;
                if (black.contains(t.northEast())) nc++;
                if (black.contains(t.northWest())) nc++;

                if (black.contains(t)) {
                    if (nc == 1 || nc == 2) {
                        blackNext.add(t);
                    }
                } else {
                    if (nc == 2) {
                        blackNext.add(t);
                    }
                }
            }

            black = blackNext;
        }

        System.out.println("Part 2: " + black.size());
    }

    private static Set<Point> getBlack(List<String> instructions) {
        Point reference = new Point(0, 0);
        Set<Point> black = new HashSet<>();
        for (String instruction : instructions) {
            Point target = identify(reference, instruction);
            if (!black.contains(target)) {
                black.add(target);
            } else {
                black.remove(target);
            }
        }

        return black;
    }

    private static Point identify(Point reference, String instruction) {
        Point current = reference;
        int idx = 0;
        while (idx < instruction.length()) {
            char ch = instruction.charAt(idx);
            if (ch == 'e') {
                current = current.east();
            } else if (ch == 'w') {
                current = current.west();
            } else if (ch == 's') {
                idx++;
                if (idx == instruction.length()) {
                    throw new IllegalStateException();
                }

                ch = instruction.charAt(idx);
                if (ch == 'e') {
                    current = current.southEast();
                } else if (ch == 'w') {
                    current = current.southWest();
                } else {
                    throw new IllegalStateException();
                }
            } else if (ch == 'n') {
                idx++;
                if (idx == instruction.length()) {
                    throw new IllegalStateException();
                }

                ch = instruction.charAt(idx);
                if (ch == 'e') {
                    current = current.northEast();
                } else if (ch == 'w') {
                    current = current.northWest();
                } else {
                    throw new IllegalStateException();
                }
            } else {
                throw new IllegalStateException();
            }

            idx++;
        }

        return current;
    }

    private static List<String> getInstructions() throws Exception {
        List<String> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                instructions.add(line);
            }
        }

        return instructions;
    }

    private static class Point {

        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point east() {
            return new Point(x + 2, y);
        }

        Point southEast() {
            return new Point(x + 1, y + 1);
        }

        Point southWest() {
            return new Point(x - 1, y + 1);
        }

        Point west() {
            return new Point(x - 2, y);
        }

        Point northWest() {
            return new Point(x - 1, y - 1);
        }

        Point northEast() {
            return new Point(x + 1, y - 1);
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return other.x == x && other.y == y;
        }
    }
}

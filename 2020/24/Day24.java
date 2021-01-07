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

        System.out.println("Part 1: " + black.size());
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

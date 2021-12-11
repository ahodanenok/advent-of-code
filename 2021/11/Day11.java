import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2021/day/11
 */
public class Day11 {

    public static void main(String[] args) throws Exception {
        Map<Point, Integer> octopuses = getInput();
        part1(octopuses);
    }

    private static Map<Point, Integer> getInput() throws Exception {
        Map<Point, Integer> octopuses = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                for (int col = 0; col < line.length(); col++) {
                    octopuses.put(new Point(row, col), line.charAt(col) - '0');
                }

                row++;
            }
        }

        return octopuses;
    }

    private static void part1(Map<Point, Integer> octopuses) {
        int flashesCount = 0;
        for (int i = 0; i < 100; i++) {
            for (Point p : octopuses.keySet()) {
                octopuses.merge(p, 1, Integer::sum);
            }

            Set<Point> alreadyFlashed = new HashSet<>();
            while (true) {
                Set<Point> willFlash = new HashSet<>();
                for (Point p : octopuses.keySet()) {
                    if (octopuses.get(p) > 9 && !alreadyFlashed.contains(p)) {
                        willFlash.add(p);
                    }
                }

                if (willFlash.isEmpty()) {
                    break;
                }

                for (Point p : willFlash) {
                    for (Point n : Arrays.asList(
                            p.topLeft(), p.top(), p.topRight(), 
                            p.left(), p.right(),
                            p.bottomLeft(), p.bottom(), p.bottomRight())) {
                        if (octopuses.containsKey(n)) {
                            octopuses.merge(n, 1, Integer::sum);
                        }
                    }
                }

                alreadyFlashed.addAll(willFlash);
                flashesCount += willFlash.size();
            }

            for (Point p : alreadyFlashed) {
                octopuses.put(p, 0);
            }
        }

        System.out.println("Part 1: " + flashesCount);
    }

    private static class Point {

        final int row;
        final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Point topLeft() {
            return new Point(row - 1, col - 1);
        }

        Point top() {
            return new Point(row - 1, col);
        }

        Point topRight() {
            return new Point(row - 1, col + 1);
        }

        Point left() {
            return new Point(row, col - 1);
        }

        Point right() {
            return new Point(row, col + 1);
        }

        Point bottomLeft() {
            return new Point(row + 1, col - 1);
        }

        Point bottom() {
            return new Point(row + 1, col);
        }

        Point bottomRight() {
            return new Point(row + 1, col + 1);
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return row == other.row && col == other.col;
        }
    }
}

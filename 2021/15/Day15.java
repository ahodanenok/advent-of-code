import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2021/day/15
 */
public class Day15 {

    public static void main(String[] args) throws Exception {
        Map<Point, Integer> caves = getInput();
        part1(caves);
    }

    private static Map<Point, Integer> getInput() throws Exception {
        Map<Point, Integer> caves = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.trim().length(); col++) {
                    caves.put(new Point(row, col), line.charAt(col) - '0');
                }

                row++;
            }
        }

        return caves;
    }

    private static void part1(Map<Point, Integer> caves) {
        Point initial = new Point(0, 0);
        Point target = new Point(
            caves.keySet().stream().mapToInt(p -> p.row).max().orElse(0),
            caves.keySet().stream().mapToInt(p -> p.col).max().orElse(0));

        Map<Point, Integer> risks = new HashMap<>();
        risks.put(initial, 0);

        Set<Point> unvisited = new HashSet<>();
        unvisited.addAll(caves.keySet());

        Point current = null;
        while (true) {
            current = unvisited.stream()
                .min((a, b) -> Integer.compare(
                    risks.getOrDefault(a, Integer.MAX_VALUE),
                    risks.getOrDefault(b, Integer.MAX_VALUE)))
                .orElseThrow(IllegalStateException::new);

            if (current.equals(target)) {
                break;
            }

            int riskCurrent = risks.get(current);
            for (Point n : Arrays.asList(current.left(), current.right(), current.top(), current.bottom())) {
                if (!unvisited.contains(n)) {
                    continue;
                }

                int risk = riskCurrent + caves.get(n);
                if (risk < risks.getOrDefault(n, Integer.MAX_VALUE)) {
                    risks.put(n, risk);
                }
            }

            unvisited.remove(current);
        }

        System.out.println("Part 1: " + risks.get(target));
    }

    private static class Point {

        final int row;
        final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Point left() {
            return new Point(row, col - 1);
        }

        Point right() {
            return new Point(row, col + 1);
        }

        Point top() {
            return new Point(row - 1, col);
        }

        Point bottom() {
            return new Point(row + 1, col);
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2021/day/15
 */
public class Day15 {

    public static void main(String[] args) throws Exception {
        Cave cave = getInput();
        part1(cave);
        part2(cave);
    }

    private static Cave getInput() throws Exception {
        Map<Point, Integer> walls = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.trim().length(); col++) {
                    walls.put(new Point(row, col), line.charAt(col) - '0');
                }

                row++;
            }
        }

        return new Cave(walls);
    }

    private static void part1(Cave cave) {
        System.out.println("Part 1: " + lowestRisk(cave));
    }

    private static void part2(Cave cave) {
        Cave entireCave = new Cave(extend(
            extend(cave.walls, 5, (p, idx) -> new Point(p.row, p.col + (cave.width * idx))),
            5,
            (p, idx) -> new Point(p.row + (cave.height * idx), p.col)));

        System.out.println("Part 2: " + lowestRisk(entireCave));
    }

    private static Map<Point, Integer> extend(Map<Point, Integer> walls, int times, BiFunction<Point, Integer, Point> mapper) {
        Map<Point, Integer> extended = new HashMap<>();
        extended.putAll(walls);

        for (Map.Entry<Point, Integer> entry : walls.entrySet()) {
            for (int i = 1; i < times; i++) {
                int risk = entry.getValue() + i;
                if (risk > 9) {
                    risk -= 9;
                }

                extended.put(mapper.apply(entry.getKey(), i), risk);
            }
        }

        return extended;
    }

    private static int lowestRisk(Cave cave) {
        Map<Point, Integer> totalRisk = new HashMap<>();
        totalRisk.put(cave.entrance, 0);

        Set<Point> considered = new HashSet<>();
        considered.add(cave.entrance);

        PriorityQueue<Point> queue = new PriorityQueue<>(
            (a, b) -> Integer.compare(
                totalRisk.getOrDefault(a, Integer.MAX_VALUE),
                totalRisk.getOrDefault(b, Integer.MAX_VALUE)));
        queue.offer(cave.entrance);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(cave.exit)) {
                break;
            }

            int riskCurrent = totalRisk.get(current);
            for (Point n : Arrays.asList(current.left(), current.right(), current.top(), current.bottom())) {
                if (!cave.walls.containsKey(n) || considered.contains(n)) {
                    continue;
                }

                int risk = riskCurrent + cave.walls.get(n);
                if (risk < totalRisk.getOrDefault(n, Integer.MAX_VALUE)) {
                    totalRisk.put(n, risk);
                    // order may change after the new risk level has been assigned
                    queue.remove(n);
                }

                considered.add(n);
                queue.offer(n);
            }
        }

        return totalRisk.get(cave.exit);
    }

    private static class Cave {

        final Map<Point, Integer> walls;

        final Point entrance;
        final Point exit;

        final int width;
        final int height;

        Cave(Map<Point, Integer> walls) {
            this.walls = walls;

            this.entrance = new Point(0, 0);
            this.exit = new Point(
                walls.keySet().stream().mapToInt(p -> p.row).max().orElse(0),
                walls.keySet().stream().mapToInt(p -> p.col).max().orElse(0));

            this.width = exit.col + 1;
            this.height = exit.row + 1;
        }
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

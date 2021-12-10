import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2021/day/9
 */
public class Day9 {

    public static void main(String[] args) throws Exception {
        Grid grid = getInput();
        part1(grid);
        part2(grid);
    }

    private static Grid getInput() throws Exception {
        List<String> map = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                map.add(line.trim());
            }
        }

        return new Grid(map);
    }

    private static void part1(Grid grid) {
        int sum = getLowPoints(grid).stream()
            .mapToInt(p -> grid.heightAt(p) + 1)
            .sum();

        System.out.println("Part 1: " + sum);
    }

    private static void part2(Grid grid) {
        List<Integer> basinSizes = new ArrayList<>();
        for (Point p : getLowPoints(grid)) {
            Set<Point> basin = new HashSet<>();
            basin.add(p);

            LinkedList<Point> queue = new LinkedList<>();
            queue.offer(p);

            while (!queue.isEmpty()) {
                Point current = queue.poll();
                int currentHeight = grid.heightAt(current);

                for (Point n : Arrays.asList(
                        current.left(), 
                        current.top(),
                        current.right(),
                        current.bottom())) {

                    if (!grid.contains(n)) {
                        continue;
                    }

                    int h = grid.heightAt(n);
                    if (h != 9 && h > currentHeight) {
                        basin.add(n);
                        queue.offer(n);
                    }
                }
            }

            basinSizes.add(basin.size());
        }

        int result = basinSizes.stream()
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce((r, n) -> r * n)
            .orElse(0);

        System.out.println("Part 2: " + result);
    }

    private static List<Point> getLowPoints(Grid grid) {
        List<Point> points = new ArrayList<>();
        for (int row = 0; row < grid.height; row++) {
            for (int col = 0; col < grid.width; col++) {
                Point p = new Point(row, col);
                int h = grid.heightAt(p);

                if (grid.contains(p.topLeft()) && grid.heightAt(p.topLeft()) <= h) {
                    continue;
                }

                if (grid.contains(p.top()) && grid.heightAt(p.top()) <= h) {
                    continue;
                }

                if (grid.contains(p.topRight()) && grid.heightAt(p.topRight()) <= h) {
                    continue;
                }

                if (grid.contains(p.left()) && grid.heightAt(p.left()) <= h) {
                    continue;
                }

                if (grid.contains(p.right()) && grid.heightAt(p.right()) <= h) {
                    continue;
                }

                if (grid.contains(p.bottomLeft()) && grid.heightAt(p.bottomLeft()) <= h) {
                    continue;
                }

                if (grid.contains(p.bottom()) && grid.heightAt(p.bottom()) <= h) {
                    continue;
                }

                if (grid.contains(p.bottomRight()) && grid.heightAt(p.bottomRight()) <= h) {
                    continue;
                }

                points.add(p);
            }
        }

        return points;
    }

    private static class Grid {

        private final List<String> rows;
        final int height;
        final int width;

        Grid(List<String> rows) {
            this.height = rows.size();
            this.width = rows.get(0).length();
            this.rows = rows;
        }

        boolean contains(Point p) {
            return p.row >= 0 && p.row < height && p.col >= 0 && p.col < width;
        }

        int heightAt(Point p) {
            return rows.get(p.row).charAt(p.col) - '0';
        }
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

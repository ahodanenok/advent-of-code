import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 10 
 * https://adventofcode.com/2019/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        List<Point> asteroids = getInput();
        part1(asteroids);
    }

    private static void part1(List<Point> asteroids) {
        int maxDetectCount = Integer.MIN_VALUE;
        Point station = null;
        for (Point from : asteroids) {
            int detectCount = 0;

            next:
            for (Point to : asteroids) {
                if (from == to) continue;

                for (Point a : asteroids) {
                     if (a == to) continue;
                     if (a == from) continue;

                     if (sameLine(from, to, a) 
                         && a.distanceTo(from) < to.distanceTo(from)
                         && a.x >= Math.min(from.x, to.x)
                         && a.x <= Math.max(from.x, to.x)
                         && a.y >= Math.min(from.y, to.y)
                         && a.y <= Math.max(from.y, to.y)) { 
                             //System.out.println("same line: " + from + ", " + to + ", " + a);
                             continue next;
                     }
                }

                detectCount++;
            }

            if (detectCount > maxDetectCount) {
                station = from;
                maxDetectCount = detectCount;
            }
        }

        System.out.println("Part 1: " + maxDetectCount);
    }

    private static boolean sameLine(Point a, Point b, Point c) {
        int res = (a.y - b.y) * c.x + (b.x - a.x) * c.y + (a.x * b.y - b.x * a.y);
        return res == 0;
    }

    private static List<Point> getInput() throws Exception {
        List<Point> asteroids = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int y = 0;
            String row;
            while ((row = reader.readLine()) != null) {
                for (int x = 0; x < row.length(); x++) {
                    if (row.charAt(x) == '#') {
                        asteroids.add(new Point(x, y));
                    }
                }

                y++;
            }
        }

        return asteroids;
    }

    private static class Point {

        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int distanceTo(Point other) {
            return (int) Math.abs(x - other.x) + (int) Math.abs(y - other.y);
        }

        @Override
        public String toString() {
             return "(" + x + ", " + y + ")";
        }
    }
}


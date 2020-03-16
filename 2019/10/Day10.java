import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Advent of Code - Day 10 
 * https://adventofcode.com/2019/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        List<Point> asteroids = getInput();
        part1(asteroids);
        part2(asteroids);
    }

    private static void part1(List<Point> asteroids) {
        int maxDetectableCount = Integer.MIN_VALUE;
        Point station = null;
        for (Point from : asteroids) {
            List<Point> detectable = detectableAsteroids(asteroids, from);
            if (detectable.size() > maxDetectableCount) {
                station = from;
                maxDetectableCount = detectable.size();
            }
        }

        System.out.println("Part 1: " + maxDetectableCount + ", station at " + station);
    }

    private static void part2(List<Point> asteroids) {
        asteroids = new ArrayList<>(asteroids);

        int vaporizedCount = 0;
        Point station = new Point(23, 19);

        Comparator<Point> clockwise = new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {
                if (a.x < station.x && b.x >= station.x) {
                    return 1;
                } else if (a.x >= station.x && b.x < station.x) {
                    return -1;
                } else {
                    return (a.x - station.x) * (station.y - b.y) - (b.x - station.x) * (station.y - a.y);
                }
            }
        };

        List<Point> detectable;
        while (!(detectable = detectableAsteroids(asteroids, station)).isEmpty()) {
            detectable.sort(clockwise);
            for (Point a : detectable) {
                asteroids.remove(a);
                vaporizedCount++;
                if (vaporizedCount == 200) {
                    System.out.println("Part 2: " + ((a.x * 100) + a.y));
                    return;
                }
            }
        }        
    }

    private static List<Point> detectableAsteroids(List<Point> asteroids, Point from) {
        List<Point> detectable = new ArrayList<>();

        next:
        for (Point to : asteroids) {
            if (from.equals(to)) continue;

            for (Point a : asteroids) {
                 if (a.equals(to)) continue;
                 if (a.equals(from)) continue;

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

            detectable.add(to);
        }

        return detectable;
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

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }
    }
}


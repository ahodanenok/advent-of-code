import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Comparator;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2019/day/3
 */
public class Day3 {

    public static void main(String[] args) throws Exception {
        Point startingPoint = new Point(0, 0);
        List<Wire> wires = getWires(startingPoint);

        Set<Point> intersections = new LinkedHashSet<>(wires.get(0).path);
        // retainAll works much faster with set as argument
        intersections.retainAll(new LinkedHashSet<>(wires.get(1).path));
        intersections.remove(startingPoint); 

        part1(wires, startingPoint, intersections);
        part2(wires, startingPoint, intersections);
    }

    private static void part1(List<Wire> wires, Point startingPoint, Set<Point> intersections) {
        Point closestPoint = intersections.iterator().next();
        for (Point p : intersections) {
            if (p.distanceTo(startingPoint) < closestPoint.distanceTo(startingPoint)) {
                closestPoint = p;
            }
        } 
       
        System.out.println("Part 1: " + closestPoint.distanceTo(startingPoint));
    }

    private static void part2(List<Wire> wires, Point startingPoint, Set<Point> intersections) {
        int delay = Integer.MAX_VALUE;
        for (Point p : intersections) {
            int timingA = 0;
            for (Point wp : wires.get(0).path) {
                timingA++;
                if (wp.equals(p)) {
                    break;
                }
            }

            int timingB = 0;
            for (Point wp : wires.get(1).path) {
                timingB++;
                if (wp.equals(p)) {
                    break;
                }
            }

            if (timingA + timingB < delay) {
                delay = timingA + timingB;
            }
        }

        System.out.println("Part 2: " + delay);
    }

    private static List<Wire> getWires(Point startingPoint) throws Exception {
        List<Wire> wires = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Wire wire = new Wire(startingPoint);

                String[] parts = line.split(",");
                for (String part : parts) {
                    wire.trace(part.substring(0, 1), Integer.parseInt(part.substring(1)));
                }

                wires.add(wire);
            }
        }

        return wires;
    }

    private static class Wire {

        Point lastPoint;
        List<Point> path = new ArrayList<>();

        Wire(Point startingPoint) {
            this.lastPoint = startingPoint;
        }

        void trace(String dir, int length) {
            for (int n = 0; n < length; n++) {
                if ("U".equals(dir)) {
                    lastPoint = new Point(lastPoint.x, lastPoint.y - 1);                    
                } else if ("D".equals(dir)) {
                    lastPoint = new Point(lastPoint.x, lastPoint.y + 1);                    
                } else if ("R".equals(dir)) {
                    lastPoint = new Point(lastPoint.x + 1, lastPoint.y);                    
                } else if ("L".equals(dir)) {
                    lastPoint = new Point(lastPoint.x - 1, lastPoint.y);                    
                } else {
                    throw new IllegalArgumentException(dir);
                }

                path.add(lastPoint);
            }
        }
    }

    private static class Point {

        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        } 

        public int distanceTo(Point other) {
            return (int) Math.abs(x - other.x) + (int) Math.abs(y - other.y);
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}


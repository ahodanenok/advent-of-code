import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2023/day/24
 */
public class Day24 {

    // https://www.mathsisfun.com/algebra/line-equation-2points.html
    // https://www.cuemath.com/geometry/intersection-of-two-lines/

    public static void main(String[] args) throws Exception {
        List<Hailstone> hailstones = getInput();
        part1(hailstones);
    }

    private static void part1(List<Hailstone> hailstones) {
        double min = 200000000000000f;
        double max = 400000000000000f;
    
        int count = 0;
        for (int i = 0; i < hailstones.size(); i++) {
            Hailstone h1 = hailstones.get(i);
            LineEquation eq1 = getLine(new Point(h1.x, h1.y), new Point(h1.x + h1.ax, h1.y + h1.ay));
            for (int j = i + 1; j < hailstones.size(); j++) {
                Hailstone h2 = hailstones.get(j);
                LineEquation eq2 = getLine(new Point(h2.x, h2.y), new Point(h2.x + h2.ax, h2.y + h2.ay));
                Point p = getIntersection(eq1, eq2);
                if (p == null) {
                    continue;
                }

                if (p.x < min || p.x > max) {
                    continue;
                }

                if (p.y < min || p.y > max) {
                    continue;
                }

                if (h1.isInThePast(p.x, p.y) || h2.isInThePast(p.x, p.y)) {
                    continue;
                }

                count++;
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static LineEquation getLine(Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double a = dy / dx;
        double b = -1;
        double c = a * -p1.x + p1.y;

        return new LineEquation(a, b, c);
    }

    private static Point getIntersection(LineEquation eq1, LineEquation eq2) {
        double d = eq1.a * eq2.b - eq2.a * eq1.b;
        if (d == 0) {
            return null;
        }

        double x = (eq1.b * eq2.c - eq2.b * eq1.c) / d;
        double y = (eq1.c * eq2.a - eq2.c * eq1.a) / d;

        return new Point(x, y);
    }

    private static List<Hailstone> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Hailstone> hailstones = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("@");
                String[] coordinates = parts[0].trim().split(",");
                String[] accelerations = parts[1].trim().split(",");
                hailstones.add(new Hailstone(
                    Double.parseDouble(coordinates[0].trim()),
                    Double.parseDouble(coordinates[1].trim()),
                    Double.parseDouble(coordinates[2].trim()),
                    Double.parseDouble(accelerations[0].trim()),
                    Double.parseDouble(accelerations[1].trim()),
                    Double.parseDouble(accelerations[2].trim())));
            }

            return hailstones;
        }
    }

    private static class Hailstone {

        final double x;
        final double y;
        final double z;

        final double ax;
        final double ay;
        final double az;

        Hailstone(double x, double y, double z, double ax, double ay, double az) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.ax = ax;
            this.ay = ay;
            this.az = az;
        }

        boolean isInThePast(double x, double y) {
            if (x < this.x && ax > 0) {
                return true;
            }

            if (x > this.x && ax < 0) {
                return true;
            }

            if (y < this.y && ay > 0) {
                return true;
            }

            if (y > this.y && ay < 0) {
                return true;
            }

            return false;
        }
    }

    private static class LineEquation {

        final double a;
        final double b;
        final double c;

        LineEquation(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    private static class Point {

        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}

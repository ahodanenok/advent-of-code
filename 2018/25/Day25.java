import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2018/day/25
 */
public class Day25 {

    public static void main(String[] args) {
        List<Point> points = getPoints();
        part1(points);
    }

    private static void part1(List<Point> points) {
        int[] cons = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            cons[i] = i;
        }

        int consCount = points.size();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (points.get(i).distanceTo(points.get(j)) > 3) {
                    continue;
                }

                int p = i;
                while (cons[p] != p) p = cons[p];

                int q = j;
                while (cons[q] != q) q = cons[q];

                if (p != q) {
                    cons[p] = q;
                    consCount--;
                }
            }
        }

        System.out.println(consCount);
    }

    private static List<Point> getPoints() {
        List<Point> points = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            points.add(new Point(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3])));
        }

        return points;
    }

    private static class Point {

        private final int a;
        private final int b;
        private final int c;
        private final int d;

        Point(int a, int b, int c, int d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        int distanceTo(Point other) {
            return (int) (Math.abs(a - other.a) + Math.abs(b - other.b) + Math.abs(c - other.c) + Math.abs(d - other.d));
        }
    }
}

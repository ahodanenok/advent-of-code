import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2016/day/3
 */
public class Day3 {

    public static void main(String[] args) {
        List<Triangle> triangles = getTriangles();
        part1(triangles);
    }

    private static void part1(List<Triangle> triangles) {
        int count = 0;
        for (Triangle t : triangles) {
            if (t.isValid()) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static List<Triangle> getTriangles() {
        List<Triangle> triangles = new ArrayList<Triangle>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            triangles.add(new Triangle(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim())));
        }

        return triangles;
    }

    private static class Triangle {

        private final int a;
        private final int b;
        private final int c;

        Triangle(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        boolean isValid() {
            return a < b + c && b < a + c && c < a + b;
        }
    }
}

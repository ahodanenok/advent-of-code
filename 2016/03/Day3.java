import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2016/day/3
 */
public class Day3 {

    public static void main(String[] args) {
        List<String> specs = getSpecs();
        System.out.println(countValid(getTrianglesFromRows(specs))); // part 1
        System.out.println(countValid(getTrianglesFromColumns(specs))); // part 2
    }

    private static List<Triangle> getTrianglesFromRows(List<String> specs) {
        List<Triangle> triangles = new ArrayList<Triangle>();
        for (String spec : specs) {
            String[] parts = spec.trim().split("\\s+");
            triangles.add(new Triangle(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim())));
        }

        return triangles;
    }

    private static List<Triangle> getTrianglesFromColumns(List<String> specs) {
        List<Triangle> triangles = new ArrayList<Triangle>();
        for (int i = 0; i < specs.size(); i += 3) {
            String[] a = specs.get(i).trim().split("\\s+");
            String[] b = specs.get(i + 1).trim().split("\\s+");
            String[] c = specs.get(i + 2).trim().split("\\s+");

            triangles.add(new Triangle(
                Integer.parseInt(a[0].trim()),
                Integer.parseInt(b[0].trim()),
                Integer.parseInt(c[0].trim())));
            triangles.add(new Triangle(
                Integer.parseInt(a[1].trim()),
                Integer.parseInt(b[1].trim()),
                Integer.parseInt(c[1].trim())));
            triangles.add(new Triangle(
                Integer.parseInt(a[2].trim()),
                Integer.parseInt(b[2].trim()),
                Integer.parseInt(c[2].trim())));
        }

        return triangles;
    }

    private static int countValid(List<Triangle> triangles) {
        int count = 0;
        for (Triangle t : triangles) {
            if (t.isValid()) {
                count++;
            }
        }

        return count;
    }

    private static List<String> getSpecs() {
        List<String> specs = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            specs.add(scanner.nextLine().trim());
        }

        return specs;
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

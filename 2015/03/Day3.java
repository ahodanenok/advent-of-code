import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2015/day/3
 */
public class Day3 {

    private static final char UP = '^';
    private static final char DOWN = 'v';
    private static final char LEFT = '<';
    private static final char RIGHT = '>';

    public static void main(String[] args) {
        String instructions = getInstructions();
        part1(instructions);
        part2(instructions);
    }

    private static void part1(String instructions) {
        Set<Point> visited = new HashSet<Point>();
        visited.add(new Point(0, 0));

        Point location = new Point(0, 0);
        for (int i = 0; i < instructions.length(); i++) {
            char dest = instructions.charAt(i);
            if (dest == UP) {
                location.y++;
            } else if (dest == DOWN) {
                location.y--;
            } else if (dest == LEFT) {
                location.x--;
            } else if (dest == RIGHT) {
                location.x++;
            } else {
                throw new IllegalArgumentException("Unknown destination: " + dest);
            }

            if (!visited.contains(location)) {
                visited.add(new Point(location.x, location.y));
            }
        }

        System.out.println(visited.size());
    }

    private static void part2(String instructions) {
        Point santaLocation = new Point(0, 0);
        Point roboLocation = new Point(0, 0);

        Set<Point> visited = new HashSet<Point>(); 
        visited.add(new Point(0, 0));

        Point location;
        boolean santaMoves = true;
        for (int i = 0; i < instructions.length(); i++) {
            char dest = instructions.charAt(i);
            location = santaMoves ? santaLocation : roboLocation;
            if (dest == UP) {
                location.y++;
            } else if (dest == DOWN) {
                location.y--;
            } else if (dest == LEFT) {
                location.x--;
            } else if (dest == RIGHT) {
                location.x++;
            } else {
                throw new IllegalArgumentException("Unknown destination: " + dest);
            }

            if (!visited.contains(location)) {
                visited.add(new Point(location.x, location.y));
            }

            santaMoves = !santaMoves;
        }

        System.out.println(visited.size());
    }

    private static String getInstructions() {
        return new Scanner(System.in).nextLine().trim();
    }

    private static class Point {

        private int x;
        private int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
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
    }
}

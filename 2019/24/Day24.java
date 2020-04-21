import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2019/day/24
 */
public class Day24 {

    public static void main(String[] args) throws Exception {
        Layout layout = getInput();
        part1(layout);
    }

    private static void part1(Layout layout) {
        Set<Point> currentBugs = layout.bugs;

        Set<Set<Point>> seen = new HashSet<>();
        seen.add(currentBugs);

        while (true) {
            Set<Point> nextBugs = new HashSet<>();
            for (int y = 0; y < layout.size; y++) {
                for (int x = 0; x < layout.size; x++) {
                    Point p = new Point(x, y);

                    int nearBugsCount = 0;
                    if (currentBugs.contains(p.left())) nearBugsCount++;
                    if (currentBugs.contains(p.right())) nearBugsCount++;
                    if (currentBugs.contains(p.up())) nearBugsCount++;
                    if (currentBugs.contains(p.down())) nearBugsCount++;

                    if (currentBugs.contains(p) && nearBugsCount == 1) {
                        nextBugs.add(p);
                    } else if (!currentBugs.contains(p)
                            && (nearBugsCount == 1 || nearBugsCount == 2)) {
                        nextBugs.add(p);
                    }
                }
            } 

            currentBugs = nextBugs;
            if (!seen.add(nextBugs)) {
                break;
            }
        }

        int rating = 0;
        for (Point bug : currentBugs) {
            rating += Math.pow(2, bug.y * layout.size + bug.x);
        }

        System.out.println("Part 1: " + rating);
    }

    private static Layout getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Layout layout = new Layout();

            int y = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                layout.size = line.length();
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '#') {
                        layout.bugs.add(new Point(x, y));
                    }
                }

                y++;
            }

            return layout;
        }
    }

    private static class Layout {

        int size;
        Set<Point> bugs = new HashSet<>();
    }

    private static class Point {

        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point left() {
            return new Point(x - 1, y);
        }

        Point right() {
            return new Point(x + 1, y);
        }

        Point down() {
            return new Point(x, y + 1);
        }

        Point up() {
            return new Point(x, y - 1);
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


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
        part2(layout);
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

    private static void part2(Layout layout) {
        Set<Point> currentBugs = layout.bugs;

        for (int m = 0; m < 200; m++) {
            Set<Point> affected = new HashSet<>(currentBugs);
            for (Point bug : currentBugs) {
                affected.addAll(adjacent(bug, layout.size)); 
            }

            Set<Point> nextBugs = new HashSet<>();
            for (Point p : affected) {
                int nearBugsCount = 0;
                for (Point adj : adjacent(p, layout.size)) {
                    if (currentBugs.contains(adj)) nearBugsCount++;
                } 
               

                if (currentBugs.contains(p) && nearBugsCount == 1) {
                    nextBugs.add(p);
                } else if (!currentBugs.contains(p)
                        && (nearBugsCount == 1 || nearBugsCount == 2)) {
                    nextBugs.add(p);
                }

            }

            currentBugs = nextBugs;
        }

        System.out.println("Part 2: " + currentBugs.size());
    }

    private static Set<Point> adjacent(Point p, int layoutSize) {
        Set<Point> adjacent = new HashSet<>();

        int nestedIdx = layoutSize / 2;
        Point nested = new Point(nestedIdx, nestedIdx, p.z);

        if (p.left().x >= 0 && !p.left().equals(nested)) { 
            adjacent.add(p.left());
        }
        if (p.right().x < layoutSize && !p.right().equals(nested)) { 
            adjacent.add(p.right());
        }
        if (p.up().y >= 0 && !p.up().equals(nested)) {
            adjacent.add(p.up());
        }
        if (p.down().y < layoutSize && !p.down().equals(nested)) {
            adjacent.add(p.down());
        }

        if (p.x == 0) {
            adjacent.add(new Point(nestedIdx - 1, nestedIdx, p.z + 1));
        }
        if (p.x == layoutSize - 1) {
            adjacent.add(new Point(nestedIdx + 1, nestedIdx, p.z + 1));
        }
        if (p.y == 0) {
            adjacent.add(new Point(nestedIdx, nestedIdx - 1, p.z + 1));
        }
        if (p.y == layoutSize - 1) {
            adjacent.add(new Point(nestedIdx, nestedIdx + 1, p.z + 1));
        }

        if (p.right().equals(nested)) {
            for (int ny = 0; ny < layoutSize; ny++) {
                adjacent.add(new Point(0, ny, p.z - 1));
            }
        }
        if (p.left().equals(nested)) {
            for (int ny = 0; ny < layoutSize; ny++) {
                adjacent.add(new Point(layoutSize - 1, ny, p.z - 1));
            }
        }
        if (p.down().equals(nested)) {
            for (int nx = 0; nx < layoutSize; nx++) {
                adjacent.add(new Point(nx, 0, p.z - 1));
            }
        }
        if (p.up().equals(nested)) {
            for (int nx = 0; nx < layoutSize; nx++) {
                adjacent.add(new Point(nx, layoutSize - 1, p.z - 1));
            }
        }


        return adjacent;
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
        private final int z;

        Point(int x, int y) {
            this(x, y, 0); 
        }

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }


        Point left() {
            return new Point(x - 1, y, z);
        }

        Point right() {
            return new Point(x + 1, y, z);
        }

        Point down() {
            return new Point(x, y + 1, z);
        }

        Point up() {
            return new Point(x, y - 1, z);
        }

        @Override
        public int hashCode() {
            return 31 * 31 * x + 31 * y + z;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return x == other.x && y == other.y && z == other.z;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }
    }
}


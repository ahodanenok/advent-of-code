import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2019/day/20
 */
public class Day20 {

    public static void main(String[] args) throws Exception {
        Maze maze = getMaze();
        part1(maze);
    }

    private static void part1(Maze maze) {
        Set<Point> seen = new HashSet<>();

        LinkedList<State> queue = new LinkedList<>();
        queue.addLast(new State(maze.start, 0));

        while (!queue.isEmpty()) {
            State current = queue.removeFirst();
            if (current.point.equals(maze.exit)) {
                System.out.println("Part 1: " + current.steps);
                break;
            }

            if (maze.portals.containsKey(current.point)) {
                current = new State(maze.portals.get(current.point), current.steps + 1);
                seen.add(current.point);
            }

            for (Point next : Arrays.asList(
                    current.point.up(), current.point.down(),
                    current.point.left(), current.point.right())) {
                if (!maze.open.contains(next)) {
                    continue;
                }

                if (seen.contains(next)) {
                    continue;
                }

                State state = new State(next, current.steps + 1);
                seen.add(next);
                queue.addLast(state);
            }
        }
    }

    private static class State {

        private Point point;
        private int steps;

        State(Point point, int steps) {
            this.point = point;
            this.steps = steps;
        }
    }

    private static Maze getMaze() throws Exception {
        Maze maze = new Maze();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            String prevLine = null;
            int y = 0;
            Map<String, Point> portals = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    char ch = line.charAt(x);
                    if (ch >= 'A' && ch <= 'Z') {                        
                        String portal = null;             
                        Point point = null;
                        if (x + 1 < line.length() && line.charAt(x + 1) >= 'A' && line.charAt(x + 1) <= 'Z') {
                            portal = "" + ch + line.charAt(x + 1);
                            if (x + 2 < line.length() && line.charAt(x + 2) == '.') {
                                point = new Point(x + 2, y); 
                            } else if (x - 1 >= 0 && line.charAt(x - 1) == '.') {
                                point = new Point(x - 1, y);
                            } else {
                                throw new IllegalStateException();
                            }
                        } else if (prevLine != null && prevLine.charAt(x) >= 'A' && prevLine.charAt(x) <= 'Z') {
                            portal = "" + prevLine.charAt(x) + ch;
                            if (maze.open.contains(new Point(x, y - 2))) {
                                point = new Point(x, y - 2);
                            } else {
                                point = new Point(x, y + 1);
                            }
                        }

                        if (portal == null) continue;

                        if ("AA".equals(portal)) {
                            maze.start = point;
                        }
                        if ("ZZ".equals(portal)) {
                            maze.exit = point;
                        } 
                        if (portals.containsKey(portal)) {
                            maze.portals.put(portals.get(portal), point);
                            maze.portals.put(point, portals.get(portal));
                        } else {
                            portals.put(portal, point);
                        }
                    } else if (ch == '.') {
                        maze.open.add(new Point(x, y));
                    }
                }
 
                prevLine = line;
                y++;
            }
        }

        return maze;
    }

    private static class Maze {

        Set<Point> open = new HashSet<>();
        Map<Point, Point> portals = new HashMap<>();
        Point start;
        Point exit;
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


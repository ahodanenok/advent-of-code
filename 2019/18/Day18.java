import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.LinkedList;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2019/day/18
 */
public class Day18 {

    /**
     * Thanks to:
     * https://www.reddit.com/r/adventofcode/comments/ec8090/2019_day_18_solutions#t1_fhiwgh4
     */

    public static void main(String[] args) throws Exception {
        TunnelsMap map = getMap();
        part1(map);
    }

    private static void part1(TunnelsMap map) {
        System.out.println("Part 1: " + collectKeys(map));
    }

    private static int collectKeys(TunnelsMap map) {
        State start = new State();
        start.point = map.start;

        Map<State, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        java.util.PriorityQueue<State> queue = new java.util.PriorityQueue<>(new java.util.Comparator<State>() {
            @Override
            public int compare(State a, State b) {
                return a.steps - b.steps;
            }
        });
        queue.offer(start);

        while (!queue.isEmpty()) {
            State current = queue.remove();
            if (current.keys.size() == map.keys.size()) {
                return current.steps;
            }

            Set<Point> reachableKeys = findReachableKeys(current.point, current.keys, map);
            for (Point k : reachableKeys) {
                State next = new State();
                next.point = k;
                next.keys = new TreeSet<>(current.keys);
                next.keys.add(map.keys.get(k));

                if (!distances.containsKey(next)) {
                    distances.put(next, 100000);
                }

                next.steps = current.steps + steps(current.point, k, current.keys, map);
                if (!distances.containsKey(next) || next.steps < distances.get(next)) {
                    distances.put(next, next.steps);
                    queue.offer(next);
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    private static Set<Point> findReachableKeys(Point from, Set<Character> keys, TunnelsMap map) {
        Set<Point> seen = new HashSet<>();
        Set<Point> reachable = new HashSet<>();

        LinkedList<Point> queue = new LinkedList<>();
        queue.addLast(from);

        while (!queue.isEmpty()) {
            Point current = queue.removeFirst();
            if (map.keys.containsKey(current) && !keys.contains(map.keys.get(current))) {
                reachable.add(current); 
            }

            for (Point next : Arrays.asList(
                    current.left(),
                    current.right(),
                    current.up(),
                    current.down())) {

                if (seen.contains(next)) {
                    continue;
                }

                if (map.doors.containsKey(next) 
                        && keys.contains(Character.toLowerCase(map.doors.get(next)))
                        || map.open.contains(next)) {
                    queue.addLast(next);
                    seen.add(next);
                }
            }
        }

        return reachable;
    }

    private static int steps(Point from, Point to, Set<Character> keys, TunnelsMap map) {
        Set<Point> seen = new HashSet<>();

        LinkedList<Path> queue = new LinkedList<>();
        queue.addLast(new Path(from, 0));

        while (!queue.isEmpty()) {
            Path current = queue.removeFirst();
            if (current.point.equals(to)) {
                return current.steps;
            }

            for (Point next : Arrays.asList(
                    current.point.left(),
                    current.point.right(),
                    current.point.up(),
                    current.point.down())) {

                if (seen.contains(next)) {
                    continue;
                }

                if (map.doors.containsKey(next) 
                        && keys.contains(Character.toLowerCase(map.doors.get(next)))
                        || map.open.contains(next)) {
                    queue.addLast(new Path(next, current.steps + 1));
                    seen.add(next);
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    private static TunnelsMap getMap() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            TunnelsMap map = new TunnelsMap();

            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    Point point = new Point(row, col);
                    char cell = line.charAt(col);
                    if (cell == '#') {
                        continue;
                    } else if (cell == '.') {
                        map.open.add(point);
                    } else if (cell == '@') {
                        map.start = point;
                        map.open.add(point);
                    } else if (cell >= 'a' && cell <= 'z') { 
                        map.keys.put(point, cell);
                        map.open.add(point);
                    } else if (cell >= 'A' && cell <= 'Z') {
                        map.doors.put(point, cell);
                    } else {
                        throw new IllegalArgumentException(cell + "");
                    }
                }
 
                row++;
            }

            return map;
        }
    }

    private static class TunnelsMap {

        Point start;
        Set<Point> open = new HashSet<>();
        Map<Point, Character> doors = new HashMap<>();
        Map<Point, Character> keys = new HashMap<>();
    }

    private static class Path {

        Point point;
        int steps;

        Path(Point point, int steps) {
            this.point = point;
            this.steps = steps; 
        }
    }

    private static class State {

        int steps;
        Point point;
        TreeSet<Character> keys = new TreeSet<>();

        public boolean equals(Object obj) {
            State other = (State) obj;
            return point.equals(other.point) && keys.equals(other.keys);
        }

        public int hashCode() {
            return 31 * point.hashCode() + keys.hashCode();
        }
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


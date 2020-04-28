import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

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
        System.out.println("Part 1: " + collectKeys(getMap("input.txt")));
        System.out.println("Part 2: " + collectKeys(getMap("input2.txt")));
    }

    private static int collectKeys(TunnelsMap map) {
        RobotState start = new RobotState(map.start);

        Map<RobotState, Integer> distances = new HashMap<>();
        distances.put(start, 0);

        PriorityQueue<RobotState> queue = new PriorityQueue<>(new Comparator<RobotState>() {
            @Override
            public int compare(RobotState a, RobotState b) {
                return a.steps - b.steps;
            }
        });
        queue.offer(start);

        while (!queue.isEmpty()) {
            RobotState current = queue.remove();
            if (current.keys.size() == map.keys.size()) {
                return current.steps;
            }

            for (int i = 0; i < current.positions.length; i++) {
                for (Point k : reachableKeys(current.positions[i], current.keys, map)) {
                    RobotState next = new RobotState(current);
                    next.positions[i] = k;
                    next.keys.add(map.keys.get(k));

                    if (!distances.containsKey(next)) {
                        distances.put(next, Integer.MAX_VALUE);
                    }

                    next.steps = current.steps + steps(current.positions[i], k, current.keys, map);
                    if (!distances.containsKey(next) || next.steps < distances.get(next)) {
                        distances.put(next, next.steps);
                        queue.offer(next);
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    private static Set<Point> reachableKeys(Point from, Set<Character> keys, TunnelsMap map) {
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

    private static TunnelsMap getMap(String file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
                        map.start.add(point);
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

        List<Point> start = new LinkedList<>();
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

    private static class RobotState {

        int steps;
        TreeSet<Character> keys; 
        Point[] positions;

        RobotState(List<Point> positions) {
            this.keys = new TreeSet<>();
            this.positions = new Point[positions.size()];
            for (int i = 0; i < positions.size(); i++) {
                this.positions[i] = positions.get(i);
            }
        }

        RobotState(RobotState s) {
            this.keys = new TreeSet<>(s.keys);
            this.steps = s.steps;
            this.positions = Arrays.copyOf(s.positions, s.positions.length);
        }

        @Override
        public boolean equals(Object obj) {
            RobotState other = (RobotState) obj;
            return Arrays.equals(positions, other.positions) && keys.equals(other.keys);
        }

        @Override
        public int hashCode() {
            return 31 * Arrays.hashCode(positions) + keys.hashCode();
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


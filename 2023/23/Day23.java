import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2023/day/23
 */
public class Day23 {

    // took some inspiration from
    // https://www.reddit.com/r/adventofcode/comments/18oy4pc/2023_day_23_solutions/

    public static void main(String[] args) throws Exception {
        Input map = getInput();
        part1(map);
        part2(map);
    }

    private static void part1(Input map) {
        int longestPath = -1;

        Set<Location> visitedLocations = new HashSet<>();
        LinkedList<Location> path = new LinkedList<>();

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(new Step(0, map.startLocation, null));
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();

            path.addLast(currentStep.location);
            visitedLocations.add(currentStep.location);

            if (currentStep.location.equals(map.endLocation)) {
                longestPath = Math.max(currentStep.num, longestPath);

                if (!queue.isEmpty()) {
                    while (!path.getLast().equals(queue.getFirst().prev)) {
                        visitedLocations.remove(path.removeLast());
                    }
                }

                continue;
            }

            List<Location> nextLocations = new ArrayList<>();
            Slope slope = map.slopes.get(currentStep.location);
            if (slope != null) {
                if (slope == Slope.UP) {
                    nextLocations.add(currentStep.location.up());
                } else if (slope == Slope.DOWN) {
                    nextLocations.add(currentStep.location.down());
                } else if (slope == Slope.LEFT) {
                    nextLocations.add(currentStep.location.left());
                } else if (slope == Slope.RIGHT) {
                    nextLocations.add(currentStep.location.right());
                } else {
                    throw new IllegalStateException();
                }
            } else {
                nextLocations.add(currentStep.location.up());
                nextLocations.add(currentStep.location.down());
                nextLocations.add(currentStep.location.left());
                nextLocations.add(currentStep.location.right());
            }

            int count = 0;
            for (Location nextLocation : nextLocations) {
                if (!map.paths.contains(nextLocation)) {
                    continue;
                }

                if (visitedLocations.contains(nextLocation)) {
                    continue;
                }

                queue.addFirst(new Step(currentStep.num + 1, nextLocation, currentStep.location));
                count++;
            }

            if (count == 0 && !queue.isEmpty()) {
                while (!path.getLast().equals(queue.getFirst().prev)) {
                    visitedLocations.remove(path.removeLast());
                }
            }
        }

        System.out.println("Part 1: " + longestPath);
    }

    /* brute-force: ~30 min
    private static void part2(Input map) {
        int longestPath = -1;

        Set<Location> visitedLocations = new HashSet<>();
        LinkedList<Location> path = new LinkedList<>();

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(new Step(0, map.startLocation, null));
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();

            path.addLast(currentStep.location);
            visitedLocations.add(currentStep.location);

            if (currentStep.location.equals(map.endLocation)) {
                longestPath = Math.max(currentStep.num, longestPath);

                if (!queue.isEmpty()) {
                    while (!path.getLast().equals(queue.getFirst().prev)) {
                        visitedLocations.remove(path.removeLast());
                    }
                }

                continue;
            }

            List<Location> nextLocations = new ArrayList<>();
            nextLocations.add(currentStep.location.up());
            nextLocations.add(currentStep.location.down());
            nextLocations.add(currentStep.location.left());
            nextLocations.add(currentStep.location.right());

            int count = 0;
            for (Location nextLocation : nextLocations) {
                if (!map.paths.contains(nextLocation)) {
                    continue;
                }

                if (visitedLocations.contains(nextLocation)) {
                    continue;
                }

                queue.addFirst(new Step(currentStep.num + 1, nextLocation, currentStep.location));
                count++;
            }

            if (count == 0 && !queue.isEmpty()) {
                while (!path.getLast().equals(queue.getFirst().prev)) {
                    visitedLocations.remove(path.removeLast());
                }
            }
        }

        System.out.println("Part 2: " + longestPath);
    }
    */

    private static void part2(Input map) {
        Set<Location> nodes = new HashSet<>();
        nodes.add(map.startLocation);
        nodes.add(map.endLocation);
        for (Location location : map.paths) {
            int adjacentCount = 0;
            for (Location nextLocation : List.of(location.up(), location.down(), location.left(), location.right())) {
                if (map.paths.contains(nextLocation)) {
                    adjacentCount++;
                }
            }

            if (adjacentCount > 2) {
                nodes.add(location);
            }
        }

        TrailGraph graph = new TrailGraph(nodes);
        for (Location location : nodes) {
            connectNode(graph, map, location);
        }

        int longestPath = -1;

        Set<Location> visitedLocations = new HashSet<>();
        LinkedList<Location> path = new LinkedList<>();

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(new Step(0, map.startLocation, null));
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();

            path.addLast(currentStep.location);
            visitedLocations.add(currentStep.location);

            if (currentStep.location.equals(map.endLocation)) {
                longestPath = Math.max(currentStep.num, longestPath);

                if (!queue.isEmpty()) {
                    while (!path.getLast().equals(queue.getFirst().prev)) {
                        visitedLocations.remove(path.removeLast());
                    }
                }

                continue;
            }

            int count = 0;
            for (Location nextLocation : graph.adjacentTo(currentStep.location)) {
                if (visitedLocations.contains(nextLocation)) {
                    continue;
                }

                queue.addFirst(new Step(
                    currentStep.num + graph.getDistance(currentStep.location, nextLocation),
                    nextLocation,
                    currentStep.location));
                count++;
            }

            if (count == 0 && !queue.isEmpty()) {
                while (!path.getLast().equals(queue.getFirst().prev)) {
                    visitedLocations.remove(path.removeLast());
                }
            }
        }

        System.out.println("Part 2: " + longestPath);
    }

    private static void connectNode(TrailGraph graph, Input map, Location fromNode) {
        Set<Location> visitedLocations = new HashSet<>();

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(new Step(0, fromNode, null));
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();
            visitedLocations.add(currentStep.location);

            if (!currentStep.location.equals(fromNode) && graph.contains(currentStep.location)) {
                graph.connect(fromNode, currentStep.location, currentStep.num);
                continue;
            }

            for (Location nextLocation : List.of(
                    currentStep.location.up(),
                    currentStep.location.down(),
                    currentStep.location.left(),
                    currentStep.location.right())) {
                if (!map.paths.contains(nextLocation)) {
                    continue;
                }

                if (visitedLocations.contains(nextLocation)) {
                    continue;
                }

                queue.addLast(new Step(currentStep.num + 1, nextLocation, null));
            }
        }
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Input input = new Input();

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line.trim();
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    Location location = new Location(row, col);
                    if (ch == '.') {
                        if (input.startLocation == null) {
                            input.startLocation = location;
                        }
                        input.endLocation = location;
                        input.paths.add(location);
                    } else if (ch == '#') {
                        // skip
                    } else if (ch == '^') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.UP);
                    } else if (ch == 'v') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.DOWN);
                    } else if (ch == '<') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.LEFT);
                    } else if (ch == '>') {
                        input.paths.add(location);
                        input.slopes.put(location, Slope.RIGHT);
                    } else {
                        throw new IllegalStateException("Unknown symbol: " + ch);
                    }
                }

                row++;
            }

            return input;
        }
    }

    private static class Input {

        Location startLocation;
        Location endLocation;
        Set<Location> paths = new HashSet<>();
        Map<Location, Slope> slopes = new HashMap<>();
    }

    private enum Slope {

        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    private static class Step {

        final int num;
        final Location location;
        final Location prev;

        Step(int num, Location location, Location prev) {
            this.num = num;
            this.location = location;
            this.prev = prev;
        }
    }

    private static class TrailGraph {

        private final Map<Location, Map<Location, Integer>> nodes = new HashMap<>();

        TrailGraph(Set<Location> nodes) {
            for (Location location : nodes) {
                this.nodes.put(location, new HashMap<>());
            }
        }

        boolean contains(Location node) {
            return nodes.containsKey(node);
        }

        void connect(Location fromNode, Location toNode, int distance) {
            nodes.get(fromNode).put(toNode, distance);
            nodes.get(toNode).put(fromNode, distance);
        }

        Set<Location> adjacentTo(Location node) {
            return nodes.get(node).keySet();
        }

        int getDistance(Location fromNode, Location toNode) {
            return nodes.get(fromNode).get(toNode);
        }
    }

    private static class Location {

        final int row;
        final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Location up() {
            return new Location(row - 1, col);
        }

        Location down() {
            return new Location(row + 1, col);
        }

        Location left() {
            return new Location(row, col - 1);
        }

        Location right() {
            return new Location(row, col + 1);
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return row == other.row && col == other.col;
        }
    }
}

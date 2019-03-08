import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2016/day/22
 */
public class Day22 {

    public static void main(String[] args) {
        List<Node> nodes = getNodes();
        System.out.println(viablePairs(nodes).size());
        System.out.println(stepsToMoveData(nodes, new Location(34, 0), new Location(0, 0)));
    }

    private static List<Node> getNodes() {
        List<Node> nodes = new ArrayList<Node>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            String[] pathParts = parts[0].split("-");
            nodes.add(new Node(
                Integer.parseInt(pathParts[1].substring(1, pathParts[1].length())),
                Integer.parseInt(pathParts[2].substring(1, pathParts[2].length())),
                Integer.parseInt(parts[2].substring(0, parts[2].length() - 1)),
                Integer.parseInt(parts[3].substring(0, parts[3].length() - 1))));
        }

        return nodes;
    }

    private static List<Pair> viablePairs(List<Node> nodes) {
        List<Pair> pairs = new ArrayList<Pair>();
        for (int i = 0; i < nodes.size(); i++) {
            Node a = nodes.get(i);
            if (a.used == 0) {
                continue;
            }

            for (int j = 0; j < nodes.size(); j++) {
                if (i == j) {
                    continue;
                }

                Node b = nodes.get(j);
                if (a.used <= b.avail) {
                    pairs.add(new Pair(a, b));
                }
            }
        }

        return pairs;
    }

    private static List<Node> viableNodesFor(int x, int y, List<Node> nodes) {
        List<Pair> pairs = viablePairs(nodes);
        List<Node> viable = new ArrayList<Node>();
        for (Pair p : pairs) {
            if (p.a.x == x && p.a.y == y) {
                viable.add(p.b);
            }
        }

        return viable;
    }

    private static int stepsToMoveData(List<Node> nodes, Location from, Location to) {
        Grid grid = new Grid(nodes);
        int result = Integer.MAX_VALUE;
        for (Node n : viableNodesFor(from.x, from.y, nodes)) {
            int carrierToTarget = steps(new Location(n.x, n.y), from, n, grid);
            // each move here is 5 steps long
            int targetToDest = (steps(from, to, n, grid) - 1) * 5; 
            result = Math.min(carrierToTarget + targetToDest, result);
        }

        return result;
    }

    private static int steps(Location from, Location to, Node carrier, Grid grid) {
        State initialState = new State(from);

        Set<State> seen = new HashSet<State>();
        seen.add(initialState);

        LinkedList<State> queue = new LinkedList<State>();
        queue.add(initialState);

        while (queue.size() > 0) {
            State currentState = queue.poll();
            if (currentState.location.equals(to)) {
                return currentState.steps;
            }

            for (Location location : Arrays.asList(
                        new Location(currentState.location.x, currentState.location.y - 1),
                        new Location(currentState.location.x + 1, currentState.location.y),
                        new Location(currentState.location.x, currentState.location.y + 1),
                        new Location(currentState.location.x - 1, currentState.location.y))) {
                if (location.x >= 0 && location.x < grid.width && location.y >= 0 && location.y < grid.height
                        && carrier.avail >= grid.nodes[location.y][location.x].used) {
                    State nextState = new State(location, currentState.steps + 1);
                    if (!seen.contains(nextState)) {
                        queue.offer(nextState);
                        seen.add(nextState);
                    }
                }
            }
        }

        throw new IllegalStateException();
    }

    private static void printGrid(List<Node> nodes, Node carrier) {
        Grid grid = new Grid(nodes);
        for (int row = 0; row < grid.height; row++) {
            for (int col = 0; col < grid.width; col++) {
                Node node = grid.nodes[row][col];
                if (node.x == carrier.x && node.y == carrier.y) {
                    System.out.print(" _ ");
                } else if (node.used <= carrier.avail) {
                    System.out.print(" . ");
                } else if (node.used > carrier.avail) {
                    System.out.print(" # ");
                }
            }
            System.out.println();
        }
    }

    private static class Pair {

        private final Node a;
        private final Node b;

        Pair(Node a, Node b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "Pair: [A=" + a + ", B=" + b + "]";
        }
    }

    private static class Location {

        private final int x;
        private final int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "Location: [x=" + x + ", y=" + y + "]";
        }
    }

    private static class Grid {

        private final int width;
        private final int height;
        private final Node[][] nodes;

        Grid(List<Node> nodes) {
            int width = Integer.MIN_VALUE;
            int height = Integer.MIN_VALUE;
            for (Node n : nodes) {
                width = Math.max(n.x, width);
                height = Math.max(n.y, height);
            }
            width++;
            height++;

            this.width = width;
            this.height = height;
            this.nodes = new Node[height][width];
            for (Node n : nodes) {
                this.nodes[n.y][n.x] = n;
            }
        }
    }

    private static class Node {

        private final int x;
        private final int y;
        private final int used;
        private final int avail;

        Node(int x, int y, int used, int avail) {
            this.x = x;
            this.y = y;
            this.used = used;
            this.avail = avail;
        }

        @Override
        public String toString() {
            return "Node: [x=" + x + ", y=" + y + ", used=" + used + ", avail=" + avail + "]";
        }
    }

    private static class State {

        private final int steps;
        private final Location location;

        State(Location location) {
            this(location, 0);
        }

        State(Location location, int steps) {
            this.location = location;
            this.steps = steps;
        }

        @Override
        public int hashCode() {
            return 31 * location.hashCode() + steps;
        }

        @Override
        public boolean equals(Object object) {
            State other = (State) object;
            return location.equals(other.location) && steps == other.steps;
        }
    }
}

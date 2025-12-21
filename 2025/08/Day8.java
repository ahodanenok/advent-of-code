import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2025/day/8
 */
public class Day8 {

    public static void main(String... args) throws Exception {
        List<Box> boxes = getInput();
        part1(boxes);
        part2(boxes);
    }

    private static List<Box> getInput() throws Exception {
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            List<Box> boxes = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                boxes.add(new Box(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])));
            }

            return boxes;
        }
    }

    private static void part1(List<Box> boxes) {
        Map<Box, Integer> circuits = new HashMap<>();
        for (int i = 0; i < boxes.size(); i++) {
            circuits.put(boxes.get(i), i);
        }

        LinkedList<Connection> connections = getConnections(boxes);
        for (int n = 0; n < 1000; n++) {
            Connection connection = connections.poll();
            int circuit = circuits.get(connection.to);
            for (Box box : boxes) {
                if (circuits.get(box) == circuit) {
                    circuits.put(box, circuits.get(connection.from));
                }
            }
        }

        Map<Integer, Integer> counts = new HashMap<>();
        for (Box box : boxes) {
            counts.merge(circuits.get(box), 1, (a, b) -> a + b);
        }

        System.out.println("Part 1: "
            + counts.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b));
    }

    private static void part2(List<Box> boxes) {
        Map<Box, Integer> circuits = new HashMap<>();
        for (int i = 0; i < boxes.size(); i++) {
            circuits.put(boxes.get(i), i);
        }

        LinkedList<Connection> connections = getConnections(boxes);
        int circuitsCount = boxes.size();
        while (true) {
            Connection connection = connections.poll();
            int circuit = circuits.get(connection.to);
            for (Box box : boxes) {
                if (circuits.get(box) == circuit) {
                    circuits.put(box, circuits.get(connection.from));
                }
            }

            if (circuit != circuits.get(connection.from)) {
                circuitsCount--;
                if (circuitsCount == 1) {
                    System.out.println("Part 2: "
                        + (connection.from.x * connection.to.x));
                    break;
                }
            }
        }
    }
    
    private static LinkedList<Connection> getConnections(List<Box> boxes) {
        LinkedList<Connection> connections = new LinkedList<>();
        for (int i = 0; i < boxes.size(); i++) {
            Box box = boxes.get(i);
            for (int j = i + 1; j < boxes.size(); j++) {
                connections.add(new Connection(boxes.get(i), boxes.get(j)));
            }
        }

        connections.sort(Comparator.<Connection, Double>comparing(conn -> conn.distance));
        return connections;
    }

    private static class Box {

        final int x;
        final int y;
        final int z;

        Box(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceTo(Box box) {
            return Math.sqrt(
                  Math.pow(x - box.x, 2)
                + Math.pow(y - box.y, 2)
                + Math.pow(z - box.z, 2));
        }

        @Override
        public int hashCode() {
            return 31 * 31 * x + 31 * y + z;
        }

        @Override
        public boolean equals(Object obj) {
            Box other = (Box) obj;
            return x == other.x && y == other.y && z == other.z;
        }

        @Override
        public String toString() {
            return "(%s, %s, %s)".formatted(x, y, z);
        }
    }

    private static class Connection {

        final Box from;
        final Box to;
        final double distance;

        Connection(Box from, Box to) {
            this.from = from;
            this.to = to;
            this.distance = from.distanceTo(to);
        }

        @Override
        public int hashCode() {
            return 31 * from.hashCode() + to.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Connection other = (Connection) obj;
            return from.equals(other.from) && to.equals(other.to);
        }

        @Override
        public String toString() {
            return "%s -> %s (%s)".formatted(from, to, distance);
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2023/day/18
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        System.out.println("Part 1: " + calculateArea(getInput(false)));
        System.out.println("Part 2: " + calculateArea(getInput(true)));
    }

    private static long calculateArea(List<Action> plan) {
        List<Edge> edges = new ArrayList<>();
        Location currentLocation = new Location(0, 0);
        for (Action action : plan) {
            Location nextLocation = action.direction.move(currentLocation, action.meters);
            edges.add(new Edge(currentLocation, nextLocation));
            currentLocation = nextLocation;
        }

        TreeSet<Integer> scanRows = new TreeSet<>(Comparator.naturalOrder());
        for (Edge edge : edges) {
            scanRows.add(edge.from.row);
            scanRows.add(edge.to.row);
        }

        long area = 0;
        int prevScanRow = scanRows.iterator().next();
        for (int scanRow : scanRows) {
            List<Edge> verticalEdges = new ArrayList<>();
            List<Edge> horizontalEdges = new ArrayList<>();
            for (Edge edge : edges) {
                if (edge.from.row >= scanRow && edge.to.row < scanRow
                        || edge.to.row >= scanRow && edge.from.row < scanRow) {
                    verticalEdges.add(edge);
                } else if (edge.from.row == edge.to.row && edge.from.row == scanRow) {
                    horizontalEdges.add(edge);
                }
            }

            if (verticalEdges.size() % 2 != 0) {
                throw new IllegalStateException("not even");
            }

            verticalEdges.sort(Comparator.comparing(e -> Math.min(e.from.col, e.to.col)));

            List<Edge> includedHorizontalEdges = new ArrayList<>();
            if (!verticalEdges.isEmpty()) {
                for (int i = 0; i < verticalEdges.size(); i += 2) {
                    Edge a = verticalEdges.get(i);
                    Edge b = verticalEdges.get(i + 1);
                    area += (long) Math.abs(scanRow - prevScanRow) * (Math.abs(a.from.col - b.from.col) + 1);
                    includedHorizontalEdges.add(
                        new Edge(new Location(scanRow, a.from.col), new Location(scanRow, b.to.col)));
                }
            }

            if (!horizontalEdges.isEmpty()) {
                for (Edge edge : horizontalEdges) {
                    area += Math.abs(edge.from.col - edge.to.col) + 1; 
                }
            }

            for (Edge includedEdge : includedHorizontalEdges) {
                for (Edge edge : horizontalEdges) {
                    int includedFromCol = Math.min(includedEdge.from.col, includedEdge.to.col);
                    int includedToCol = Math.max(includedEdge.from.col, includedEdge.to.col);
                    int edgeFromCol = Math.min(edge.from.col, edge.to.col);
                    int edgeToCol = Math.max(edge.from.col, edge.to.col);
                    if (includedFromCol <= edgeToCol && includedToCol >= edgeFromCol) {
                        area -= (Math.min(includedToCol, edgeToCol) - Math.max(includedFromCol, edgeFromCol) + 1);
                    }
                }
            }

            prevScanRow = scanRow;
        }

        return area;
    }

    private static List<Action> getInput(boolean useColor) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Action> actions = new ArrayList<>();

            Pattern p = Pattern.compile("(U|D|L|R)\\s+(\\d+)\\s+\\(#([a-z0-9]{6})\\)");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line.trim());
                if (!m.find()) {
                    throw new IllegalStateException("Unknown action: " + line);
                }

                Direction direction;
                int meters;
                if (useColor) {
                    String color = m.group(3);
                    meters = Integer.parseInt(color.substring(0, 5), 16);
                    if (color.charAt(5) == '0') {
                        direction = Direction.RIGHT;
                    } else if (color.charAt(5) == '1') {
                        direction = Direction.DOWN;
                    } else if (color.charAt(5) == '2') {
                        direction = Direction.LEFT;
                    } else if (color.charAt(5) == '3') {
                        direction = Direction.UP;
                    } else {
                        throw new IllegalStateException("Unknown direction: " + color.charAt(5));
                    }
                } else {
                    meters = Integer.parseInt(m.group(2));
                    if ("U".equals(m.group(1))) {
                        direction = Direction.UP;
                    } else if ("D".equals(m.group(1))) {
                        direction = Direction.DOWN;
                    } else if ("L".equals(m.group(1))) {
                        direction = Direction.LEFT;
                    } else if ("R".equals(m.group(1))) {
                        direction = Direction.RIGHT;
                    } else {
                        throw new IllegalStateException("Unknown direction: " + m.group(1));
                    }
                }

                actions.add(new Action(direction, meters));
            }

            return actions;
        }
    }

    private static class Action {

        final Direction direction;
        final int meters;

        Action(Direction direction, int meters) {
            this.direction = direction;
            this.meters = meters;
        }
    }

    private enum Direction {

        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        final int rowOffset;
        final int colOffset;

        Direction(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        Location move(Location location, int meters) {
            return new Location(location.row + rowOffset * meters, location.col + colOffset * meters);
        }
    }

    private static class Edge {

        final Location from;
        final Location to;

        Edge(Location from, Location to) {
            this.from = from;
            this.to = to;
        }
    }

    private static class Location {

        final int row;
        final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
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

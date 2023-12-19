import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2023/day/18
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        List<Action> plan = getInput();
        part1(plan);
    }

    private static void part1(List<Action> plan) {
        Location location = new Location(0, 0);
        Set<Location> interior = new HashSet<>();
        interior.add(location);
        for (Action action : plan) {
            for (int n = 0; n < action.meters; n++) {
                location = action.direction.move(location);
                interior.add(location);
            }
        }

        int rowMin = Integer.MAX_VALUE;
        int rowMax = Integer.MIN_VALUE;
        int colMin = Integer.MAX_VALUE;
        int colMax = Integer.MIN_VALUE;
        for (Location loc : interior) {
            rowMin = Math.min(loc.row, rowMin);
            rowMax = Math.max(loc.row, rowMax);
            colMin = Math.min(loc.col, colMin);
            colMax = Math.max(loc.col, colMax);
        }

        Set<Location> outside = new HashSet<>();
        for (int row = rowMin; row <= rowMax; row++) {
            exploreOutside(new Location(row, colMin), interior, outside, rowMin, rowMax, colMin, colMax);
            exploreOutside(new Location(row, colMax), interior, outside, rowMin, rowMax, colMin, colMax);
        }
        for (int col = colMin; col <= colMax; col++) {
            exploreOutside(new Location(rowMin, col), interior, outside, rowMin, rowMax, colMin, colMax);
            exploreOutside(new Location(rowMax, col), interior, outside, rowMin, rowMax, colMin, colMax);
        }

        int width = Math.abs(colMax - colMin) + 1;
        int height = Math.abs(rowMax - rowMin) + 1;

        System.out.println("Part 1: " + (width * height - outside.size()));
    }

    private static void exploreOutside(Location from, Set<Location> interior, Set<Location> outside,
            int rowMin, int rowMax, int colMin, int colMax) {

        if (interior.contains(from) || outside.contains(from)) {
            return;
        }

        LinkedList<Location> queue = new LinkedList<>();
        queue.addLast(from);
        while (!queue.isEmpty()) {
            Location location = queue.removeFirst();
            outside.add(location);
            for (Direction direction : Direction.values()) {
                Location next = direction.move(location);
                if (next.row < rowMin || next.row > rowMax) {
                    continue;
                }
                if (next.col < colMin || next.col > colMax) {
                    continue;
                }
                if (interior.contains(next) || outside.contains(next)) {
                    continue;
                }

                queue.addLast(next);
                outside.add(next);
            }
        }
    }

    private static List<Action> getInput() throws Exception {
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

                actions.add(new Action(direction, Integer.parseInt(m.group(2)), m.group(3)));
                /*for (int i = 0; i <= m.groupCount(); i++) {
                    System.out.print("'" + m.group(i) + "' ");
                    
                }
                System.out.println();*/
            }

            return actions;
        }
    }

    private static class Action {

        final Direction direction;
        final int meters;
        final String color;

        Action(Direction direction, int meters, String color) {
            this.direction = direction;
            this.meters = meters;
            this.color = color;
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

        Location move(Location location) {
            return new Location(location.row + rowOffset, location.col + colOffset);
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

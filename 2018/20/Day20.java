import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.EnumSet;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2018/day/20
 */
public class Day20 {

    public static void main(String[] args) {
        RoomsMap map = buildMap(getRegex());
        part1(map);
    }

    private static void part1(RoomsMap map) {
        Map<Location, Integer> distances = new HashMap<Location, Integer>();
        distances.put(new Location(0, 0), 0);

        LinkedList<Location> queue = new LinkedList<Location>();
        queue.add(new Location(0, 0));

        while (!queue.isEmpty()) {
            Location current = queue.removeFirst();
            for (Location location : map.getAdjacentRooms(current)) {
                if (!distances.containsKey(location)) {
                    distances.put(location, distances.get(current) + 1);
                    queue.addLast(location);
                }
            }
        }

        int maxDistance = Integer.MIN_VALUE;
        for (Map.Entry<Location, Integer> entry : distances.entrySet()) {
            maxDistance = Math.max(entry.getValue(), maxDistance);
        }
        System.out.println(maxDistance);
    }

    private static String getRegex() {
        return new Scanner(System.in).nextLine().trim();
    }

    private static RoomsMap buildMap(String regex) {
        Location current = new Location(0, 0);

        Map<Location, Set<Direction>> rooms = new HashMap<Location, Set<Direction>>();
        rooms.put(current, EnumSet.noneOf(Direction.class));

        LinkedList<Location> stack = new LinkedList<Location>();
        for (int i = 1; i < regex.length() - 1; i++) {
            char ch = regex.charAt(i);
            if (ch == 'N') {
                rooms.get(current).add(Direction.NORTH);

                Location next = current.north();
                if (!rooms.containsKey(next)) {
                    rooms.put(next, EnumSet.noneOf(Direction.class));
                }
                rooms.get(next).add(Direction.SOUTH);

                current = next;
            } else if (ch == 'E') {
                rooms.get(current).add(Direction.EAST);

                Location next = current.east();
                if (!rooms.containsKey(next)) {
                    rooms.put(next, EnumSet.noneOf(Direction.class));
                }
                rooms.get(next).add(Direction.WEST);

                current = next;
            } else if (ch == 'S') {
                rooms.get(current).add(Direction.SOUTH);

                Location next = current.south();
                if (!rooms.containsKey(next)) {
                    rooms.put(next, EnumSet.noneOf(Direction.class));
                }
                rooms.get(next).add(Direction.NORTH);

                current = next;
            } else if (ch == 'W') {
                rooms.get(current).add(Direction.WEST);

                Location next = current.west();
                if (!rooms.containsKey(next)) {
                    rooms.put(next, EnumSet.noneOf(Direction.class));
                }
                rooms.get(next).add(Direction.EAST);

                current = next;
            } else if (ch == '(') {
                stack.push(current);
            } else if (ch == ')') {
                stack.pop();
            } else if (ch == '|') {
                current = stack.peek();
            } else {
                throw new IllegalStateException("Unknown char: " + ch);
            }
        }

        return new RoomsMap(rooms);
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }

    private static class Location {

        private final int row;
        private final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Location north() {
            return new Location(row - 1, col);
        }

        Location east() {
            return new Location(row, col + 1);
        }

        Location south() {
            return new Location(row + 1, col);
        }

        Location west() {
            return new Location(row, col - 1);
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

        @Override
        public String toString() {
            return String.format("(%d, %d)", row, col);
        }
    }

    private static class RoomsMap {

        private int rowStart;
        private int rowEnd;
        private int colStart;
        private int colEnd;

        private final int width;
        private final int height;

        private final Map<Location, Set<Direction>> rooms;

        RoomsMap(Map<Location, Set<Direction>> rooms) {
            this.rooms = rooms;

            rowStart = Integer.MAX_VALUE;
            rowEnd = Integer.MIN_VALUE;
            colStart = Integer.MAX_VALUE;
            colEnd = Integer.MIN_VALUE;
            for (Map.Entry<Location, Set<Direction>> entry : rooms.entrySet()) {
                rowStart = Math.min(entry.getKey().row, rowStart);
                rowEnd = Math.max(entry.getKey().row, rowEnd);
                colStart = Math.min(entry.getKey().col, colStart);
                colEnd = Math.max(entry.getKey().col, colEnd);
            }

            width = colEnd - colStart + 1;
            height = rowEnd - rowStart + 1;
        }

        List<Location> getAdjacentRooms(Location location) {
            List<Location> result = new ArrayList<Location>();

            if (rooms.get(location).contains(Direction.NORTH)) {
                result.add(location.north());
            }

            if (rooms.get(location).contains(Direction.EAST)) {
                result.add(location.east());
            }

            if (rooms.get(location).contains(Direction.SOUTH)) {
                result.add(location.south());
            }

            if (rooms.get(location).contains(Direction.WEST)) {
                result.add(location.west());
            }

            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    Location loc = new Location(row + rowStart, col + colStart);
                    if (rooms.containsKey(loc) && rooms.get(loc).contains(Direction.NORTH)) {
                        sb.append("#-");
                    } else {
                        sb.append("##");
                    }

                    if (col == width - 1) {
                        sb.append('#');
                    }
                }
                sb.append('\n');

                for (int col = 0; col < width; col++) {
                    Location loc = new Location(row + rowStart, col + colStart);
                    if (rooms.containsKey(loc) && rooms.get(loc).contains(Direction.WEST)) {
                        sb.append("|.");
                    } else {
                        sb.append("#.");
                    }

                    if (col == width - 1) {
                        if (rooms.containsKey(loc) && rooms.get(loc).contains(Direction.EAST)) {
                            sb.append("|");
                        } else {
                            sb.append("#");
                        }
                    }
                }
                sb.append('\n');

                if (row == height - 1) {
                    for (int col = 0; col < width; col++) {
                        Location loc = new Location(row + rowStart, col + colStart);
                        if (rooms.containsKey(loc) && rooms.get(loc).contains(Direction.SOUTH)) {
                            sb.append("#-");
                        } else {
                            sb.append("##");
                        }

                        if (col == width - 1) {
                            sb.append('#');
                        }
                    }
                    sb.append('\n');
                }
            }

            return sb.toString();
        }
    }
}
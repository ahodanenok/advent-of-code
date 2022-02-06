import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2021/day/25
 */
public class Day25 {

    public static void main(String[] args) throws Exception {
        part1(getCucumers());
    }

    private static Cucumbers getCucumers() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            int width = 0;
            Set<Location> east = new HashSet<>();
            Set<Location> south = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                width = line.length();
                for (int col = 0; col < width; col++) {
                    char ch = line.charAt(col);
                    if (ch == '>') {
                        east.add(new Location(row, col));
                    } else if (ch == 'v') {
                        south.add(new Location(row, col));
                    }
                }

                row++;
            }

            return new Cucumbers(width, row, east, south);
        }
    }

    private static void part1(Cucumbers cucumbers) {
        int steps = 0;
        while (true) {
            steps++;

            Set<Location> movingEast = new HashSet<>();
            for (Location cc : cucumbers.east) {
                Location next = new Location(cc.row, (cc.col + 1) % cucumbers.width);
                if (!cucumbers.east.contains(next) && !cucumbers.south.contains(next)) {
                    movingEast.add(cc);
                }
            }

            for (Location cc : movingEast) {
                cucumbers.east.remove(cc);
                cucumbers.east.add(new Location(cc.row, (cc.col + 1) % cucumbers.width));
            }

            Set<Location> movingSouth = new HashSet<>();
            for (Location cc : cucumbers.south) {
                Location next = new Location((cc.row + 1) % cucumbers.height, cc.col);
                if (!cucumbers.east.contains(next) && !cucumbers.south.contains(next)) {
                    movingSouth.add(cc);
                }
            }

            for (Location cc : movingSouth) {
                cucumbers.south.remove(cc);
                cucumbers.south.add(new Location((cc.row + 1) % cucumbers.height, cc.col));
            }

            if (movingEast.isEmpty() && movingSouth.isEmpty()) {
                break;
            }
        }

        System.out.println("Part 1: " + steps);
    }

    private static class Cucumbers {

        final int width;
        final int height;

        final Set<Location> east;
        final Set<Location> south;

        Cucumbers(int width, int height, Set<Location> east, Set<Location> south) {
            this.width = width;
            this.height = height;
            this.east = east;
            this.south = south;
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

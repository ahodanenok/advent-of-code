import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2018/day/17
 */
public class Day17 {

    public static void main(String[] args) {
        Scan scan = new Scan(getClayCoordinates());
        part1(scan);
    }

    private static void part1(Scan scan) {
        hydrate(scan);

        int reached = 0;
        for (int row = scan.rowStart; row <= scan.rowEnd; row++) {
            for (int col = scan.colStart; col <= scan.colEnd; col++) {
                Location location = new Location(row, col);
                if (scan.get(location) == Scan.MOVING_WATER || scan.get(location) == Scan.STILL_WATER) {
                    reached++;
                }
            }
        }

        System.out.println(reached);
    }

    private static void hydrate(Scan scan) {
        LinkedList<Location> downLocations = new LinkedList<Location>();
        downLocations.add(new Location(scan.rowStart, 500));
        while (!downLocations.isEmpty()) {
            Location currentLocation = downLocations.removeFirst();
            scan.set(currentLocation, Scan.MOVING_WATER);

            while (scan.inRange(currentLocation.down()) && scan.get(currentLocation.down()) == Scan.EMPTY) {
                currentLocation = currentLocation.down();
                scan.set(currentLocation, Scan.MOVING_WATER);
            }

            Location leftWall = null;
            Location rightWall = null;

            Location jointLocation = currentLocation;
            while (scan.inRange(currentLocation.left())
                    && scan.get(currentLocation.left()) != Scan.CLAY
                    && scan.inRange(currentLocation.down())
                    && (scan.get(currentLocation.down()) == Scan.CLAY || scan.get(currentLocation.down()) == Scan.STILL_WATER)) {
                currentLocation = currentLocation.left();
                scan.set(currentLocation, Scan.MOVING_WATER);
            }

            if (scan.inRange(currentLocation)) {
                if (scan.inRange(currentLocation.left())
                        && scan.get(currentLocation.left()) == Scan.CLAY
                        && scan.inRange(currentLocation.down())
                        && (scan.get(currentLocation.down()) == Scan.CLAY || scan.get(currentLocation.down()) == Scan.STILL_WATER)) {
                    leftWall = currentLocation;
                }

                if (scan.inRange(currentLocation.down()) && scan.get(currentLocation.down()) == Scan.EMPTY) {
                    downLocations.add(currentLocation);
                }
            }

            currentLocation = jointLocation;
            while (scan.inRange(currentLocation.right())
                    && scan.get(currentLocation.right()) != Scan.CLAY
                    && scan.inRange(currentLocation.down())
                    && (scan.get(currentLocation.down()) == Scan.CLAY || scan.get(currentLocation.down()) == Scan.STILL_WATER)) {
                currentLocation = currentLocation.right();
                scan.set(currentLocation, Scan.MOVING_WATER);
            }

            if (scan.inRange(currentLocation)) {
                if (scan.inRange(currentLocation.right())
                        && scan.get(currentLocation.right()) == Scan.CLAY
                        && scan.inRange(currentLocation.down())
                        && (scan.get(currentLocation.down()) == Scan.CLAY || scan.get(currentLocation.down()) == Scan.STILL_WATER)) {
                    rightWall = currentLocation;
                }

                if (scan.inRange(currentLocation.down()) && scan.get(currentLocation.down()) == Scan.EMPTY) {
                    downLocations.add(currentLocation);
                }
            }

            if (leftWall != null && rightWall != null) {
                for (int col = leftWall.col; col <= rightWall.col; col++) {
                    scan.set(new Location(jointLocation.row, col), Scan.STILL_WATER);
                }

                downLocations.addFirst(jointLocation.up());
            }
        }

        //System.out.println(scan);
    }

    private static class Scan {

        private static final int EMPTY = 0;
        private static final int CLAY = 1;
        private static final int STILL_WATER = 3;
        private static final int MOVING_WATER = 4;

        private int rowStart = Integer.MAX_VALUE;
        private int rowEnd = Integer.MIN_VALUE;
        private int colStart = Integer.MAX_VALUE;
        private int colEnd = Integer.MIN_VALUE;

        private int[][] scan;

        Scan(List<Location> clay) {
            for (Location location : clay) {
                rowStart = Math.min(location.row, rowStart);
                rowEnd = Math.max(location.row, rowEnd);
                colStart = Math.min(location.col, colStart);
                colEnd = Math.max(location.col, colEnd);
            }

            colStart--;
            colEnd++;

            int width = colEnd - colStart + 1;
            int height = rowEnd - rowStart + 1;
            scan = new int[height][width];
            for (Location location : clay) {
                scan[location.row - rowStart][location.col - colStart] = CLAY;
            }
        }

        boolean inRange(Location location) {
            return location.row >= rowStart && location.row <= rowEnd && location.col >= colStart && location.col <= colEnd;
        }

        void set(Location location, int type) {
            scan[location.row - rowStart][location.col - colStart] = type;
        }

        int get(Location location) {
            return scan[location.row - rowStart][location.col - colStart];
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < scan.length; row++) {
                for (int col = 0; col < scan[row].length; col++) {
                    if (scan[row][col] == EMPTY) {
                        sb.append('.');
                    } else if (scan[row][col] == CLAY) {
                        sb.append('#');
                    } else if (scan[row][col] == STILL_WATER) {
                        sb.append('~');
                    }  else if (scan[row][col] == MOVING_WATER) {
                        sb.append('|');
                    } else {
                        throw new IllegalStateException();
                    }
                }
                sb.append('\n');
            }

            return sb.toString();
        }
    }

    private static List<Location> getClayCoordinates() {
        List<Location> coordinates = new ArrayList<Location>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split(",");

            if (parts[0].trim().startsWith("x")) {
                int col = Integer.parseInt(parts[0].trim().substring(2));
                String[] rowParts = parts[1].trim().substring(2).split("\\.\\.");
                int rowStart = Integer.parseInt(rowParts[0]);
                int rowEnd = Integer.parseInt(rowParts[1]);
                if (rowEnd < rowStart) throw new IllegalStateException();
                
                for (int row = rowStart; row <= rowEnd; row++) {
                    coordinates.add(new Location(row, col));
                }
            } else {
                int row = Integer.parseInt(parts[0].trim().substring(2));
                String[] colParts = parts[1].trim().substring(2).split("\\.\\.");
                int colStart = Integer.parseInt(colParts[0]);
                int colEnd = Integer.parseInt(colParts[1]);
                if (colEnd < colStart) throw new IllegalStateException();

                for (int col = colStart; col <= colEnd; col++) {
                    coordinates.add(new Location(row, col));
                }
            }
        }

        return coordinates;
    }

    private static class Location {

        private final int row;
        private final int col;

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
        public String toString() {
            return row + "," + col;
        }
    }
}

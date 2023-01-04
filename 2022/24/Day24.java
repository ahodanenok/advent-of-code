import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * https://adventofcode.com/2022/day/24
 */
public class Day24 {

    public static void main(String[] args) throws Exception {
        Valley valley = getValley();
        part1(valley);
    }

    private static void part1(Valley valley) {
        List<Blizzard> blizzards = valley.blizzards;
        Set<Location> expeditions = new HashSet<>();
        expeditions.add(valley.entry);

        int minutes = 0;
        while (!expeditions.contains(valley.exit)) {
            Set<Location> nextExpeditions = new HashSet<>();
            for (Location expedition : expeditions) {
                nextExpeditions.add(expedition);
                for (Direction direction : Direction.values()) {
                    Location nextExpedition = direction.next(expedition);
                    if (nextExpedition.equals(valley.exit)) {
                        nextExpeditions.add(nextExpedition);
                    } else if (nextExpedition.row >= 0 && nextExpedition.row < valley.height
                            && nextExpedition.col >= 0 && nextExpedition.col < valley.width) {
                        nextExpeditions.add(nextExpedition);
                    }
                }
            }

            List<Blizzard> nextBlizzards = new ArrayList<>();
            for (Blizzard blizzard : blizzards) {
                Location nextLocation = blizzard.direction.next(blizzard.location);
                if (nextLocation.row < 0) {
                    nextLocation = new Location(valley.height - 1, nextLocation.col);
                } else if (nextLocation.row == valley.height) {
                    nextLocation = new Location(0, nextLocation.col);
                } else if (nextLocation.col < 0) {
                    nextLocation = new Location(nextLocation.row, valley.width - 1);
                } else if (nextLocation.col == valley.width) {
                    nextLocation = new Location(nextLocation.row, 0);
                }

                nextExpeditions.remove(nextLocation);
                nextBlizzards.add(new Blizzard(blizzard.direction, nextLocation));
            }

            blizzards = nextBlizzards;
            expeditions = nextExpeditions;
            minutes++;
        }

        System.out.println("Part 1: " + minutes);
    }

    private static Valley getValley() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Location entry = null;
            String line = reader.readLine();
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '.') {
                    entry = new Location(-1, col - 1);
                    break;
                }
            }

            int row = 0;
            String last = null;
            List<Blizzard> blizzards = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    if (ch == '<') {
                        blizzards.add(new Blizzard(Direction.LEFT, new Location(row, col - 1)));
                    } else if (ch == '>') {
                        blizzards.add(new Blizzard(Direction.RIGHT, new Location(row, col - 1)));
                    } else if (ch == '^') {
                        blizzards.add(new Blizzard(Direction.UP, new Location(row, col - 1)));
                    } else if (ch == 'v') {
                        blizzards.add(new Blizzard(Direction.DOWN, new Location(row, col - 1)));
                    } else if (ch != '#' && ch != '.') {
                        throw new IllegalStateException(line);
                    }
                }

                last = line;
                row++;
            }

            Location exit = null;
            for (int col = 0; col < last.length(); col++) {
                if (last.charAt(col) == '.') {
                    exit = new Location(row - 1, col - 1);
                    break;
                }
            }

            return new Valley(row - 1, last.length() - 2, entry, exit, blizzards);
        }
    }

    private static class Valley {

        final int height;
        final int width;
        final Location entry;
        final Location exit;
        final List<Blizzard> blizzards;

        Valley(int height, int width, Location entry, Location exit, List<Blizzard> blizzards) {
            this.height = height;
            this.width = width;
            this.entry = entry;
            this.exit = exit;
            this.blizzards = blizzards;
        }
    }

    private static class Blizzard {

        final Direction direction;
        final Location location;

        Blizzard(Direction direction, Location location) {
            this.direction = direction;
            this.location = location;
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

        @Override
        public String toString() {
            return String.format("(%d, %d)", row, col);
        }
    }

    private enum Direction {

        UP {
            @Override
            Location next(Location location) {
                return new Location(location.row - 1, location.col);
            }
        },
        DOWN {
            @Override
            Location next(Location location) {
                return new Location(location.row + 1, location.col);
            }
        },
        LEFT {
            @Override
            Location next(Location location) {
                return new Location(location.row, location.col - 1);
            }
        },
        RIGHT {
            @Override
            Location next(Location location) {
                return new Location(location.row, location.col + 1);
            }
        };

        abstract Location next(Location location);
    }
}

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2018/day/13
 */
public class Day13 {

    public static void main(String[] args) {
        TracksMap map = getMap();
        part1(map);
    }

    private static void part1(TracksMap map) {
        Location collisionLocation = null;
        while (collisionLocation == null) {
            Set<Location> cartLocations = new HashSet<Location>();
            for (Cart cart : map.carts) {
                cart.move(map);
                if (!cartLocations.add(cart.location)) {
                    collisionLocation = cart.location;
                    break;
                }
            }
        }

        System.out.println(collisionLocation);
    }

    private static TracksMap getMap() {
        TracksMap map = new TracksMap();

        Scanner scanner = new Scanner(System.in);
        for (int row = 0; scanner.hasNextLine(); row++) {
            String line = scanner.nextLine();

            boolean horizontalLine = false;
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                Location location = new Location(row, col);

                if (ch == '-') {
                    horizontalLine = true;
                } else if (ch == '|') {
                    horizontalLine = false;
                } else if (ch == '/') {
                    if (horizontalLine) {
                        map.turns.put(location, new Turn(Direction.EAST, Direction.SOUTH));
                    } else {
                        map.turns.put(location, new Turn(Direction.WEST, Direction.NORTH));
                    }

                    horizontalLine = false;
                } else if (ch == '\\') {                    
                    if (horizontalLine) {
                        map.turns.put(location, new Turn(Direction.NORTH, Direction.EAST));
                    } else {
                        map.turns.put(location, new Turn(Direction.SOUTH, Direction.WEST));
                    }

                    horizontalLine = false;
                } else if (ch == '+') {
                    map.intersections.add(location);
                    horizontalLine = true;
                } else if (ch == 'v') {
                    map.carts.add(new Cart(location, Direction.SOUTH));
                    horizontalLine = false;
                } else if (ch == '^') {
                    map.carts.add(new Cart(location, Direction.NORTH));
                    horizontalLine = false;
                } else if (ch == '<') {
                    map.carts.add(new Cart(location, Direction.WEST));
                    horizontalLine = true;
                } else if (ch == '>') {
                    map.carts.add(new Cart(location, Direction.EAST));
                    horizontalLine = true;
                } else if (ch == ' ') {
                    horizontalLine = false;
                } else {
                    throw new IllegalStateException("Unknown map char: " + ch);
                }
            }
        }

        Collections.sort(map.carts, new Comparator<Cart>() {
            @Override
            public int compare(Cart a, Cart b) {
                int cmp = Integer.compare(a.location.row, b.location.row);
                if (cmp == 0) {
                    return Integer.compare(a.location.col, b.location.col);
                } else {
                    return cmp;
                }
            }
        });
        return map;
    }

    private static class TracksMap {

        Set<Location> intersections = new HashSet<Location>();
        Map<Location, Turn> turns = new HashMap<Location, Turn>();
        List<Cart> carts = new ArrayList<Cart>();
    }

    private static class Turn {

        private final Direction turnLeft;
        private final Direction turnRight;

        Turn(Direction turnLeft, Direction turnRight) {
            this.turnLeft = turnLeft;
            this.turnRight = turnRight;
        }

        Direction turn(Direction dir) {
            if (dir == turnLeft) {
                return dir.left();
            } else if (dir == turnRight) {
                return dir.right();
            } else {
                throw new IllegalStateException("Can't turn");
            }
        }
    }

    private static class Cart {

        private Location location;
        private Direction dir;
        private int turns;

        Cart(Location location, Direction dir) {
            this.location = location;
            this.dir = dir;
        }

        void move(TracksMap map) {
            location = dir.next(location);

            if (map.turns.containsKey(location)) {
                dir = map.turns.get(location).turn(dir);
            } else if (map.intersections.contains(location)) {
                if (turns % 3 == 0) {
                    dir = dir.left();
                } else if (turns % 3 == 1) {
                    // straight
                } else if (turns % 3 == 2) {
                    dir = dir.right();
                }

                turns++;
            }
        }
    }

    private enum Direction {
        NORTH(-1, 0) {
            @Override
            public Direction left() {
                return WEST;
            }

            @Override
            public Direction right() {
                return EAST;
            }
        },
        EAST(0, 1) {
            @Override
            public Direction left() {
                return NORTH;
            }

            @Override
            public Direction right() {
                return SOUTH;
            }
        },
        SOUTH(1, 0) {
            @Override
            public Direction left() {
                return EAST;
            }

            @Override
            public Direction right() {
                return WEST;
            }
        },
        WEST(0, -1) {
            @Override
            public Direction left() {
                return SOUTH;
            }

            @Override
            public Direction right() {
                return NORTH;
            }
        };

        private final int offsetRow;
        private final int offsetCol;

        Direction(int offsetRow, int offsetCol) {
            this.offsetRow = offsetRow;
            this.offsetCol = offsetCol;
        }

        Location next(Location location) {
            return new Location(location.row + offsetRow, location.col + offsetCol);
        }

        abstract Direction left();
        abstract Direction right();
    }

    private static class Location {

        private final int row;
        private final int col;

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
            return other.row == row && other.col == col;
        }

        @Override
        public String toString() {
            return col + "," + row;
        }
    }
}

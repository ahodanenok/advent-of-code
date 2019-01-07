import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2018
 */
public class Day13 {

    public static void main(String[] args) {
        Map map = getMap();

        Location firstCollisionLocation = null;
        while (map.carts.size() > 1) {
            List<Cart> carts = new ArrayList<Cart>(map.carts);
            Collections.sort(carts);

            for (Cart cart : carts) {
                cart.move();

                for (Cart other : carts) {
                    if (cart != other && cart.location.equals(other.location)) {
                        map.carts.remove(cart);
                        map.carts.remove(other);

                        if (firstCollisionLocation == null) {
                            firstCollisionLocation = cart.location;
                        }

                        break;
                    }
                }
            }
        }

        System.out.println(firstCollisionLocation);
        System.out.println(map.carts.get(0).location);
    }

    private static Map getMap() {
        List<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        Map map = new Map(lines.get(0).length(), lines.size());
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(0).length(); col++) {
                char ch = lines.get(row).charAt(col);
                Location location = new Location(row, col);

                if (ch == ' ') {
                    map.set(location, Map.EMPTY);
                } else if (ch == '|' || ch == '-') {
                    map.set(location, Map.PATH);
                } else if (ch == '/') {
                    map.set(location, Map.A_TURN);
                } else if (ch == '\\') {
                    map.set(location, Map.B_TURN);
                } else if (ch == '+') {
                    map.set(location, Map.INTERSECTION);
                } else if (ch == '^') {
                    map.carts.add(new Cart(location, Direction.NORTH, map));
                } else if (ch == '>') {
                    map.carts.add(new Cart(location, Direction.EAST, map));
                } else if (ch == 'v') {
                    map.carts.add(new Cart(location, Direction.SOUTH, map));
                } else if (ch == '<') {
                    map.carts.add(new Cart(location, Direction.WEST, map));
                } else {
                    throw new IllegalStateException("Unknown char: " + ch);
                }
            }
        }

        return map;
    }

    private static class Location {

        private final int row;
        private final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public String toString() {
            return col + "," + row;
        }
    }

    private static class Map {

        private static int EMPTY = 0;
        private static int PATH = 1;         // |-
        private static int A_TURN = 2;       // / 
        private static int B_TURN = 3;       // \ 
        private static int INTERSECTION = 4; // +

        private int width;
        private int height;
        private int[][] grid;
        private List<Cart> carts;

        Map(int width, int height) {
            this.width = width;
            this.height = height;
            this.grid = new int[height][width];
            this.carts = new ArrayList<Cart>();
        }

        void set(Location location, int type) {
            this.grid[location.row][location.col] = type;
        }

        int get(Location location) {
            return grid[location.row][location.col];
        }
    }

    private static class Cart implements Comparable<Cart> {

        private Map map;
        private Location location;
        private Direction direction;
        private int turns;

        Cart(Location location, Direction direction, Map map) {
            this.location = location;
            this.direction = direction;
            this.map = map;
        }

        void move() {
            if (direction == Direction.NORTH) {
                location = new Location(location.row - 1, location.col);
            } else if (direction == Direction.EAST) {
                location = new Location(location.row, location.col + 1);
            } else if (direction == Direction.SOUTH) {
                location = new Location(location.row + 1, location.col);
            } else if (direction == Direction.WEST) {
                location = new Location(location.row, location.col - 1);
            }

            if (map.get(location) == Map.A_TURN) {
                if (direction == Direction.NORTH) {
                    turnRight();
                } else if (direction == Direction.WEST) {
                    turnLeft();
                } else if (direction == Direction.SOUTH) {
                    turnRight();
                } else if (direction == Direction.EAST) {
                    turnLeft();
                }
            } else if (map.get(location) == Map.B_TURN) {
                if (direction == Direction.NORTH) {
                    turnLeft();
                } else if (direction == Direction.WEST) {
                    turnRight();
                } else if (direction == Direction.SOUTH) {
                    turnLeft();
                } else if (direction == Direction.EAST) {
                    turnRight();
                }
            } else if (map.get(location) == Map.INTERSECTION) {
                if (turns % 3 == 0) {
                    turnLeft();
                } else if (turns % 3 == 1) {
                    // no-op
                } else if (turns % 3 == 2) {
                    turnRight();
                }

                turns++;
            }
        }

        private void turnLeft() {
            if (direction == Direction.NORTH) {
                direction = Direction.WEST;
            } else if (direction == Direction.EAST) {
                direction = Direction.NORTH;
            } else if (direction == Direction.SOUTH) {
                direction = Direction.EAST;
            } else if (direction == Direction.WEST) {
                direction = Direction.SOUTH;
            } else {
                throw new IllegalStateException("Unknown direction: " + direction);
            }
        }

        private void turnRight() {
            if (direction == Direction.NORTH) {
                direction = Direction.EAST;
            } else if (direction == Direction.EAST) {
                direction = Direction.SOUTH;
            } else if (direction == Direction.SOUTH) {
                direction = Direction.WEST;
            } else if (direction == Direction.WEST) {
                direction = Direction.NORTH;
            } else {
                throw new IllegalStateException("Unknown direction: " + direction);
            }
        }

        @Override
        public int compareTo(Cart other) {
            int rowCmp = Integer.compare(location.row, other.location.row);
            if (rowCmp == 0) {
                return Integer.compare(location.col, other.location.col);
            } else {
                return rowCmp;
            }
        }
    }

    private enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }
}

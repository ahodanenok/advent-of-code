import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2023/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        Grid grid = getInput();
        part1(grid);
        part2(grid);
    }

    private static void part1(Grid grid) {
        int steps = getPerimeter(grid).size();
        System.out.println("Part 1: " + (steps - steps / 2));
    }

    private static void part2(Grid grid) {
        Point fromLocation = new Point(0, 0);
        Set<Point> outside = new HashSet<>();

        Set<Point> visited = new HashSet<>();
        visited.add(fromLocation);

        LinkedList<Point> queue = new LinkedList<>();
        queue.addLast(fromLocation);
        while (!queue.isEmpty()) {
            Point currentLocation = queue.removeFirst();

            // translating to grid coordinates, as we are travelling between the tiles
            Point topLeft = new Point(currentLocation.row - 1, currentLocation.col - 1);
            Point topRight = new Point(currentLocation.row - 1, currentLocation.col);
            Point bottomLeft = new Point(currentLocation.row, currentLocation.col - 1);
            Point bottomRight = new Point(currentLocation.row, currentLocation.col);

            for (Point p : List.of(topLeft, topRight, bottomLeft, bottomRight)) {
                if (grid.tiles.containsKey(p)) {
                    outside.add(p);
                }
            }

            List<Point> nextLocations = new ArrayList<>();
            if (!grid.isWall(topLeft, topRight, Direction.UP)) {
                nextLocations.add(Direction.UP.move(currentLocation));
            }
            if (!grid.isWall(bottomLeft, bottomRight, Direction.DOWN)) {
                nextLocations.add(Direction.DOWN.move(currentLocation));
            }
            if (!grid.isWall(topLeft, bottomLeft, Direction.LEFT)) {
                nextLocations.add(Direction.LEFT.move(currentLocation));
            }
            if (!grid.isWall(topRight, bottomRight, Direction.RIGHT)) {
                nextLocations.add(Direction.RIGHT.move(currentLocation));
            }

            for (Point p : nextLocations) {
                if (visited.contains(p)) {
                    continue;
                }

                queue.addLast(p);
                visited.add(p);
            }
        }

        //grid.print(getPerimeter(grid), outside);
        System.out.println("Part 2: " + (grid.tiles.size() - outside.size()));
    }

    private static Set<Point> getPerimeter(Grid grid) {
        Set<Point> perimeter = new HashSet<>();

        Point previousLocation = grid.startLocation;
        Point currentLocation = grid.startLocation;
        while (true) {
            perimeter.add(currentLocation);

            Point nextLocation;
            Tile tile = grid.tiles.get(currentLocation);

            nextLocation = tile.in(currentLocation);
            if (!nextLocation.equals(previousLocation)) {
                if (nextLocation.equals(grid.startLocation)) {
                    break;
                }

                previousLocation = currentLocation;
                currentLocation = nextLocation;
                continue;
            }

            nextLocation = tile.out(currentLocation);
            if (!nextLocation.equals(previousLocation)) {
                if (nextLocation.equals(grid.startLocation)) {
                    break;
                }

                previousLocation = currentLocation;
                currentLocation = nextLocation;
                continue;
            }
        }

        return perimeter;
    }

    private static Grid getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Point startLocation = null;
            Map<Point, Tile> tiles = new HashMap<>();
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                for (int col = 0; col < line.length(); col++) {
                    char tileChar = line.charAt(col);
                    if (tileChar == 'S') {
                        startLocation = new Point(row, col);
                        continue;
                    }

                    boolean found = false;
                    for (Tile tile : Tile.values()) {
                        if (tile.symbol == tileChar) {
                            tiles.put(new Point(row, col), tile);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalStateException("Unknown tile: " + tileChar);
                    }
                }

                row++;
            }

            return new Grid(startLocation, tiles);
        }
    }

    private static class Grid {

        final Point startLocation;
        final Map<Point, Tile> tiles;

        Grid(Point startLocation, Map<Point, Tile> tiles) {
            this.startLocation = startLocation;
            this.tiles = tiles;
            for (Tile tile : Tile.values()) {
                if (connectedTo(tile, startLocation, tile.in(startLocation))
                        && connectedTo(tile, startLocation, tile.out(startLocation))) {
                    this.tiles.put(startLocation, tile);
                    break;
                }
            }

            if (!this.tiles.containsKey(startLocation)) {
                throw new IllegalStateException("Couldn't determine S pipe");
            }
        }

        boolean connectedTo(Tile fromTile, Point fromPos, Point toPos) {
            if (!fromTile.in(fromPos).equals(toPos) && !fromTile.out(fromPos).equals(toPos)) {
                return false;
            }

            Tile toTile = tiles.get(toPos);
            if (toTile == null) {
                return false;
            }

            if (!toTile.in(toPos).equals(fromPos) && !toTile.out(toPos).equals(fromPos)) {
                return false;
            }

            return true;
        }

        boolean isWall(Point a, Point b, Direction direction) {
            if (!tiles.containsKey(a) && !tiles.containsKey(b)) {
                return true;
            }

            if (direction == Direction.UP || direction == Direction.DOWN) {
                return tiles.getOrDefault(a, Tile.GROUND).isWall(Direction.RIGHT)
                    && tiles.getOrDefault(b, Tile.GROUND).isWall(Direction.LEFT);
            }

            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                return tiles.getOrDefault(a, Tile.GROUND).isWall(Direction.DOWN)
                    && tiles.getOrDefault(b, Tile.GROUND).isWall(Direction.UP);
            }

            throw new IllegalStateException();
        }

        void print(Set<Point> perimeter, Set<Point> outside) {
            int rowMin = Integer.MAX_VALUE;
            int rowMax = Integer.MIN_VALUE;
            int colMin = Integer.MAX_VALUE;
            int colMax = Integer.MIN_VALUE;
            for (Point p : tiles.keySet()) {
                rowMin = Math.min(p.row, rowMin);
                rowMax = Math.max(p.row, rowMax);
                colMin = Math.min(p.col, colMin);
                colMax = Math.max(p.col, colMax);
            }

            for (int row = rowMin; row <= rowMax; row++) {
                for (int col = colMin; col <= colMax; col++) {
                    Point p = new Point(row, col);
                    Tile t = tiles.get(p);
                    if (perimeter.contains(p)) {
                        if (t == Tile.PIPE_NORTH_EAST) {
                            System.out.print("\u2517");
                        } else if (t == Tile.PIPE_NORTH_WEST) {
                            System.out.print("\u251B");
                        } else if (t == Tile.PIPE_SOUTH_EAST) {
                            System.out.print("\u250F");
                        } else if (t == Tile.PIPE_SOUTH_WEST) {
                            System.out.print("\u2513");
                        } else if (t == Tile.PIPE_VERTICAL) {
                            System.out.print("\u2503");
                        } else if (t == Tile.PIPE_HORIZONTAL) {
                            System.out.print("\u2501");
                        } else {
                            throw new IllegalStateException();
                        }
                    } else {
                        if (outside.contains(p)) {
                            System.out.print("O");
                        } else if (t == Tile.PIPE_NORTH_EAST) {
                            System.out.print("\u2514");
                        } else if (t == Tile.PIPE_NORTH_WEST) {
                            System.out.print("\u2518");
                        } else if (t == Tile.PIPE_SOUTH_EAST) {
                            System.out.print("\u250C");
                        } else if (t == Tile.PIPE_SOUTH_WEST) {
                            System.out.print("\u2510");
                        } else if (t == Tile.PIPE_VERTICAL) {
                            System.out.print("\u2502");
                        } else if (t == Tile.PIPE_HORIZONTAL) {
                            System.out.print("\u2500");
                        } else if (t == Tile.GROUND) {
                            System.out.print(".");
                        } else {
                            throw new IllegalStateException();
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    private enum Direction {

        UP {
            @Override
            Point move(Point point) {
                return new Point(point.row - 1, point.col);
            }
        },
        DOWN {
            @Override
            Point move(Point point) {
                return new Point(point.row + 1, point.col);
            }
        },
        LEFT {
            @Override
            Point move(Point point) {
                return new Point(point.row, point.col - 1);
            }
        },
        RIGHT {
            @Override
            Point move(Point point) {
                return new Point(point.row, point.col + 1);
            }
        };

        abstract Point move(Point point);
    }

    private static class Point {

        final int row;
        final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    private enum Tile {

        PIPE_VERTICAL('|', Direction.UP, Direction.DOWN),
        PIPE_HORIZONTAL('-', Direction.LEFT, Direction.RIGHT),
        PIPE_NORTH_EAST('L', Direction.UP, Direction.RIGHT),
        PIPE_NORTH_WEST('J', Direction.UP, Direction.LEFT),
        PIPE_SOUTH_WEST('7', Direction.DOWN, Direction.LEFT),
        PIPE_SOUTH_EAST('F', Direction.DOWN, Direction.RIGHT),
        GROUND('.', null, null);

        final char symbol;
        final Direction in;
        final Direction out;

        Tile(char symbol, Direction in, Direction out) {
            this.symbol = symbol;
            this.in = in;
            this.out = out;
        }

        Point in(Point p) {
            if (in != null) {
                return in.move(p);
            } else {
                return p;
            }
        }

        Point out(Point p) {
            if (out != null) {
                return out.move(p);
            } else {
                return p;
            }
        }

        boolean isWall(Direction direction) {
            return in == direction || out == direction;
        }
    }
}

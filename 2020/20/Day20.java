import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2020/day/20
 */
public final class Day20 {

    private static final int TILE_SIZE = 10;

    public static void main(String[] args) throws Exception {
        List<Tile> tiles = getTiles();

        int gridSize = (int) Math.sqrt(tiles.size());
        Map<Point, Tile> placed = new HashMap<>();
        assemble(new Point(0, 0), placed, gridSize, tiles);

        long checksum = 1L;
        checksum *= placed.get(new Point(0, 0)).num;
        checksum *= placed.get(new Point(0, gridSize - 1)).num;
        checksum *= placed.get(new Point(gridSize - 1, 0)).num;
        checksum *= placed.get(new Point(gridSize - 1, gridSize - 1)).num;

        System.out.println("Part 1: " + checksum);
    }

    private static boolean assemble(Point point, Map<Point, Tile> placed, int gridSize, List<Tile> tiles) {
        if (placed.size() == tiles.size()) {
            return true;
        }

        if (point.row >= gridSize) {
            throw new IllegalStateException();
        }

        nextTile:
        for (Tile tile : tiles) {
            for (Tile placedTile : placed.values()) {
                if (placedTile.num == tile.num) {
                    continue nextTile;
                }
            }

            boolean checkTop = placed.containsKey(point.top());
            boolean checkLeft = placed.containsKey(point.left());

            nextVariant:
            for (Tile variant : getVariants(tile)) {
                if (checkTop) {
                    Tile top = placed.get(point.top());
                    for (int col = 0; col < TILE_SIZE; col++) {
                        if (variant.filled.contains(new Point(0, col)) 
                                != top.filled.contains(new Point(TILE_SIZE - 1, col))) {
                            continue nextVariant;
                        }
                    }
                }

                if (checkLeft) {
                    Tile left = placed.get(point.left());
                    for (int row = 0; row < TILE_SIZE; row++) {
                        if (variant.filled.contains(new Point(row, 0)) 
                                != left.filled.contains(new Point(row, TILE_SIZE - 1))) {
                            continue nextVariant;
                        }
                    }
                }

                placed.put(point, variant);

                Point nextPoint = point.right();
                if (nextPoint.col >= gridSize) {
                    nextPoint = new Point(point.row + 1, 0);
                }

                if (assemble(nextPoint, placed, gridSize, tiles)) {
                    return true;
                }

                placed.remove(point);
            }
        }

        return false;
    }

    private static List<Tile> getVariants(Tile tile) {
        // not optimal, but simple
        return Arrays.asList(
            tile.rotateRight(),
            tile.rotateRight().rotateRight(),
            tile.rotateLeft(),
            tile.vflip(),
            tile.vflip().rotateRight(),
            tile.hflip(),
            tile.hflip().rotateRight()
        );
    }

    private static List<Tile> getTiles() throws Exception {
        List<Tile> tiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            int row = 0;
            Tile tile = null;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    if (tile != null) {
                        tiles.add(tile);
                    }

                    break;
                }

                if (line.isEmpty()) {
                    if (tile != null) {
                        tiles.add(tile);
                    }

                    tile = null;
                    continue;
                }

                if (tile == null) {
                    tile = new Tile(Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.length() - 1)));
                    row = 0;
                    continue;
                }

                for (int col = 0; col < TILE_SIZE; col++) {
                    if (line.charAt(col) == '#') {
                        tile.fill(new Point(row, col));
                    }
                }

                row++;
            }
            
        }

        return tiles;
    }

    private static final class Tile {

        private final int num;
        private final Set<Point> filled = new HashSet<>();

        Tile(int num) {
            this.num = num;
        }

        void fill(Point p) {
            this.filled.add(p);
        }

        Tile vflip() {
            Tile tile = new Tile(num);
            for (Point p : filled) {
                tile.fill(p.vflip(TILE_SIZE));
            }

            return tile;
        }

        Tile hflip() {
            Tile tile = new Tile(num);
            for (Point p : filled) {
                tile.fill(p.hflip(TILE_SIZE));
            }

            return tile;
        }

        Tile rotateLeft() {
            Tile tile = new Tile(num);
            for (Point p : filled) {
                tile.fill(p.rotateLeft(TILE_SIZE));
            }

            return tile;
        }

        Tile rotateRight() {
            Tile tile = new Tile(num);
            for (Point p : filled) {
                tile.fill(p.rotateRight(TILE_SIZE));
            }

            return tile;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Tile ").append(num).append(":\n");
            for (int row = 0; row < TILE_SIZE; row++) {
                for (int col = 0; col < TILE_SIZE; col++) {
                    sb.append(filled.contains(new Point(row, col)) ? '#' : '.');
                }

                sb.append('\n');
            }

            return sb.toString();
        }
    }

    private static final class Point {

        private final int row;
        private final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Point vflip(int limit) {
            return new Point(limit - row - 1, col);
        }

        Point hflip(int limit) {
            return new Point(row, limit - col - 1);
        }

        Point rotateLeft(int limit) {
            return new Point(limit - col - 1, row);
        }

        Point rotateRight(int limit) {
            return new Point(col, limit - row - 1);
        }

        Point top() {
            return new Point(row - 1, col);
        }

        Point left() {
            return new Point(row, col - 1);
        }

        Point right() {
            return new Point(row, col + 1);
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return other.row == row && other.col == col;
        }
    }
}

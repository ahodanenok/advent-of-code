import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2020/day/20
 */
public final class Day20 {

    public static void main(String[] args) throws Exception {
        List<Tile> tiles = getTiles();
        part1(tiles);
        part2(tiles);
    }

    private static void part1(List<Tile> tiles) {
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

    private static void part2(List<Tile> tiles) {
        int gridSize = (int) Math.sqrt(tiles.size());
        Map<Point, Tile> placed = new HashMap<>();
        assemble(new Point(0, 0), placed, gridSize, tiles);

        int tileSizeN = tiles.get(0).size - 2;
        Tile image = new Tile(0, gridSize * tileSizeN);
        for (Map.Entry<Point, Tile> entry : placed.entrySet()) {
            Tile tile = entry.getValue();
            Point tilePoint = entry.getKey();
            for (int row = 1; row < tile.size - 1; row++) {
                for (int col = 1; col < tile.size - 1; col++) {
                    if (tile.isFilled(new Point(row, col))) {
                        image.fill(new Point(
                            tilePoint.row * tileSizeN + row - 1,
                            tilePoint.col * tileSizeN + col - 1));
                    }
                }
            }
        }

        for (Tile variant : getVariants(image)) {
            Set<Point> monsterPoints = new HashSet<>();
            for (int row = 0; row < variant.size; row++) {
                for (int col = 0; col < variant.size; col++) {
                    monsterPoints.addAll(checkForMonster(variant, new Point(row, col)));
                }
            }

            if (!monsterPoints.isEmpty()) {
                System.out.println("Part 2: " + (variant.filledCount() - monsterPoints.size()));
                break;
            }
        }
    }

    private static Set<Point> checkForMonster(Tile tile, Point point) {
        Set<Point> points = new HashSet<>();

        for (Point current : Arrays.asList(
                    point,
                    new Point(point.row + 1, point.col + 1),
                    new Point(point.row + 1, point.col + 4),
                    new Point(point.row, point.col + 5),
                    new Point(point.row, point.col + 6),
                    new Point(point.row + 1, point.col + 7),
                    new Point(point.row + 1, point.col + 10),
                    new Point(point.row, point.col + 11),
                    new Point(point.row, point.col + 12),
                    new Point(point.row + 1, point.col + 13),
                    new Point(point.row + 1, point.col + 16),
                    new Point(point.row, point.col + 17),
                    new Point(point.row, point.col + 18),
                    new Point(point.row, point.col + 19),
                    new Point(point.row - 1, point.col + 18))) {
            if (tile.isFilled(current)) {
                points.add(current);
            } else {
                return Collections.emptySet();
            }
        }

        return points;
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
                    for (int col = 0; col < variant.size; col++) {
                        if (variant.isFilled(new Point(0, col)) 
                                != top.isFilled(new Point(variant.size - 1, col))) {
                            continue nextVariant;
                        }
                    }
                }

                if (checkLeft) {
                    Tile left = placed.get(point.left());
                    for (int row = 0; row < variant.size; row++) {
                        if (variant.isFilled(new Point(row, 0)) 
                                != left.isFilled(new Point(row, variant.size - 1))) {
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
            tile,
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
                    tile = new Tile(Integer.parseInt(line.substring(line.indexOf(' ') + 1, line.length() - 1)), 10);
                    row = 0;
                    continue;
                }

                for (int col = 0; col < tile.size; col++) {
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
        private final int size;
        private final Set<Point> filled = new HashSet<>();

        Tile(int num, int size) {
            this.num = num;
            this.size = size;
        }

        void fill(Point p) {
            this.filled.add(p);
        }

        boolean isFilled(Point p) {
            return filled.contains(p);
        }

        int filledCount() {
            return filled.size();
        }

        Tile vflip() {
            Tile tile = new Tile(num, size);
            for (Point p : filled) {
                tile.fill(p.vflip(size));
            }

            return tile;
        }

        Tile hflip() {
            Tile tile = new Tile(num, size);
            for (Point p : filled) {
                tile.fill(p.hflip(size));
            }

            return tile;
        }

        Tile rotateLeft() {
            Tile tile = new Tile(num, size);
            for (Point p : filled) {
                tile.fill(p.rotateLeft(size));
            }

            return tile;
        }

        Tile rotateRight() {
            Tile tile = new Tile(num, size);
            for (Point p : filled) {
                tile.fill(p.rotateRight(size));
            }

            return tile;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Tile ").append(num).append(":\n");
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    sb.append(isFilled(new Point(row, col)) ? '#' : '.');
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

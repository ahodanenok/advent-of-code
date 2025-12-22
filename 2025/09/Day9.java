import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2025/day/9
 */
public class Day9 {

    public static void main(String... args) throws Exception {
        List<Tile> tiles = getInput();
        part1(tiles);
    }

    private static List<Tile> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Tile> tiles = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                tiles.add(new Tile(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1])));
            }

            return tiles;
        }
    }

    private static void part1(List<Tile> tiles) {
        long areaMax = Integer.MIN_VALUE;
        for (int i = 0; i < tiles.size(); i++) {
            Tile a = tiles.get(i);
            for (int j = i + 1; j < tiles.size(); j++) {
                Tile b = tiles.get(j);
                long area = (Math.abs(a.row - b.row) + 1) * (Math.abs(a.col - b.col) + 1);
                if (area > areaMax) {
                    areaMax = area;
                }
            }
        }

        System.out.println("Part 1: " + areaMax);
    }

    private static class Tile {

        final long row;
        final long col;
        
        Tile(long row, long col) {
            this.row = row;
            this.col = col;
        }
    }
}

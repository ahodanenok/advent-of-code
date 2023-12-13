import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2023/day/11
 */
public class Day11 {

    public static void main(String[] args) throws Exception {
        List<Location> galaxies = getInput();
        part1(galaxies);
    }

    private static void part1(List<Location> galaxies) {
        int sum = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                Location source = galaxies.get(i);
                Location dest = galaxies.get(j);

                Set<Integer> rowGalaxies = new HashSet<>();
                for (Location galaxy : galaxies) {
                    if (galaxy.row > source.row && galaxy.row < dest.row
                            || galaxy.row > dest.row && galaxy.row < source.row) {
                        rowGalaxies.add(galaxy.row);
                    }
                }
                int rowDist = Math.abs(dest.row - source.row)
                    + Math.max(Math.abs(dest.row - source.row) - rowGalaxies.size() - 1, 0);

                Set<Integer> colGalaxies = new HashSet<>();
                for (Location galaxy : galaxies) {
                    if (galaxy.col > source.col && galaxy.col < dest.col
                            || galaxy.col > dest.col && galaxy.col < source.col) {
                        colGalaxies.add(galaxy.col);
                    }
                }
                int colDist = Math.abs(dest.col - source.col)
                    + Math.max(Math.abs(dest.col - source.col) - colGalaxies.size() - 1, 0);

                sum += (rowDist + colDist);
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static List<Location> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Location> galaxies = new ArrayList<>();

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    if (ch == '#') {
                        galaxies.add(new Location(row, col));
                    } else if (ch == '.') {
                        // no-op
                    } else {
                        throw new IllegalStateException("Unknown symbol: " + ch);
                    }
                }

                row++;
            }

            return galaxies;
        }
    }

    private static class Location {

        final int row;
        final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}

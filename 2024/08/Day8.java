import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2024/day/8
 */
public class Day8 {

    public static void main(String... args) throws Exception {
        CityMap map = getInput();
        part1(map);
        part2(map);
    }

    private static void part1(CityMap map) {
        Set<Location> antinodeLocations = new HashSet<>();
        for (int i = 0; i < map.antennas.size(); i++) {
            for (int j = i + 1; j < map.antennas.size(); j++) {
                Antenna a = map.antennas.get(i);
                Antenna b = map.antennas.get(j);
                if (a.frequency != b.frequency) {
                    continue;
                }

                if (b.location.row < a.location.row
                        || (b.location.row == a.location.row && b.location.col < a.location.col)) {
                    Antenna tmp = a;
                    a = b;
                    b = tmp;
                }

                int rowOffset = b.location.row - a.location.row;
                int colOffset = b.location.col - a.location.col;

                for (Location antinodeLocation : List.of(
                        new Location(a.location.row - rowOffset, a.location.col - colOffset),
                        new Location(b.location.row + rowOffset, b.location.col + colOffset))) {
                    if (antinodeLocation.row >= 0 && antinodeLocation.row < map.height
                            && antinodeLocation.col >= 0 && antinodeLocation.col < map.width) {
                        antinodeLocations.add(antinodeLocation);
                    }
                }
            }
        }

        System.out.println("Part 1: " + antinodeLocations.size());
    }

    private static void part2(CityMap map) {
        Set<Location> antinodeLocations = new HashSet<>();
        for (int i = 0; i < map.antennas.size(); i++) {
            for (int j = i + 1; j < map.antennas.size(); j++) {
                Antenna a = map.antennas.get(i);
                Antenna b = map.antennas.get(j);
                if (a.frequency != b.frequency) {
                    continue;
                }

                if (b.location.row < a.location.row
                        || (b.location.row == a.location.row && b.location.col < a.location.col)) {
                    Antenna tmp = a;
                    a = b;
                    b = tmp;
                }

                int rowOffset = b.location.row - a.location.row;
                int colOffset = b.location.col - a.location.col;

                int multiplier = 1;
                Location antinodeLocation = a.location;
                while (antinodeLocation.row >= 0 && antinodeLocation.row < map.height
                        && antinodeLocation.col >= 0 && antinodeLocation.col < map.width) {
                    antinodeLocations.add(antinodeLocation);
                    antinodeLocation = new Location(
                        a.location.row - multiplier * rowOffset,
                        a.location.col - multiplier * colOffset);
                    multiplier++;
                }

                multiplier = 1;
                antinodeLocation = b.location;
                while (antinodeLocation.row >= 0 && antinodeLocation.row < map.height
                        && antinodeLocation.col >= 0 && antinodeLocation.col < map.width) {
                    antinodeLocations.add(antinodeLocation);
                    antinodeLocation = new Location(
                        b.location.row + multiplier * rowOffset,
                        b.location.col + multiplier * colOffset);
                    multiplier++;
                }
            }
        }

        System.out.println("Part 2: " + antinodeLocations.size());
    }

    private static CityMap getInput() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        int height = lines.size();
        int width = lines.get(0).length();
        List<Antenna> antennas = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char frequency = lines.get(row).charAt(col);
                if (frequency == '.') {
                    continue;
                }

                antennas.add(new Antenna(frequency, new Location(row, col)));
            }
        }

        return new CityMap(height, width, antennas);
    }

    private static class CityMap {

        final int height;
        final int width;
        final List<Antenna> antennas;

        CityMap(int height, int width, List<Antenna> antennas) {
            this.height = height;
            this.width = width;
            this.antennas = antennas;
        }
    }

    private static class Antenna {

        final char frequency;
        final Location location;

        Antenna(char frequency, Location location) {
            this.frequency = frequency;
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
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2025/day/7
 */
public class Day7 {

    public static void main(String... args) throws Exception {
        Diagram diagram = getInput();
        part1(diagram);
    }

    private static Diagram getInput() throws Exception {
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            List<Location> splitters = new ArrayList<>();
            Location entrance = null;

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);
                    if (ch == 'S') {
                        entrance = new Location(row, col);
                    } else if (ch == '^') {
                        splitters.add(new Location(row, col));
                    }
                }

                row++;
            }

            return new Diagram(entrance, splitters, row);
        }
    }

    private static void part1(Diagram diagram) {
        int splitCount = 0;
        Set<Location> beams = new HashSet<>();
        beams.add(diagram.entrance);
        while (!beams.isEmpty()) {
            Set<Location> beamsNext = new HashSet<>();
            for (Location beam : beams) {
                Location next = new Location(beam.row + 1, beam.col);
                if (next.row >= diagram.height) {
                    continue;
                }

                if (diagram.splitters.contains(next)) {
                    beamsNext.add(new Location(beam.row + 1, beam.col - 1));
                    beamsNext.add(new Location(beam.row + 1, beam.col + 1));
                    splitCount++;
                } else {
                    beamsNext.add(new Location(beam.row + 1, beam.col));
                }
            }

            beams = beamsNext;
        }

        System.out.println("Part 1: " + splitCount);
    }

    private static class Diagram {

        final Location entrance;
        final List<Location> splitters;
        final int height;

        Diagram(Location entrance, List<Location> splitters, int height) {
            this.entrance = entrance;
            this.splitters = splitters;
            this.height = height;
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
    }
}

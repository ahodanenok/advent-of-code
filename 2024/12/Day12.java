import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2024/day/12
 */
public class Day12 {

    public static void main(String... args) throws Exception {
        Map<Plot, Character> map = getInput();
        part1(map);
    }

    private static void part1(Map<Plot, Character> map) {
        long totalPrice = 0;
        Set<Plot> seenPlots = new HashSet<>();
        for (Plot plot : map.keySet()) {
            if (seenPlots.contains(plot)) {
                continue;
            }

            Set<Plot> region = new HashSet<>();
            region.add(plot);

            LinkedList<Plot> queue = new LinkedList<>();
            queue.add(plot);
            while (!queue.isEmpty()) {
                Plot currentPlot = queue.poll();
                seenPlots.add(currentPlot);

                for (Plot nextPlot : List.of(
                        currentPlot.top(), currentPlot.bottom(),
                        currentPlot.left(), currentPlot.right())) {
                    if (Objects.equals(map.get(nextPlot), map.get(plot)) && region.add(nextPlot)) {
                        queue.add(nextPlot);
                    }
                }
            }

            long perimeter = 0;
            for (Plot p : region) {
                for (Plot adjacent : List.of(p.top(), p.bottom(), p.left(), p.right())) {
                    if (!Objects.equals(map.get(p), map.get(adjacent))) {
                        perimeter++;
                    }
                }
            }

            totalPrice += (region.size() * perimeter);
        }

        System.out.println("Part 1: " + totalPrice);
    }

    private static Map<Plot, Character> getInput() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));

        Map<Plot, Character> map = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                Plot plot = new Plot(row, col);
                map.put(plot, line.charAt(col));
            }
        }

        return map;
    }

    private static class Plot {

        final int row;
        final int col;

        Plot(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Plot top() {
            return new Plot(row - 1, col);
        }

        Plot bottom() {
            return new Plot(row + 1, col);
        }

        Plot left() {
            return new Plot(row, col - 1);
        }

        Plot right() {
            return new Plot(row, col + 1);
        }

        @Override
        public boolean equals(Object obj) {
            Plot other = (Plot) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}

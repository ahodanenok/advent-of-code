import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        part2(map);
    }

    private static void part1(Map<Plot, Character> map) {
        long totalPrice = 0;
        for (Set<Plot> region : getRegions(map)) {
            long perimeter = 0;
            for (Plot plot : region) {
                for (Plot nextPlot : List.of(plot.top(), plot.bottom(), plot.left(), plot.right())) {
                    if (!Objects.equals(map.get(plot), map.get(nextPlot))) {
                        perimeter++;
                    }
                }
            }

            totalPrice += (region.size() * perimeter);
        }

        System.out.println("Part 1: " + totalPrice);
    }

    private static void part2(Map<Plot, Character> map) {
        long totalPrice = 0;
        for (Set<Plot> region : getRegions(map)) {
            long sidesNum = 0;
            Set<SideKey> seenPlots = new HashSet<>();
            for (Plot plot : region) {
                if (!Objects.equals(map.get(plot), map.get(plot.top()))
                        && seenPlots.add(new SideKey(plot, Direction.TOP))) {
                    Plot nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.top()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.TOP));
                        nextPlot = nextPlot.left();
                    }
                    nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.top()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.TOP));
                        nextPlot = nextPlot.right();
                    }

                    sidesNum++;
                }

                if (!Objects.equals(map.get(plot), map.get(plot.bottom()))
                        && seenPlots.add(new SideKey(plot, Direction.BOTTOM))) {
                    Plot nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.bottom()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.BOTTOM));
                        nextPlot = nextPlot.left();
                    }
                    nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.bottom()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.BOTTOM));
                        nextPlot = nextPlot.right();
                    }

                    sidesNum++;
                }

                if (!Objects.equals(map.get(plot), map.get(plot.left()))
                        && seenPlots.add(new SideKey(plot, Direction.LEFT))) {
                    Plot nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.left()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.LEFT));
                        nextPlot = nextPlot.bottom();
                    }
                    nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.left()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.LEFT));
                        nextPlot = nextPlot.top();
                    }

                    sidesNum++;
                }

                if (!Objects.equals(map.get(plot), map.get(plot.right()))
                        && seenPlots.add(new SideKey(plot, Direction.RIGHT))) {
                    Plot nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.right()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.RIGHT));
                        nextPlot = nextPlot.bottom();
                    }
                    nextPlot = plot;
                    while (Objects.equals(map.get(plot), map.get(nextPlot))
                            && !Objects.equals(map.get(plot), map.get(nextPlot.right()))) {
                        seenPlots.add(new SideKey(nextPlot, Direction.RIGHT));
                        nextPlot = nextPlot.top();
                    }

                    sidesNum++;
                }
            }

            totalPrice += (region.size() * sidesNum);
        }

        System.out.println("Part 2: " + totalPrice);
    }

    private static List<Set<Plot>> getRegions(Map<Plot, Character> map) {
        List<Set<Plot>> regions = new ArrayList<>();
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

            regions.add(region);
        }

        return regions;
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

    private static class SideKey {

        final Plot plot;
        final Direction direction;

        SideKey(Plot plot, Direction direction) {
            this.plot = plot;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object obj) {
            SideKey other = (SideKey) obj;
            return plot.equals(other.plot) && direction == other.direction;
        }

        @Override
        public int hashCode() {
            return 31 * plot.hashCode() + direction.hashCode();
        }
    }

    private enum Direction {

        TOP, BOTTOM, RIGHT, LEFT;
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

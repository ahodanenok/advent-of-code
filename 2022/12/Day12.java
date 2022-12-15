import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2022/day/12
 */
public class Day12 {

    public static void main(String[] args) throws Exception {
        Heightmap map = getHeightmap();
        part1(map);
        part2(map);
    }

    private static void part1(Heightmap map) {
        for (int row = 0; row < map.height; row++) {
            for (int col = 0; col < map.width; col++) {
                if (map.isStart(row, col)) {
                    System.out.println("Part 1: " + move(map, row, col));
                    return;
                }
            }
        }
    }

    private static void part2(Heightmap map) {
        int shortestPath = Integer.MAX_VALUE;
        for (int row = 0; row < map.height; row++) {
            for (int col = 0; col < map.width; col++) {
                if (map.heightAt(row, col) == 'a') {
                    shortestPath = Math.min(move(map, row, col), shortestPath);
                }
            }
        }

        System.out.println("Part 2: " + shortestPath);
    }

    private static int move(Heightmap map, int rowStart, int colStart) {
        Step initialStep = new Step(rowStart, colStart, 0);

        Set<Step> triedSteps = new HashSet<>();
        triedSteps.add(initialStep);

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(initialStep);
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();
            if (map.isEnd(currentStep.row, currentStep.col)) {
                return currentStep.count;
            }

            int row = currentStep.row;
            int col = currentStep.col;
            char height = map.heightAt(row, col);
            int nextCount = currentStep.count + 1;
            for (Step nextStep : List.of(
                    new Step(row - 1, col, nextCount),
                    new Step(row + 1, col, nextCount),
                    new Step(row, col - 1, nextCount),
                    new Step(row, col + 1, nextCount))) {
                if (triedSteps.contains(nextStep)
                        || nextStep.row < 0 || nextStep.row >= map.height
                        || nextStep.col < 0 || nextStep.col >= map.width) {
                    continue;
                }

                char heightTo = map.heightAt(nextStep.row, nextStep.col);
                if (heightTo - height > 1) {
                    continue;
                }

                queue.addLast(nextStep);
                triedSteps.add(nextStep);
            }
        }

        return Integer.MAX_VALUE;
    }

    private static Heightmap getHeightmap() throws Exception {
        Heightmap map = new Heightmap();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                map.grid.add(line);
            }
        }

        map.height = map.grid.size();
        map.width = map.grid.get(0).length();

        return map;
    }

    private static class Heightmap {

        int height;
        int width;

        List<String> grid = new ArrayList<>();

        boolean isStart(int row, int col) {
            return grid.get(row).charAt(col) == 'S';
        }

        boolean isEnd(int row, int col) {
            return grid.get(row).charAt(col) == 'E';
        }

        char heightAt(int row, int col) {
            char h = grid.get(row).charAt(col);
            if (h == 'S') {
                return 'a';
            } else if (h == 'E') {
                return 'z';
            } else {
                return h;
            }
        }
    }

    private static class Step {

        final int row;
        final int col;
        final int count;

        Step(int row, int col, int count) {
            this.row = row;
            this.col = col;
            this.count = count;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Step other = (Step) obj;
            return row == other.row && col == other.col;
        }
    }
}

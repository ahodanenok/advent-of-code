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
    }

    private static void part1(Heightmap map) {
        int gridHeight = map.grid.size();
        int gridWidth = map.grid.get(0).length();

        Step initialStep = new Step(map.rowStart, map.colStart, 0);

        Set<Step> triedSteps = new HashSet<>();
        triedSteps.add(initialStep);

        LinkedList<Step> queue = new LinkedList<>();
        queue.addLast(initialStep);
        while (!queue.isEmpty()) {
            Step currentStep = queue.removeFirst();
            char height = map.grid.get(currentStep.row).charAt(currentStep.col);
            if (height == 'E') {
                System.out.println("Part 1: " + currentStep.count);
                break;
            }

            if (height == 'S') {
                height = 'a';
            }

            int row = currentStep.row;
            int col = currentStep.col;
            int nextCount = currentStep.count + 1;
            for (Step nextStep : List.of(
                    new Step(row - 1, col, nextCount),
                    new Step(row + 1, col, nextCount),
                    new Step(row, col - 1, nextCount),
                    new Step(row, col + 1, nextCount))) {
                if (triedSteps.contains(nextStep)
                        || nextStep.row < 0 || nextStep.row >= gridHeight
                        || nextStep.col < 0 || nextStep.col >= gridWidth) {
                    continue;
                }

                char heightTo = map.grid.get(nextStep.row).charAt(nextStep.col);
                if (heightTo == 'E') {
                    heightTo = 'z';
                }

                if (heightTo - height > 1) {
                    continue;
                }

                queue.addLast(nextStep);
                triedSteps.add(nextStep);
            }
        }
    }

    private static Heightmap getHeightmap() throws Exception {
        Heightmap map = new Heightmap();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (map.rowStart == -1 && map.colStart == -1) {
                    for (int col = 0; col < line.length(); col++) {
                        if (line.charAt(col) == 'S') {
                            map.rowStart = map.grid.size();
                            map.colStart = col;
                            break;
                        }
                    }
                }

                map.grid.add(line);
            }
        }

        return map;
    }

    private static class Heightmap {

        int rowStart = -1;
        int colStart = -1;

        List<String> grid = new ArrayList<>();
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2024/day/4
 */
public class Day4 {

    public static void main(String... args) throws Exception {
        List<String> input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(List<String> input) {
        int xmasCount = 0;
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) != 'X') {
                    continue;
                }

                if (masUp(input, row - 1, col)) {
                    xmasCount++;
                }

                if (masDown(input, row + 1, col)) {
                    xmasCount++;
                }

                if (masLeft(input, row, col - 1)) {
                    xmasCount++;
                }

                if (masRight(input, row, col + 1)) {
                    xmasCount++;
                }

                if (masUpperLeft(input, row - 1, col - 1)) {
                    xmasCount++;
                }

                if (masUpperRight(input, row - 1, col + 1)) {
                    xmasCount++;
                }

                if (masLowerLeft(input, row + 1, col - 1)) {
                    xmasCount++;
                }

                if (masLowerRight(input, row + 1, col + 1)) {
                    xmasCount++;
                }
            }
        }

        System.out.println("Part 1: " + xmasCount);
    }

    private static void part2(List<String> input) {
        int xmasCount = 0;
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) != 'A') {
                    continue;
                }

                if (masLowerLeft(input, row - 1, col + 1)) {
                    if (masLowerRight(input, row - 1, col - 1)) {
                        xmasCount++;
                    }

                    if (masUpperLeft(input, row + 1, col + 1)) {
                        xmasCount++;
                    }
                }

                if (masUpperRight(input, row + 1, col - 1)) {
                    if (masLowerRight(input, row - 1, col - 1)) {
                        xmasCount++;
                    }
                    
                    if (masUpperLeft(input, row + 1, col + 1)) {
                        xmasCount++;
                    }
                }
            }
        }

        System.out.println("Part 2: " + xmasCount);
    }

    private static boolean masUp(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row - 1, col},
            {row - 2, col}});
    }

    private static boolean masDown(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row + 1, col},
            {row + 2, col}});
    }

    private static boolean masLeft(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row, col - 1},
            {row, col - 2}});
    }

    private static boolean masRight(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row, col + 1},
            {row, col + 2}});
    }

    private static boolean masUpperLeft(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row - 1, col - 1},
            {row - 2, col - 2}});
    }

    private static boolean masUpperRight(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row - 1, col + 1},
            {row - 2, col + 2}});
    }

    private static boolean masLowerLeft(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row + 1, col - 1},
            {row + 2, col - 2}});
    }

    private static boolean masLowerRight(List<String> input, int row, int col) {
        return matches(input, new int[][] {
            {row, col},
            {row + 1, col + 1},
            {row + 2, col + 2}});
    }

    private static final char[] MAS = new char[] { 'M', 'A', 'S' };
    private static boolean matches(List<String> input, int[][] positions) {
        for (int i = 0; i < MAS.length; i++) {
            int row = positions[i][0];
            int col = positions[i][1];
            if (row < 0 || row >= input.size() || col < 0 || col >= input.get(row).length()) {
                return false;
            }

            if (input.get(row).charAt(col) != MAS[i]) {
                return false;
            }
        }

        return true;
    }

    private static List<String> getInput() throws Exception {
        return Files.readAllLines(Paths.get("input.txt"));
    }
}

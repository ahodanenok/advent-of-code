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
    }

    private static void part1(List<String> input) {
        int xmasCount = 0;
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) != 'X') {
                    continue;
                }

                // up
                if (matches(input, new int[][] {
                        {row - 1, col},
                        {row - 2, col},
                        {row - 3, col}})) {
                    xmasCount++;
                }

                // down
                if (matches(input, new int[][] {
                        {row + 1, col},
                        {row + 2, col},
                        {row + 3, col}})) {
                    xmasCount++;
                }

                // left
                if (matches(input, new int[][] {
                        {row, col - 1},
                        {row, col - 2},
                        {row, col - 3}})) {
                    xmasCount++;
                }

                // right
                if (matches(input, new int[][] {
                        {row, col + 1},
                        {row, col + 2},
                        {row, col + 3}})) {
                    xmasCount++;
                }

                // upper left diagonal
                if (matches(input, new int[][] {
                        {row - 1, col - 1},
                        {row - 2, col - 2},
                        {row - 3, col - 3}})) {
                    xmasCount++;
                }

                // upper right diagonal
                if (matches(input, new int[][] {
                        {row - 1, col + 1},
                        {row - 2, col + 2},
                        {row - 3, col + 3}})) {
                    xmasCount++;
                }

                // lower left diagonal
                if (matches(input, new int[][] {
                        {row + 1, col - 1},
                        {row + 2, col - 2},
                        {row + 3, col - 3}})) {
                    xmasCount++;
                }

                // lower right diagonal
                if (matches(input, new int[][] {
                        {row + 1, col + 1},
                        {row + 2, col + 2},
                        {row + 3, col + 3}})) {
                    xmasCount++;
                }
            }
        }

        System.out.println("Part 1: " + xmasCount);
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

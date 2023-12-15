import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2023/day/14
 */
public class Day14 {

    private static final char TYPE_ROUND_ROCK = 'O';
    private static final char TYPE_CUBE_ROCK = '#';
    private static final char TYPE_EMPTY = '.';

    public static void main(String[] args) throws Exception {
        char[][] platform = getInput();
        part1(platform);
    }

    private static void part1(char[][] platform) {
        int width = platform[0].length;
        int height = platform.length;
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                if (platform[row][col] != TYPE_ROUND_ROCK) {
                    continue;
                }

                int pos = row;
                while (pos > 0 && platform[pos - 1][col] == TYPE_EMPTY) {
                    platform[pos - 1][col] = TYPE_ROUND_ROCK;
                    platform[pos][col] = TYPE_EMPTY;
                    pos--;
                }
            }
        }

        int loadTotal = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (platform[row][col] == TYPE_ROUND_ROCK) {
                    loadTotal += (height - row);
                }
            }
        }

        System.out.println("Part 1: " + loadTotal);
    }

    private static char[][] getInput() throws Exception{
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<String> lines = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }

            char[][] platform = new char[lines.size()][];
            for (int i = 0; i < lines.size(); i++) {
                platform[i] = lines.get(i).toCharArray();
            }

            return platform;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2023/day/14
 */
public class Day14 {

    private static final char TYPE_ROUND_ROCK = 'O';
    private static final char TYPE_CUBE_ROCK = '#';
    private static final char TYPE_EMPTY = '.';

    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        char[][] platform = getInput();
        tiltNorth(platform);
        System.out.println("Part 1: " + calculateTotalLoadOnNorthBeam(platform));
    }

    private static void part2() throws Exception {
        char[][] platform;

        platform = getInput();
        Set<PlatformState> history = new HashSet<>();
        int loopSize = 0;
        int n = 0;
        while (n < 1_000_000_000) {
            tiltNorth(platform);
            tiltWest(platform);
            tiltSouth(platform);
            tiltEast(platform);
            if (!history.add(new PlatformState(platform))) {
                if (loopSize == history.size()) {
                    break;
                }

                loopSize = history.size();
                history.clear();
            }

            n++;
        }

        platform = getInput();
        n = n + (1_000_000_000 - n) % loopSize;
        while (n > 0) {
            tiltNorth(platform);
            tiltWest(platform);
            tiltSouth(platform);
            tiltEast(platform);
            n--;
        }

        System.out.println("Part 2: " + calculateTotalLoadOnNorthBeam(platform));
    }

    private static void tiltNorth(char[][] platform) {
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
    }

    private static void tiltSouth(char[][] platform) {
        int width = platform[0].length;
        int height = platform.length;
        for (int col = 0; col < width; col++) {
            for (int row = height - 1; row >= 0; row--) {
                if (platform[row][col] != TYPE_ROUND_ROCK) {
                    continue;
                }

                int pos = row;
                while (pos < height - 1 && platform[pos + 1][col] == TYPE_EMPTY) {
                    platform[pos + 1][col] = TYPE_ROUND_ROCK;
                    platform[pos][col] = TYPE_EMPTY;
                    pos++;
                }
            }
        }
    }

    private static void tiltWest(char[][] platform) {
        int width = platform[0].length;
        int height = platform.length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (platform[row][col] != TYPE_ROUND_ROCK) {
                    continue;
                }

                int pos = col;
                while (pos > 0 && platform[row][pos - 1] == TYPE_EMPTY) {
                    platform[row][pos - 1] = TYPE_ROUND_ROCK;
                    platform[row][pos] = TYPE_EMPTY;
                    pos--;
                }
            }
        }
    }

    private static void tiltEast(char[][] platform) {
        int width = platform[0].length;
        int height = platform.length;
        for (int row = 0; row < height; row++) {
            for (int col = width - 1; col >= 0; col--) {
                if (platform[row][col] != TYPE_ROUND_ROCK) {
                    continue;
                }

                int pos = col;
                while (pos < width - 1 && platform[row][pos + 1] == TYPE_EMPTY) {
                    platform[row][pos + 1] = TYPE_ROUND_ROCK;
                    platform[row][pos] = TYPE_EMPTY;
                    pos++;
                }
            }
        }
    }

    private static int calculateTotalLoadOnNorthBeam(char[][] platform) {
        int loadTotal = 0;
        int width = platform[0].length;
        int height = platform.length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (platform[row][col] == TYPE_ROUND_ROCK) {
                    loadTotal += (height - row);
                }
            }
        }

        return loadTotal;
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

    private static class PlatformState {

        final char[][] platform;

        PlatformState(char[][] platform) {
            this.platform = new char[platform.length][];
            for (int row = 0; row < platform.length; row++) {
                this.platform[row] = Arrays.copyOf(platform[row], platform[row].length);
            }
        }

        @Override
        public int hashCode() {
            int h = 1;
            for (int row = 0; row < platform.length; row++) {
                h *= 31;
                h += java.util.Arrays.hashCode(platform[row]);
            }

            return h;
        }

        @Override
        public boolean equals(Object obj) {
            PlatformState other = (PlatformState) obj;
            for (int row = 0; row < platform.length; row++) {
                if (!java.util.Arrays.equals(platform[row], other.platform[row])) {
                    return false;
                }
            }

            return true;
        }
    }
}

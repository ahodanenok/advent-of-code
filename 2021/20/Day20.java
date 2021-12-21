import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2021/day/20
 */
public class Day20 {

    private static final char STATE_LIGHT = '#';
    private static final char STATE_DARK =  '.';

    public static void main(String[] args) throws Exception {
        Input input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(Input input) {
        Image image = enchance(enchance(input.image, input.algorithm), input.algorithm);
        if (!image.lit) {
            throw new IllegalStateException("Infinity is lit!");
        }

        System.out.println("Part 1: " + image.pixels.size());
    }

    private static void part2(Input input) {
        Image image = input.image;
        for (int i = 0; i < 50; i++) {
            image = enchance(image, input.algorithm);
        }

        if (!image.lit) {
            throw new IllegalStateException("Infinity is lit!");
        }

        System.out.println("Part 2: " + image.pixels.size());
    }

    private static Image enchance(Image image, String algorithm) {
        int minRow = Integer.MAX_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int maxCol = Integer.MIN_VALUE;
        for (Pixel p : image.pixels) {
            minRow = Math.min(p.row, minRow);
            minCol = Math.min(p.col, minCol);
            maxRow = Math.max(p.row, maxRow);
            maxCol = Math.max(p.col, maxCol);
        }

        boolean lit = image.lit && algorithm.charAt(0) == STATE_DARK
            || !image.lit && algorithm.charAt(algorithm.length() - 1) == STATE_DARK;
        Image result = new Image(lit);
        for (int row = minRow - 1, endRow = maxRow + 2; row < endRow; row++) {
            for (int col = minCol - 1, endCol = maxCol + 2; col < endCol; col++) {
                int value = 0;
                for (Pixel p : Arrays.asList(
                        new Pixel(row - 1, col - 1), new Pixel(row - 1, col), new Pixel(row - 1, col + 1),
                        new Pixel(row,     col - 1), new Pixel(row,     col), new Pixel(row,     col + 1),
                        new Pixel(row + 1, col - 1), new Pixel(row + 1, col), new Pixel(row + 1, col + 1))) {
                    value <<= 1;
                    if (image.isLit(p)) {
                        value |= 1;
                    }
                }

                char ch = algorithm.charAt(value);
                if (result.lit && ch == STATE_LIGHT || !result.lit && ch == STATE_DARK) {
                    result.pixels.add(new Pixel(row, col));
                }
            }
        }

        return result;
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String algorithm = reader.readLine().trim();
            reader.readLine();

            Image image = new Image(true);
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == STATE_LIGHT) {
                        image.pixels.add(new Pixel(row, col));
                    }
                }

                row++;
            }

            return new Input(image, algorithm);
        }
    }

    private static class Input {

        final Image image;
        final String algorithm;

        Input(Image image, String algorithm) {
            this.image = image;
            this.algorithm = algorithm;
        }
    }

    private static class Image {

        final boolean lit;
        final Set<Pixel> pixels = new HashSet<>();

        Image(boolean lit) {
            this.lit = lit;
        }

        boolean isLit(Pixel p) {
            return lit && pixels.contains(p) || !lit && !pixels.contains(p);
        }
    }

    private static class Pixel {

        final int row;
        final int col;

        Pixel(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Pixel other = (Pixel) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public String toString() {
            return "(" + row +"," + col + ")";
        }
    }
}

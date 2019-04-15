import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2017/day/21
 */
public class Day21 {

    public static void main(String[] args) {
        List<Rule> rules = getRules();
        part1(rules);
        part2(rules);
    }

    private static void part1(List<Rule> rules) {
        Grid grid = new Grid(rules);
        for (int i = 0; i < 5; i++) grid.transform();
        System.out.println(grid.onCount());
    }

    private static List<Rule> getRules() {
        List<Rule> rules = new ArrayList<Rule>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("=>");
            rules.add(new Rule(parts[0], parts[1]));
        }

        return rules;
    }

    private static class Grid {

        private int[][] pixels;
        private List<Rule> rules;

        Grid(List<Rule> rules) {
            this.rules = rules;
            this.pixels = new int[][] {
                { 0, 1, 0 },
                { 0, 0, 1 },
                { 1, 1, 1 }
            };
        }

        int onCount() {
            int count = 0;
            for (int row = 0; row < pixels.length; row++) {
                for (int col = 0; col < pixels.length; col++) {
                    if (pixels[row][col] == 1) {
                        count++;
                    }
                }
            }

            return count;
        }

        void transform() {
            if (pixels.length % 2 == 0) {
                transformBySize(2, 3);
            } else if (pixels.length % 3 == 0) {
                transformBySize(3, 4);
            } else {
                throw new IllegalStateException();
            }
        }

        private void transformBySize(int blockSize, int toBlockSize) {
            int blockCount = pixels.length / blockSize;
            int size = blockCount * toBlockSize;
            int[][] newPixels = new int[size][size];

            for (int rowBlock = 0; rowBlock < blockCount; rowBlock++) {
                for (int colBlock = 0; colBlock < blockCount; colBlock++) {
                    boolean found = false;
                    for (Rule rule : rules) {
                        if (rule.matches(getBlock(blockSize, rowBlock * blockSize, colBlock * blockSize))) {
                            rule.apply(newPixels, rowBlock * toBlockSize, colBlock * toBlockSize);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        print(getBlock(blockSize, rowBlock * blockSize, colBlock * blockSize));
                        throw new IllegalStateException("Enhancement rule not found");
                    }
                }
            }

            this.pixels = newPixels;
        }

        private int[][] getBlock(int size, int row, int col) {
            int[][] block = new int[size][size];
            for (int r = row, i = 0; r < row + size; r++, i++) {
                for (int c = col, j = 0; c < col + size; c++, j++) {
                    block[i][j] = pixels[r][c];
                }
            }

            return block;
        }
    }

    private static void print(int[][] pixels) {
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels.length; col++) {
                System.out.print(pixels[row][col] + " ");
            }
            System.out.println();
        }
    }

    private static class Rule {

        private int[][] from;
        private int[][] to;

        Rule(String fromDef, String toDef) {
            String[] fromDefParts = fromDef.trim().split("/");
            from = new int[fromDefParts.length][fromDefParts.length];
            for (int i = 0; i < from.length; i++) {
                for (int j = 0; j < from.length; j++) {
                    char ch = fromDefParts[i].charAt(j);
                    if (ch == '.') {
                        from[i][j] = 0;
                    } else if (ch == '#') {
                        from[i][j] = 1;
                    } else {
                        throw new IllegalStateException("Unknown pixel state: " + ch);
                    }
                }
            }

            String[] toDefParts = toDef.trim().split("/");
            to = new int[toDefParts.length][toDefParts.length];
            for (int i = 0; i < to.length; i++) {
                for (int j = 0; j < to.length; j++) {
                    char ch = toDefParts[i].charAt(j);
                    if (ch == '.') {
                        to[i][j] = 0;
                    } else if (ch == '#') {
                        to[i][j] = 1;
                    } else {
                        throw new IllegalStateException("Unknown pixel state: " + ch);
                    }
                }
            }
        }

        boolean matches(int[][] block) {
            if (equals(from, block)) {
                return true;
            }

            if (equals(from, flipVert(block))) {
                return true;
            }

            if (equals(from, flipHoriz(block))) {
                return true;
            }

            if (equals(from, flipVert(flipHoriz(block)))) {
                return true;
            }

            if (equals(from, rotateLeft(block))) {
                return true;
            }

            if (equals(from, flipVert(rotateLeft(block)))) {
                return true;
            }

            if (equals(from, rotateRight(block))) {
                return true;
            }

            if (equals(from, flipVert(rotateRight(block)))) {
                return true;
            }

            return false;
        }

        private int[][] flipVert(int[][] block) {
            int size = block.length;
            int[][] flipped = new int[size][size];

            for (int col = 0; col < size; col++) {
                for (int row = 0; row < size / 2; row++) {
                    flipped[row][col] = block[size - row - 1][col];
                    flipped[size - row - 1][col] = block[row][col];
                }

                if (size % 2 != 0) {
                    flipped[size / 2][col] = block[size / 2][col];
                }
            }

            return flipped;
        }

        private int[][] flipHoriz(int[][] block) {
            int size = block.length;
            int[][] flipped = new int[size][size];

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size / 2; col++) {
                    flipped[row][size - col - 1] = block[row][col];
                    flipped[row][col] = block[row][size - col - 1];

                    if (size % 2 != 0) {
                        flipped[row][size / 2] = block[row][size / 2];
                    }
                }
            }

            return flipped;
        }

        private int[][] rotateLeft(int[][] block) {
            int size = block.length;
            int[][] flipped = new int[size][size];

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    flipped[col][row] = block[row][col];
                }
            }
        
            return flipVert(flipped);
        }

        private int[][] rotateRight(int[][] block) {
            int size = block.length;
            int[][] flipped = new int[size][size];

            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    flipped[col][row] = block[row][col];
                }
            }

            return flipHoriz(flipped);
        }

        private boolean equals(int[][] a, int[][] b) {
            if (a.length != b.length) {
                return false;
            }

            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a.length; j++) {
                    if (a[i][j] != b[i][j]) {
                        return false;
                    }
                }
            }

            return true;
        }

        void apply(int[][] pixels, int topLeftRow, int topLeftCol) {
            for (int r = topLeftRow, i = 0; i < to.length; r++, i++) {
                for (int c = topLeftCol, j = 0; j < to.length; c++, j++) {
                    pixels[r][c] = to[i][j];
                }
            }
        }
    }
}

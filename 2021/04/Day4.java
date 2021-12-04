import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2021/day/4
 */
public class Day4 {

    public static void main(String[] args) throws Exception {
        part1(getInput());
        part2(getInput());
    }

    private static Input getInput() throws Exception {
        Input input = new Input();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line = reader.readLine();
            for (String str : line.split(",")) {
                input.numbers.add(Integer.parseInt(str.trim()));
            }

            while ((line = reader.readLine()) != null) {
                Board board = new Board();
                for (int row = 0; row < Board.SIZE; row++) {
                    String[] parts = reader.readLine().trim().split("\\s+");
                    for (int col = 0; col < Board.SIZE; col++) {
                        board.numbers[row][col] = Integer.parseInt(parts[col]);
                    }
                }

                input.boards.add(board);
            }
        }

        return input;
    }

    private static void part1(Input input) {
        for (int n : input.numbers) {
            for (Board b : input.boards) {
                b.mark(n);
                if (b.isWinner()) {
                    System.out.println("Part 1: " + b.score());
                    return;
                }
            }
        }
    }

    private static void part2(Input input) {
        Board lastWinner = null;
        for (int n : input.numbers) {
            for (int i = input.boards.size() - 1; i >= 0; i--) {
                Board b = input.boards.get(i);
                b.mark(n);
                if (b.isWinner()) {
                    lastWinner = b;
                    input.boards.remove(i);
                }
            }
        }

        System.out.println("Part 2: " + lastWinner.score());
    }

    private static class Input {

        final List<Integer> numbers = new ArrayList<>();
        final List<Board> boards = new ArrayList<>();
    }

    private static class Board {

        static final int SIZE = 5;

        private final int[][] numbers = new int[SIZE][SIZE];
        private final boolean[][] marked = new boolean[SIZE][SIZE];
        private int lastNumber;

        void mark(int n) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (numbers[row][col] == n) {
                        marked[row][col] = true;
                    }
                }
            }

            lastNumber = n;
        }

        boolean isWinner() {
            // check rows
            for (int row = 0; row < SIZE; row++) {
                int markedCount = 0;
                for (int col = 0; col < SIZE; col++) {
                    if (marked[row][col]) {
                        markedCount++;
                    }
                }

                if (markedCount == SIZE) {
                    return true;
                }
            }

            // check columns
            for (int col = 0; col < SIZE; col++) {
                int markedCount = 0;
                for (int row = 0; row < SIZE; row++) {
                    if (marked[row][col]) {
                        markedCount++;
                    }
                }

                if (markedCount == SIZE) {
                    return true;
                }
            }

            return false;
        }

        int score() {
            int sum = 0;
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (!marked[row][col]) {
                        sum += numbers[row][col];
                    }
                }
            }

            return sum * lastNumber;
        }
    }
}

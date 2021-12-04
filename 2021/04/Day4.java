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
        List<Board> boards = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line = reader.readLine();
            for (String str : line.split(",")) {
                numbers.add(Integer.parseInt(str.trim()));
            }

            while ((line = reader.readLine()) != null) {
                Board board = new Board();
                for (int row = 0; row < 5; row++) {
                    String[] parts = reader.readLine().trim().split("\\s+");
                    for (int col = 0; col < 5; col++) {
                        board.numbers[row][col] = Integer.parseInt(parts[col]);
                    }
                }

                boards.add(board);
            }
        }

        part1(boards, numbers);
    }

    private static void part1(List<Board> boards, List<Integer> numbers) {
        for (int n : numbers) {
            for (Board b : boards) {
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < 5; col++) {
                        if (b.numbers[row][col] == n) {
                            b.marked[row][col] = true;
                        }
                    }
                }

                boolean bingo = false;

                // check rows
                for (int row = 0; row < 5; row++) {
                    int markedCount = 0;
                    for (int col = 0; col < 5; col++) {
                        if (b.marked[row][col]) {
                            markedCount++;
                        }
                    }

                    if (markedCount == 5) {
                        bingo = true;
                    }
                }

                // check columns
                for (int col = 0; col < 5; col++) {
                    int markedCount = 0;
                    for (int row = 0; row < 5; row++) {
                        if (b.marked[row][col]) {
                            markedCount++;
                        }
                    }

                    if (markedCount == 5) {
                        bingo = true;
                    }
                }

                if (bingo) {
                    int sum = 0;
                    for (int row = 0; row < 5; row++) {
                        for (int col = 0; col < 5; col++) {
                            if (!b.marked[row][col]) {
                                sum += b.numbers[row][col];
                            }
                        }
                    }

                    System.out.println("Part 1: " + (sum * n));
                    return;
                }
            }
        }
    }

    private static class Board {

        final int[][] numbers = new int[5][5];
        final boolean[][] marked = new boolean[5][5];
    }
}

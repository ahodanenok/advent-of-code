/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2015/day/25
 */
public class Day25 {

    private static final int INITIAL_CODE = 20151125;
    private static final int MULTIPLIER = 252533;
    private static final int DIVISOR = 33554393;

    public static void main(String[] args) {
        System.out.println(generate(3010, 3019));
    }

    private static long generate(int row, int col) {
        int currentRow = 1;
        int currentCol = 1;
        int diagonalSize = 1;
        int currentDiagonalSize = 1;
        long code = INITIAL_CODE;

        while (currentRow != row || currentCol != col) {
            if (currentDiagonalSize == diagonalSize) {
                currentRow = diagonalSize + 1;
                currentCol = 1;
                diagonalSize = currentRow;
                currentDiagonalSize = 1;
            } else {
                currentRow--;
                currentCol++;
                currentDiagonalSize++;
            }

            code = (code * MULTIPLIER) % DIVISOR;
        }

        return code;
    }
}

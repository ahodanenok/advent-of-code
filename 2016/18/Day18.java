/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2016/day/18
 */
public class Day18 {

    private static final char SAFE_CELL = '.';
    private static final char TRAP_CELL = '^';

    public static void main(String[] args) {
        part1(".^^..^...^..^^.^^^.^^^.^^^^^^.^.^^^^.^^.^^^^^^.^...^......^...^^^..^^^.....^^^^^^^^^....^^...^^^^..^", 40);
    }

    private static void part1(String initialRow, int rows) {    
        int safeCount = countSafe(initialRow);
        String row = initialRow;
        for (int i = 1; i < rows; i++) {
            row = nextRow(row);
            safeCount += countSafe(row);
        }

        System.out.println(safeCount);
    }

    private static int countSafe(String row) {
        int safeCount = 0;
        for (int i = 0; i < row.length(); i++) {
            if (row.charAt(i) == SAFE_CELL) {
                safeCount++;
            }
        }

        return safeCount;
    }

    private static String nextRow(String row) {
        StringBuilder sb = new StringBuilder();

        sb.append(nextCell(SAFE_CELL, row.charAt(0), row.charAt(1)));
        for (int i = 1; i < row.length() - 1; i++) {
            sb.append(nextCell(row.charAt(i - 1), row.charAt(i), row.charAt(i + 1)));
        }
        sb.append(nextCell(row.charAt(row.length() - 2), row.charAt(row.length() - 1), SAFE_CELL));

        return sb.toString();
    }

    private static char nextCell(char a, char b, char c) {
        if (a == TRAP_CELL && b == TRAP_CELL && c != TRAP_CELL) {
            return TRAP_CELL;
        }

        if (a != TRAP_CELL && b == TRAP_CELL && c == TRAP_CELL) {
            return TRAP_CELL;
        }

        if (a == TRAP_CELL && b != TRAP_CELL && c != TRAP_CELL) {
            return TRAP_CELL;
        }

        if (a != TRAP_CELL && b != TRAP_CELL && c == TRAP_CELL) {
            return TRAP_CELL;
        }

        return SAFE_CELL;
    }
}

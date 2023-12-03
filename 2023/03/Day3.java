import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2023/day/3
 */
public class Day3 {

    public static void main(String[] args) throws Exception {
        List<String> schema = getInput();
        part1(schema);
        part2(schema);
    }

    private static void part1(List<String> schema) {
        int sum = 0;
        List<Num> numbers = getNumbers(schema);
        for (Num n : numbers) {
            if (isPartNumber(schema, n)) {
                sum += n.value;
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<String> schema) {
        int sum = 0;
        List<Num> numbers = getNumbers(schema);       
        for (int rowIdx = 0; rowIdx < schema.size(); rowIdx++) {
            String row = schema.get(rowIdx);
            for (int colIdx = 0; colIdx < row.length(); colIdx++) {
                if (row.charAt(colIdx) != '*') {
                    continue;
                }

                List<Num> adjacent = new ArrayList<>();
                for (Num n : numbers) {
                    if ((n.fromIdx <= colIdx + 1 && n.toIdx >= colIdx)
                            && (n.rowIdx >= rowIdx - 1 && n.rowIdx <= rowIdx + 1)) {
                        adjacent.add(n);
                    }
                }

                if (adjacent.size() == 2) {
                    sum += adjacent.get(0).value * adjacent.get(1).value;
                }
            }
        }

        System.out.println("Part 2: " + sum);
    }

    private static List<Num> getNumbers(List<String> schema) {
        List<Num> numbers = new ArrayList<>();
        for (int rowIdx = 0; rowIdx < schema.size(); rowIdx++) {
            String row = schema.get(rowIdx);
            int numFromIdx = -1;
            for (int colIdx = 0; colIdx < row.length(); colIdx++) {
                char ch = row.charAt(colIdx);
                if (Character.isDigit(ch) && numFromIdx == -1) {
                    numFromIdx = colIdx;
                } else if (!Character.isDigit(ch) && numFromIdx != -1) {
                    numbers.add(new Num(
                        Integer.parseInt(row.substring(numFromIdx, colIdx)),
                        rowIdx,
                        numFromIdx,
                        colIdx));
                    numFromIdx = -1;
                }
            }

            if (numFromIdx != -1) {
                numbers.add(new Num(
                    Integer.parseInt(row.substring(numFromIdx, row.length())),
                    rowIdx,
                    numFromIdx,
                    row.length()));
                numFromIdx = -1;
            }
        }

        return numbers;
    }

    private static boolean isPartNumber(List<String> schema, Num n) {
        String row;

        row = schema.get(n.rowIdx);
        if (n.fromIdx > 0 && isSymbol(row.charAt(n.fromIdx - 1))) {
            return true;
        }
        if (n.toIdx < row.length() && isSymbol(row.charAt(n.toIdx))) {
            return true;
        }

        if (n.rowIdx > 0) {
            row = schema.get(n.rowIdx - 1);
            for (int i = Math.max(n.fromIdx - 1, 0); i <= Math.min(n.toIdx, row.length() - 1); i++) {
                if (isSymbol(row.charAt(i))) {
                    return true;
                }
            }
        }

        if (n.rowIdx < schema.size() - 1) {
            row = schema.get(n.rowIdx + 1);
            for (int i = Math.max(n.fromIdx - 1, 0); i <= Math.min(n.toIdx, row.length() - 1); i++) {
                if (isSymbol(row.charAt(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isSymbol(char ch) {
        return ch != '.' && !Character.isDigit(ch);
    }

    private static List<String> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<String> lines = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }

            return lines;
        }
    }

    private static class Num {

        final int value;
        final int rowIdx;
        final int fromIdx;
        final int toIdx;

        Num(int value, int rowIdx, int fromIdx, int toIdx) {
            this.value = value;
            this.rowIdx = rowIdx;
            this.fromIdx = fromIdx;
            this.toIdx = toIdx;
        }
    }
}

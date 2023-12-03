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
    }

    private static void part1(List<String> schema) {
        int sum = 0;
        for (int rowIdx = 0; rowIdx < schema.size(); rowIdx++) {
            String row = schema.get(rowIdx);
            int digitStartIdx = -1;
            for (int colIdx = 0; colIdx < row.length(); colIdx++) {
                char ch = row.charAt(colIdx);
                if (Character.isDigit(ch) && digitStartIdx == -1) {
                    digitStartIdx = colIdx;
                } else if (!Character.isDigit(ch) && digitStartIdx != -1) {
                    if (isPartNumber(schema, rowIdx, digitStartIdx, colIdx)) {
                        sum += Integer.parseInt(row.substring(digitStartIdx, colIdx));
                    }

                    digitStartIdx = -1;
                }
            }

            if (digitStartIdx != -1) {
                if (isPartNumber(schema, rowIdx, digitStartIdx, row.length())) {
                    sum += Integer.parseInt(row.substring(digitStartIdx, row.length()));
                }

                digitStartIdx = -1;
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static boolean isPartNumber(List<String> schema, int rowIdx, int digitStartIdx, int digitEndIdx) {
        String row;

        row = schema.get(rowIdx);
        if (digitStartIdx > 0 && isSymbol(row.charAt(digitStartIdx - 1))) {
            return true;
        }
        if (digitEndIdx < row.length() && isSymbol(row.charAt(digitEndIdx))) {
            return true;
        }

        if (rowIdx > 0) {
            row = schema.get(rowIdx - 1);
            for (int i = Math.max(digitStartIdx - 1, 0); i <= Math.min(digitEndIdx, row.length() - 1); i++) {
                if (isSymbol(row.charAt(i))) {
                    return true;
                }
            }
        }

        if (rowIdx < schema.size() - 1) {
            row = schema.get(rowIdx + 1);
            for (int i = Math.max(digitStartIdx - 1, 0); i <= Math.min(digitEndIdx, row.length() - 1); i++) {
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
}

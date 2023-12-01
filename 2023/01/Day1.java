import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2023/day/1
 */
public class Day1 {

    public static void main(String[] args) throws Exception {
        List<String> lines = getInput();
        part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        System.out.println("Part 1: " + extractSum(lines, Map.of()));
    }

    private static void part2(List<String> lines) {
        Map<String, Integer> replacements = Map.of(
            "one",   1,
            "two",   2,
            "three", 3,
            "four",  4,
            "five",  5,
            "six",   6,
            "seven", 7,
            "eight", 8,
            "nine",  9
        );        

        System.out.println("Part 2: " + extractSum(lines, replacements));
    }

    private static int extractSum(List<String> lines, Map<String, Integer> replacements) {
        int sum = 0;
        for (String line : lines) {
            int num = 0;
            for (int i = 0; i < line.length(); i++) {
                int n = extractDigit(line, i, replacements);
                if (n >= 0) {
                    num += n * 10;
                    break;
                }
            }
            for (int i = line.length() - 1; i >= 0; i--) {
                int n = extractDigit(line, i, replacements);
                if (n >= 0) {
                    num += n;
                    break;
                }
            }

            sum += num;
        }

        return sum;
    }

    private static int extractDigit(String line, int pos, Map<String, Integer> replacements) {
        if (Character.isDigit(line.charAt(pos))) {
            return Integer.parseInt(line.charAt(pos) + "");
        }

        for (Map.Entry<String, Integer> entry : replacements.entrySet()) {
            if (line.startsWith(entry.getKey(), pos)) {
                return entry.getValue();
            }            
        }

        return -1;
    }

    private static List<String> getInput() throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        return lines;
    }
}

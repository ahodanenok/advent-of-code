import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2023/day/1
 */
public class Day1 {

    public static void main(String[] args) throws Exception {
        part1(getInput());
    }
    
    private static void part1(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            String num = "";
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    num += line.charAt(i);
                    break;
                }
            }
            for (int i = line.length() - 1; i >= 0; i--) {
                if (Character.isDigit(line.charAt(i))) {
                    num += line.charAt(i);
                    break;
                }
            }

            sum += Integer.parseInt(num);
        }

        System.out.println("Part 1: " + sum);
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

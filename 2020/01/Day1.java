import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2020/day/1#
 */
public class Day1 {

    public static void main(String[] args) throws Exception {
        List<Integer> numbers = getNumbers();
        for (int i = 0; i < numbers.size(); i++) {
            int n = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                if (n + numbers.get(j) == 2020) {
                    System.out.println("Part 1: " + (n * numbers.get(j)));
                    break;
                }
            }
        }
    }

    private static List<Integer> getNumbers() throws Exception {
        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Integer.parseInt(line));
            }
        }

        return numbers;
    }
}

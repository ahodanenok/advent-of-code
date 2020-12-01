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
        part1(numbers);
        part2(numbers);
    }

    private static void part1(List<Integer> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            int a = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                int b = numbers.get(j);
                if (a + b == 2020) {
                    System.out.println("Part 1: " + a * b);
                }
            }
        }
    }

    private static void part2(List<Integer> numbers) {
        for (int i = 0; i < numbers.size(); i++) {
            int a = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                int b = numbers.get(j);
                for (int k = j + 1; k < numbers.size(); k++) {
                    int c = numbers.get(k);
                    if (a + b + c == 2020) {
                        System.out.println("Part 2: " + a * b * c);
                    }
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2020/day/9
 */
public class Day9 {

    private static final int PREAMBLE_SIZE = 25;

    public static void main(String[] args) throws Exception {
        List<Long> numbers = getNumbers();
        part1(numbers);
        part2(numbers);
    }

    private static void part1(List<Long> numbers) {
        System.out.println("Part 1: " + getFirstInvalid(numbers));
    }

    private static void part2(List<Long> numbers) {
        Long n = getFirstInvalid(numbers);

        long sum = 0;
        int fromIdx = 0;
        int toIdx = 0;
        while (true) {
            sum += numbers.get(toIdx);
            if (sum == n && toIdx - fromIdx > 0) {
                break;
            }

            if (sum > n) {
                sum = 0;
                fromIdx++;
                toIdx = fromIdx; 
            } else {
                toIdx++;
            }
        }

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int i = fromIdx; i <= toIdx; i++) {
            min = Math.min(numbers.get(i), min);
            max = Math.max(numbers.get(i), max);
        }

        System.out.println("Part 2: " + (min + max));
    }

    private static long getFirstInvalid(List<Long> numbers) {
        for (int i = PREAMBLE_SIZE; i < numbers.size(); i++) {
            long n = numbers.get(i);

            boolean valid = false;

            check:
            for (int j = i - PREAMBLE_SIZE; j < i; j++) {
                for (int k = j + 1; k < i; k++) {
                    if (numbers.get(j) + numbers.get(k) == n) {
                        valid = true;
                        break check;
                    }
                }
            }

            if (!valid) {
                return n;
            }
        }

        throw new IllegalStateException("not found");
    }

    private static List<Long> getNumbers() throws Exception {
        List<Long> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Long.parseLong(line));
            }
        }

        return numbers;
    }
}

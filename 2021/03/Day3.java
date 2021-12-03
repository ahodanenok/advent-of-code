import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiPredicate;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2021/day/3
 */
public class Day3 {

    public static void main(String[] args) throws Exception {
        List<String> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(line.trim());
            }
        }

        part1(numbers);
        part2(numbers);
    }

    private static void part1(List<String> numbers) {
        int[] signals = new int[numbers.get(0).length()];
        for (String num : numbers) {
            for (int i = 0; i < signals.length; i++) {
                char digit = num.charAt(i);
                if (digit == '0') {
                    signals[i]--;
                } else if (digit == '1') {
                    signals[i]++;
                } else {
                    throw new IllegalStateException("what is this digit?? -> '" + digit + "'");
                }
            }
        }

        int gamma = 0;
        int epsilon = 0;
        for (int s : signals) {
            if (s == 0) {
                throw new IllegalStateException("Zero! - no majority");
            }

            gamma <<= 1;
            if (s > 0) {
                gamma |= 1;
            }

            epsilon <<= 1;
            if (s < 0) {
                epsilon |= 1;
            }
        }

        System.out.println("Part 1: " + (gamma * epsilon));
    }

    private static void part2(List<String> numbers) {
        int supportRating = findRating(new ArrayList<>(numbers),
            (s, digit) -> s > 0 && digit == '1' || s < 0 && digit == '0' || s == 0 && digit == '1');
        int scrubberRating = findRating(new ArrayList<>(numbers),
            (s, digit) -> s > 0 && digit == '0' || s < 0 && digit == '1' || s == 0 && digit == '0');

        System.out.println("Part 2: " + (supportRating * scrubberRating));
    }

    private static int findRating(List<String> numbers, BiPredicate<Integer, Character> criteria) {
        int round = 0;
        while (numbers.size() > 1) {
            int s = 0;
            for (String num : numbers) {
                char digit = num.charAt(round);
                if (digit == '0') {
                    s--;
                } else if (digit == '1') {
                    s++;
                } else {
                    throw new IllegalStateException("what is this digit?? -> '" + digit + "'");
                }
            }

            for (int i = numbers.size() - 1; i >= 0; i--) {
                char digit = numbers.get(i).charAt(round);
                if (!criteria.test(s, digit)) {
                    numbers.remove(i);
                }
            }

            round++;
        }

        return Integer.parseInt(numbers.get(0), 2);
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2022/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        String signal = getSignal();
        part1(signal);
    }

    private static String getSignal() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            return reader.readLine();
        }
    }

    private static void part1(String signal) {
        for (int i = 3; i < signal.length(); i++) {
            HashSet<Character> buffer =  new HashSet<>();
            buffer.add(signal.charAt(i - 3));
            buffer.add(signal.charAt(i - 2));
            buffer.add(signal.charAt(i - 1));
            buffer.add(signal.charAt(i));

            if (buffer.size() == 4) {
                System.out.println("Part 1: " + (i + 1));
                break;
            }
        }
    }
}

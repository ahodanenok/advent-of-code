import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2021/day/1
 */
public class Day1 {

    public static void main(String[] args) throws Exception {
        int increasedCount = 0;
        int previousDepth = Integer.MAX_VALUE;
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int depth = Integer.parseInt(line);
                if (depth > previousDepth) {
                    increasedCount++;
                }

                previousDepth = depth;
            }
        }

        System.out.println("Part 1: " + increasedCount);
    }
}

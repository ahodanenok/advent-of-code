import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2021/day/1
 */
public class Day1 {

    public static void main(String[] args) throws Exception {
        List<Integer> depths = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                depths.add(Integer.parseInt(line));
            }
        }

        part1(depths);
        part2(depths);
    }

    private static void part1(List<Integer> depths) {
        int increasedCount = 0;
        int previousDepth = Integer.MAX_VALUE;
        for (int depth : depths) {
            if (depth > previousDepth) {
                increasedCount++;
            }

            previousDepth = depth;
        }

        System.out.println("Part 1: " + increasedCount);
    }

    private static void part2(List<Integer> depths) {
        int increasedCount = 0;
        int previousSum = Integer.MAX_VALUE;
        for (int i = 2; i < depths.size(); i++) {
            int sum = depths.get(i - 2) + depths.get(i - 1) + depths.get(i);
            if (sum > previousSum) {
                increasedCount++;
            }

            previousSum = sum;
        }

        System.out.println("Part 2: " + increasedCount);
    }
}

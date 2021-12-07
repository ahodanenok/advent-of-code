import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2021/day/7
 */
public class Day7 {

    public static void main(String[] args) throws Exception {
        List<Integer> crabs = getInput();
        System.out.println("Part 1: " + findLeastFuel(crabs, n -> n));
        System.out.println("Part 2: " + findLeastFuel(crabs, n -> n * (n + 1) / 2));
    }

    private static List<Integer> getInput() throws Exception {
        List<Integer> crabs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            for (String part : reader.readLine().trim().split(",")) {
                crabs.add(Integer.parseInt(part.trim()));
            }
        }

        return crabs;
    }

    private static int findLeastFuel(List<Integer> crabs, Function<Integer, Integer> costFunction) {
        int fuelMin = Integer.MAX_VALUE;
        int posStart = crabs.stream().mapToInt(Integer::intValue).min().orElse(Integer.MAX_VALUE);
        int posEnd = crabs.stream().mapToInt(Integer::intValue).max().orElse(Integer.MIN_VALUE);
        for (int p = posStart; p <= posEnd; p++) {
            int fuel = 0;
            for (int c : crabs) {
                int n = Math.abs(p - c);
                fuel += costFunction.apply(n);
            }

            fuelMin = Math.min(fuel, fuelMin);
        }

        return fuelMin;
    }
}

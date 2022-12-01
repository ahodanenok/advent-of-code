import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2022/day/1
 */
public class Day1 {

    public static void main(String[] args) throws Exception {
        List<Integer> calories = getCalories();
        part1(calories);
        part2(calories);
    }

    private static List<Integer> getCalories() throws Exception {
        List<Integer> calories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int elfNum = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    elfNum++;
                } else if (calories.size() > elfNum) {
                    calories.set(elfNum, calories.get(elfNum) + Integer.parseInt(line));
                } else {
                    calories.add(Integer.parseInt(line));
                }
            }
        }

        return calories;
    }

    private static void part1(List<Integer> calories) {
        Integer maxCalories = calories.stream().sorted(Comparator.reverseOrder()).findFirst().get();
        System.out.println("Part 1: " + maxCalories);
    }

    private static void part2(List<Integer> calories) {
        Integer maxCalories = calories.stream().sorted(Comparator.reverseOrder()).limit(3).reduce(0, Integer::sum);
        System.out.println("Part 2: " + maxCalories);
    }
}

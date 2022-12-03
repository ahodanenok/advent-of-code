import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2022/day/3
 */
public class Day3 {

    public static void main(String[] args) throws Exception {
        List<String> items = getItems();
        part1(items);
    }

    private static List<String> getItems() throws Exception {
        List<String> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(line);
            }
        }

        return items;
    }

    private static void part1(List<String> items) {
        int prioritySum = 0;
        for (String rucksack : items) {
            int mid = rucksack.length() / 2;
            for (int i = 0; i < mid; i++) {
                char item = rucksack.charAt(i);
                if (rucksack.indexOf(item, mid) != -1) {
                    prioritySum += getPriority(item);
                    break; // one dublicate per rucksack
                }
            }
        }

        System.out.println("Part 1: " + prioritySum);
    }

    private static int getPriority(char item) {
        if (item >= 'a' && item <= 'z') {
            return 1 + (item - 'a');
        } else if (item >= 'A' && item <= 'Z') {
            return 27 + (item - 'A');
        } else {
            throw new IllegalArgumentException("Unknown item: " + item);
        }
    }
}

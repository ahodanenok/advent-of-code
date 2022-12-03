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
        part2(items);
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
                    break; // one common item between compartments
                }
            }
        }

        System.out.println("Part 1: " + prioritySum);
    }

    private static void part2(List<String> items) {
        int prioritySum = 0;

        nextGroup:
        for (int r = 0; r < items.size(); r += 3) {
            String first = items.get(r);
            for (int i = 0; i < first.length(); i++) {
                char item = first.charAt(i);

                String second = items.get(r + 1);
                if (second.indexOf(item) == -1) {
                    continue;
                }

                
                String third = items.get(r + 2);
                if (third.indexOf(item) == -1) {
                    continue;
                }

                prioritySum += getPriority(item);
                continue nextGroup; // one common item per group
            }
        }

        System.out.println("Part 2: " + prioritySum);
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

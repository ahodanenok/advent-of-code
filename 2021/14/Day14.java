import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2021/day/14
 */
public class Day14 {

    public static void main(String[] args) throws Exception {
        Manual manual = getInput();
        part1(manual);
    }

    private static Manual getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Manual manual = new Manual(reader.readLine().trim());

            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                manual.rules.put(parts[0].trim(), parts[1].trim());
            }

            return manual;
        }
    }

    private static void part1(Manual manual) {
        String polymer = manual.template;
        for (int i = 0; i < 10; i++) {
            polymer = polimerize(polymer, manual.rules);
        }

        Map<Character, Integer> occurrences = new HashMap<>();
        for (int i = 0; i < polymer.length(); i++) {
            occurrences.merge(polymer.charAt(i), 1, Integer::sum);
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int count : occurrences.values()) {
            min = Math.min(count, min);
            max = Math.max(count, max);
        }

        System.out.println("Part 1: " + (max - min));
    }

    private static String polimerize(String template, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < template.length(); i++) {
            String pair = template.substring(i - 1, i + 1);
            String insertion = rules.get(pair);
            if (insertion != null) {
                sb.append(pair.charAt(0));
                sb.append(insertion.charAt(0));
            } else {
                sb.append(pair.charAt(0));
            }
        }
        sb.append(template.charAt(template.length() - 1));

        return sb.toString();
    }

    private static class Manual {

        final String template;
        final Map<String, String> rules = new HashMap<>();

        Manual(String template) {
            this.template = template;
        }
    }
}

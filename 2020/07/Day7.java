import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2020/day/7
 */
public class Day7 {

    public static void main(String[] args) throws Exception {
        List<Bag> bags = getBags();

        Map<String, List<String>> index = new HashMap<>();
        for (Bag b : bags) {
            for (String h : b.holds.keySet()) {
                index.computeIfAbsent(h, __ -> new ArrayList<>()).add(b.name);
            }
        }

        Set<String> allContainers = new HashSet<>();

        LinkedList<String> queue = new LinkedList<>();
        queue.addLast("shiny gold");
        while (!queue.isEmpty()) {
            String bag = queue.removeFirst();
            List<String> containers = index.get(bag);
            if (containers != null) {
                allContainers.addAll(containers);
                queue.addAll(containers);
            }
        }

        System.out.println("Part 1: " + allContainers.size());
    }

    private static List<Bag> getBags() throws Exception {
        List<Bag> bags = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("contain");
                String namePart = parts[0].trim();
                Bag bag = new Bag(
                    namePart.substring(0, namePart.lastIndexOf(' ')),
                    parseHolds(parts[1]));

                bags.add(bag);
            }
        }

        return bags;
    }

    private static Map<String, Integer> parseHolds(String definition) {
        definition = definition.trim();
        if (definition.startsWith("no")) {
            return Collections.emptyMap();
        }

        Map<String, Integer> bags = new HashMap<>();

        String[] parts = definition.split(",");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            int firstSpaceIdx = part.indexOf(' ');
            int lastSpaceIdx = part.lastIndexOf(' ');
            bags.put(
                part.substring(firstSpaceIdx + 1, lastSpaceIdx),
                Integer.parseInt(part.substring(0, firstSpaceIdx)));
        }

        return bags;
    }

    private static class Bag {

        final String name;
        final Map<String, Integer> holds;

        Bag(String name, Map<String, Integer> holds) {
            this.name = name;
            this.holds = holds;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2019/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        Map<String, String> orbits = getOrbits();
        part1(orbits);
        part2(orbits);
    }

    private static void part1(Map<String, String> orbits) {
        int count = 0;
        for (String obj : orbits.keySet()) {
            String other = obj;
            while ((other = orbits.get(other)) != null) {
                count++;
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static void part2(Map<String, String> orbits) {
        Map<String, List<String>> orbiters = new HashMap<>();
        for (Map.Entry<String, String> obj : orbits.entrySet()) {
            if (orbiters.containsKey(obj.getValue())) {
                orbiters.get(obj.getValue()).add(obj.getKey());
            } else {
                List<String> orb = new ArrayList<>();
                orb.add(obj.getKey());
                orbiters.put(obj.getValue(), orb);
            }
        }

        String from = orbits.get("YOU");
        String to = orbits.get("SAN");

        int count = 0;

        Set<String> seen = new HashSet<>();
        seen.add(from);

        LinkedList<Pair> queue = new LinkedList<>();
        queue.add(new Pair(from, 0));
        while (!queue.isEmpty()) {
            Pair current = queue.removeFirst();
            if (current.obj.equals(to)) {
                count = current.steps;
                break;
            }

            if (orbits.containsKey(current.obj) && seen.contains(current.obj)) {
                String other = orbits.get(current.obj);
                if (!seen.contains(other)) {
                    queue.addLast(new Pair(other, current.steps + 1));
                    seen.add(other);
                }
            }

            if (orbiters.containsKey(current.obj)) {
                for (String orb : orbiters.get(current.obj)) {
                    if (!seen.contains(orb)) {
                        queue.addLast(new Pair(orb, current.steps + 1));
                        seen.add(orb);
                    }
                }
            }

            count++;
        }

        System.out.println("Part 2: " + count);
    }

    private static class Pair {

        final String obj;
        final int steps;

        Pair(String obj, int steps) {
            this.obj = obj;
            this.steps = steps;
        }
    }

    private static Map<String, String> getOrbits() throws Exception {
        Map<String, String> orbits = new HashMap<String, String>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\)");
                orbits.put(parts[1].trim(), parts[0].trim());
            }
        }

        return orbits;
    }
}


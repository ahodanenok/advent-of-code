import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2019/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        Map<String, String> orbits = getOrbits();
        part1(orbits);
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


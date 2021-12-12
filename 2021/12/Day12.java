import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2021/day/12
 */
public class Day12 {

    public static void main(String[] args) throws Exception {
        Map<String, List<String>> caves = getInput();
        part1(caves);
    }

    private static Map<String, List<String>> getInput() throws Exception {
        Map<String, List<String>> caves = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                String from = parts[0].trim();
                String to = parts[1].trim();

                if (!caves.containsKey(from)) {
                    caves.put(from, new ArrayList<>());
                }
                caves.get(from).add(to);

                if (!caves.containsKey(to)) {
                    caves.put(to, new ArrayList<>());
                }
                caves.get(to).add(from);
            }
        }

        return caves;
    }

    private static void part1(Map<String, List<String>> caves) {
        Set<String> visited = new HashSet<>();
        visited.add("start");
        int count = countPaths(caves, "start", visited);
        System.out.println("Part 1: " + count);
    }

    private static int countPaths(Map<String, List<String>> caves, String currentCave, Set<String> visited) {
        if (currentCave.equals("end")) {
            return 1;
        }

        int count = 0;
        for (String cave : caves.get(currentCave)) {
            if (cave.toUpperCase().equals(cave) || visited.add(cave)) {
                count += countPaths(caves, cave, visited);
                visited.remove(cave);
            }
        }

        return count;
    }
}

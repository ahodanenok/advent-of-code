import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2024/day/1
 */
public class Day1 {

    public static void main(String... args) throws Exception {
        List<Pair> pairs = getInput();
        part1(pairs);
        part2(pairs);
    }

    private static void part1(List<Pair> pairs) {
        List<Integer> leftLocations = new ArrayList<>(pairs.size());
        pairs.forEach(p -> leftLocations.add(p.left));
        leftLocations.sort(Integer::compare);

        List<Integer> rightLocations = new ArrayList<>(pairs.size());
        pairs.forEach(p -> rightLocations.add(p.right));
        rightLocations.sort(Integer::compare);

        int totalDistance = 0;
        for (int i = 0; i < pairs.size(); i++) {
            totalDistance += Math.abs(leftLocations.get(i) - rightLocations.get(i));
        }

        System.out.println("Part 1: " + totalDistance);
    }

    private static void part2(List<Pair> pairs) {
        Map<Integer, Integer> rightLocationCounts = new HashMap<>();
        pairs.forEach(p -> rightLocationCounts.merge(p.right, 1, Integer::sum));

        long similarityScore = 0;
        for (Pair pair : pairs) {
            similarityScore += pair.left * rightLocationCounts.getOrDefault(pair.left, 0);
        }

        System.out.println("Part 2: " + similarityScore);
    }

    private static List<Pair> getInput() throws Exception {
        List<Pair> pairs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                pairs.add(new Pair(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])));
            }
        }

        return pairs;
    }

    private static class Pair {

        final int left;
        final int right;

        Pair(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }
}

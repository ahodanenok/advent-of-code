import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2024/day/1
 */
public class Day1 {

    public static void main(String... args) throws Exception {
        List<Pair> pairs = getInput();
        part1(pairs);
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

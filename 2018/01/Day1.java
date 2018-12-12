import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2018/day/1
 */
public class Day1 {

    public static void main(String[] args) {
        List<Integer> changes = getChanges();
        part1(changes);
        part2(changes);
    }

    private static void part1(List<Integer> changes) {
        System.out.println(changes.stream().mapToInt(n -> n).sum());
    }

    private static void part2(List<Integer> changes) {
        int frequency = 0;
        Set<Integer> seenFrequencies = new HashSet<Integer>();

        int i = 0;
        while (seenFrequencies.add(frequency)) {
            frequency += changes.get(i);
            i = (i + 1) % changes.size();
        }

        System.out.println(frequency);
    }

    private static List<Integer> getChanges() {
        Scanner scanner = new Scanner(System.in);
        List<Integer> changes = new ArrayList<Integer>();
        while (scanner.hasNextLine()) {
            int change = Integer.parseInt(scanner.nextLine().trim());
            changes.add(change);
        }

        return changes;
    }
}
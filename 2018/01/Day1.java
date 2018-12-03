import java.util.Scanner;
import java.util.LinkedList;
import java.util.HashSet;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2018/day/1
 */
public class Day1 {

    public static void main(String[] args) {
        LinkedList<Integer> changes = getChanges();
        part1(changes);
        part2(changes);
    }

    private static void part1(LinkedList<Integer> changes) {
        int frequency = 0;
        for (int change : changes) {
            frequency += change;
        }

        System.out.println(frequency);
    }

    private static void part2(LinkedList<Integer> changes) {
        int frequency = 0;
        HashSet<Integer> seenFrequencies = new HashSet<Integer>();
        seenFrequencies.add(frequency);

        seachingFrequency:
        while (true) {
            for (int change : changes) {
                frequency += change;
                if (seenFrequencies.contains(frequency)) {
                    break seachingFrequency;
                }

                seenFrequencies.add(frequency);
            }
        }

        System.out.println(frequency);
    }

    private static LinkedList<Integer> getChanges() {
        Scanner scanner = new Scanner(System.in);
        LinkedList<Integer> changes = new LinkedList<Integer>();
        while (scanner.hasNextLine()) {
            int change = Integer.parseInt(scanner.nextLine().trim());
            changes.add(change);
        }

        return changes;
    }
}
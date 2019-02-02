import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2015/day/17
 */
public class Day17 {

    public static void main(String[] args) {
        List<Integer> containers = getContainers();
        System.out.println(part1(150, containers, 0));
        System.out.println(part2(150, containers, 0, 0));
    }

    private static List<Integer> getContainers() {
        List<Integer> containers = new ArrayList<Integer>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            containers.add(Integer.parseInt(scanner.nextLine().trim()));
        }

        return containers;
    }

    private static int part1(int eggnog, List<Integer> containers, int fromIdx) {
        if (eggnog < 0) {
            return 0;
        } else if (eggnog == 0) {
            return 1;
        } else {
            int count = 0;
            for (int i = fromIdx; i < containers.size(); i++) {
                count += part1(eggnog - containers.get(i), containers, i + 1);
            }

            return count;
        }
    }

    private static int part2(int eggnog, List<Integer> containers, int fromIdx, int usedContainers) {
        if (eggnog < 0) {
            return Integer.MAX_VALUE;
        } else if (eggnog == 0) {
            return usedContainers;
        } else {
            int minUsedContainers = Integer.MAX_VALUE;
            for (int i = fromIdx; i < containers.size(); i++) {
                minUsedContainers = Math.min(part2(eggnog - containers.get(i), containers, i + 1, usedContainers + 1), minUsedContainers);
            }

            return minUsedContainers;
        }
    }
}

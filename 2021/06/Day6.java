import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2021/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        List<Integer> fish = getInput();
        part1(fish);
        part2(fish);
    }

    private static List<Integer> getInput() throws Exception {
        List<Integer> fish = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            for (String part : reader.readLine().trim().split(",")) {
                fish.add(Integer.parseInt(part.trim()));
            }
        }

        return fish;
    }

    private static void part1(List<Integer> input) {
        List<Integer> fish = getFishAfter(input, 80);
        System.out.println("Part 1: " + fish.size());
    }

    private static void part2(List<Integer> initialFish) {
        int daysLeft = 256;
        Map<Integer, Long> totalGroups = groupFishByDays(initialFish);
        // Doing christmas tree!
        while (daysLeft > 0) {
            // 32 is a random step, just not to get a lot of fish after it
            // and too long garland can tangle...
            int step = Math.min(daysLeft, 32);

            Map<Integer, Long> stepGroups = new HashMap<>();
            for (int f : totalGroups.keySet()) {
                List<Integer> fish = getFishAfter(Collections.singletonList(f), step);
                for (Map.Entry<Integer, Long> entry : groupFishByDays(fish).entrySet()) {
                    stepGroups.merge(entry.getKey(), entry.getValue() * totalGroups.getOrDefault(f, 1L), Long::sum);
                }
            }

            totalGroups = stepGroups;
            daysLeft -= step;
        }

        long totalCount = 0;
        for (long count : totalGroups.values()) {
            totalCount += count;
        }

        System.out.println("Part 2: " + totalCount);
    }

    private static List<Integer> getFishAfter(List<Integer> initialFish, int afterDays) {
        LinkedList<Integer> fish = new LinkedList<>(initialFish);
        for (int d = 0; d < afterDays; d++) {
            int checkCount = fish.size();
            while (checkCount > 0) {
                int f = fish.poll();

                f--;
                if (f < 0) {
                    fish.offer(6);
                    fish.offer(8);
                } else {
                    fish.offer(f);
                }

                checkCount--;
            }
        }

        return fish;
    }

    private static Map<Integer, Long> groupFishByDays(List<Integer> fish) {
        Map<Integer, Long> groups = new HashMap<>();
        for (int f : fish) {
            groups.merge(f, 1L, Long::sum);
        }

        return groups;
    }
}

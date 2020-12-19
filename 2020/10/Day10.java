import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2020/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        List<Integer> adapters = getAdapters();
        adapters.sort(Comparator.naturalOrder());
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        part1(adapters);
        part2(adapters);
    }

    private static void part1(List<Integer> adapters) {
        int oneCount = 0;
        int threeCount = 0;
        int prevJolt = 0;
        for (int n : adapters) {
            if (n - prevJolt == 1) {
                oneCount++;
            } else if (n - prevJolt == 3) {
                threeCount++;
            }

            prevJolt = n;
        }

        System.out.println("Part 1: " + oneCount * threeCount);
    }

    private static void part2(List<Integer> adapters) {
        List<Integer> copy = new ArrayList<>();
        copy.add(0);
        copy.addAll(adapters);
        System.out.println("Part 2: " + countCombinations(copy, 0, new HashMap<>()));
    }

    private static long countCombinations(List<Integer> adapters, int currentIdx, Map<Integer, Long> memo) {
        int adapter = adapters.get(currentIdx);

        if (memo.containsKey(adapter)) {
            return memo.get(adapter);
        }

        if (currentIdx == adapters.size() - 1) {
            return 1;
        }

        long count = 0;
        int i = currentIdx + 1;
        while (i < adapters.size() && adapters.get(i) - adapter < 4) {
            count += countCombinations(adapters, i++, memo);
        }

        memo.put(adapter, count);
        return count;
    }

    private static List<Integer> getAdapters() throws Exception {
        List<Integer> adapters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                adapters.add(Integer.parseInt(line));
            }
        }

        return adapters;
    }
}

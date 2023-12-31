import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2023/day/12
 */
public class Day12 {

    public static void main(String[] args) throws Exception {
        System.out.println("Part 1: " + countPossibleArrangements(getInput(false)));
        System.out.println("Part 2: " + countPossibleArrangements(getInput(true)));
    }

    private static long countPossibleArrangements(List<Record> records) {
        long count = 0;
        for (Record record : records) {
            count += countPossibleArrangements(record, 0, 0, new HashMap<>());
        }

        return count;
    }

    private static long countPossibleArrangements(Record record, int groupIdx, int stateIdx, Map<String, Long> cache) {
        if (cache.containsKey(groupIdx + "_" + stateIdx)) {
            return cache.get(groupIdx + "_" + stateIdx);
        }

        if (groupIdx == record.counts.length) {
            for (int i = stateIdx; i < record.state.length; i++) {
                if (record.state[i] == '#') {
                    return 0;
                }
            }

            return 1;
        }

        if (stateIdx >= record.state.length) {
            return 0;
        }

        int firstHashIdx = record.state.length;
        for (int i = stateIdx; i < record.state.length; i++) {
            if (record.state[i] == '#') {
                firstHashIdx = i;
                break;
            }
        }

        int nextStateIdx = stateIdx;
        long count = 0;
        while (nextStateIdx < record.state.length) {
            while (nextStateIdx < record.state.length && record.state[nextStateIdx] == '.') {
                nextStateIdx++;
            }

            if (nextStateIdx > firstHashIdx) {
                break;
            }

            int groupSize = record.counts[groupIdx];
            int i = nextStateIdx;
            while (groupSize > 0 && i < record.state.length && record.state[i] != '.') {
                groupSize--;
                i++;
            }

            if (groupSize < 0) {
                throw new IllegalStateException();
            }

            if (groupSize > 0) {
                nextStateIdx++;
                continue;
            }

            if (i < record.state.length && record.state[i] == '#') {
                nextStateIdx++;
                continue;
            }

            nextStateIdx++;
            count += countPossibleArrangements(record, groupIdx + 1, i + 1, cache);
        }
        cache.put(groupIdx + "_" + stateIdx, count);

        return count;
    }

    private static List<Record> getInput(boolean unfold) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Record> records = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (unfold) {
                    parts[0] = parts[0].trim();
                    parts[0] = parts[0] + "?" + parts[0] + "?" + parts[0] + "?" + parts[0] + "?" + parts[0];
                    parts[1] = parts[1].trim();
                    parts[1] = parts[1] + "," + parts[1] + "," + parts[1] + "," + parts[1] + "," + parts[1];
                }

                String[] countsPart = parts[1].split(",");
                int[] counts = new int[countsPart.length];
                for (int i = 0; i < countsPart.length; i++) {
                    counts[i] = Integer.parseInt(countsPart[i].trim());
                }

                records.add(new Record(parts[0].trim().toCharArray(), counts));
            }

            return records;
        }
    }

    private static class Record {

        final char[] state;
        final int[] counts;

        Record(char[] state, int[] counts) {
            this.state = state;
            this.counts = counts;
        }
    }
}

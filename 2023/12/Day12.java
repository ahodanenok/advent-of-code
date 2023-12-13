import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2023/day/12
 */
public class Day12 {

    public static void main(String[] args) throws Exception {
        List<Record> records = getInput();
        part1(records);
    }

    private static void part1(List<Record> records) {
        int sum = 0;
        for (Record record : records) {
            sum += countPossibleArrangements(record);
        }

        System.out.println("Part 1: " + sum);
    }

    private static int countPossibleArrangements(Record record) {
        if (!isValid(record)) {
            return 0;
        }

        if (isComplete(record)) {
            return 1;
        }

        int count = 0;
        for (int i = 0; i < record.state.length; i++) {
            if (record.state[i] == '?') {
                record.state[i] = '.';
                count += countPossibleArrangements(record);

                record.state[i] = '#';
                count += countPossibleArrangements(record);

                record.state[i] = '?';
                break;
            }
        }

        return count;
    }

    private static boolean isValid(Record record) {
        int pos = 0;
        for (int i = 0; i < record.counts.length; i++) {
            while (pos < record.state.length && record.state[pos] == '.') {
                pos++;
            }

            int count = record.counts[i];
            while (pos < record.state.length && count > 0 && record.state[pos] == '#') {
                pos++;
                count--;
            }

            if (pos < record.state.length && count > 0 && record.state[pos] == '?') {
                return true;
            }

            if (count > 0) {
                return false;
            }

            if (pos < record.state.length && record.state[pos] == '#') {
                return false;
            }
        }

        while (pos < record.state.length) {
            if (record.state[pos] == '#') {
                return false;
            }

            pos++;
        }

        return true;
    }

    private static boolean isComplete(Record record) {
        for (int i = 0; i < record.state.length; i++) {
            if (record.state[i] == '?') {
                return false;
            }
        }

        return true;
    }

    private static List<Record> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Record> records = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
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

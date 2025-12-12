import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2025/day/2
 */
public class Day2 {

    public static void main(String... args) throws Exception {
        List<Range> ranges = getInput();
        part1(ranges);
    }

    private static List<Range> getInput() throws Exception {
        List<Range> ranges = new ArrayList<>();
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            for (String rangeStr : reader.readLine().split(",")) {
                String[] parts = rangeStr.split("-");
                ranges.add(new Range(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1])));
            }
        }

        return ranges;
    }

    private static void part1(List<Range> ranges) {
        long sum = 0;
        for (Range range : ranges) {
            for (long n = range.start; n <= range.end; n++) {
                String s = Long.toString(n);
                int repeatLength = s.length() / 2;
                if (s.substring(0, repeatLength).equals(s.substring(repeatLength))) {
                    sum += n;
                }
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static class Range {

        final long start;
        final long end;

        Range(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }
}

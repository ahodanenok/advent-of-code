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
        part2(ranges);
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
                if (isRepeated(s, (s.length() + 1) / 2)) {
                    sum += n;
                }
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<Range> ranges) {
        long sum = 0;
        for (Range range : ranges) {
            for (long n = range.start; n <= range.end; n++) {
                String s = Long.toString(n);
                for (int repeatLength = 1, halfLength = (s.length() + 1) / 2;
                        repeatLength <= halfLength;
                        repeatLength++) {
                    if (isRepeated(s, repeatLength)) {
                        sum += n;
                        break;
                    }
                }
            }
        }

        System.out.println("Part 2: " + sum);
    }

    private static boolean isRepeated(String str, int repeatLength) {
        if (repeatLength == 0 || repeatLength == str.length()) {
            return false;
        }

        String pattern = str.substring(0, repeatLength);
        for (int i = repeatLength, len = str.length(); i < len; i+= repeatLength) {
            if (!str.substring(i, Math.min(i + repeatLength, len)).equals(pattern)) {
                return false;
            }
        }

        return true;
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

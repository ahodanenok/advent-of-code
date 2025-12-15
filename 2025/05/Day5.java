import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2025/day/5
 */
public class Day5 {

    public static void main(String... args) throws Exception {
        Database db = getInput();
        part1(db);
        part2(db);
    }

    private static Database getInput() throws Exception {
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            String line;

            List<FreshRange> ranges = new ArrayList<>();
            while (!(line = reader.readLine()).isEmpty()) {
                String[] parts = line.split("-");
                ranges.add(new FreshRange(
                    Long.parseLong(parts[0]),
                    Long.parseLong(parts[1])));
            }

            List<Long> ingridients = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                ingridients.add(Long.parseLong(line));
            }

            return new Database(ranges, ingridients);
        }
    }

    private static void part1(Database db) {
        int freshCount = 0;
        for (Long ingridient : db.ingridients) {
            for (FreshRange range : db.ranges) {
                if (ingridient >= range.start && ingridient <= range.end) {
                    freshCount++;
                    break;
                }
            }
        }

        System.out.println("Part 1: " + freshCount);
    }

    private static void part2(Database db) {
        List<FreshRange> mergedRanges = new ArrayList<>();
        List<FreshRange> ranges = new ArrayList<>(db.ranges);
        for (int i = 0; i < ranges.size(); i++) {
            FreshRange a = ranges.get(i);
            
            int j = ranges.size() - 1;
            while (j > i) {
                FreshRange b = ranges.get(j);
                if (a.start <= b.end && b.start <= a.end) {
                    a = new FreshRange(
                        Math.min(a.start, b.start),
                        Math.max(a.end, b.end));
                    ranges.remove(j);
                    j = ranges.size() - 1;
                } else {
                    j--;
                }
            }

            mergedRanges.add(a);
        }

        long freshCount = 0;
        for (FreshRange range : mergedRanges) {
            freshCount += range.end - range.start + 1;
        }

        System.out.println("Part 2: " + freshCount);
    }

    private static class Database {

        final List<FreshRange> ranges;
        final List<Long> ingridients;
        
        Database(List<FreshRange> ranges, List<Long> ingridients) {
            this.ranges = ranges;
            this.ingridients = ingridients;
        }
    }

    private static class FreshRange {
    
        final long start;
        final long end;

        FreshRange(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }
}

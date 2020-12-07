import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2020/day/5
 */
public class Day5 {

    private static final int ROW_COUNT = 128;
    private static final int ROW_BITS_COUNT = 31 - Integer.numberOfLeadingZeros(ROW_COUNT);
    private static final int COL_COUNT = 8;
    private static final int COL_BITS_COUNT = 31 - Integer.numberOfLeadingZeros(COL_COUNT);
    private static final int ID_MULTIPLIER = COL_COUNT;

    public static void main(String[] args) throws Exception {
        List<String> passes = getPasses();
        part1(passes);
        part2(passes);
    }

    private static void part1(List<String> passes) {
        int maxSeatId = 0;
        for (String pass : passes) {
            //System.out.printf("row=%d, col=%d%n", getRow(pass), getCol(pass));
            maxSeatId = Math.max(getRow(pass) * ID_MULTIPLIER + getCol(pass), maxSeatId);
        }

        System.out.println("Part 1: " + maxSeatId);
    }

    private static void part2(List<String> passes) {
        Map<Integer, List<Integer>> rows = new HashMap<>();
        for (String pass : passes) {
            rows.computeIfAbsent(getRow(pass), k -> new ArrayList<>()).add(getCol(pass));
        }

        for (Map.Entry<Integer, List<Integer>> entry : rows.entrySet()) {
            List<Integer> cols = entry.getValue();
            //if (cols.size() != COL_COUNT) {
            //    java.util.Collections.sort(cols);
            //    System.out.println(entry.getKey() + " " + cols);
            //}

            for (int col = 0; col < COL_COUNT; col++) {
                if (!cols.contains(col) && cols.contains(col - 1) && cols.contains(col + 1)) {
                    System.out.println("Part 2: " + (entry.getKey() * ID_MULTIPLIER + col));
                    return;
                }
            }
        }
    }

    private static int getRow(String pass) {
        int from = 0;
        int to = ROW_COUNT;
        for (int i = 0; i < ROW_BITS_COUNT; i++) {
            char ch = pass.charAt(i);
            if (ch == 'B') {
                from = from + (to - from) / 2;
            } else if (ch == 'F') {
                to = from + (to - from) / 2;
            } else {
                throw new IllegalStateException(pass);
            }
        }

        return from;
    }

    private static int getCol(String pass) {
        int from = 0;
        int to = COL_COUNT;
        for (int i = pass.length() - COL_BITS_COUNT; i < pass.length(); i++) {
            char ch = pass.charAt(i);
            if (ch == 'R') {
                from = from + (to - from) / 2;
            } else if (ch == 'L') {
                to = from + (to - from) / 2;
            } else {
                throw new IllegalStateException(pass);
            }
        }

        return from;
    }

    private static List<String> getPasses() throws Exception {
        List<String> passes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                passes.add(line);
            }
        }

        return passes;
    }
}

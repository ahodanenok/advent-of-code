import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2024/day/2
 */
public class Day2 {

    public static void main(String... args) throws Exception {
        List<Report> reports = getInput();
        part1(reports);
        part2(reports);
    }

    private static void part1(List<Report> reports) {
        int safeCount = 0;
        for (Report report : reports) {
            if (isSafe(report.levels, -1)) {
                safeCount++;
            }
        }

        System.out.println("Part 1: " + safeCount);
    }

    private static void part2(List<Report> reports) {
        int safeCount = 0;
        for (Report report : reports) {
            for (int i = 0; i < report.levels.size(); i++) {
                if (isSafe(report.levels, i)) {
                    safeCount++;
                    break;
                }
            }
        }

        System.out.println("Part 2: " + safeCount);
    }

    private static boolean isSafe(List<Integer> levels, int excludeIdx) {
        int prevDelta = 0;
        int idx = (excludeIdx == 0) ? 2 : 1;
        while (idx < levels.size()) {
            if (idx == excludeIdx) {
                idx++;
                continue;
            }

            int prevIdx = idx - 1;
            if (prevIdx == excludeIdx) {
                prevIdx--;
            }

            int delta = levels.get(idx) - levels.get(prevIdx);
            if (delta == 0
                    || Math.abs(delta) > 3
                    || (prevDelta < 0 && delta > 0)
                    || (prevDelta > 0 && delta < 0)) {
                return false;
            }

            prevDelta = delta;
            idx++;
        }

        return true;
    }

    private static List<Report> getInput() throws Exception {
        List<Report> reports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                List<Integer> levels = new ArrayList<>(parts.length);
                for (String part : parts) {
                    levels.add(Integer.parseInt(part));
                }

                reports.add(new Report(levels));
            }
        }

        return reports;
    }

    private static class Report {

        final List<Integer> levels;

        Report(List<Integer> levels) {
            this.levels = levels;
        }
    }
}

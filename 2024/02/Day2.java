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
    }

    private static void part1(List<Report> reports) {
        int safeCount = 0;
        for (Report report : reports) {
            boolean safe = true;

            int prevDelta = 0;
            for (int i = 1; i < report.levels.size(); i++) {
                int delta = report.levels.get(i) - report.levels.get(i - 1);
                if (delta == 0
                        || Math.abs(delta) > 3
                        || (prevDelta < 0 && delta > 0)
                        || (prevDelta > 0 && delta < 0)) {
                    safe = false;
                    break;
                }

                prevDelta = delta;
            }

            if (safe) {
                safeCount++;
            }
        }

        System.out.println("Part 1: " + safeCount);
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

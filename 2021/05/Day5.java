import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2021/day/5
 *
 */
public class Day5 {

    public static void main(String[] args) throws Exception {
        List<Line> lines = getInput();
        System.out.println("Part 1: " + getOverlapsCount(lines, false));
        System.out.println("Part 2: " + getOverlapsCount(lines, true));
    }

    private static List<Line> getInput() throws Exception {
        List<Line> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                String[] startParts = parts[0].trim().split(",");
                String[] endParts = parts[1].trim().split(",");
                lines.add(new Line(
                    Integer.parseInt(startParts[0].trim()),
                    Integer.parseInt(startParts[1].trim()),
                    Integer.parseInt(endParts[0].trim()),
                    Integer.parseInt(endParts[1].trim())
                ));
            }
        }

        return lines;
    }

    private static int getOverlapsCount(List<Line> lines, boolean considerDiagonals) {
        Map<String, Integer> overlaps = new HashMap<>();
        for (Line line : lines) {
            if (line.x1 == line.x2) {
                int yStart = Math.min(line.y1, line.y2);
                int yEnd = Math.max(line.y1, line.y2);
                for (int y = yStart; y <= yEnd; y++) {
                    overlaps.merge(line.x1 + "_" + y, 1, Integer::sum);
                }
            } else if (line.y1 == line.y2) {
                int xStart = Math.min(line.x1, line.x2);
                int xEnd = Math.max(line.x1, line.x2);
                for (int x = xStart; x <= xEnd; x++) {
                    overlaps.merge(x + "_" + line.y1, 1, Integer::sum);
                }
            } else if (considerDiagonals) {
                int dx = line.x1 < line.x2 ? 1 : -1;
                int dy = line.y1 < line.y2 ? 1 : -1;
                for (int x = line.x1, y = line.y1; x != line.x2; x += dx, y += dy) {
                    overlaps.merge(x + "_" + y, 1, Integer::sum);
                }

                overlaps.merge(line.x2 + "_" + line.y2, 1, Integer::sum);
            }
        }

        int overlapsCount = 0;
        for (int count : overlaps.values()) {
            if (count > 1) {
                overlapsCount++;
            }
        }

        return overlapsCount;
    }

    private static class Line {

        final int x1;
        final int y1;
        final int x2;
        final int y2;

        Line(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}

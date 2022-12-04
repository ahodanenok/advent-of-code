import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2022/day/4
 */
public class Day4 {

    public static void main(String[] args) throws Exception {
        List<Assignment> assignments = getAssignments();
        part1(assignments);
        part2(assignments);
    }

    private static List<Assignment> getAssignments() throws Exception {
        List<Assignment> assignments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] pair = line.split(",");
                String[] firstElf = pair[0].split("-");
                String[] secondElf = pair[1].split("-");
                assignments.add(new Assignment(
                    new Sections(Integer.parseInt(firstElf[0]), Integer.parseInt(firstElf[1])),
                    new Sections(Integer.parseInt(secondElf[0]), Integer.parseInt(secondElf[1]))
                ));
            }
        }

        return assignments;
    }

    private static void part1(List<Assignment> assignments) {
        int containedCount = 0;
        for (Assignment a : assignments) {
            if (a.firstElf.contains(a.secondElf) || a.secondElf.contains(a.firstElf)) {
                containedCount++;
            }
        }

        System.out.println("Part 1: " + containedCount);
    }

    private static void part2(List<Assignment> assignments) {
        int overlapedCount = 0;
        for (Assignment a : assignments) {
            if (a.firstElf.overlaps(a.secondElf)) {
                overlapedCount++;
            }
        }

        System.out.println("Part 2: " + overlapedCount);
    }

    private static class Assignment {

        final Sections firstElf;
        final Sections secondElf;

        Assignment(Sections firstElf, Sections secondElf) {
            this.firstElf = firstElf;
            this.secondElf = secondElf;
        }
    }

    private static class Sections {

        final int from;
        final int to;

        Sections(int from, int to) {
            this.from = from;
            this.to = to;
        }

        boolean contains(Sections other) {
            return from <= other.from && to >= other.to;
        }

        boolean overlaps(Sections other) {
            return from <= other.to && to >= other.from;
        }
    }
}

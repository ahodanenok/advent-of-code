import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2021/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        Manual manual = getInput();
        part1(manual);
        part2(manual);
    }

    private static Manual getInput() throws Exception {
        Manual manual = new Manual();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;

            while (!(line = reader.readLine()).isEmpty()) {
                String[] parts = line.trim().split(",");
                manual.dots.add(new Dot(Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[0].trim())));
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=");
                String part = parts[0].trim();
                char axis = part.charAt(part.length() - 1);
                manual.folds.add(new Fold(axis, Integer.parseInt(parts[1].trim())));
            }
        }

        return manual;
    }

    private static void part1(Manual manual) {
        Set<Dot> dots = manual.folds.get(0).execute(manual.dots);
        System.out.println("Part 1: " + dots.size());
    }

    private static void part2(Manual manual) {
        Set<Dot> dots = manual.dots;
        for (Fold fold : manual.folds) {
            dots = fold.execute(dots);
        }

        int rowMax = Integer.MIN_VALUE;
        int colMax = Integer.MIN_VALUE;
        for (Dot dot : dots) {
            rowMax = Math.max(dot.row, rowMax);
            colMax = Math.max(dot.col, colMax);
        }

        System.out.println("Part 2:");
        for (int row = 0; row <= rowMax; row++) {
            for (int col = 0; col <= colMax; col++) {
                if (dots.contains(new Dot(row, col))) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }
    }

    private static class Manual {

        final Set<Dot> dots = new HashSet<>();
        final List<Fold> folds = new ArrayList<>();
    }

    private static class Dot {

        final int row;
        final int col;

        Dot(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Dot other = (Dot) obj;
            return row == other.row && col == other.col;
        }
    }

    private static class Fold {

        private final char axis;
        private final int line;

        Fold(char axis, int line) {
            if (axis != 'x' && axis != 'y') {
                throw new IllegalStateException("Unknown axis: " + axis);
            }

            this.axis = axis;
            this.line = line;
        }

        Set<Dot> execute(Set<Dot> dots) {
            Set<Dot> folded = new HashSet<>();

            if (axis == 'x') {
                for (Dot dot : dots) {
                    if (dot.col < line) {
                        folded.add(dot);
                    } else {
                        folded.add(new Dot(dot.row, line - (dot.col - line)));
                    }
                }
            } else if (axis == 'y') {
                for (Dot dot : dots) {
                    if (dot.row < line) {
                        folded.add(dot);
                    } else {
                        folded.add(new Dot(line - (dot.row - line), dot.col));
                    }
                }
            }

            return folded;
        }
    }
}

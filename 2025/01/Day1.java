import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2025/day/1
 */
public class Day1 {

    public static void main(String... args) throws Exception {
        List<Rotation> rotations = getInput();
        part1(rotations);
    }

    private static List<Rotation> getInput() throws Exception {
        List<Rotation> rotations = new ArrayList<>();
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rotations.add(new Rotation(
                    line.charAt(0) == 'R',
                    Integer.parseInt(line.substring(1))));
            }
        }

        return rotations;
    }

    private static void part1(List<Rotation> rotations) {
        int pos = 50;
        int zeroTimes = 0;
        for (Rotation r : rotations) {
            if (r.clockwise) {
                pos = (pos + r.count)% 100;
            } else {
                pos = (pos - r.count)% 100;
            }

            if (pos == 0) {
                zeroTimes++;
            }
        }

        System.out.println("Part 1: " + zeroTimes);
    }

    private static class Rotation {

        final boolean clockwise;
        final int count;

        Rotation(boolean clockwise, int count) {
            this.clockwise = clockwise;
            this.count = count;
        }
    }
}

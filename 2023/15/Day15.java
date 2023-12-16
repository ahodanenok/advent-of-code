import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2023/day/15
 */
public class Day15 {

    public static void main(String[] args) throws Exception {
        List<String> commands = getInput();
        part1(commands);
        part2(commands);
    }

    private static void part1(List<String> commands) {
        int sum = 0;
        for (String cmd : commands) {
            sum += hash(cmd);
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<String> commands) {
        Lens[] boxes = new Lens[256];
        for (String cmd : commands) {
            if (cmd.endsWith("-")) {
                String label = cmd.substring(0, cmd.length() - 1);
                int boxNum = hash(label);

                Lens lens = boxes[boxNum];
                if (lens == null) {
                    continue;
                }

                if (lens.label.equals(label)) {
                    boxes[boxNum] = lens.prev;
                    continue;
                }

                while (lens.prev != null) {
                    if (lens.prev.label.equals(label)) {
                        lens.prev = lens.prev.prev;
                        break;
                    }

                    lens = lens.prev;
                }
            } else if (cmd.charAt(cmd.length() - 2) == '=') {
                String label = cmd.substring(0, cmd.length() - 2);
                int focalLength = cmd.charAt(cmd.length() - 1) - '0';
                int boxNum = hash(label);

                if (boxes[boxNum] == null) {
                    boxes[boxNum] = new Lens(label, focalLength);
                    continue;
                }

                boolean found = false;
                Lens lens = boxes[boxNum];
                while (lens != null) {
                    if (lens.label.equals(label)) {
                        lens.focalLength = focalLength;
                        found = true;
                        break;
                    }

                    lens = lens.prev;
                }
                if (found) {
                    continue;
                }

                Lens newLens = new Lens(label, focalLength);
                newLens.prev = boxes[boxNum];
                boxes[boxNum] = newLens;
            } else {
                throw new IllegalStateException("Unknown command: " + cmd);
            }
        }

        int sum = 0;
        for (int i = 0; i < boxes.length; i++) {
            Lens lens;

            int count = 0;
            lens = boxes[i];
            while (lens != null) {
                count++;
                lens = lens.prev;
            }

            int slot = count;
            lens = boxes[i];
            while (lens != null) {
                sum += (i + 1) * slot * lens.focalLength;
                slot--;
                lens = lens.prev;
            }
        }

        System.out.println("Part 2: " + sum);
    }

    private static int hash(String str) {
        int h = 0;
        for (int i = 0; i < str.length(); i++) {
            h += (int) str.charAt(i);
            h *= 17;
            h %= 256;
        }

        return h;
    }

    private static List<String> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<String> commands = new ArrayList<>();
            for (String cmd : reader.readLine().split(",")) {
                commands.add(cmd);
            }

            return commands;
        }
    }

    private static class Lens {

        final String label;
        int focalLength;
        Lens prev;

        Lens(String label, int focalLength) {
            this.label = label;
            this.focalLength = focalLength;
        }
    }
}

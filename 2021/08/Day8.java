import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2021/day/8
 */
public class Day8 {

    public static void main(String[] args) throws Exception {
        List<Signal> signals = getInput();
        part1(signals);
    }

    private static List<Signal> getInput() throws Exception {
        List<Signal> signals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                signals.add(new Signal(parts[0].trim().split(" "), parts[1].trim().split(" ")));
            }
        }

        return signals;
    }

    private static void part1(List<Signal> signals) {
        int count = 0;
        for (Signal s : signals) {
            for (String d : s.output) {
                if (d.length() == 2
                      || d.length() == 4
                      || d.length() == 3
                      || d.length() == 7) {
                    count++;
                }
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static class Signal {

        final String[] patterns;
        final String[] output;

        Signal(String[] patterns, String[] output) {
            this.patterns = patterns;
            this.output = output;
        }
    }
}

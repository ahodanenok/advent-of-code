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
    }

    private static void part1(List<String> commands) {
        int sum = 0;
        for (String cmd : commands) {
            int hash = 0;
            for (int i = 0; i < cmd.length(); i++) {
                hash += (int) cmd.charAt(i);
                hash *= 17;
                hash %= 256;
            }

            sum += hash;
        }

        System.out.println("Part 1: " + sum);
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
}

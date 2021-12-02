import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2021/day/2
 */
public class Day2 {

    public static void main(String[] args) throws Exception {
        List<Command> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                commands.add(new Command(parts[0], Integer.parseInt(parts[1])));
            }
        }

        part1(commands);
    }

    public static void part1(List<Command> commands) {
        long x = 0;
        long y = 0;
        for (Command cmd : commands) {
            if ("forward".equals(cmd.action)) {
                x += cmd.arg;
            } else if ("up".equals(cmd.action)) {
                y -= cmd.arg;
            } else if ("down".equals(cmd.action)) {
                y += cmd.arg;
            } else {
                throw new IllegalStateException("Unknown command: " + cmd.action);
            }
        }

        System.out.println("Part 1: " + (x * y));
    }

    private static class Command {

        final String action;
        final int arg;

        Command(String action, int arg) {
            this.action = action;
            this.arg = arg;
        }
    }
}

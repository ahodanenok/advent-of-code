import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2020/day/14
 */
public class Day14 {

    public static void main(String[] args) throws Exception {
        List<Command> commands = getCommands();

        Map<Long, Long> memory = new HashMap<>();
        Mask mask = null;
        for (Command cmd : commands) {
            if (cmd.mask != null) {
                mask = cmd.mask;
            } else {
                memory.put(cmd.address, mask.apply(cmd.value));
            }
        }

        System.out.println("Part 1: " + memory.values().stream().mapToLong(Long::longValue).sum());
    }

    private static List<Command> getCommands() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Command> commands = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                Command cmd = new Command();

                String[] parts = line.split("=");
                if ("mask".equals(parts[0].trim())) {
                    cmd.mask = new Mask(parts[1].trim());
                } else {
                    cmd.address = Long.parseLong(parts[0].substring(4, parts[0].lastIndexOf(']')));
                    cmd.value = Long.parseLong(parts[1].trim());
                }

                commands.add(cmd);
            }

            return commands;
        }
    }

    private static class Command {

        Mask mask;
        long address = -1;
        long value = -1;
    }

    private static class Mask {

        private long orMask;
        private long andMask;

        Mask(String str) {
            orMask = Long.parseLong(str.replaceAll("X", "0"), 2);
            andMask = Long.parseLong(str.replaceAll("X", "1"), 2);
        }

        long apply(long n) {
            return (n & andMask) | orMask;
        }
    }
}

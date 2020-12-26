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
        part1(commands);
        part2(commands);
    }

    private static void part1(List<Command> commands) {
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

    private static void part2(List<Command> commands) {
        Map<Long, Long> memory = new HashMap<>();
        Mask mask = null;
        for (Command cmd : commands) {
            if (cmd.mask != null) {
                mask = cmd.mask;
            } else {
                for (long address : mask.apply_V2(cmd.address)) {
                    memory.put(address, cmd.value);
                }
            }
        }

        System.out.println("Part 2: " + memory.values().stream().mapToLong(Long::longValue).sum());
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

        private String mask;
        private long orMask;
        private long andMask;
        private List<Integer> floating;

        Mask(String str) {
            mask = str;
            orMask = Long.parseLong(str.replaceAll("X", "0"), 2);
            andMask = Long.parseLong(str.replaceAll("X", "1"), 2);

            floating = new ArrayList<>();
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == 'X') {
                    floating.add(str.length() - i - 1);
                }
            }
        }

        long apply(long n) {
            return (n & andMask) | orMask;
        }

        List<Long> apply_V2(long n) {
            //System.out.println("- applying mask");
            //System.out.println("value:    " + toBinary(n));
            //System.out.println("mask:     " + mask);
            long masked = n | orMask;
            //System.out.println("result:   " + toBinary(masked));
            //StringBuilder wf = new StringBuilder(toBinary(masked));
            //for (int f : floating) {
            //    int idx = mask.length() - f;
            //    wf.replace(idx - 1, idx, "X");
            //}
            //System.out.println("floating: " + wf.toString());

            List<Long> result = new ArrayList<>();
            expand(0, masked, result);

            //System.out.println();
            //for (long r : result) {
            //    System.out.println("          " + toBinary(r));
            //}
            //System.out.println();

            return result;
        }

        void expand(int floatingIdx, long n, List<Long> result) {
            if (floatingIdx == floating.size()) {
                result.add(n);
                return;
            }

            long withZero = n & (0xFFFFFFFFFL - (1L << floating.get(floatingIdx)));
            expand(floatingIdx + 1, withZero, result);

            long withOne = n | (1L << floating.get(floatingIdx));
            expand(floatingIdx + 1, withOne, result);
        }
    }

    private static String toBinary(long n) {
        String nStr = Long.toBinaryString(n);
        StringBuilder sb = new StringBuilder();

        int zeroesCount = 36 - nStr.length();
        while (sb.length() < zeroesCount) {
            sb.append('0');
        }
        sb.append(nStr);

        return sb.toString();
    }
}

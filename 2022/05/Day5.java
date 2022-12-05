import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2022/day/5
 */
public class Day5 {

    public static void main(String[] args) throws Exception {
        part1(getInput());
        part2(getInput());
    }

    private static Input getInput() throws Exception {
        Input input = new Input();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()).charAt(1) != '1') {
                for (int i = 0, crate = 1; i < line.length(); i += 4, crate++) {
                    if (line.charAt(i) == '[') {
                        input.stacks.computeIfAbsent(crate, __ -> new LinkedList<>()).add(line.charAt(i + 1));
                    }
                }
            }

            reader.readLine(); // skip empty line after crate numbers
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                input.procedure.add(new Move(
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[3]),
                    Integer.parseInt(parts[5])));
            }
        }

        return input;
    }

    private static void part1(Input input) {
        for (Move move : input.procedure) {
            for (int n = 0; n < move.count; n++) {
                input.stacks.get(move.to).push(input.stacks.get(move.from).pop());
            }
        }

        System.out.print("Part 1: ");
        for (LinkedList<Character> stack : input.stacks.values()) {
            System.out.print(stack.peek());
        }
        System.out.println();
    }

    private static void part2(Input input) {
        LinkedList<Character> crane = new LinkedList<>();
        for (Move move : input.procedure) {
            for (int n = 0; n < move.count; n++) {
                crane.push(input.stacks.get(move.from).pop());
            }

            while (!crane.isEmpty()) {
                input.stacks.get(move.to).push(crane.pop());
            }
        }

        System.out.print("Part 2: ");
        for (LinkedList<Character> stack : input.stacks.values()) {
            System.out.print(stack.peek());
        }
        System.out.println();
    }

    private static class Input {

        final Map<Integer, LinkedList<Character>> stacks = new TreeMap<>();
        final List<Move> procedure = new LinkedList<>();
    }

    private static class Move {

        final int count;
        final int from;
        final int to;

        Move(int count, int from, int to) {
            this.count = count;
            this.from = from;
            this.to = to;
        }
    }
}

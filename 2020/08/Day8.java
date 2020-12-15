import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2020/day/8
 */
public class Day8 {

    private static final String ACC_CMD = "acc";
    private static final String JMP_CMD = "jmp";
    private static final String NOP_CMD = "nop";

    public static void main(String[] args) throws Exception {
        List<Command> commands = getCommands();
        part1(commands);
        part2(commands);
    }

    private static void part1(List<Command> commands) {
        System.out.println("Part 1: " + execute(commands).acc);
    }

    private static void part2(List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            Command cmd = commands.get(i);
            if (JMP_CMD.equals(cmd.name)) {
                commands.set(i, new Command(NOP_CMD, cmd.arg));
            } else if (NOP_CMD.equals(cmd.name)) {
                commands.set(i, new Command(JMP_CMD, cmd.arg));
            } else {
                continue;
            }

            ExecutionResult result = execute(commands);
            if (!result.cycle) {
                System.out.println("Part 2: " + result.acc);
                break;
            }

            commands.set(i, cmd);
        }
    }

    private static ExecutionResult execute(List<Command> commands) {
        int acc = 0;
        int pos = 0;
        Set<Integer> executed = new HashSet<>();
        while (pos >= 0 && pos < commands.size()) {
            Command cmd = commands.get(pos);
            if (!executed.add(pos)) {
                return new ExecutionResult(acc, true);
            }

            if (ACC_CMD.equals(cmd.name)) {
                acc += cmd.arg;
                pos++;
            } else if (JMP_CMD.equals(cmd.name)) {
                pos += cmd.arg;
            } else if (NOP_CMD.equals(cmd.name)) {
                pos++;
            } else {
                throw new IllegalStateException(cmd.name);
            }
            
        }

        return new ExecutionResult(acc, false);
    }

    private static List<Command> getCommands() throws Exception {
        List<Command> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                commands.add(new Command(parts[0], Integer.parseInt(parts[1])));
            }
        }

        return commands;
    }

    private static class Command {

        final String name;
        final int arg;

        Command(String name, int arg) {
            this.name = name;
            this.arg = arg;
        }
    }

    private static class ExecutionResult {

        final int acc;
        final boolean cycle;

        ExecutionResult(int acc, boolean cycle) {
            this.acc = acc;
            this.cycle = cycle;
        }
    }
}

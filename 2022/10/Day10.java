import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2022/day/10
 */
public class Day10 {

    private static final String INST_NOOP = "noop";
    private static final String INST_ADDX = "addx";

    public static void main(String[] args) throws Exception {
        List<Instruction> instructions = getInstructions();
        part1(instructions);
    }

    private static void part1(List<Instruction> instructions) {
        int signal = 0;
        int cycle = 0;
        int x = 1;

        for (Instruction inst : instructions) {
            if (INST_NOOP == inst.type) {
                cycle++;
                signal += readSignalStrength(x, cycle);
            } else if (INST_ADDX == inst.type) {
                cycle++;
                signal += readSignalStrength(x, cycle);

                cycle++;
                signal += readSignalStrength(x, cycle);

                x += inst.arg;
            }
        }

        System.out.println("Part 1: " + signal);
    }

    private static int readSignalStrength(int x, int cycle) {
        if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220) {
            return x * cycle;
        }

        return 0;
    }

    private static List<Instruction> getInstructions() throws Exception {
        List<Instruction> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (INST_NOOP.equals(parts[0])) {
                    instructions.add(new Instruction(INST_NOOP, 0));
                } else if (INST_ADDX.equals(parts[0])) {
                    instructions.add(new Instruction(INST_ADDX, Integer.parseInt(parts[1])));
                } else {
                    throw new IllegalStateException("Unknown instruction: " + line);
                }
            }
        }

        return instructions;
    }

    private static class Instruction {

        final String type;
        final int arg;

        Instruction(String type, int arg) {
            this.type = type;
            this.arg = arg;
        }
    }
}

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2019/day/7
 */
public class Day7 {

    public static void main(String[] args) throws Exception {
        List<Integer> ams = getInput();

        List<Integer> availablePhases = new ArrayList<>();
        availablePhases.add(0);
        availablePhases.add(1);
        availablePhases.add(2);
        availablePhases.add(3);
        availablePhases.add(4);

        List<List<Integer>> allPhases = new ArrayList<>();
        perms(availablePhases, new ArrayList<>(), allPhases);

        int currentSignal = Integer.MIN_VALUE;
        for (List<Integer> phases : allPhases) {
            currentSignal = Math.max(signal(phases, ams), currentSignal);
        }

        System.out.println("Part 1: " + currentSignal);
    }

    private static void perms(List<Integer> remaining, List<Integer> current, List<List<Integer>> all) {
        if (remaining.isEmpty()) {
           all.add(new ArrayList<>(current)); 
        } else {
            for (int i = 0; i < remaining.size(); i++) {
               current.add(remaining.remove(i));
               perms(remaining, current, all); 
               remaining.add(i, current.remove(current.size() - 1));
            }
        }
    }

    private static int signal(List<Integer> phases, List<Integer> ams) {
        int in = 0;
        for (int i = 0; i < phases.size(); i++) {
            Memory m = new Memory(ams);
            in = run(ams, m, Arrays.asList(phases.get(i), in)).get(0);
        }

        return in;
    }

    private static List<Integer> getInput() throws Exception {
        List<Integer> input = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line = reader.readLine();
            for (String n : line.split(",")) {
                input.add(Integer.parseInt(n.trim()));
            }
        }

        return input;
    }

    private static List<Integer> run(List<Integer> input, Memory memory, List<Integer> args) {
        int pos = 0;
        int inputIdx = 0;
        List<Integer> out = new ArrayList<>();
        while (pos < input.size() && memory.get(pos) != 99) {
            int cmd = memory.get(pos);
            int opcode = opcode(cmd);
            if (opcode == 1) {
                int a = value(memory, cmd, pos, 1);
                int b = value(memory, cmd, pos, 2);
                memory.set(memory.get(pos + 3), a + b);
                pos += 4;
            } else if (opcode == 2) {
                int a = value(memory, cmd, pos, 1);
                int b = value(memory, cmd, pos, 2);
                memory.set(memory.get(pos + 3), a * b);
                pos += 4;
            } else if (opcode == 3) {
                int in = args.get(inputIdx++);
                memory.set(memory.get(pos + 1), in);
                pos += 2;
            } else if (opcode == 4) {
                //System.out.println(value(memory, cmd, pos, 1));
                out.add(value(memory, cmd, pos, 1));
                pos += 2;
            } else if (opcode == 5) {
                int v = value(memory, cmd, pos, 1);
                if (v != 0) {
                    pos = value(memory, cmd, pos, 2);
                } else {
                    pos += 3;
                }
            } else if (opcode == 6) {
                int v = value(memory, cmd, pos, 1);
                if (v == 0) {
                    pos = value(memory, cmd, pos, 2);
                } else {
                    pos += 3;
                }
            } else if (opcode == 7) {
                int a = value(memory, cmd, pos, 1);
                int b = value(memory, cmd, pos, 2);
                if (a < b) {
                    memory.set(memory.get(pos + 3), 1);
                } else {
                    memory.set(memory.get(pos + 3), 0);
                }
                pos += 4;
            } else if (opcode == 8) {
                int a = value(memory, cmd, pos, 1);
                int b = value(memory, cmd, pos, 2);
                if (a == b) {
                    memory.set(memory.get(pos + 3), 1);
                } else {
                    memory.set(memory.get(pos + 3), 0);
                }
                pos += 4;
            } else {
                throw new IllegalStateException("Unknown command: " + opcode); 
            } 
        }

        return out;
    }

    private static int opcode(int instruction) {
        return instruction % 100;
    }

    private static int value(Memory memory, int instruction, int pos, int idx) {
        int mode = instruction / (int) Math.pow(10, idx + 1) % 10;
        if (mode == 0) {
            return memory.get(memory.get(pos + idx));
        } else {
            return memory.get(pos + idx);
        } 
    }

    private static class Memory {


        private List<Integer> data; 

        Memory(List<Integer> initialData) {
            this.data = new ArrayList<>(initialData);
        }

        void set(int idx, int value) {
            data.set(idx, value);
        }

        int get(int idx) {
            return data.get(idx);
        }
    }
}


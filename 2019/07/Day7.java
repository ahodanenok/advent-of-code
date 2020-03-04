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
        part1(ams);
        part2(ams);
    }

    private static void part1(List<Integer> ams) {
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
            int in = 0;
            for (int i = 0; i < phases.size(); i++) {
                Context ctx = new Context(new Memory(ams), Arrays.asList(phases.get(i), in));
                in = run(ams, ctx); 
            }
 
            currentSignal = Math.max(in, currentSignal);
        }

        System.out.println("Part 1: " + currentSignal);
    }

    private static void part2(List<Integer> ams) {
        List<Integer> availablePhases = new ArrayList<>();
        availablePhases.add(5);
        availablePhases.add(6);
        availablePhases.add(7);
        availablePhases.add(8);
        availablePhases.add(9);

        List<List<Integer>> allPhases = new ArrayList<>();
        perms(availablePhases, new ArrayList<>(), allPhases);

        int currentSignal = Integer.MIN_VALUE;
        for (List<Integer> phases : allPhases) {
            List<Context> contexts = Arrays.asList(
                new Context(new Memory(ams), new ArrayList<>()),
                new Context(new Memory(ams), new ArrayList<>()),
                new Context(new Memory(ams), new ArrayList<>()),
                new Context(new Memory(ams), new ArrayList<>()),
                new Context(new Memory(ams), new ArrayList<>()));
    
            for (int i = 0; i < phases.size(); i++) {
                contexts.get(i).in.add(phases.get(i));
            }
            

            int in = 0;
            int currentAmp = 0;
            while (true) {
                contexts.get(currentAmp).in.add(in);
                Integer out = run(ams, contexts.get(currentAmp));
                if (out == null) {
                    break;
                } 

                in = out;
                currentAmp = (currentAmp + 1) % 5;
            }

            currentSignal = Math.max(in, currentSignal);
        }

        System.out.println("Part 2: " + currentSignal);
 
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

    private static Integer run(List<Integer> input, Context ctx) { 
        Memory memory = ctx.memory;
        while (ctx.pos < input.size() && memory.get(ctx.pos) != 99) {
            int cmd = ctx.memory.get(ctx.pos);
            int opcode = opcode(cmd);
            if (opcode == 1) {
                int a = value(memory, cmd, ctx.pos, 1);
                int b = value(memory, cmd, ctx.pos, 2);
                memory.set(memory.get(ctx.pos + 3), a + b);
                ctx.pos += 4;
            } else if (opcode == 2) {
                int a = value(memory, cmd, ctx.pos, 1);
                int b = value(memory, cmd, ctx.pos, 2);
                memory.set(memory.get(ctx.pos + 3), a * b);
                ctx.pos += 4;
            } else if (opcode == 3) {
                int in = ctx.in.get(ctx.inputIdx++);
                memory.set(memory.get(ctx.pos + 1), in);
                ctx.pos += 2;
            } else if (opcode == 4) {
                int out = value(memory, cmd, ctx.pos, 1);
                ctx.pos += 2;
                return out;
            } else if (opcode == 5) {
                int v = value(memory, cmd, ctx.pos, 1);
                if (v != 0) {
                    ctx.pos = value(memory, cmd, ctx.pos, 2);
                } else {
                    ctx.pos += 3;
                }
            } else if (opcode == 6) {
                int v = value(memory, cmd, ctx.pos, 1);
                if (v == 0) {
                    ctx.pos = value(memory, cmd, ctx.pos, 2);
                } else {
                    ctx.pos += 3;
                }
            } else if (opcode == 7) {
                int a = value(memory, cmd, ctx.pos, 1);
                int b = value(memory, cmd, ctx.pos, 2);
                if (a < b) {
                    memory.set(memory.get(ctx.pos + 3), 1);
                } else {
                    memory.set(memory.get(ctx.pos + 3), 0);
                }
                ctx.pos += 4;
            } else if (opcode == 8) {
                int a = value(memory, cmd, ctx.pos, 1);
                int b = value(memory, cmd, ctx.pos, 2);
                if (a == b) {
                    memory.set(memory.get(ctx.pos + 3), 1);
                } else {
                    memory.set(memory.get(ctx.pos + 3), 0);
                }
                ctx.pos += 4;
            } else {
                throw new IllegalStateException("Unknown command: " + opcode); 
            } 
        }

        return null;
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

    private static class Context {

        private Memory memory;
        private int pos = 0;
        private int inputIdx = 0;
        private List<Integer> in;

        Context(Memory memory, List<Integer> in) {
            this.memory = memory;
            this.in = in;
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


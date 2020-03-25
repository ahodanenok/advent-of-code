import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2019/day/11
 */
public class Day13 {

    private static final int COLOR_BLACK = 0;
    private static final int COLOR_WHITE = 1;

    private static final int DIR_LEFT = 0;
    private static final int DIR_RIGHT = 1;
    private static final int DIR_UP = 2;
    private static final int DIR_DOWN = 3;

    public static void main(String[] args) throws Exception {
        List<Long> input = getInput();
        part1(input);
    }

    private static List<Long> getInput() throws Exception {
        List<Long> input = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line = reader.readLine();
            for (String n : line.split(",")) {
                input.add(Long.parseLong(n.trim()));
            }
        }

        return input;
    }

    private static void part1(List<Long> program) {
        Context ctx = new Context();
        ctx.memory = new Memory(program);

        int blockCount = 0;
        while (true) {
            Integer x = run(ctx, program, 0);
            Integer y = run(ctx, program, 0);
            Integer tile = run(ctx, program, 0);

            if (x == null || y == null || tile == null) {
                break;
            }

            if (tile == 2) {
                blockCount++;
            }
        }

        System.out.println("Part 1: " + blockCount);
    }

    private static Integer run(Context ctx, List<Long> input, int id) {
        while (ctx.pos < input.size() && ctx.memory.get(ctx.pos) != 99) {
            int cmd = (int) ctx.memory.get(ctx.pos);
            int opcode = opcode(cmd);
            if (opcode == 1) {
                long a = value(ctx, cmd, 1);
                long b = value(ctx, cmd, 2);
                ctx.memory.set(addr(ctx, cmd, 3), a + b);
                ctx.pos += 4;
            } else if (opcode == 2) {
                long a = value(ctx, cmd, 1);
                long b = value(ctx, cmd, 2);
                ctx.memory.set(addr(ctx, cmd, 3), a * b);
                ctx.pos += 4;
            } else if (opcode == 3) {
                int in = id;
                ctx.memory.set(addr(ctx, cmd, 1), in);
                ctx.pos += 2;
            } else if (opcode == 4) {
                int out = (int) value(ctx, cmd, 1);
                ctx.pos += 2;
                return out;
            } else if (opcode == 5) {
                long v = value(ctx, cmd, 1);
                if (v != 0) {
                    ctx.pos = (int) value(ctx, cmd, 2);
                } else {
                    ctx.pos += 3;
                }
            } else if (opcode == 6) {
                long v = value(ctx, cmd, 1);
                if (v == 0) {
                    ctx.pos = (int) value(ctx, cmd, 2);
                } else {
                    ctx.pos += 3;
                }
            } else if (opcode == 7) {
                long a = value(ctx, cmd, 1);
                long b = value(ctx, cmd, 2);
                if (a < b) {
                    ctx.memory.set(addr(ctx, cmd, 3), 1);
                } else {
                    ctx.memory.set(addr(ctx, cmd, 3), 0);
                }
                ctx.pos += 4;
            } else if (opcode == 8) {
                long a = value(ctx, cmd, 1);
                long b = value(ctx, cmd, 2);
                if (a == b) {
                    ctx.memory.set(addr(ctx, cmd, 3), 1);
                } else {
                    ctx.memory.set(addr(ctx, cmd, 3), 0);
                }
                ctx.pos += 4;
            } else if (opcode == 9) {
                ctx.relativeBase += (int) value(ctx, cmd, 1);
                ctx.pos += 2;
            } else {
                throw new IllegalStateException("Unknown command: " + opcode); 
            } 
        }

        return null;
    }

    private static int opcode(int instruction) {
        return instruction % 100;
    }

    private static int addr(Context ctx, int instruction, int idx) {
        int mode = instruction / (int) Math.pow(10, idx + 1) % 10;
        if (mode == 0) {
            return (int) ctx.memory.get(ctx.pos + idx);
        } else if (mode == 1) {
            return ctx.pos + idx;
        } else if (mode == 2) {
            return ctx.relativeBase + (int) ctx.memory.get(ctx.pos + idx);
        } else {
            throw new IllegalArgumentException("mode");
        }
    }

    private static long value(Context ctx, int instruction, int idx) {
        return ctx.memory.get(addr(ctx, instruction, idx));
    }

    private static class Context {

        Memory memory;
        int relativeBase;
        int pos;
    }

    private static class Memory {

        private List<Long> data; 

        Memory(List<Long> initialData) {
            this.data = new ArrayList<>(initialData);
        }

        void set(int idx, long value) {
            expandIfNeeded(idx);
            data.set(idx, value);
        }

        long get(int idx) {
            expandIfNeeded(idx);
            return data.get(idx);
        }

        private void expandIfNeeded(int idx) {
            while (data.size() <= idx) data.add(0L);
        }
    }
}


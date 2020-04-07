import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2019/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        List<Long> program = getInput();
        part1(program);
        part2(program);
    }

    private static void part1(List<Long> program) {
        int affectedCount = 0;

        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                Context ctx = new Context();
                ctx.memory = new Memory(program);
                ctx.in = new LinkedList<>();
                ctx.in.add(x);
                ctx.in.add(y);
                int state = run(ctx);
                if (state == 1) {
                    affectedCount++;
                }

            }
        }

        System.out.println("Part 1: " + affectedCount);
    }

    private static void part2(List<Long> program) {
        Set<Point> affected = new HashSet<>();

        for (int y = 0; y < 10000; y++) {
            Point rowStart = null;
            for (int x = 0; x < 10000; x++) {
                Context ctx = new Context();
                ctx.memory = new Memory(program);
                ctx.in = new LinkedList<>();
                ctx.in.add(x);
                ctx.in.add(y);

                int state = run(ctx);
                if (state == 1) {
                    if (rowStart == null) rowStart = new Point(x, y);
                    affected.add(new Point(x, y));
                }

                if (state == 0 && rowStart != null) {
                    break;
                }
            }

            if (rowStart == null) {
                continue;
            }

            Point current = rowStart;

            int height = 0;
            while (true) {
                height++;
                if (height >= 100) { 
                    break;
                }

                current = current.up();
                if (!affected.contains(current) || current.y < 0) {
                    break;
                }
            }

            Point closest = current;

            int width = 0;
            while (true) {
                width++; 
                if (width >= 100) {
                    break;
                }

                current = current.right();
                if (!affected.contains(current) || current.x < 0) {
                    break;
                }
            }

            if (height == 100 && width == 100) {
                System.out.println("Part 2: " + (closest.x * 10000 + closest.y));
                break;
            }
        }
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

    private static class Point {

        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point right() {
            return new Point(x + 1, y);
        }

        Point up() {
            return new Point(x, y - 1);
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private static Integer run(Context ctx) {
        while (ctx.memory.get(ctx.pos) != 99) {
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
                int in = ctx.in.removeFirst();
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
        LinkedList<Integer> in;
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


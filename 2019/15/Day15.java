import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2019/day/15
 */
public class Day15 {

    private static final int DIR_NORTH = 1;
    private static final int DIR_SOUTH = 2;
    private static final int DIR_WEST = 3;
    private static final int DIR_EAST = 4;
    private static final int[] DIRS = new int[] { DIR_NORTH, DIR_SOUTH, DIR_WEST, DIR_EAST };

    private static final int STATUS_WALL_AHEAD = 0;
    private static final int STATUS_MOVED = 1;
    private static final int STATUS_OXYGEN_SYSTEM_REACHED = 2;

    public static void main(String[] args) throws Exception {
        List<Long> program = getInput();
        part1(program);
    } 

    private static void part1(List<Long> program) {
        Context ctx = new Context();
        ctx.memory = new Memory(program);

        State initialState = new State();
        initialState.point = new Point(0, 0);
        initialState.visited = Collections.singleton(initialState.point);
        initialState.ctx = ctx;

        LinkedList<State> queue = new LinkedList<>();
        queue.add(initialState);

        while (!queue.isEmpty()) {
            State current = queue.removeFirst();
            for (int dir : DIRS) {
                Context nextCtx = current.ctx.copy();
                int status = run(nextCtx, program, dir);
                if (status == STATUS_WALL_AHEAD) {
                    continue;
                } else if (status == STATUS_MOVED) {
                    State next = new State();
                    if (dir == DIR_NORTH) {
                       next.point = current.point.up(); 
                    } else if (dir == DIR_SOUTH) {
                       next.point = current.point.down(); 
                    } else if (dir == DIR_WEST) {
                       next.point = current.point.left(); 
                    } else if (dir == DIR_EAST) {
                       next.point = current.point.right(); 
                    } else {
                        throw new IllegalStateException("dir: " + dir);
                    }

                    if (!current.visited.contains(next.point)) {
                        next.visited = new HashSet<>(current.visited);
                        next.visited.add(next.point);
                        next.steps = current.steps + 1;
                        next.ctx = nextCtx;
                        queue.addLast(next);
                    }
                } else if (status == STATUS_OXYGEN_SYSTEM_REACHED) {
                    System.out.println("Part 1: " + (current.steps + 1)); 
                    return;
                } else {
                    throw new IllegalStateException("status: " + status);
                }
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

        Point left() {
            return new Point(x - 1, y);
        }

        Point right() {
            return new Point(x + 1, y);
        }

        Point down() {
            return new Point(x, y - 1);
        }

        Point up() {
            return new Point(x, y + 1);
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

        Context copy() {
            Context ctx = new Context();
            ctx.relativeBase = relativeBase;
            ctx.pos = pos;
            ctx.memory = new Memory(memory.data);
            return ctx;
        }
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

    private static class State {

        private Point point;
        private int steps;
        private Context ctx;
        private Set<Point> visited;
    }
}


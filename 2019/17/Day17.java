import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2019/day/17
 */
public class Day17 {

    private static final int DIR_LEFT = 60;
    private static final int DIR_RIGHT = 62;
    private static final int DIR_UP = 94;
    private static final int DIR_DOWN = 86;

    public static void main(String[] args) throws Exception {
        List<Long> input = getInput();

input.set(0, 2L);

Context ctx = new Context();
ctx.memory = new Memory(input);
run(ctx, input, 65);

        //part1(input);
        //part2(input);
    }

    private static void part1(List<Long> input) {
        Set<Point> scaffolds = view(input).scaffolds;

        Set<Point> intersections = new HashSet<>();
        for (Point sc : scaffolds) {
            int nc = 0;
            if (scaffolds.contains(sc.left())) nc++;
            if (scaffolds.contains(sc.right())) nc++;
            if (scaffolds.contains(sc.up())) nc++;
            if (scaffolds.contains(sc.down())) nc++;
            if (nc == 4) {
                intersections.add(sc);
            }
        }

        int paramSum = 0;
        for (Point cr : intersections) {
            paramSum += cr.x * cr.y;
        }

        System.out.println("Part 1: " + paramSum);
    }

    private static void part2(List<Long> input) {
        View view = view(input);

        int steps = 0;
        List<String> instructions = new ArrayList<>();

        Point currentPoint = view.robot;
        Dir currentDir = view.dir;
        while (currentPoint != null) {
            Point next = currentDir.next(currentPoint);
            Point left = currentDir.left().next(currentPoint);
            Point right = currentDir.right().next(currentPoint);
            if (!view.scaffolds.contains(next)
                    && !view.scaffolds.contains(left)
                    && !view.scaffolds.contains(right)) {
                break;
            }

            if (view.scaffolds.contains(next)) {
                steps++;
                currentPoint = next; 
                continue;
            } 

            if (steps > 0) instructions.add(steps + "");
            steps = 0;

            if (view.scaffolds.contains(right)) {
                currentDir = currentDir.right();
                instructions.add(Dir.RIGHT.name().charAt(0) + "");
            } else if (view.scaffolds.contains(left)) {
                currentDir = currentDir.left();
                instructions.add(Dir.LEFT.name().charAt(0) + "");
            } else {
                currentDir = currentDir.right();
                instructions.add(Dir.RIGHT.name().charAt(0) + "");
            }

        }

        if (steps > 0) instructions.add(steps + "");
//instructions = Arrays.asList("R","8","R","8","R","4","R","4","R","8","L","6","L","2","R","4","R","4","R","8","R","8","R","8","L","6","L","2");
        System.out.println(instructions);

        boolean[] used = new boolean[instructions.size()];
        List<Routine> routines = new ArrayList<>();
        routines(instructions, used, routines, 3);

        for (Routine r : routines) {
            System.out.println(r.length + " at " + r.positions);
        }

        List<Integer> main = new ArrayList<>();

        int pos = 0;
        while (pos < instructions.size()) {
            for (int i = 0; i < routines.size(); i++) {
                Routine r = routines.get(i);
                if (r.positions.contains(pos)) {
                    main.add(i);
                    pos += r.length;
                    break;
                }
            }
        }

        System.out.println(main);

        input = new ArrayList<>(input);
        input.set(0, 2L);

        Context ctx = new Context();
        ctx.memory = new Memory(input);

        run(ctx, input, 65);

        /*for (int i = 0; i < main.size(); i++) {
            run(ctx, input, 'A' + main.get(i));
            if (i < main.size() - 1) {
                run(ctx, input, 44);
            }
        }
        run(ctx, input, 10);

        for (Routine r : routines) {
            for (int i = r.positions.get(0); i < r.positions.get(0) + r.length; i++) {
                char[] cmd = instructions.get(i).toCharArray();
                for (int j = 0; j < cmd.length; j++) {
                    run(ctx, input, cmd[j]); 
                }
      
                if (i < r.positions.get(0) + r.length - 1) {
                    run(ctx, input, 44);
                }
            } 

            run(ctx, input, 10);
        }

        run(ctx, input, 'n');

        Long result = 0L;
        while (true) {
           Long out = run(ctx, input, 0);
           if (out == null) {
               System.out.println(result); 
               break;
           }

           result = out;
        }*/
    }

    private static boolean routines(List<String> instructions, boolean[] used, List<Routine> routines, int count) {
        if (count <= 0) {
            int usedCount = 0;
            for (int i = 0; i < used.length; i++) {
                if (used[i]) usedCount++;
            }

            if (usedCount == instructions.size()) {
                return true;
            }

            return false;
        }

        for (int length = 4; length < 20; length += 2) {
            int pos = 0;
            while (pos < used.length && used[pos]) pos += 2;
 
            Routine routine = new Routine();
            routine.length = length;
            routine.positions.add(pos);
          
            int nextPos = pos + routine.length; 
            while (nextPos < instructions.size()) {
                int from = nextPos;
                while (from < instructions.size() && used[from])  {
                   from++; 
                } 

                int to = from;
                while (to < instructions.size() && !used[to]) {
                    to++;
                }

                if (to - from >= routine.length
                        && instructions.subList(pos, pos + routine.length)
                              .equals(instructions.subList(from, from + routine.length))) {
                    routine.positions.add(from);
                    nextPos = from + routine.length;
                } else {
                    nextPos += 2;
                } 
            } 

            routines.add(routine);

            boolean[] usedCopy = Arrays.copyOf(used, used.length);
            for (Integer i : routine.positions) {
                for (int j = i; j < i + routine.length; j++) {
                    usedCopy[j] = true;
                }
            }

            if (routines(instructions, usedCopy, routines, count - 1)) {
                return true;
            }

            routines.remove(routines.size() - 1);
        }

        return false;
    }

    private static int ascii(int n) {
        return (n + "").charAt(0);
    }

    private static View view(List<Long> input) {
        Context ctx = new Context();
        ctx.memory = new Memory(input);

        View view = new View();
        view.scaffolds = new HashSet<>();

        int x = 0;
        int y = 0;
        Long out;
        while ((out = run(ctx, input, 0)) != null) {
            if (out == 35) {
                view.scaffolds.add(new Point(x, y));
                x++;
            } else if (out == 46) {
                x++;
            } else if (out == 10) {
                x = 0;
                y++;
            } else if (out == DIR_LEFT) {
                view.robot = new Point(x, y);
                view.dir = Dir.LEFT;
                x++;
            } else if (out == DIR_RIGHT) {
                view.robot = new Point(x, y);
                view.dir = Dir.RIGHT;
                x++;
            } else if (out == DIR_DOWN) {
                view.robot = new Point(x, y);
                view.dir = Dir.DOWN;
                x++;
            } else if (out == DIR_UP) {
                view.robot = new Point(x, y);
                view.dir = Dir.UP;
                x++;
            }
        }

        return view;
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

    private static class Routine {

        private int length;
        private List<Integer> positions = new ArrayList<>();
    }

    private static class View {

        private Set<Point> scaffolds;
        private Point robot;
        private Dir dir;
    }

    private static enum Dir {
        UP() {
            @Override
            Dir left() {
                return LEFT; 
            }

            @Override
            Dir right() {
                return RIGHT;
            }

            @Override
            Point next(Point p) {
                return p.up();
            }
        },
        DOWN() {
            @Override
            Dir left() {
                return RIGHT; 
            }

            @Override
            Dir right() {
                return LEFT; 
            }

            @Override
            Point next(Point p) {
                return p.down();
            }
        },
        RIGHT() {
            @Override
            Dir left() {
                return UP; 
            }

            @Override
            Dir right() {
                return DOWN;
            }

            @Override
            Point next(Point p) {
                return p.right();
            }
        },
        LEFT() {
            @Override
            Dir left() {
                return DOWN;
            }

            @Override
            Dir right() {  
                return UP; 
            }

            @Override
            Point next(Point p) {
                return p.left();
            }
        };

        abstract Dir left();
        abstract Dir right();
        abstract Point next(Point p);

        int ascii() {
            return name().charAt(0);
        }
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
            return new Point(x, y + 1);
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

    private static Long run(Context ctx, List<Long> input, int id) {
        while (ctx.memory.get(ctx.pos) != 99) {
            int cmd = (int) ctx.memory.get(ctx.pos);
            int opcode = opcode(cmd);
            if (opcode == 1) {
//System.out.println("SUM");
                long a = value(ctx, cmd, 1);
//System.out.println("  a = " + a);  
                long b = value(ctx, cmd, 2);
//System.out.println("  b = " + b);  
//System.out.println("  to = " + addr(ctx, cmd, 3));
                ctx.memory.set(addr(ctx, cmd, 3), a + b);
                ctx.pos += 4;
            } else if (opcode == 2) {
//System.out.println("MUL");
                long a = value(ctx, cmd, 1);
//System.out.println("  a = " + a);  
                long b = value(ctx, cmd, 2);
//System.out.println("  b = " + b);  
//System.out.println("  to = " + addr(ctx, cmd, 3));
                ctx.memory.set(addr(ctx, cmd, 3), a * b);
                ctx.pos += 4;
            } else if (opcode == 3) {
                int in = id;
                System.out.print((char) id);
                ctx.memory.set(addr(ctx, cmd, 1), in);
                ctx.pos += 2;
            } else if (opcode == 4) {
                long out = value(ctx, cmd, 1);
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
//System.out.println("  " + idx + ": " + mode);
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

        List<Long> input;
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


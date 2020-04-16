import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.WIn;
import ahodanenok.aoc.intcode.Out;

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
        long[] program = IntcodeComputer.load("input.txt");
        part1(program);
        part2(program);
    }

    private static void part1(long[] program) {
        Set<Point> scaffolds = view(program).scaffolds;

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

    private static void part2(long[] program) {
        View view = view(program);

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
        //System.out.println(instructions);

        boolean[] used = new boolean[instructions.size()];
        List<Routine> routines = new ArrayList<>();
        routines(instructions, used, routines, 3);

        //for (Routine r : routines) {
        //    System.out.println(r.length + " at " + r.positions);
        //}

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

        //System.out.println(main);

        WIn in = new WIn();
        for (int i = 0; i < main.size(); i++) {
            in.add('A' + main.get(i));
            if (i < main.size() - 1) {
                in.add(',');
            }
        }
        in.add('\n');

        for (Routine r : routines) {
            for (int i = r.positions.get(0); i < r.positions.get(0) + r.length; i++) {
                char[] cmd = instructions.get(i).toCharArray();
                for (int j = 0; j < cmd.length; j++) {
                    in.add(cmd[j]);
                }

                if (i < r.positions.get(0) + r.length - 1) {
                    in.add(',');
                }
            } 

           in.add('\n');
        }

        in.add('n');
        in.add('\n');

        IntcodeComputer pc = new IntcodeComputer(program);
        pc.memset(0, 2);
        pc.setIn(in);
        pc.setOut(new Out() {
            private long dust;

            @Override
            public void write(long n) {
                dust = n;
            }

            @Override
            public void close() {
                System.out.println("Part 2: " + dust);
            }
        });
        pc.run();
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

    private static View view(long[] program) {
        

        View view = new View();
        view.scaffolds = new HashSet<>();

        IntcodeComputer pc = new IntcodeComputer(program);
        pc.setOut(new Out() {
            int x = 0;
            int y = 0;

            public void write(long out) {
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
                } else {
                    throw new IllegalStateException();
                }
            }
        });
        pc.run();

        return view;
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
}

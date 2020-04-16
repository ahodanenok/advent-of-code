import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.In;
import ahodanenok.aoc.intcode.Out;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2019/day/11
 */
public class Day11 {

    private static final int COLOR_BLACK = 0;
    private static final int COLOR_WHITE = 1;

    private static final int DIR_LEFT = 0;
    private static final int DIR_RIGHT = 1;
    private static final int DIR_UP = 2;
    private static final int DIR_DOWN = 3;

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");
        part1(program);
        part2(program);
    }

    private static void part1(long[] program) {
        Point startingPoint = new Point(0, 0);
        Map<Point, Integer> painted = paint(program, startingPoint, COLOR_BLACK);
        System.out.println("Part 1: " + painted.size());
    }

    private static void part2(long[] program) {
        Point startingPoint = new Point(0, 0);
        Map<Point, Integer> painted = paint(program, startingPoint, COLOR_WHITE);

        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (Point p : painted.keySet()) {
            xMin = Math.min(p.x, xMin);
            xMax = Math.max(p.x, xMax);
            yMin = Math.min(p.y, yMin);
            yMax = Math.max(p.y, yMax);
        }

        for (int y = yMin; y <= yMax; y++) {
            for (int x = xMin; x <= xMax; x++) {
                Integer color = painted.get(new Point(x, y));
                if (color != null && color == COLOR_WHITE) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                } 
            } 

            System.out.println();
        }
    }

    private static Map<Point, Integer> paint(long[] program, Point startingPoint, int startingPointColor) {
        Map<Point, Integer> panels = new HashMap<>();
        panels.put(startingPoint, startingPointColor);

        new PaintingRobot(program, startingPoint, panels).launch();

        return panels;
    }

    private static class PaintingRobot implements In, Out{

        private IntcodeComputer pc;
        private LinkedList<Integer> in = new LinkedList<>();

        private Point currentPoint;
        private int currentDir = DIR_UP;
        private Map<Point, Integer> panels;

        PaintingRobot(long[] program, Point startingPoint, Map<Point, Integer> panels) {
            this.pc = new IntcodeComputer(program);
            this.pc.setIn(this);
            this.pc.setOut(this);
            this.panels = panels;
            this.currentPoint = startingPoint;
        }

        void launch() {
            pc.run();
        }

        @Override
        public void write(long n) {
            in.addLast((int) n);
            if (in.size() == 2) {
                paint(in.removeFirst(), in.removeFirst());
            }
        }

        @Override
        public long read() {
            if (!panels.containsKey(currentPoint)) {
                panels.put(currentPoint, COLOR_BLACK);
            }

            if (panels.get(currentPoint) == COLOR_BLACK) {
                return 0;
            } else if (panels.get(currentPoint) == COLOR_WHITE) {
                return 1;
            } else {
                throw new IllegalStateException(panels.get(currentPoint) + "");
            }
        }

        private void paint(int color, int turnTo) {
            panels.put(currentPoint, color);

            if (currentDir == DIR_UP) {
                if (turnTo == DIR_LEFT) {
                    currentPoint = currentPoint.left();
                    currentDir = DIR_LEFT;
                } else if (turnTo == DIR_RIGHT) {
                    currentPoint = currentPoint.right();
                    currentDir = DIR_RIGHT;
                } else {
                    throw new IllegalStateException(turnTo + "");
                }
            } else if (currentDir == DIR_LEFT) {
                if (turnTo == DIR_LEFT) {
                    currentPoint = currentPoint.down();
                    currentDir = DIR_DOWN;
                } else if (turnTo == DIR_RIGHT) {
                    currentPoint = currentPoint.up();
                    currentDir = DIR_UP;
                } else {
                    throw new IllegalStateException(turnTo + "");
                }
            } else if (currentDir == DIR_DOWN) {
                if (turnTo == DIR_LEFT) {
                    currentPoint = currentPoint.right();
                    currentDir = DIR_RIGHT;
                } else if (turnTo == DIR_RIGHT) {
                    currentPoint = currentPoint.left();
                    currentDir = DIR_LEFT;
                } else {
                    throw new IllegalStateException(turnTo + "");
                }
            } else if (currentDir == DIR_RIGHT) {
                if (turnTo == DIR_LEFT) {
                    currentPoint = currentPoint.up();
                    currentDir = DIR_UP;
                } else if (turnTo == DIR_RIGHT) {
                    currentPoint = currentPoint.down();
                    currentDir = DIR_DOWN;
                } else {
                    throw new IllegalStateException(turnTo + "");
                }
            }
        }

        public void close() { }
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

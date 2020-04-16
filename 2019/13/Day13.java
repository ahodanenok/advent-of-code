import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.In;
import ahodanenok.aoc.intcode.Out;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2019/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");
        part1(program);
        part2(program);
    }

    private static void part1(long[] program) {
        Game game = new Game(program);
        System.out.println("Part 1: " + game.blockCount);
    }

    private static void part2(long[] program) {
        int score;
        do {
            // playing till we win :)
            Game game = new Game(program);
            game.start();
            score = game.score;
        } while (score == 0);
        System.out.println("Part 2: " + score);
    }

    private static class Game implements In, Out {

        private static final int TILE_EMPTY = 0;
        private static final int TILE_WALL = 1;
        private static final int TILE_BLOCK = 2;
        private static final int TILE_PADDLE = 3;
        private static final int TILE_BALL = 4;

        private IntcodeComputer pc;        
        private LinkedList<Integer> in = new LinkedList<>();

        private long move;

        private int score;
        private int scoreCount;

        private Point paddle;
        private Point ball;

        Point prevTarget;
        int prevTargetCount = 0;

        private Map<Point, Integer> tiles = new HashMap<>();
        private int blockCount;
        private int width = -1;
        private int height = -1;

        private boolean init;

        Game(long[] program) {
            this.pc = new IntcodeComputer(program);
            this.pc.memset(0, 2);
            this.pc.setIn(this);
            this.pc.setOut(this);

            init = true;
            IntcodeComputer initPc = new IntcodeComputer(program);
            initPc.setOut(this);
            initPc.run();
            init = false;

            width++;
            height++;
        }

        void start() {
            pc.run();
        }

        @Override
        public long read() {
            if (move > 2) System.out.println(move);
            return move;
        }

        @Override
        public void write(long n) {
            in.addLast((int) n);
            if (in.size() == 3) {
                int x = in.removeFirst();
                int y = in.removeFirst();
                int tile = in.removeFirst();

                if (init) {
                    width = Math.max(x, width);
                    height = Math.max(y, height);
                    tiles.put(new Point(x, y), tile);

                    if (tile == TILE_BLOCK) {
                        blockCount++;
                    } else if (tile == TILE_PADDLE) {
                        paddle = new Point(x, y);
                    } else if (tile == TILE_BALL) {
                        ball = new Point(x, y);
                    }

                    return;
                }

                if (x == -1 && y == 0) {
                    score = tile;
                    //System.out.println("Score " + scoreCount++ + ": " + score);
                    return;
                } 

                if (tile == TILE_PADDLE) {
                    paddle = new Point(x, y);
                }

                if (ball.x == paddle.x && ball.y == paddle.y - 1) {
                    if (prevTarget != null && prevTarget.equals(ball)) {
                        prevTargetCount++;
                    } else {
                        prevTargetCount = 0;
                    }

                    prevTarget = ball;
                    if (prevTargetCount > 100) {
                        move = -1 * (int) Math.floor(Math.random() * 3);
                        return;
                    }
                }

                if (tile == TILE_BALL && x == paddle.x && y == paddle.y - 1) {
                    ball = new Point(x, y);
                    move = -1 * (int) Math.floor(Math.random() * 3);
                    return;
                }

                int targetX = -1;
                if (tile == TILE_BALL) {
                    if (x - ball.x > 0 && y - ball.y > 0) { // SE
                        targetX = x + (paddle.y - y);
                        if (targetX >= width) {
                            targetX = width - (targetX - width) - 1;
                        }
                    } else if (x - ball.x < 0 && y - ball.y > 0) { // SW
                        targetX = x - (paddle.y - y);
                        if (targetX < 1) {
                            targetX = -1 * (targetX - 1) + 1;
                        }
                    }

                    ball = new Point(x, y);
                }

                if (targetX > 0 && targetX < width) {
                    if (paddle.x < targetX) {
                        move = 1;
                    } else if (paddle.x > targetX) {
                        move = -1;
                    } else {
                        move = 0;
                    }
                } else if (paddle.x < ball.x) {
                    move = 1;
                } else if (paddle.x > ball.x) {
                    move = -1;
                } else {
                    move = 0;
                }

                if (tile == TILE_BALL) {
                    //System.out.printf("bx=%d, by=%d, targetX=%d, paddleX=%d, paddleY=%d%n", ball.x, ball.y, targetX, paddle.x, paddle.y);
                }

                tiles.put(new Point(x, y), tile);
            }
        }

        public void print() {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int tile = tiles.get(new Point(x, y));
                    if (tile == 0) { // empty
                        System.out.print(" ");
                    } else if (tile == 1) { // wall
                        System.out.print("#");
                    } else if (tile == 2) { // block
                        System.out.print("=");
                    } else if (tile == 3) { // horizontal paddle
                        System.out.print("-");
                    } else if (tile == 4) {
                        System.out.print("o");
                    } else {
                        throw new IllegalStateException();
                    }
                }

                System.out.println();
            }
        }

        @Override
        public void close() { }
    }

    private static class Point {

        private final int x;
        private final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return other.x == x && other.y == y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}

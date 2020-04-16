import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.Out;
import ahodanenok.aoc.intcode.WIn;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2019/day/9
 */
public class Day9 {

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");
        part1(program);
        part2(program);
    }

    private static void part1(long[] program) {
        IntcodeComputer pc = new IntcodeComputer(program);
        pc.setIn(new WIn(1));
        pc.setOut(new Out() {
            @Override
            public void write(long n) {
                System.out.println("Part 1: " + n);
            }
        });
        pc.run();
    }

    private static void part2(long[] program) {
        IntcodeComputer pc = new IntcodeComputer(program);
        pc.setIn(new WIn(2));
        pc.setOut(new Out() {
            @Override
            public void write(long n) {
                System.out.println("Part 2: " + n);
            }
        });
        pc.run();
    }
}

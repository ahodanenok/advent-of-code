import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.WIn;
import ahodanenok.aoc.intcode.Out;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2019/day/5  
 */
public class Day5 {

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");

        IntcodeComputer pc_1 = new IntcodeComputer(program);
        pc_1.setIn(new WIn(1));
        pc_1.setOut(new Out() {
            @Override
            public void write(long n) {
                if (n > 0) {
                    System.out.println("Part 1: " + n);
                }
            }
        });
        pc_1.run();

        IntcodeComputer pc_2 = new IntcodeComputer(program);
        pc_2.setIn(new WIn(5));
        pc_2.setOut(new Out() {
            @Override
            public void write(long n) {
                System.out.println("Part 2: " + n);
            }
        });
        pc_2.run();
    } 
}

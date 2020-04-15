import ahodanenok.aoc.intcode.IntcodeComputer;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2019/day/2  
 */
public class Day2 {

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");

        IntcodeComputer pc_1 = new IntcodeComputer(program);
        pc_1.memset(1, 12);
        pc_1.memset(2, 2);
        pc_1.run();
        System.out.println("Part 1: " + pc_1.memread(0));

        search:
        for (int a = 0; a <= 99; a++) {
            for (int b = 0; b <= 99; b++) {
                IntcodeComputer pc_2 = new IntcodeComputer(program);
                pc_2.memset(1, a);
                pc_2.memset(2, b);
                pc_2.run();
                if (pc_2.memread(0) == 19690720) {
                    System.out.println("Part 2: " + (100 * pc_2.memread(1) + pc_2.memread(2)));
                    break search;
                }
            }
        } 
    }
}

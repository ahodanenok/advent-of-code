import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.WIn;
import ahodanenok.aoc.intcode.Out;

import java.util.Scanner;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2019/day/21
 */
public class Day21 {

    /**
     * Part 1:
     *   NOT A T
     *   NOT B J
     *   OR T J
     *   NOT C T
     *   OR T J
     *   NOT D T
     *   NOT T T
     *   AND T J
     *   WALK
     */

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");

        WIn in = new WIn();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String cmd = scanner.nextLine().toUpperCase();
            for (char ch : cmd.toCharArray()) {
                in.add(ch);
            } 

            in.add('\n');
            if ("WALK".equals(cmd)) {
                break;
            }
        }

        IntcodeComputer pc = new IntcodeComputer(program);
        pc.setIn(in);
        pc.setOut(new Out() {
            @Override
            public void write(long n) {
                if (n > 0xFF) {
                    System.out.println("Part 1: " + n);
                } else {
                    System.out.print((char) n);
                }
            } 
        });
        pc.run();
    }
}


import ahodanenok.aoc.intcode.*;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2019/day/25
 */
public class Day25 {

    /**
     * fixed point
     * sand
     * wreath
     * space law space brochure
     */

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");
        IntcodeComputer pc = new IntcodeComputer(program);
        pc.setIn(new In() {

            LinkedList<Character> chars = new LinkedList<>();
            Scanner scanner = new Scanner(System.in);

            @Override
            public long read() {
                if (chars.isEmpty()) {
                    String in = scanner.nextLine();
                    for (int i = 0; i < in.length(); i++) {
                        chars.addLast(in.charAt(i));
                    }
                    chars.addLast('\n');
                } 

                return chars.removeFirst();
            }
        });
        pc.setOut(new Out() {
            @Override
            public void write(long n) {
                System.out.print((char) n);
            }
        });
        pc.run();
    }
}


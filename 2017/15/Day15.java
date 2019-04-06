/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2017/day/15
 */
public class Day15 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        Generator a = new Generator(116, 16807);
        Generator b = new Generator(299, 48271);

        int count = 0;
        for (int i = 0; i < 40_000_000; i++) {
            if ((a.next() & 0xFFFF) == (b.next() & 0xFFFF)) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static void part2() {
        Generator a = new Generator(116, 16807, 4);
        Generator b = new Generator(299, 48271, 8);

        int count = 0;
        for (int i = 0; i < 5_000_000; i++) {
            if ((a.next() & 0xFFFF) == (b.next() & 0xFFFF)) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static class Generator {

        private long divider = 2147483647;

        private long prev;
        private long factor;
        private long multiplierCriteria;

        Generator(long initital, long factor) {
            this(initital, factor, 1);
        }

        Generator(long initital, long factor, long multiplierCriteria) {
            this.prev = initital;
            this.factor = factor;
            this.multiplierCriteria = multiplierCriteria;
        }

        long next() {
            long result;
            do {
                result = (prev * factor) % divider;
                prev = result;
            } while (result % multiplierCriteria != 0);
            return result;
        }
    }
}

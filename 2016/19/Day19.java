import java.util.Arrays;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2016/day/19
 */
public class Day19 {

    public static void main(String[] args) {
        part1(3004953);
        part2(3004953);
    }

    private static void part1(int elfsCount) {
        Elf[] elfs = new Elf[elfsCount];
        elfs[0] = new Elf(0);
        for (int i = 1; i < elfs.length; i++) {
            elfs[i] = new Elf(i);
            elfs[i - 1].next = elfs[i];
        }
        elfs[elfs.length - 1].next = elfs[0];

        Elf n = elfs[0];
        while (n.next != n) {
            n.next = n.next.next;
            n = n.next;
        }

        System.out.println(n.idx + 1);
    }

    private static void part2(int elfsCount) {
        Elf[] elfs = new Elf[elfsCount];
        elfs[0] = new Elf(0);
        for (int i = 1; i < elfs.length; i++) {
            elfs[i] = new Elf(i);
            elfs[i - 1].next = elfs[i];
        }
        elfs[elfs.length - 1].next = elfs[0];

        int elfsLeft = elfs.length;
        Elf n = elfs[0];
        while (n.next != n) {
            int skip = elfsLeft / 2;
            Elf exch = n;
            while (skip > 1) {
                exch = exch.next;
                skip--;
            }

            exch.next = exch.next.next;
            n = n.next;
            elfsLeft--;

            //System.out.println(elfsLeft);
            if (elfsLeft % 1000 == 0) {
                System.out.println(elfsLeft);
            }
        }

        System.out.println(n.idx + 1);
    }

    private static class Elf {

        Elf next;
        int idx;

        Elf(int idx) {
            this.idx = idx;
        }

        @Override
        public String toString() {
            String str = idx + " -> ";
            Elf n = next;
            while (n != this) {
                str += n.idx + " -> ";
                n = n.next;
            }

            return str;
        }
    }
}

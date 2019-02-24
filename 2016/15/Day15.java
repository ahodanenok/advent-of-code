/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2016/day/15
 */
public class Day15 {

    public static void main(String[] args) {
        part1();
    }

    private static void part1() {
        Disc[] discs = new Disc[] {
            new Disc(1, 17),
            new Disc(0, 7),
            new Disc(2, 19),
            new Disc(0, 5),
            new Disc(0, 3),
            new Disc(5, 13)
        };

        int time = 0;
        boolean passed = false;
        while (!passed) {
            for (Disc d : discs) {
                d.reset();
            }

            time++;
            passed = simulate(time, discs);
        }

        System.out.println(time);
    }

    public static boolean simulate(int time,Disc[] discs) {
        for (int i = 0; i < discs.length; i++) {
            discs[i].move(time + i + 1);
        }

        for (Disc d : discs) {
            if (d.currentPos != 0) {
                return false;
            }
        }

        return true;
    }

    private static class Disc {

        private final int initialPos;
        private final int positions;

        private int currentPos;

        Disc(int initialPos, int positions) {
            this.initialPos = initialPos;
            this.currentPos = initialPos;
            this.positions = positions;
        }

        void move(int time) {
            currentPos = (currentPos + time) % positions;
        }

        void reset() {
            currentPos = initialPos;
        }
    }
}

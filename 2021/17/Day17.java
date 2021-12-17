/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2021/day/17
 */
public class Day17 {

    public static void main(String[] args) throws Exception {
        //target area: x=20..30, y=-10..-5
        //Target target = new Target(20, -10, 30, -5);

        //target area: x=209..238, y=-86..-59
        Target target = new Target(209, -86, 238, -59);
        part1(target);
    }

    private static void part1(Target target) {
        int ayMax = Math.abs(Math.min(target.yStart, target.yEnd));
        int axMax = Math.min(target.xStart, target.xEnd);

        int yMax = Integer.MIN_VALUE;
        for (int ay = 1; ay < ayMax; ay++) {
            for (int ax = 1; ax < axMax; ax++) {
                int y = launchWithStyle(ax, ay, target);
                if (y != -1) {
                    yMax = Math.max(y, yMax);
                }
            }
        }

        System.out.println("Part 1: " + yMax);
    }

    private static int launchWithStyle(int ax, int ay, Target target) {
        int x = 0;
        int y = 0;
        int dax = ax > 0 ? 1 : -1;
        int yMax = y;

        while (x < target.xEnd && y > target.yEnd) {
            x += ax;
            y += ay;
            yMax = Math.max(y, yMax);

            if (x >= target.xStart && x <= target.xEnd && y >= target.yStart && y <= target.yEnd) {
                return yMax;
            }

            if (ax != 0) {
                ax -= dax;
            }

            ay--;
        }

        return -1;
    }

    private static class Target {

        final int xStart;
        final int yStart;
        final int xEnd;
        final int yEnd;

        Target(int xStart, int yStart, int xEnd, int yEnd) {
            this.xStart = xStart;
            this.yStart = yStart;
            this.xEnd = xEnd;
            this.yEnd = yEnd;
        }
    }
}

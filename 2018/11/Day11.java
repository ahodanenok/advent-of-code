/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2018/day/11
 */
public class Day11 {

    public static void main(String[] args) {
        part1(7803);
    }

    private static void part1(int number) {
        Grid grid = new Grid(300, number);

        int sy = 0;
        int sx = 0;
        int maxPower = Integer.MIN_VALUE;
        for (int y = 0; y < grid.getSize(); y++) {
            for (int x = 0; x < grid.getSize(); x++) {
                int power = grid.getSquarePowerLevel(x, y);
                if (power > maxPower) {
                    sy = y;
                    sx = x;
                    maxPower = power;
                }
            }
        }

        System.out.println(sx + "," + sy);
    }

    private static class Grid {

        private int[][] grid;
        private int number;

        Grid(int size, int number) {
            this.grid = new int[size][size];
            this.number = number;
        }

        int getSize() {
            return grid.length;
        }

        int getSquarePowerLevel(int topX, int topY) {
            if (topY + 3 >= getSize() || topX + 3 >= getSize()) {
                return Integer.MIN_VALUE;
            }

            int power = 0;
            for (int y = topY; y < topY + 3; y++) {
                for (int x = topX; x < topX + 3; x++) {
                    power += getCellPowerLevel(x, y);
                }
            }

            return power;
        }

        private int getCellPowerLevel(int x, int y) {
            int rackId = x + 10;
            int power = rackId * y;
            power += number;
            power *= rackId;
            power = (power % 1000) / 100;
            return power - 5;
        }
    }
}

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2018/day/11
 */
public class Day11 {

    public static void main(String[] args) {
        Grid grid = new Grid(300, 7803);
        part1(grid);
        part2(grid);
    }

    private static void part1(Grid grid) {
        int topY = 0;
        int topX = 0;
        int maxPower = Integer.MIN_VALUE;
        for (int y = 0; y < grid.getSize(); y++) {
            for (int x = 0; x < grid.getSize(); x++) {
                int power = grid.getSquarePowerLevel(x, y, 3);
                if (power > maxPower) {
                    topX = x;
                    topY = y;
                    maxPower = power;
                }
            }
        }

        System.out.println(topX + "," + topY);
    }

    private static void part2(Grid grid) {
        int topY = 0;
        int topX = 0;
        int squareSize = 1;
        int maxPower = Integer.MIN_VALUE;
        for (int y = 0; y < grid.getSize(); y++) {
            for (int x = 0; x < grid.getSize(); x++) {
                for (int s = 1; s < grid.getSize(); s++) {
                    int power = grid.getSquarePowerLevel(x, y, s);
                    if (power > maxPower) {
                        topX = x;
                        topY = y;
                        squareSize = s;
                        maxPower = power;
                    }
                }
            }
        }

        System.out.println(topX + "," + topY + "," + squareSize);
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

        int getSquarePowerLevel(int topX, int topY, int squareSize) {
            if (topY + squareSize >= getSize() || topX + squareSize >= getSize()) {
                return Integer.MIN_VALUE;
            }

            int power = 0;
            for (int y = topY; y < topY + squareSize; y++) {
                for (int x = topX; x < topX + squareSize; x++) {
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

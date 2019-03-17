/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2017/day/3
 */
public class Day3 {

    public static void main(String[] args) {
        part1(277678);
    }

    private static void part1(int toCell) {
        Memory m = new Memory();
        Cell cell = null;
        do {
            cell = m.nextCell();
        } while (cell.num != toCell);

        System.out.println(Math.abs(cell.row) + Math.abs(cell.col));
    }

    private static class Memory {

        private static final Dir[] directions = new Dir[] { Dir.BOTTOM, Dir.RIGHT, Dir.TOP, Dir.LEFT };

        private int row;
        private int col;
        private int cellNum;
        private int sideLength;
        private int dirLength;
        private int dirIdx;

        Memory() {
            this.row = 0;
            this.col = 0;
            this.cellNum = 1;
            this.sideLength = 0;
            this.dirLength = 0;
            this.dirIdx = 0;
        }

        Cell nextCell() {
            if (dirLength == 0) {
                dirIdx = (dirIdx + 1) % directions.length;
                sideLength += directions[dirIdx].sideIncrement;
                dirLength = sideLength;
            }

            Dir dir = directions[dirIdx];
            row += dir.rowOffset;
            col += dir.colOffset;
            dirLength--;
            cellNum++;

            return new Cell(cellNum, row, col);
        }
    }

    private enum Dir {
        TOP(-1, 0, 0),
        LEFT(0, -1, 1),
        BOTTOM(1, 0, 0),
        RIGHT(0, 1, 1);

        private final int rowOffset;
        private final int colOffset;
        private final int sideIncrement;

        Dir(int rowOffset, int colOffset, int sideIncrement) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
            this.sideIncrement = sideIncrement;
        }
    }

    private static class Cell {

        private final int num;
        private final int row;
        private final int col;

        Cell(int num, int row, int col) {
            this.num = num;
            this.row = row;
            this.col = col;
        }
    }
}

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2016/day/8
 */
public class Day8 {

    public static void main(String[] args) throws Exception {
        List<Instruction> instructions = getInstructions();
        part1(instructions);
        part2(instructions);
    }

    private static void part1(List<Instruction> instructions) {
        Grid grid = new Grid(50, 6);
        for (Instruction inst : instructions) {
            inst.execute(grid);
        }

        int litCount = 0;
        for (int row = 0; row < grid.getHeight(); row++) {
            for (int col = 0; col < grid.getWidth(); col++) {
                if (grid.isOn(row, col)) {
                    litCount++;
                }
            }
        }

        System.out.println(litCount);
    }

    private static void part2(List<Instruction> instructions) {
        Grid grid = new Grid(50, 6);
        for (Instruction inst : instructions) {
            inst.execute(grid);
        }

        grid.print();
    }

    private static List<Instruction> getInstructions() {
        List<Instruction> instructions = new ArrayList<Instruction>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            if ("rect".equals(parts[0])) {
                String[] coords = parts[1].split("x");
                instructions.add(new RectInstruction(Integer.parseInt(coords[1]), Integer.parseInt(coords[0])));
            } else if ("rotate".equals(parts[0])) {
                if ("row".equals(parts[1])) {
                    instructions.add(new RotateRowInstruction(Integer.parseInt(parts[2].split("=")[1]), Integer.parseInt(parts[4])));
                } else if ("column".equals(parts[1])) {
                    instructions.add(new RotateColInstruction(Integer.parseInt(parts[2].split("=")[1]), Integer.parseInt(parts[4])));
                } else {
                    throw new IllegalArgumentException("Unknown instruction: " + line);
                }
            } else {
                throw new IllegalArgumentException("Unknown instruction: " + line);
            }
        }

        return instructions;
    }

    private static class Grid {

        private int width;
        private int height;
        private boolean[][] cells;

        Grid(int width, int height) {
            this.width = width;
            this.height = height;
            this.cells = new boolean[height][width];
        }

        int getHeight() {
            return height;
        }

        int getWidth() {
            return width;
        }

        boolean isOn(int row, int col) {
            return cells[row][col];
        }

        void turnOn(int row, int col) {
            this.cells[row][col] = true;
        }

        void turnOff(int row, int col) {
            this.cells[row][col] = false;
        }

        void print() {
            for (int row = 0; row < getHeight(); row++) {
                for (int col = 0; col < getWidth(); col++) {
                    if (isOn(row, col)) {
                        System.out.print("#");
                    } else {
                        System.out.print(".");
                    }
                }
                System.out.println();
            }
        }
    }

    private interface Instruction {
        void execute(Grid grid);
    }

    private static class RectInstruction implements Instruction {

        private int rows;
        private int cols;

        RectInstruction(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        public void execute(Grid grid) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    grid.turnOn(row, col);
                }
            }
        }
    }

    private static class RotateRowInstruction implements Instruction {

        private int row;
        private int offset;

        RotateRowInstruction(int row, int offset) {
            this.row = row;
            this.offset = offset;
        }

        public void execute(Grid grid) {
            boolean[] rowValues = new boolean[grid.getWidth()];
            int rowIdx = 0;
            int move = offset % grid.getWidth();
            for (int col = grid.getWidth() - move; col < grid.getWidth(); col++) {
                rowValues[rowIdx++] = grid.isOn(row, col);
            }
            for (int col = 0; col < grid.getWidth() - move; col++) {
                rowValues[rowIdx++] = grid.isOn(row, col);
            }

            for (int col = 0; col < grid.getWidth(); col++) {
                if (rowValues[col]) {
                    grid.turnOn(row, col);
                } else {
                    grid.turnOff(row, col);
                }
            }
        }
    }

    private static class RotateColInstruction implements Instruction {

        private int col;
        private int offset;

        RotateColInstruction(int col, int offset) {
            this.col = col;
            this.offset = offset;
        }

        public void execute(Grid grid) {
            boolean[] column = new boolean[grid.getHeight()];
            int columnIdx = 0;
            int move = offset % grid.getHeight();
            for (int row = grid.getHeight() - move; row < grid.getHeight(); row++) {
                column[columnIdx++] = grid.isOn(row, col);
            }
            for (int row = 0; row < grid.getHeight() - move; row++) {
                column[columnIdx++] = grid.isOn(row, col);
            }

            for (int row = 0; row < grid.getHeight(); row++) {
                if (column[row]) {
                    grid.turnOn(row, col);
                } else {
                    grid.turnOff(row, col);
                }
            }
        }
    }
}

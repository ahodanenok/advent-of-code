import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2015/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        List<Instruction> instructions = getInstructions();
        part1(instructions);
    }

    private static void part1(List<Instruction> instructions) {
        boolean[][] grid = new boolean[1000][1000];
        for (Instruction instruction : instructions) {
            for (int row = instruction.startRow; row <= instruction.endRow; row++) {
                for (int col = instruction.startCol; col <= instruction.endCol; col++) {
                    if (instruction.action == Instruction.ON) {
                        grid[row][col] = true;
                    } else if (instruction.action == Instruction.OFF) {
                        grid[row][col] = false;
                    } else if (instruction.action == Instruction.TOGGLE) {
                        grid[row][col] = !grid[row][col];
                    }
                }
            }
        }

        int onCount = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
                if (grid[row][col]) {
                    onCount++;
                }
            }
        }

        System.out.println(onCount);
    }

    private static List<Instruction> getInstructions() {
        List<Instruction> instructions = new ArrayList<Instruction>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            int action;
            String start;
            String end;
            if ("turn".equals(parts[0])) {
                if ("on".equals(parts[1])) {
                    action = Instruction.ON;
                } else if ("off".equals(parts[1])) {
                    action = Instruction.OFF;
                } else {
                    throw new IllegalArgumentException("Unknown instruction: " + parts[1]);
                }

                start = parts[2];
                end = parts[4];
            } else if ("toggle".equals(parts[0])) {
                action = Instruction.TOGGLE;
                start = parts[1];
                end = parts[3];
            } else {
                throw new IllegalArgumentException("Unknown instruction: " + parts[0]);
            }

            instructions.add(new Instruction(
                action,
                Integer.parseInt(start.split(",")[1]), 
                Integer.parseInt(end.split(",")[1]),
                Integer.parseInt(start.split(",")[0]),
                Integer.parseInt(end.split(",")[0])));
        }

        return instructions;
    }

    private static class Instruction {

        static final int OFF = 0;
        static final int ON = 1;
        static final int TOGGLE = 2;

        private final int action;
        private final int startRow;
        private final int endRow;
        private final int startCol;
        private final int endCol;

        Instruction(int action, int startRow, int endRow, int startCol, int endCol) {
            this.action = action;
            this.startRow = startRow;
            this.endRow = endRow;
            this.startCol = startCol;
            this.endCol = endCol;
        }
    }
}

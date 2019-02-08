import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2016/day/2
 */
public class Day2 {

    public static void main(String[] args) {
        List<String> instructions = getInstructions();
        part1(instructions);
    }

    private static void part1(List<String> instructions) {
        char[][] grid = new char[][] {
            { '\0', '\0', '\0', '\0', '\0' },
            { '\0',  '1',  '2',  '3', '\0' },
            { '\0',  '4',  '5',  '6', '\0' },
            { '\0',  '7',  '8',  '9', '\0' },
            { '\0', '\0', '\0', '\0', '\0' }
        };

        System.out.println(getCode(grid, 2, 2, instructions));
    }

    private static String getCode(char[][] grid, int row, int col, List<String> instructions) {
        String code = "";
        int currentRow = row;
        int currentCol = col;

        for (String instruction : instructions) {
            for (int i = 0; i < instruction.length(); i++) {
                char dir = instruction.charAt(i);
                //System.out.print("dir=" + dir + ", BEFORE: row=" + currentRow + ", col=" + currentCol);
                if (dir == 'U') {
                    if (grid[currentRow - 1][currentCol] != '\0') currentRow--;
                } else if (dir == 'D') {
                    if (grid[currentRow + 1][currentCol] != '\0') currentRow++;
                } else if (dir == 'L') {
                    if (grid[currentRow][currentCol - 1] != '\0') currentCol--;
                } else if (dir == 'R') {
                    if (grid[currentRow][currentCol + 1] != '\0') currentCol++;
                } else {
                    throw new IllegalArgumentException("Unknown direction: " + dir);
                }
                //System.out.println(", AFTER: row=" + currentRow + ", col=" + currentCol);
            }

            code += grid[currentRow][currentCol];
        }

        return code;
    }

    private static List<String> getInstructions() {
        List<String> instructions = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            instructions.add(scanner.nextLine().trim());
        }

        return instructions;
    }
}

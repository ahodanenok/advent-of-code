import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2025/day/4
 */
public class Day4 {

    public static void main(String... args) throws Exception {
        Set<Position> rolls = getInput();
        part1(rolls);
    }

    private static Set<Position> getInput() throws Exception {
        Set<Position> rolls = new HashSet<>();
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == '@') {
                        rolls.add(new Position(row, col));
                    }
                }
                
                row++;
            }
        }

        return rolls;
    }

    private static void part1(Set<Position> rolls) {
        int accessibleCount = 0;
        for (Position roll : rolls) {
            int rollsNearby = 0;
            if (rolls.contains(new Position(roll.row - 1, roll.col))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row - 1, roll.col + 1))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row, roll.col + 1))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row + 1, roll.col + 1))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row + 1, roll.col))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row + 1, roll.col - 1))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row, roll.col - 1))) {
                rollsNearby++;
            }
            if (rolls.contains(new Position(roll.row - 1, roll.col - 1))) {
                rollsNearby++;
            }
            
            if (rollsNearby < 4) {
                accessibleCount++;
            }
        }

        System.out.println("Part 1: " + accessibleCount);
    }

    private static class Position {

        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return row == other.row && col == other.col;
        }
    }
}

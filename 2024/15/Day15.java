import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2024/day/15
 */
public class Day15 {

    public static void main(String... args) throws Exception {
        Input input = getInput();
        part1(input);
    }

    private static void part1(Input input) {
        Position robot = input.robot;
        Set<Position> boxes = new HashSet<>(input.boxes);
        for (int i = 0; i < input.moves.length(); i++) {
            robot = switch (input.moves.charAt(i)) {
                case '^' -> move(robot, boxes, input.walls, -1,  0);
                case 'v' -> move(robot, boxes, input.walls,  1,  0);
                case '<' -> move(robot, boxes, input.walls,  0, -1);
                case '>' -> move(robot, boxes, input.walls,  0,  1);
                default -> robot;
            };
        }

        int sum = 0;
        for (Position box : boxes) {
            sum += 100 * box.row + box.col;
        }

        System.out.println("Part 1: " + sum);
    }

    private static Position move(Position robot, Set<Position> boxes, Set<Position> walls, int dy, int dx) {
        Position nextPosition = new Position(robot.row + dy, robot.col + dx);
        if (walls.contains(nextPosition)) {
            return robot;
        }

        if (boxes.contains(nextPosition)) {
            Position shiftPosition = nextPosition;
            while (boxes.contains(shiftPosition)) {
                shiftPosition = new Position(shiftPosition.row + dy, shiftPosition.col + dx);
            }

            if (walls.contains(shiftPosition)) {
                return robot;
            }

            boxes.remove(nextPosition);
            boxes.add(shiftPosition);
        }

        return nextPosition;
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Position robot = null;
            Set<Position> boxes = new HashSet<>();
            Set<Position> walls = new HashSet<>();

            int row = 0;
            String line;
            while (!(line = reader.readLine()).isBlank()) {
                line = line.trim();
                for (int col = 0; col < line.length(); col++) {
                    switch (line.charAt(col)) {
                        case '#' -> walls.add(new Position(row, col));
                        case 'O' -> boxes.add(new Position(row, col));
                        case '@' -> robot = new Position(row, col);
                        case '.' -> {}
                        default -> throw new IllegalStateException();
                    }
                }

                row++;
            }

            StringBuilder movesBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                movesBuilder.append(line.trim());
            }

            return new Input(robot, boxes, walls, movesBuilder.toString());
        }
    }

    private static class Input {

        final Position robot;
        final Set<Position> boxes;
        final Set<Position> walls;
        final String moves;

        Input(Position robot, Set<Position> boxes, Set<Position> walls, String moves) {
            this.robot = robot;
            this.boxes = boxes;
            this.walls = walls;
            this.moves = moves;
        }
    }

    private static class Position {

        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}

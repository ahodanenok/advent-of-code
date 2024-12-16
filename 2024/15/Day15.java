import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2024/day/15
 */
public class Day15 {

    public static void main(String... args) throws Exception {
        Input input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(Input input) {
        Position robot = input.robot;
        Set<Position> boxes = new HashSet<>(input.boxes);
        for (int i = 0; i < input.moves.length(); i++) {
            robot = switch (input.moves.charAt(i)) {
                case '^' -> move1(robot, boxes, input.walls, -1,  0);
                case 'v' -> move1(robot, boxes, input.walls,  1,  0);
                case '<' -> move1(robot, boxes, input.walls,  0, -1);
                case '>' -> move1(robot, boxes, input.walls,  0,  1);
                default -> robot;
            };
        }

        int sum = 0;
        for (Position box : boxes) {
            sum += 100 * box.row + box.col;
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(Input input) {
        Position robot = new Position(input.robot.row, input.robot.col * 2);

        Map<Position, Position> boxes = new HashMap<>();
        for (Position box : input.boxes) {
            Position left = new Position(box.row, box.col * 2);
            Position right = new Position(box.row, box.col * 2 + 1);
            boxes.put(left, right);
            boxes.put(right, left);
        }

        Set<Position> walls = new HashSet<>();
        for (Position wall : input.walls) {
            walls.add(new Position(wall.row, wall.col * 2));
            walls.add(new Position(wall.row, wall.col * 2 + 1));
        }

        for (int i = 0; i < input.moves.length(); i++) {
            robot = switch (input.moves.charAt(i)) {
                case '^' -> move2(robot, boxes, walls, -1,  0);
                case 'v' -> move2(robot, boxes, walls,  1,  0);
                case '<' -> move2(robot, boxes, walls,  0, -1);
                case '>' -> move2(robot, boxes, walls,  0,  1);
                default -> robot;
            };
        }

        int sum = 0;
        for (Map.Entry<Position, Position> box : boxes.entrySet()) {
            sum += 100 * box.getKey().row + Math.min(box.getKey().col, box.getValue().col);
        }

        System.out.println("Part 2: " + (sum / 2));
    }

    private static Position move1(Position robot, Set<Position> boxes, Set<Position> walls, int dy, int dx) {
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

    private static Position move2(Position robot, Map<Position, Position> boxes, Set<Position> walls, int dy, int dx) {
        Position nextPosition = new Position(robot.row + dy, robot.col + dx);
        if (walls.contains(nextPosition)) {
            return robot;
        }

        if (boxes.containsKey(nextPosition)) {
            Set<Position> currentShiftPositions = Set.of(nextPosition, boxes.get(nextPosition));
            Set<Position> allShiftPositions = new HashSet<>(currentShiftPositions);
            while (true) {
                Set<Position> nextShiftPositions = new HashSet<>();
                for (Position shiftPosition : currentShiftPositions) {
                    Position position = new Position(shiftPosition.row + dy, shiftPosition.col + dx);
                    if (currentShiftPositions.contains(position)) {
                        continue;
                    }

                    if (walls.contains(position)) {
                        return robot;
                    }

                    if (boxes.containsKey(position)) {
                        nextShiftPositions.add(position);
                        nextShiftPositions.add(boxes.get(position));
                    }
                }

                if (nextShiftPositions.isEmpty()) {
                    break;
                }

                allShiftPositions.addAll(nextShiftPositions);
                currentShiftPositions = nextShiftPositions;
            }

            Map<Position, Position> movedBoxes = new HashMap<>();
            for (Position shiftPosition : allShiftPositions) {
                if (!boxes.containsKey(shiftPosition)) {
                    continue;
                }

                Position linkedPosition = boxes.get(shiftPosition);
                Position left = new Position(shiftPosition.row + dy, shiftPosition.col + dx);
                Position right = new Position(linkedPosition.row + dy, linkedPosition.col + dx);
                movedBoxes.put(left, right);
                movedBoxes.put(right, left);
                boxes.remove(boxes.remove(shiftPosition));
            }

            for (Map.Entry<Position, Position> entry : movedBoxes.entrySet()) {
                if (boxes.containsKey(entry.getKey()) || boxes.containsKey(entry.getValue())) {
                    throw new IllegalStateException();
                }
            }

            boxes.putAll(movedBoxes);
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

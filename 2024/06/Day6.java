import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2024/day/6
 */
public class Day6 {

    public static void main(String... args) throws Exception {
        LabMap map = getInput();
        part1(map);
        part2(map);
    }

    private static void part1(LabMap map) {
        System.out.println("Part 1: " + getVisitedPositions(map).size());
    }

    private static void part2(LabMap map) {
        int stuckCount = 0;
        Set<Position> obstructions = new HashSet<>(map.obstructions);
        for (Position newObstructionPosition : getVisitedPositions(map)) {
            if (newObstructionPosition.equals(map.guardPosition)) {
                continue;
            }

            obstructions.add(newObstructionPosition);
            if (isLoopStuck(new LabMap(map.height, map.width,
                    map.guardPosition, map.guardDirection, obstructions))) {
                stuckCount++;
            }
            obstructions.remove(newObstructionPosition);
        }

        System.out.println("Part 2: " + stuckCount);
    }

    private static boolean isLoopStuck(LabMap map) {
        Set<VisitedObstruction> visitedObstructions = new HashSet<>();
        boolean[] isLoop = new boolean[1];
        trace(map, (position, direction) -> {
            Position nextPosition = direction.move(position);
            if (map.obstructions.contains(nextPosition)
                    && !visitedObstructions.add(new VisitedObstruction(nextPosition, direction))) {
                isLoop[0] = true;
                return false;
            }

            return true;
        });

        return isLoop[0];
    }

    private static Set<Position> getVisitedPositions(LabMap map) {
        Set<Position> visitedPositions = new HashSet<>();
        trace(map, (position, __) -> {
            visitedPositions.add(position);
            return true;
        });

        return visitedPositions;
    }

    private static void trace(LabMap map, BiFunction<Position, Direction, Boolean> tracer) {
        Position guardPosition = map.guardPosition;
        Direction guardDirection = map.guardDirection;
        while (guardPosition.row >= 0 && guardPosition.row < map.height
                && guardPosition.col >= 0 && guardPosition.col < map.width) {
            if (!tracer.apply(guardPosition, guardDirection)) {
                break;
            }

            Position nextPosition = guardDirection.move(guardPosition);
            if (map.obstructions.contains(nextPosition)) {
                nextPosition = guardDirection.move(guardPosition);
                if (map.obstructions.contains(nextPosition)) {
                    guardDirection = guardDirection.turnRight();
                } else {
                    guardDirection = guardDirection.turnRight();
                    guardPosition = nextPosition;
                }
            } else {
                guardPosition = nextPosition;
            }
        }
    }

    private static LabMap getInput() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));

        int height = lines.size();
        int width = lines.get(0).length();
        Position guardPosition = null;
        Set<Position> obstructions = new HashSet<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                switch (lines.get(row).charAt(col)) {
                    case '#' -> obstructions.add(new Position(row, col));
                    case '^' -> guardPosition = new Position(row, col);
                }
            }
        }

        return new LabMap(height, width,
            guardPosition, Direction.UP, obstructions);
    }

    private static class LabMap {

        final int height;
        final int width;
        final Position guardPosition;
        final Direction guardDirection;
        final Set<Position> obstructions;

        LabMap(int height, int width,
                Position guardPosition,
                Direction guardDirection,
                Set<Position> obstructions) {
            this.height = height;
            this.width = width;
            this.guardPosition = guardPosition;
            this.guardDirection = guardDirection;
            this.obstructions = obstructions;
        }
    }

    private static class VisitedObstruction {

        final Position position;
        final Direction direction;

        VisitedObstruction(Position position, Direction direction) {
            this.position = position;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object obj) {
            VisitedObstruction other = (VisitedObstruction) obj;
            return position.equals(other.position) && direction == other.direction;
        }

        @Override
        public int hashCode() {
            return 31 * position.hashCode() + direction.hashCode();
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

    private enum Direction {

        UP {
            @Override
            Direction turnRight() {
                return Direction.RIGHT;
            }

            @Override
            Position move(Position pos) {
                return new Position(pos.row - 1, pos.col);
            }
        },
        DOWN {
            @Override
            Direction turnRight() {
                return Direction.LEFT;
            }

            @Override
            Position move(Position pos) {
                return new Position(pos.row + 1, pos.col);
            }
        },
        LEFT {
            @Override
            Direction turnRight() {
                return Direction.UP;
            }

            @Override
            Position move(Position pos) {
                return new Position(pos.row, pos.col - 1);
            }
        },
        RIGHT {
            @Override
            Direction turnRight() {
                return Direction.DOWN;
            }

            @Override
            Position move(Position pos) {
                return new Position(pos.row, pos.col + 1);
            }
        };

        abstract Direction turnRight();
        abstract Position move(Position pos);
    }
}

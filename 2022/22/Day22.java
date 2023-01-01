import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2022/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        Notes notes = getNotes();
        part1(notes);
        part2(notes);
    }

    private static void part1(Notes notes) {
        Map map = notes.map;
        System.out.println("Part 1: " + move(notes, new MovingStrategy() {

            @Override
            public Location next(Location location) {
                int x = location.x;
                int y = location.y;
                do {
                    x = (map.width + x + location.dir.dx) % map.width;
                    y = (map.height + y + location.dir.dy) % map.height;
                } while (map.isVoid(x, y));

                return new Location(x, y, location.dir);
            }
        }));
    }

    private static void part2(Notes notes) {
        Map map = notes.map;

        HashMap<Side, Side> sides = new HashMap<>();

        //int sideLength = 4;
        //sides.put(new Side(1, 1, Direction.UP), new Side(2, 0, Direction.LEFT));
        //sides.put(new Side(2, 1, Direction.RIGHT), new Side(3, 2, Direction.UP));
        //sides.put(new Side(2, 2, Direction.DOWN), new Side(0, 1, Direction.DOWN));

        int sideLength = 50;
        sides.put(new Side(1, 0, Direction.UP), new Side(0, 3, Direction.LEFT));
        sides.put(new Side(1, 0, Direction.LEFT), new Side(0, 2, Direction.LEFT));
        sides.put(new Side(2, 0, Direction.UP), new Side(0, 3, Direction.DOWN));
        sides.put(new Side(2, 0, Direction.RIGHT), new Side(1, 2, Direction.RIGHT));
        sides.put(new Side(2, 0, Direction.DOWN), new Side(1, 1, Direction.RIGHT));
        sides.put(new Side(1, 1, Direction.LEFT), new Side(0, 2, Direction.UP));
        sides.put(new Side(1, 1, Direction.RIGHT), new Side(2, 0, Direction.DOWN));
        sides.put(new Side(0, 2, Direction.UP), new Side(1, 1, Direction.LEFT));
        sides.put(new Side(0, 2, Direction.LEFT), new Side(1, 0, Direction.LEFT));
        sides.put(new Side(1, 2, Direction.RIGHT), new Side(2, 0, Direction.RIGHT));
        sides.put(new Side(1, 2, Direction.DOWN), new Side(0, 3, Direction.RIGHT));
        sides.put(new Side(0, 3, Direction.LEFT), new Side(1, 0, Direction.UP));
        sides.put(new Side(0, 3, Direction.RIGHT), new Side(1, 2, Direction.DOWN));
        sides.put(new Side(0, 3, Direction.DOWN), new Side(2, 0, Direction.UP));

        System.out.println("Part 2: " + move(notes, new MovingStrategy() {

            @Override
            public Location next(Location location) {
                int x = location.x + location.dir.dx;
                int y = location.y + location.dir.dy;
                if (x >= 0 && x < map.width && y >= 0 && y < map.height && !map.isVoid(x, y)) {
                    return new Location(x, y, location.dir);
                }

                Location next = translate(location);
                if (map.isVoid(next.x, next.y)) {
                    throw new IllegalStateException(String.format("%s (%d, %d) -> (%d, %d)",
                        location.dir, location.x, location.y, next.x, next.y));
                }

                return next;
            }

            private Location translate(Location location) {
                int sx = location.x / sideLength;
                int sy = location.y / sideLength;
                Side toSide = sides.get(new Side(sx, sy, location.dir));

                int offsetX = location.x - sx * sideLength;
                int offsetY = location.y - sy * sideLength;

                if (location.dir == Direction.UP) {
                    if (toSide.dir == Direction.UP) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1 - offsetX),
                            toSide.y * sideLength,
                            Direction.DOWN);
                    } else if (toSide.dir == Direction.DOWN) {
                        return new Location(
                            toSide.x * sideLength + offsetX,
                            toSide.y * sideLength + (sideLength - 1),
                            Direction.UP);
                    } else if (toSide.dir == Direction.LEFT) {
                        return new Location(
                            toSide.x * sideLength,
                            toSide.y * sideLength + offsetX,
                            Direction.RIGHT);
                    } else if (toSide.dir == Direction.RIGHT) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1),
                            toSide.y * sideLength + (sideLength - 1 - offsetX),
                            Direction.LEFT);
                    }
                } else if (location.dir == Direction.DOWN) {
                    if (toSide.dir == Direction.UP) {
                        return new Location(
                            toSide.x * sideLength + offsetX,
                            toSide.y * sideLength,
                            Direction.DOWN);
                    } else if (toSide.dir == Direction.DOWN) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1 - offsetX),
                            toSide.y * sideLength + (sideLength - 1),
                            Direction.UP);
                    } else if (toSide.dir == Direction.LEFT) {
                        return new Location(
                            toSide.x * sideLength,
                            toSide.y * sideLength + (sideLength - 1 - offsetX),
                            Direction.RIGHT);
                    } else if (toSide.dir == Direction.RIGHT) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1),
                            toSide.y * sideLength + offsetX,
                            Direction.LEFT);
                    }
                } else if (location.dir == Direction.LEFT) {
                    if (toSide.dir == Direction.UP) {
                        return new Location(
                            toSide.x * sideLength + offsetY,
                            toSide.y * sideLength,
                            Direction.DOWN);
                    } else if (toSide.dir == Direction.DOWN) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1 - offsetY),
                            toSide.y * sideLength + (sideLength - 1),
                            Direction.UP);
                    } else if (toSide.dir == Direction.LEFT) {
                        return new Location(
                            toSide.x * sideLength,
                            toSide.y * sideLength + (sideLength - 1 - offsetY),
                            Direction.RIGHT);
                    } else if (toSide.dir == Direction.RIGHT) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1),
                            toSide.y * sideLength + offsetY,
                            Direction.LEFT);
                    }
                } else if (location.dir == Direction.RIGHT) {
                    if (toSide.dir == Direction.UP) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1 - offsetY),
                            toSide.y * sideLength,
                            Direction.DOWN);
                    } else if (toSide.dir == Direction.DOWN) {
                        return new Location(
                            toSide.x * sideLength + offsetY,
                            toSide.y * sideLength + (sideLength - 1),
                            Direction.UP);
                    } else if (toSide.dir == Direction.LEFT) {
                        return new Location(
                            toSide.x * sideLength,
                            toSide.y * sideLength + offsetY,
                            Direction.LEFT);
                    } else if (toSide.dir == Direction.RIGHT) {
                        return new Location(
                            toSide.x * sideLength + (sideLength - 1),
                            toSide.y * sideLength + (sideLength - 1 - offsetY),
                            Direction.LEFT);
                    }
                }

                throw new IllegalStateException();
            }
        }));
    }

    private static int move(Notes notes, MovingStrategy m) {
        Map map = notes.map;
        List<Direction> directions =
            List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);

        Location location;
        {
            int x = 0;
            while (map.isVoid(x, 0)) {
                x++;
            }

            location = new Location(x, 0, Direction.RIGHT);
        }
        int dirIdx = directions.indexOf(location.dir);

        /*for (int row = 0; row < map.height; row++) {
            for (int col = 0; col < map.width; col++) {
                if (map.tiles[row][col] == 0) {
                    System.out.print(' ');
                } else if (map.tiles[row][col] == 1) {
                    System.out.print('.');
                } else if (map.tiles[row][col] == 2) {
                    System.out.print('#');
                }
            }
            System.out.println();
        }*/

        //System.out.printf("%5s (%d, %d)%n", location.dir, location.x, location.y);
        for (Command cmd : notes.path) {
            for (int i = 0; i < cmd.steps; i++) {
                Location next = m.next(location);
                if (!map.isOpen(next.x, next.y)) {
                    break;
                }
 
                location = next;
                dirIdx = directions.indexOf(location.dir);
                //System.out.printf("%5s (%d, %d)%n", location.dir, location.x, location.y);
            }

            if (cmd.turn == Turn.LEFT) {
                dirIdx = (directions.size() + dirIdx - 1) % directions.size();
            } else if (cmd.turn == Turn.RIGHT) {
                dirIdx = (dirIdx + 1) % directions.size();
            }

            location = new Location(location.x, location.y, directions.get(dirIdx));
        }

        return 1000 * (location.y + 1) + 4 * (location.x + 1) + directions.get(dirIdx).score;
    }

    private static Notes getNotes() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            List<String> rows = new ArrayList<>();
            while (!(line = reader.readLine()).isBlank()) {
                rows.add(line);
            }

            Pattern p = Pattern.compile("([0-9]+)(R|L|$)");
            List<Command> commands = new ArrayList<>();
            Matcher m = p.matcher(reader.readLine());
            while (m.find()) {
                Turn turn;
                switch (m.group(2)) {
                    case "R":
                        turn = Turn.RIGHT;
                        break;
                    case "L":
                        turn = Turn.LEFT;
                        break;
                    default:
                        turn = null;
                }

                commands.add(new Command(Integer.parseInt(m.group(1)), turn));
            }

            return new Notes(new Map(rows), commands);
        }
    }

    private interface MovingStrategy {

        Location next(Location location);
    }

    private static class Side {

        final int x;
        final int y;
        final Direction dir;

        Side(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        @Override
        public int hashCode() {
            return 31 * 31 * x + 31 * y + dir.ordinal();
        }

        @Override
        public boolean equals(Object obj) {
            Side other = (Side) obj;
            return x == other.x && y == other.y && dir == other.dir;
        }
    }

    private static class Notes {

        final Map map;
        final List<Command> path;

        Notes(Map map, List<Command> path) {
            this.map = map;
            this.path = path;
        }
    }

    private static class Map {

        // the tiles array is filled with void by default
        private static final int VOID_TILE = 0;
        private static final int OPEN_TILE = 1;
        private static final int WALL_TILE = 2;

        private final int height;
        private final int width;
        private final int[][] tiles;

        Map(List<String> rows) {
            this.height = rows.size();
            this.width = rows.stream().mapToInt(String::length).max().getAsInt();
            this.tiles = new int[height][width];

            for (int y = 0; y < height; y++) {
                String row = rows.get(y);
                for (int x = 0; x < width; x++) {
                    if (x >= row.length()) {
                        continue;
                    }

                    char ch = row.charAt(x);
                    if (ch == '.') {
                        tiles[y][x] = OPEN_TILE;
                    } else if (ch == '#') {
                        tiles[y][x] = WALL_TILE;
                    }
                }
            }
        }

        boolean isVoid(int x, int y) {
            return tiles[y][x] == VOID_TILE;
        }

        boolean isOpen(int x, int y) {
            return tiles[y][x] == OPEN_TILE;
        }
    }

    private static class Location {

        final int x;
        final int y;
        final Direction dir;

        Location(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }

    private static class Command {

        final int steps;
        final Turn turn;

        Command(int steps, Turn turn) {
            this.steps = steps;
            this.turn = turn;
        }
    }

    private enum Turn {

        LEFT, RIGHT;
    }

    private enum Direction {

        UP(3, 0, -1), DOWN(1, 0, 1), LEFT(2, -1, 0), RIGHT(0, 1, 0);

        final int score;
        final int dx;
        final int dy;

        Direction(int score, int dx, int dy) {
            this.score = score;
            this.dx = dx;
            this.dy = dy;
        }
    }
}

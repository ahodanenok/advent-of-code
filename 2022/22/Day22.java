import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
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
    }

    private static void part1(Notes notes) {
        Map map = notes.map;
        List<Direction> directions =
            List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);

        int y = 0;
        int x = map.nextRight(0, y);
        int dirIdx = directions.indexOf(Direction.RIGHT);

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

        for (Command cmd : notes.path) {
            Direction dir = directions.get(dirIdx);
            if (dir == Direction.UP) {
                int yNext = y;
                for (int i = 0; i < cmd.steps; i++) {
                    yNext = map.nextUp(x, yNext);
                    if (!map.isOpen(x, yNext)) {
                        break;
                    }

                    y = yNext;
                }
            } else if (dir == Direction.DOWN) {
                int yNext = y;
                for (int i = 0; i < cmd.steps; i++) {
                    yNext = map.nextDown(x, yNext);
                    if (!map.isOpen(x, yNext)) {
                        break;
                    }

                    y = yNext;
                }
            } else if (dir == Direction.LEFT) {
                int xNext = x;
                for (int i = 0; i < cmd.steps; i++) {
                    xNext = map.nextLeft(xNext, y);
                    if (!map.isOpen(xNext, y)) {
                        break;
                    }

                    x = xNext;
                }
            } else if (dir == Direction.RIGHT) {
                int xNext = x;
                for (int i = 0; i < cmd.steps; i++) {
                    xNext = map.nextRight(xNext, y);
                    if (!map.isOpen(xNext, y)) {
                        break;
                    }

                    x = xNext;
                }
            }

            if (cmd.turn == Turn.LEFT) {
                dirIdx = (directions.size() + dirIdx - 1) % directions.size();
            } else if (cmd.turn == Turn.RIGHT) {
                dirIdx = (dirIdx + 1) % directions.size();
            }
        }

        int pwd = 1000 * (y + 1) + 4 * (x + 1) + directions.get(dirIdx).score;
        System.out.println("Part 1: " + pwd);
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

        boolean isOpen(int x, int y) {
            return tiles[y][x] == OPEN_TILE;
        }

        int nextUp(int x, int y) {
            int yNext = y;
            do {
                yNext = (height + yNext - 1) % height;
            } while (tiles[yNext][x] == VOID_TILE);

            return yNext;
        }

        int nextDown(int x, int y) {
            int yNext = y;
            do {
                yNext = (yNext + 1) % height;
            } while (tiles[yNext][x] == VOID_TILE);

            return yNext;
        }

        int nextLeft(int x, int y) {
            int xNext = x;
            do {
                xNext = (width + xNext - 1) % width;
            } while (tiles[y][xNext] == VOID_TILE);

            return xNext;
        }

        int nextRight(int x, int y) {
            int xNext = x;
            do {
                xNext = (xNext + 1) % width;
            } while (tiles[y][xNext] == VOID_TILE);

            return xNext;
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

        UP(3), DOWN(1), LEFT(2), RIGHT(0);

        final int score;

        Direction(int score) {
            this.score = score;
        }
    }
}

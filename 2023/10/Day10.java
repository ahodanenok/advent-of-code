import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2023/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        Grid grid = getInput();
        part1(grid);
    }

    private static void part1(Grid grid) {
        for (Pipe p : Pipe.values()) {
            Point prevPos = grid.startPos;
            Point currPos = grid.startPos;
            Pipe currPipe = p;
            int steps = 0;
            while (true) {
                boolean found = false;
                Point pos = currPipe.in(currPos);
                steps++;

                if (!pos.equals(prevPos)) {
                    if (pos.equals(grid.startPos)) {
                        System.out.println("Part 1: " + (steps - steps / 2));
                        return;
                    }

                    if (grid.pipes.containsKey(pos)
                            && (grid.pipes.get(pos).in(pos).equals(currPos) || grid.pipes.get(pos).out(pos).equals(currPos))) {
                        prevPos = currPos;
                        currPos = pos;
                        currPipe = grid.pipes.get(pos);
                        found = true;
                    }
                }

                if (!found) {
                    pos = currPipe.out(currPos);
                    if (!pos.equals(prevPos)) {
                        if (pos.equals(grid.startPos)) {
                            System.out.println("Part 1: " + (steps - steps / 2));
                            return;
                        }

                        if (grid.pipes.containsKey(pos)
                                && (grid.pipes.get(pos).in(pos).equals(currPos) || grid.pipes.get(pos).out(pos).equals(currPos))) {
                            prevPos = currPos;
                            currPos = pos;
                            currPipe = grid.pipes.get(pos);
                            found = true;
                        }
                    }
                }

                if (!found) {
                    break;
                }
            }
        }
    }

    private static Grid getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Grid grid = new Grid();

            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                for (int col = 0; col < line.length(); col++) {
                    char tile = line.charAt(col);
                    if (tile == '.') {
                        continue;
                    }

                    if (tile == 'S') {
                        grid.startPos = new Point(row, col);
                        continue;
                    }

                    boolean found = false;
                    for (Pipe pipe : Pipe.values()) {
                        if (pipe.symbol == tile) {
                            grid.pipes.put(new Point(row, col), pipe);
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalStateException("Unknown tile: " + tile);
                    }
                }

                row++;
            }

            return grid;
        }
    }

    private static class Grid {

        Point startPos;
        final Map<Point, Pipe> pipes = new HashMap<>();
    }

    private static class Point {

        final int row;
        final int col;

        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Point other = (Point) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    private enum Pipe {

        VERTICAL('|', -1, 0, 1, 0),
        HORIZONTAL('-', 0, -1, 0, 1),
        NORTH_EAST('L', -1, 0, 0, 1),
        NORTH_WEST('J', -1, 0, 0, -1),
        SOUTH_WEST('7', 1, 0, 0, -1),
        SOUTH_EAST('F', 1, 0, 0, 1);

        final char symbol;
        final int inRowOffset;
        final int inColOffset;
        final int outRowOffset;
        final int outColOffset;

        Pipe(char symbol, int inRowOffset, int inColOffset, int outRowOffset, int outColOffset) {
            this.symbol = symbol;
            this.inRowOffset = inRowOffset;
            this.inColOffset = inColOffset;
            this.outRowOffset = outRowOffset;
            this.outColOffset = outColOffset;
        }

        Point in(Point p) {
            return new Point(p.row + inRowOffset, p.col + inColOffset);
        }

        Point out(Point p) {
            return new Point(p.row + outRowOffset, p.col + outColOffset);
        }
    }
}

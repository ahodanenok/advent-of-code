import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2022/day/9
 */
public class Day9 {

    private static final char DIR_UP = 'U';
    private static final char DIR_RIGHT = 'R';
    private static final char DIR_DOWN = 'D';
    private static final char DIR_LEFT = 'L';

    public static void main(String[] args) throws Exception {
        List<Motion> motions = getMotions();
        part1(motions);
    }

    private static void part1(List<Motion> motions) {
        Position head = new Position(0, 0);
        Position tail = head;

        Set<Position> tailVisited = new HashSet<>();
        tailVisited.add(tail);

        for (Motion m : motions) {
            for (int s = 0; s < m.steps; s++) {
                if (m.dir == DIR_UP) {
                    head = new Position(head.row - 1, head.col);
                } else if (m.dir == DIR_RIGHT) {
                    head = new Position(head.row, head.col + 1);
                } else if (m.dir == DIR_DOWN) {
                    head = new Position(head.row + 1, head.col);
                } else if (m.dir == DIR_LEFT) {
                    head = new Position(head.row, head.col - 1);
                } else {
                    throw new IllegalStateException("Unknown dir: " + m.dir);
                }

                int dx = head.col - tail.col;
                int dy = head.row - tail.row;

                if (dy == 2) { // top
                    tail = new Position(tail.row + 1, head.col);
                } else if (dx == -2) { // right
                    tail = new Position(head.row, tail.col - 1);
                } else if (dy == -2) { // bottom
                    tail = new Position(tail.row - 1, head.col);
                } else if (dx == 2) { // left
                    tail = new Position(head.row, tail.col + 1);
                }

                tailVisited.add(tail);
            }
        }

        System.out.println("Part 1: " + tailVisited.size());
    }

    private static List<Motion> getMotions() throws Exception {
        List<Motion> motions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                motions.add(new Motion(parts[0].charAt(0), Integer.parseInt(parts[1])));
            }
        }

        return motions;
    }

    private static class Motion {

        final char dir;
        final int steps;

        Motion(char dir, int steps) {
            this.dir = dir;
            this.steps = steps;
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

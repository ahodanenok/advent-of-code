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
        System.out.println("Part 1: " + simulateRope(2, motions));
        System.out.println("Part 2: " + simulateRope(10, motions));
    }

    private static int simulateRope(int knots, List<Motion> motions) {
        List<Position> rope = new ArrayList<>();
        for (int i = 0; i < knots; i++) {
            rope.add(new Position(0, 0));
        }

        Set<Position> tailVisited = new HashSet<>();
        tailVisited.add(rope.get(0));

        for (Motion m : motions) {
            for (int s = 0; s < m.steps; s++) {
                Position head = rope.get(0);
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

                rope.set(0, head);
                for (int i = 1; i < knots; i++) {
                    rope.set(i, pull(rope.get(i), rope.get(i - 1)));
                }

                tailVisited.add(rope.get(knots - 1));
            }
        }

        return tailVisited.size();
    }

    private static Position pull(Position source, Position target) {
        int dx = target.col - source.col;
        int dy = target.row - source.row;

        Position pos;
        if (dx == -2 && dy == 2) { // top-right
            pos = new Position(source.row + 1, source.col - 1);
        } else if (dx == -2 && dy == -2) { // bottom-right
            pos = new Position(source.row - 1, source.col - 1);
        } else if (dx == 2 && dy == -2) { // bottom-left
            pos = new Position(source.row - 1, source.col + 1);
        } else if (dx == 2 && dy == 2) { // top-left
            pos = new Position(source.row + 1, source.col + 1);
        } else if (dy == 2) { // top
            pos = new Position(source.row + 1, target.col);
        } else if (dx == -2) { // right
            pos = new Position(target.row, source.col - 1);
        } else if (dy == -2) { // bottom
            pos = new Position(source.row - 1, target.col);
        } else if (dx == 2) { // left
            pos = new Position(target.row, source.col + 1);
        } else {
            pos = source;
        }

        return pos;
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

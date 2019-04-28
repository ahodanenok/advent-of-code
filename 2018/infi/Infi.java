import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Infi
 * https://aoc.infi.nl
 */
public class Infi {

    public static void main(String[] args) {
        Labyrinth lb = getLabyrinth();
        Location dest = new Location(lb.height - 1, lb.width - 1);
        part1(lb, dest);
        part2(lb, dest);
    }

    public static void part1(Labyrinth lb, Location dest) {
        LinkedList<Location> next = new LinkedList<>();
        next.add(new Location(0, 0));

        Set<Location> visited = new HashSet<>();

        int steps = 0;

        search:
        while (!next.isEmpty()) {
            int processCount = next.size();
            while (processCount > 0) {
                Location current = next.poll();
                if (current.equals(dest)) {
                    break search;
                }

                if (visited.add(current)) {
                    if (lb.isTopConnected(current)) {
                        next.add(current.top());
                    }

                    if (lb.isBottomConnected(current)) {
                        next.add(current.bottom());
                    }

                    if (lb.isLeftConnected(current)) {
                        next.add(current.left());
                    }

                    if (lb.isRightConnected(current)) {
                        next.add(current.right());
                    }
                }

                processCount--;
            }

            steps++; 
        }

        System.out.println(steps);
    }

    public static void part2(Labyrinth lb, Location dest) {
        Set<Location> next = new HashSet<>();
        next.add(new Location(0, 0));

        int steps = 0;

        search:
        while (!next.isEmpty()) {
            Set<Location> prevNext = next;
            next = new HashSet<>();

            LinkedList<Location> availableNow = new LinkedList<>();
            for (Location current : prevNext) {
                if (current.equals(dest)) {
                    break search;
                }

                if (lb.isTopConnected(current)) {
                    availableNow.add(current.top());
                }

                if (lb.isBottomConnected(current)) {
                    availableNow.add(current.bottom());
                }

                if (lb.isLeftConnected(current)) {
                    availableNow.add(current.left());
                }

                if (lb.isRightConnected(current)) {
                    availableNow.add(current.right());
                }
            }

            int shiftCol = -1;
            int shiftRow = -1;

            if (steps % 2 != 0) {
                shiftCol = steps % lb.width;
                lb.shiftCol(shiftCol);
            } else {
                shiftRow = steps % lb.height;
                lb.shiftRow(shiftRow);
            }

            while (!availableNow.isEmpty()) {
                Location loc = availableNow.poll();
                if (loc.col == shiftCol && !loc.equals(dest)) {
                    loc = loc.bottom();
                    if (loc.row >= lb.height) {
                        loc = new Location(0, shiftCol);
                    }
                } else if (loc.row == shiftRow && !loc.equals(dest)) {
                    loc = loc.right();
                    if (loc.col >= lb.width) {
                        loc = new Location(shiftRow, 0);
                    }
                }

                next.add(loc);
            }

            steps++;
        }

        System.out.println(steps);
    }

    private static void print(Labyrinth lb, Location current) {
        for (int row = 0; row < lb.height; row++) {
            for (int col = 0; col < lb.width; col++) {
                Location a = new Location(row, col);
                boolean top = lb.hasOutput(a, Labyrinth.OUTPUT_TOP);
                boolean bottom = lb.hasOutput(a, Labyrinth.OUTPUT_BOTTOM);
                boolean left = lb.hasOutput(a, Labyrinth.OUTPUT_LEFT);
                boolean right = lb.hasOutput(a, Labyrinth.OUTPUT_RIGHT);

                System.out.print(a.equals(current) ? "[" : " ");
                if (top && bottom && !left && !right) { // ║
                    System.out.print("\u2551");
                } else if (!top && bottom && !left && right) { // ╔
                    System.out.print("\u2554");
                } else if (!top && bottom && left && !right) { // ╗
                    System.out.print("\u2557");
                } else if (top && bottom && !left && right) { // ╠
                    System.out.print("\u2560");
                } else if (!top && bottom && left && right) { // ╦
                    System.out.print("\u2566");
                } else if (top && !bottom && !left && right) { // ╚
                    System.out.print("\u255A");
                } else if (top && !bottom && left && !right) { // ╝
                    System.out.print("\u255D");
                } else if (top && bottom && left && right) { // ╬
                    System.out.print("\u256C");
                } else if (top && !bottom && left && right) { // ╩
                    System.out.print("\u2569");
                } else if (!top && !bottom && left && right) { // ═
                    System.out.print("\u2550");
                } else if (top && bottom && left && !right) { // ╣
                    System.out.print("\u2563");
                }
                System.out.print(a.equals(current) ? "]" : " ");
            }
            System.out.println();
        }
    }

    private static Labyrinth getLabyrinth() {
        List<String> rows = new ArrayList<>();

        Scanner scanner = new Scanner(System.in, "utf-8");
        while (scanner.hasNextLine()) {
            rows.add(scanner.nextLine().trim());
        }

        int width = rows.get(0).length();
        int height = rows.size();

        Labyrinth lb = new Labyrinth(width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char ch = rows.get(row).charAt(col);
                Location a = new Location(row, col);
                if (ch == '\u2551') { // ║
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else if (ch == '\u2554') { // ╔
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else if (ch == '\u2557') { // ╗
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else if (ch == '\u2560') { // ╠
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else if (ch == '\u2566') { // ╦'
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else if (ch == '\u255A') { // ╚
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                } else if (ch == '\u255D') { // ╝
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                } else if (ch == '\u256C') { // ╬
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else if (ch == '\u2569') { // ╩
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                } else if (ch == '\u2550') { // ═
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                    lb.output(a, Labyrinth.OUTPUT_RIGHT);
                } else if (ch == '\u2563') { // ╣
                    lb.output(a, Labyrinth.OUTPUT_TOP);
                    lb.output(a, Labyrinth.OUTPUT_LEFT);
                    lb.output(a, Labyrinth.OUTPUT_BOTTOM);
                } else {
                    throw new IllegalArgumentException(ch + "");
                }
            }
        }

        return lb;
    }

    private static class Labyrinth {

        private static final byte OUTPUT_TOP = 1;
        private static final byte OUTPUT_LEFT = 2;
        private static final byte OUTPUT_RIGHT = 4;
        private static final byte OUTPUT_BOTTOM = 8;

        private byte[][] cells;
        private int width;
        private int height;

        Labyrinth(int width, int height) {
            this.width = width;
            this.height = height;
            this.cells = new byte[height][width];
        }

        public boolean hasOutput(Location a, int dest) {
            return (cells[a.row][a.col] & dest) != 0;
        }

        private void output(Location loc, byte output) {
            if (isValid(loc)) {
                this.cells[loc.row][loc.col] |= output;
            }
        }

        public boolean isTopConnected(Location a) {
            return isConnected(a, a.top(), OUTPUT_TOP, OUTPUT_BOTTOM);
        }

        public boolean isBottomConnected(Location a) {
            return isConnected(a, a.bottom(), OUTPUT_BOTTOM, OUTPUT_TOP);
        }

        public boolean isLeftConnected(Location a) {
            return isConnected(a, a.left(), OUTPUT_LEFT, OUTPUT_RIGHT);
        }

        public boolean isRightConnected(Location a) {
            return isConnected(a, a.right(), OUTPUT_RIGHT, OUTPUT_LEFT);
        }

        private boolean isConnected(Location a, Location b, byte outputA, byte outputB) {
            return isValid(a) && isValid(b)
                && (this.cells[a.row][a.col] & outputA) != 0
                && (this.cells[b.row][b.col] & outputB) != 0;
        }

        public void shiftCol(int col) {
            byte lastCell = cells[height - 1][col];
            for (int row = height - 1; row > 0; row--) {
                cells[row][col] = cells[row - 1][col];
            }
            cells[0][col] = lastCell;
        }

        public void shiftRow(int row) {
            byte lastCell = cells[row][width - 1];
            for (int col = width - 1; col > 0; col--) {
                cells[row][col] = cells[row][col - 1];
            }
            cells[row][0] = lastCell;
        }

        private boolean isValid(Location a) {
            return a.row >= 0 && a.row < height && a.col >= 0 && a.col < width;
        }
    }

    private static class Location {

        private final int row;
        private final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Location top() {
            return new Location(row - 1, col);
        }

        Location bottom() {
            return new Location(row + 1, col);
        }

        Location left() {
            return new Location(row, col - 1);
        }

        Location right() {
            return new Location(row, col + 1);
        }

        @Override
        public String toString() {
            return row + "," + col;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}
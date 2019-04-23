import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Advent of Code - Infi
 * https://aoc.infi.nl
 */
public class Infi {

    public static void main(String[] args) {
        Labyrinth lb = getLabyrinth();
        Location dest = new Location(lb.height - 1, lb.width - 1);

        Map<Location, Integer> steps = new HashMap<>();
        PriorityQueue<State> santaStates = new PriorityQueue<>(11, new java.util.Comparator<State>() {
            public int compare(State a, State b) {
                return Integer.compare(a.steps, b.steps);
            }
        });
        santaStates.add(new State(new Location(0, 0), 0));
        while (!santaStates.isEmpty()) {
            State current = santaStates.poll();
            if (steps.containsKey(current.loc) && steps.get(current.loc) <= current.steps) {
                continue;
            }
            steps.put(current.loc, current.steps);

            if (current.loc.equals(dest)) {
                break;
            }

            if (lb.isTopConnected(current.loc)) {
                santaStates.add(new State(current.loc.top(), current.steps + 1));
            }

            if (lb.isBottomConnected(current.loc)) {
                santaStates.add(new State(current.loc.bottom(), current.steps + 1));
            }

            if (lb.isLeftConnected(current.loc)) {
                santaStates.add(new State(current.loc.left(), current.steps + 1));
            }

            if (lb.isRightConnected(current.loc)) {
                santaStates.add(new State(current.loc.right(), current.steps + 1));
            }
        }
        
        System.out.println(steps.get(dest));
    }

    private static void print(Labyrinth lb) {
        for (int row = 0; row < lb.height; row++) {
            for (int col = 0; col < lb.width; col++) {
                Location a = new Location(row, col);
                if (lb.isTopConnected(a)) {
                    System.out.print("T");
                } else {
                    System.out.print(" ");
                }
                
                if (lb.isRightConnected(a)) {
                    System.out.print("R");
                } else {
                    System.out.print(" ");
                }
                
                if (lb.isLeftConnected(a)) {
                    System.out.print("L");
                } else {
                    System.out.print(" ");
                }
                
                if (lb.isBottomConnected(a)) {
                    System.out.print("B");
                } else {
                    System.out.print(" ");
                }
                
                System.out.print(" ");
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
                    lb.outputTop(a);
                    lb.outputBottom(a);
                } else if (ch == '\u2554') { // ╔
                    lb.outputRight(a);
                    lb.outputBottom(a);
                } else if (ch == '\u2557') { // ╗
                    lb.outputLeft(a);
                    lb.outputBottom(a);
                } else if (ch == '\u2560') { // ╠
                    lb.outputTop(a);
                    lb.outputRight(a);
                    lb.outputBottom(a);
                } else if (ch == '\u2566') { // ╦'
                    lb.outputLeft(a);
                    lb.outputRight(a);
                    lb.outputBottom(a);
                } else if (ch == '\u255A') { // ╚
                    lb.outputTop(a);
                    lb.outputRight(a);
                } else if (ch == '\u255D') { // ╝
                    lb.outputTop(a);
                    lb.outputLeft(a);
                } else if (ch == '\u256C') { // ╬
                    lb.outputTop(a);
                    lb.outputLeft(a);
                    lb.outputRight(a);
                    lb.outputBottom(a);
                } else if (ch == '\u2569') { // ╩
                    lb.outputTop(a);
                    lb.outputLeft(a);
                    lb.outputRight(a);
                } else if (ch == '\u2550') { // ═
                    lb.outputLeft(a);
                    lb.outputRight(a);
                } else if (ch == '\u2563') { // ╣
                    lb.outputTop(a);
                    lb.outputLeft(a);
                    lb.outputBottom(a);
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

        void outputTop(Location a) {
            output(a, OUTPUT_TOP);
        }

        void outputBottom(Location a) {
            output(a, OUTPUT_BOTTOM);
        }

        void outputLeft(Location a) {
            output(a, OUTPUT_LEFT);
        }

        void outputRight(Location a) {
            output(a, OUTPUT_RIGHT);
        }

        boolean isTopConnected(Location a) {
            return isConnected(a, a.top(), OUTPUT_TOP, OUTPUT_BOTTOM);
        }

        boolean isBottomConnected(Location a) {
            return isConnected(a, a.bottom(), OUTPUT_BOTTOM, OUTPUT_TOP);
        }

        boolean isLeftConnected(Location a) {
            return isConnected(a, a.left(), OUTPUT_LEFT, OUTPUT_RIGHT);
        }

        boolean isRightConnected(Location a) {
            return isConnected(a, a.right(), OUTPUT_RIGHT, OUTPUT_LEFT);
        }

        private void output(Location loc, byte output) {
            if (isValid(loc)) {
                this.cells[loc.row][loc.col] |= output;
            }
        }

        private boolean isConnected(Location a, Location b, byte outputA, byte outputB) {
            return isValid(a) && isValid(b)
                && (this.cells[a.row][a.col] & outputA) != 0
                && (this.cells[b.row][b.col] & outputB) != 0;
        }

        private boolean isValid(Location a) {
            return a.row >= 0 && a.row < height && a.col >= 0 && a.col < width;
        }
    }

    private static class State {

        private final Location loc;
        private final int steps;

        State(Location loc, int steps) {
            this.loc = loc;
            this.steps = steps;
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
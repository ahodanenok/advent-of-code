import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2017/day/11
 */
public class Day11 {

    public static void main(String[] args) {
        List<String> path = getPath();
        part1(path);
    }

    private static void part1(List<String> path) {
        State state = new State(0, 0);
        move(path, state);
        System.out.println(state.distance());
    }

    private static List<String> getPath() {
        List<String> path = new ArrayList<String>();

        String[] moves = new Scanner(System.in).nextLine().split(",");
        for (String move : moves) {
            path.add(move);
        }

        return path;
    }

    private static void move(List<String> path, State state) {
        for (String move : path) {
            if ("n".equals(move)) {
                state.row--;
            } else if ("nw".equals(move)) {
                if (state.col % 2 == 0) {
                    state.row--;
                }
                state.col--;
            } else if ("sw".equals(move)) {
                if (state.col % 2 != 0) {
                    state.row++;
                }
                state.col--;
            } else if ("s".equals(move)) {
                state.row++;
            } else if ("se".equals(move)) {
                if (state.col % 2 != 0) {
                    state.row++;
                }
                state.col++;
            } else if ("ne".equals(move)) {
                if (state.col % 2 == 0) {
                    state.row--;
                }
                state.col++;
            } else {
                throw new IllegalStateException("unknown move: " + move);
            }
        }
    }

    private static class State {

        private int col;
        private int row;

        State(int col, int row) {
            this.col = col;
            this.row = row;
        }

        int distance() {
            int x = col;
            int z = row - (col - (col & 1)) / 2;
            int y = -x - z;

            return Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
        }
    }
}

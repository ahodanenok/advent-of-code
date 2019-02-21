import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2016/day/13
 */
public class Day13 {

    public static void main(String[] args) {
        State state = new State(1, 1);
        part1(state, 31, 39);
        part2(state, 50);
    }

    public static void part1(State initialState, int x, int y) {
        Set<State> seen = new HashSet<State>();
        seen.add(initialState);

        LinkedList<State> queue = new LinkedList<State>();
        queue.add(initialState);

        int moves = Integer.MAX_VALUE;
        while (queue.size() > 0 && moves == Integer.MAX_VALUE) {
            State currentState = queue.poll();
            if (currentState.x == x && currentState.y == y) {
                moves = currentState.moves;
            }

            for (State nextState : getAdjacentCells(currentState)) {
                if (isOpen(nextState.x, nextState.y) && !seen.contains(nextState)) {
                    queue.offer(nextState);
                    seen.add(nextState);
                }
            }
        }

        System.out.println(moves);
    }

    public static void part2(State initialState, int moves) {
        Set<State> seen = new HashSet<State>();
        seen.add(initialState);

        LinkedList<State> queue = new LinkedList<State>();
        queue.add(initialState);

        while (queue.size() > 0) {
            State currentState = queue.poll();
            if (currentState.moves >= moves) {
                continue;
            }

            for (State nextState : getAdjacentCells(currentState)) {
                if (isOpen(nextState.x, nextState.y) && !seen.contains(nextState)) {
                    queue.offer(nextState);
                    seen.add(nextState);
                }
            }
        }

        System.out.println(seen.size());
    }

    public static boolean isOpen(int x, int y) {
        if (x < 0 || y < 0) return false;    
        int value = x * x + 3 * x + 2 * x * y + y + y * y;
        value += 1358;
        return Integer.bitCount(value) % 2 == 0;
    }

    public static State[] getAdjacentCells(State state) {
        return new State[] {
            state.moveTop(),
            state.moveRight(),
            state.moveBottom(),
            state.moveLeft()
        };
    }

    private static class State {

        private int moves;
        private int x;
        private int y;

        State(int x, int y) {
            this.x = x;
            this.y = y;
        }

        State moveTop() {
            return move(0, -1);
        }

        State moveRight() {
            return move(1, 0);
        }

        State moveBottom() {
            return move(0, 1);
        }

        State moveLeft() {
            return move(-1, 0);
        }

        State move(int dx, int dy) {
            State copy = new State(x, y);
            copy.moves = moves + 1;
            copy.x += dx;
            copy.y += dy;

            return copy;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object object) {
            State other = (State) object;
            return x == other.x && y == other.y;
        }
    }
}

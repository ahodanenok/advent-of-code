import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2016/day/11
 */
public class Day11 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        State state = new State(5, 4);
        state.add("P", 1, 2);
        state.add("T", 1, 1);
        state.add("Pr", 1, 2);
        state.add("R", 1, 1);
        state.add("C", 1, 1);
        System.out.println(move(state, 4));
    }

    private static void part2() {
        State state = new State(7, 4);
        state.add("P", 1, 2);
        state.add("T", 1, 1);
        state.add("Pr", 1, 2);
        state.add("R", 1, 1);
        state.add("C", 1, 1);
        state.add("E", 1, 1);
        state.add("D", 1, 1);
        System.out.println(move(state, 4));
    }

    public static int move(State initialState, int destFloor) {
        Set<State> seen = new HashSet<State>();
        seen.add(initialState);

        LinkedList<State> queue = new LinkedList<State>();
        queue.offer(initialState);

        while (queue.size() > 0) {
            State currentState = queue.poll();
            if (currentState.getItems(destFloor).size() == currentState.getItemsCount()) {
                return currentState.getMovesCount();
            }

            for (State nextState : getNextStates(currentState)) {
                if (!seen.contains(nextState)) {
                    queue.offer(nextState);
                    seen.add(nextState);
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    public static List<State> getNextStates(State state) {
        List<State> states = new ArrayList<State>();
        List<String> items = state.getItems(state.getElevator());

        State nextState;
        for (int i = 0; i < items.size(); i++) {
            String itemA = items.get(i);

            nextState = state.moveUp(itemA);
            if (nextState.isValid()) {
                states.add(nextState);
            }

            nextState = state.moveDown(itemA);
            if (nextState.isValid()) {
                states.add(nextState);
            }

            for (int j = i + 1; j < items.size(); j++) {
                String itemB = items.get(j);

                nextState = state.moveUp(itemA, itemB);
                if (nextState.isValid()) {
                    states.add(nextState);
                }

                nextState = state.moveDown(itemA, itemB);
                if (nextState.isValid()) {
                    states.add(nextState);
                }
            }
        }

        return states;
    }

    private static class State {

        // 0=G, 1=M, 2=G, 3=M, ...
        private int[] itemFloors;
        private Map<String, Integer> items;
        private int elevator;
        private int floorsCount;
        private int moves;

        private int[] identity;

        private State() { }

        State(int elementsCount, int floorsCount) {
            this.itemFloors = new int[elementsCount * 2];
            this.items = new LinkedHashMap<String, Integer>();
            this.elevator = 1;
            this.floorsCount = floorsCount;
            this.moves = 0;
        }

        int getMovesCount() {
            return moves;
        }

        int getItemsCount() {
            return items.size();
        }

        void add(String element, int generatorFloor, int chipFloor) {
            int idx = items.size();
            items.put(element + "G", idx);
            items.put(element + "M", idx + 1);
            itemFloors[idx] = generatorFloor;
            itemFloors[idx + 1] = chipFloor;
        }

        int getElevator() {
            return elevator;
        }

        boolean isValid() {
            if (elevator < 1 || elevator > floorsCount) {
                return false;
            }

            for (int i = 0; i < itemFloors.length; i += 2) {
                int generatorFloor = itemFloors[i];
                int chipFloor = itemFloors[i + 1];

                // generator and chip of the same kind are on the same floor - chip is safe
                if (generatorFloor == chipFloor) {
                    continue;
                }

                for (int j = 0; j < itemFloors.length; j += 2) {
                    // some other generator is located on the same floor with this chip - burnt
                    if (j != i && itemFloors[j] == chipFloor) {
                        return false;
                    }
                }
            }

            return true;
        }

        List<String> getItems(int floor) {
            List<String> result = new ArrayList<String>();
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                if (itemFloors[entry.getValue()] == floor) {
                    result.add(entry.getKey());
                }
            }

            return result;
        }

        State moveUp(String... items) {
            return move(1, items);
        }

        State moveDown(String... items) {
            return move(-1, items);
        }

        private State move(int dir, String... items) {
            State copy = new State();
            copy.itemFloors = new int[itemFloors.length];
            System.arraycopy(itemFloors, 0, copy.itemFloors, 0, itemFloors.length);
            copy.items = this.items;
            copy.elevator = elevator;
            copy.floorsCount = floorsCount;
            copy.moves = moves + 1;

            if (dir > 0) {
                copy.elevator++;
                for (String item : items) {
                    copy.itemFloors[copy.items.get(item)]++;
                }
            } else if (dir < 0) {
                copy.elevator--;
                for (String item : items) {
                    copy.itemFloors[copy.items.get(item)]--;
                }
            }

            return copy;
        }

        @Override
        public String toString() {
            return "State: { elevator=" + elevator + ", items=" + items + ", floors=" + Arrays.toString(itemFloors) + ", moves=" + moves + " }";
        }

        @Override
        public int hashCode() {
            initIdentity();
            return Arrays.hashCode(identity);
        }

        @Override
        public boolean equals(Object object) {
            State other = (State) object;
            other.initIdentity();
            initIdentity();
            return elevator == other.elevator && Arrays.equals(identity, other.identity);
        }

        private void initIdentity() {
            if (identity == null) {
                identity = new int[itemFloors.length];
                System.arraycopy(itemFloors, 0, identity, 0, identity.length);
                for (int i = 0; i < identity.length; i += 2) {
                    int min = i;
                    for (int j = 2; j < identity.length; j += 2) {
                        if (identity[j] < identity[i]) {
                            min = j;
                        } else if (identity[j] == identity[i] && identity[j + 1] < identity[i + 1]) {
                            min = j;
                        }
                    }

                    int a = identity[i];
                    int b = identity[i + 1];
                    identity[i] = identity[min];
                    identity[i + 1] = identity[min + 1];
                    identity[min] = a;
                    identity[min + 1] = b;
                }
            }
        }
    }
}

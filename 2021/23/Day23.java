import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2021/day/23
 */
public class Day23 {

    public static void main(String[] args) throws Exception {
        Situation s = new Situation();
        /*
        s.cells[Amphipod.room1(Amphipod.A)] = Amphipod.B;
        s.cells[Amphipod.room2(Amphipod.A)] = Amphipod.A;
        s.cells[Amphipod.room1(Amphipod.B)] = Amphipod.C;
        s.cells[Amphipod.room2(Amphipod.B)] = Amphipod.D;
        s.cells[Amphipod.room1(Amphipod.C)] = Amphipod.B;
        s.cells[Amphipod.room2(Amphipod.C)] = Amphipod.C;
        s.cells[Amphipod.room1(Amphipod.D)] = Amphipod.D;
        s.cells[Amphipod.room2(Amphipod.D)] = Amphipod.A;
        */

        s.cells[Amphipod.room1(Amphipod.A)] = Amphipod.D;
        s.cells[Amphipod.room2(Amphipod.A)] = Amphipod.D;
        s.cells[Amphipod.room1(Amphipod.B)] = Amphipod.C;
        s.cells[Amphipod.room2(Amphipod.B)] = Amphipod.A;
        s.cells[Amphipod.room1(Amphipod.C)] = Amphipod.B;
        s.cells[Amphipod.room2(Amphipod.C)] = Amphipod.A;
        s.cells[Amphipod.room1(Amphipod.D)] = Amphipod.C;
        s.cells[Amphipod.room2(Amphipod.D)] = Amphipod.B;
        s.print();

        Situation result = organize(s, new HashMap<>());
        result.print();
        System.out.println("Part 1: " + result.energyUsed);
    }

    private static Situation organize(Situation situation, Map<Situation, Integer> seen) {
        if (situation.isOrganized()) {
            return situation;
        }

        return situation.amphipods().boxed().map(amphipod -> {
            Situation result = null;

            Situation st = situation.moveToTargetRoom(amphipod);
            if (st != null && st.energyUsed < seen.getOrDefault(st, Integer.MAX_VALUE)) {
                seen.put(st, st.energyUsed);
                result = organize(st, seen);
            }

            for (int pos : situation.hallwayCells()) {
                Situation sh = situation.moveToHallway(amphipod, pos);
                if (sh != null && sh.energyUsed < seen.getOrDefault(sh, Integer.MAX_VALUE)) {
                    seen.put(sh, sh.energyUsed);
                    Situation r = organize(sh, seen);
                    if (r != null && (result == null || r.energyUsed < result.energyUsed)) {
                        result = r;
                    }
                }
            }

            return result;
        }).filter(s -> s != null).min((a, b) -> Integer.compare(a.energyUsed, b.energyUsed)).orElse(null);
    }

    private static class Situation {

        static final int HALLWAY_START = 0;
        static final int HALLWAY_END = 10;

        // 11 - hallway, 2 - A, 2 - B, 2 - C, 2 - D (total 19)
        static final int CELLS_COUNT = 19;

        private int energyUsed;
        private Amphipod[] cells = new Amphipod[CELLS_COUNT];

        Situation() {
            Arrays.fill(cells, Amphipod.NONE);
        }

        Situation copy() {
            Situation s = new Situation();
            s.energyUsed = energyUsed;
            s.cells = Arrays.copyOf(cells, cells.length);

            return s;
        }

        boolean isOrganized() {
            for (Amphipod a : Amphipod.values()) {
                if (cells[Amphipod.room1(a)] != a || cells[Amphipod.room2(a)] != a) {
                    return false;
                }
            }

            return true;
        }

        Situation moveToHallway(int amphipodPosition, int hallwayPosition) {
            // already in hallway
            if (amphipodPosition >= HALLWAY_START && amphipodPosition <= HALLWAY_END) {
                return null;
            }

            // hallway is occupied
            if (cells[hallwayPosition] != Amphipod.NONE) {
                return null;
            }

            Amphipod amphipod = cells[amphipodPosition];
            int targetRoom1 = Amphipod.room1(amphipod);
            int targetRoom2 = Amphipod.room2(amphipod);

            // in the second cell of its own room or the room is filled with the right amphipods
            if (amphipodPosition == targetRoom2 || amphipodPosition == targetRoom1 && cells[targetRoom2] == amphipod) {
                return null;
            }

            Amphipod currentRoomType = Amphipod.roomType(amphipodPosition);
            int currentRoom1 = Amphipod.room1(currentRoomType);
            int currentRoom2 = Amphipod.room2(currentRoomType);

            // some amphipod is blocking the room entrance
            if (amphipodPosition == currentRoom2 && cells[currentRoom1] != Amphipod.NONE) {
                return null;
            }

            // hallway is blocked
            int from = Math.min(currentRoomType.roomEntrance, hallwayPosition);
            int to = Math.max(currentRoomType.roomEntrance, hallwayPosition);
            for (int i = from; i <= to; i++) {
                if (i == amphipodPosition) {
                    continue;
                }

                if (cells[i] != Amphipod.NONE) {
                    return null;
                }
            }

            Situation s = copy();
            s.cells[amphipodPosition] = Amphipod.NONE;
            s.cells[hallwayPosition] = amphipod;
            s.energyUsed += (to - from) * amphipod.energyRequired; // to move in the hallway
            s.energyUsed += amphipod.energyRequired; // to exit the room
            if (amphipodPosition == currentRoom2) {
                s.energyUsed += amphipod.energyRequired; // to move one cell in the room
            }

            return s;
        }

        Situation moveToTargetRoom(int amphipodPosition) {
            // not in hallway
            if (amphipodPosition > HALLWAY_END) {
                return null;
            }

            Amphipod amphipod = cells[amphipodPosition];
            int targetRoom1 = Amphipod.room1(amphipod);
            int targetRoom2 = Amphipod.room2(amphipod);

            // room entrance is blocked
            if (cells[targetRoom1] != Amphipod.NONE) {
                return null;
            }

            // amphipod of different kind is in the room
            if (cells[targetRoom2] != Amphipod.NONE && cells[targetRoom2] != amphipod) {
                return null;
            }

            int from = Math.min(amphipodPosition, amphipod.roomEntrance);
            int to = Math.max(amphipodPosition, amphipod.roomEntrance);

            // hallway is blocked
            for (int i = from; i <= to; i++) {
                if (i == amphipodPosition) {
                    continue;
                }

                if (cells[i] != Amphipod.NONE) {
                    return null;
                }
            }

            Situation s = copy();
            s.cells[amphipodPosition] = Amphipod.NONE;
            s.energyUsed += (to - from) * amphipod.energyRequired;
            if (cells[targetRoom2] == Amphipod.NONE) {
                s.cells[targetRoom2] = amphipod;
                s.energyUsed += 2 * amphipod.energyRequired;
            } else {
                s.cells[targetRoom1] = amphipod;
                s.energyUsed += amphipod.energyRequired;
            }

            return s;
        }

        IntStream amphipods() {
            return IntStream.range(0, CELLS_COUNT).filter(pos -> cells[pos] != Amphipod.NONE);
        }

        int[] hallwayCells() {
            return new int[] { 0, 1, 3, 5, 7, 9, 10 };
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cells);
        }

        @Override
        public boolean equals(Object obj) {
            Situation other = (Situation) obj;
            return Arrays.equals(cells, other.cells);
        }

        void print() {
            System.out.println();
            System.out.println("Energy used: " + energyUsed);
            System.out.println("#############");
            System.out.print("#");
            for (int i = HALLWAY_START; i <= HALLWAY_END; i++) {
                System.out.print(cellToString(i));
            }
            System.out.println("#");

            System.out.printf("###%s#%s#%s#%s###%n", cellToString(Amphipod.room1(Amphipod.A)), cellToString(Amphipod.room1(Amphipod.B)), cellToString(Amphipod.room1(Amphipod.C)), cellToString(Amphipod.room1(Amphipod.D)));
            System.out.printf("  #%s#%s#%s#%s#  %n", cellToString(Amphipod.room2(Amphipod.A)), cellToString(Amphipod.room2(Amphipod.B)), cellToString(Amphipod.room2(Amphipod.C)), cellToString(Amphipod.room2(Amphipod.D)));
            System.out.println("  #########  ");
        }

        String cellToString(int pos) {
            if (cells[pos] == Amphipod.NONE) {
                return ".";
            } else {
                return cells[pos].name();
            }
        }
    }

    private enum Amphipod {

        NONE(0, -1),
        A(1, 2),
        B(10, 4),
        C(100, 6),
        D(1000, 8);

        final int energyRequired;
        final int roomEntrance;

        Amphipod(int energyRequired, int roomEntrance) {
            this.energyRequired = energyRequired;
            this.roomEntrance = roomEntrance;
        }

        static Amphipod roomType(int pos) {
            for (Amphipod a : values()) {
                if (pos == room1(a) || pos == room2(a)) {
                    return a;
                }
            }

            throw new IllegalStateException("Unknown room at position: " + pos);
        }

        static int room1(Amphipod a) {
            return a.roomEntrance + 9;
        }

        static int room2(Amphipod a) {
            return a.roomEntrance + 10;
        }
    }
}

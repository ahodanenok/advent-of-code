import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;

public class Day23 {

    public static void main(String[] args) {
        System.out.println("Part 1: " + organize(getInput1(), new HashMap<>()));
        System.out.println("Part 2: " + organize(getInput2(), new HashMap<>()));
    }

    private static Situation getInput1() {
        Situation s = new Situation();

        //s.addRoom(new Room(AmphipodType.A, 2, new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.A)), 2);
        //s.addRoom(new Room(AmphipodType.B, 2, new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.D)), 4);
        //s.addRoom(new Room(AmphipodType.C, 2, new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.C)), 6);
        //s.addRoom(new Room(AmphipodType.D, 2, new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.A)), 8);

        s.addRoom(new Room(AmphipodType.A, 2, new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.D)), 2);
        s.addRoom(new Room(AmphipodType.B, 2, new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.A)), 4);
        s.addRoom(new Room(AmphipodType.C, 2, new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.A)), 6);
        s.addRoom(new Room(AmphipodType.D, 2, new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.B)), 8);

        return s;
    }

    private static Situation getInput2() {
        Situation s = new Situation();

        //s.addRoom(new Room(AmphipodType.A, 4, new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.A)), 2);
        //s.addRoom(new Room(AmphipodType.B, 4, new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.D)), 4);
        //s.addRoom(new Room(AmphipodType.C, 4, new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.A), new Amphipod(AmphipodType.C)), 6);
        //s.addRoom(new Room(AmphipodType.D, 4, new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.A), new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.A)), 8);

        s.addRoom(new Room(AmphipodType.A, 4, new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.D), new Amphipod(AmphipodType.D)), 2);
        s.addRoom(new Room(AmphipodType.B, 4, new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.A)), 4);
        s.addRoom(new Room(AmphipodType.C, 4, new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.B), new Amphipod(AmphipodType.A), new Amphipod(AmphipodType.A)), 6);
        s.addRoom(new Room(AmphipodType.D, 4, new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.A), new Amphipod(AmphipodType.C), new Amphipod(AmphipodType.B)), 8);

        return s;
    }

    private static int organize(Situation situation, Map<Situation, Integer> seen) {
        if (situation.isOrganized()) {
            return situation.totalEnergyUsed();
        }

        if (situation.totalEnergyUsed() > seen.getOrDefault(situation, Integer.MAX_VALUE)) {
            return Integer.MAX_VALUE;
        }

        seen.put(situation, situation.totalEnergyUsed());

        int energyUsed = Integer.MAX_VALUE;
        for (Room room : situation.rooms.values()) {
            for (int pos = Situation.HALLWAY_START; pos <= Situation.HALLWAY_END; pos++) {
                if (situation.canMoveToHallway(room.num, pos)) {
                    Situation s = situation.copy();
                    s.moveToHallway(room.num, pos);
                    energyUsed = Math.min(organize(s, seen), energyUsed);
                }
            }
        }

        for (Amphipod amphipod : situation.hallwayAmphipods.values()) {
            if (situation.canMoveToRoom(amphipod.num)) {
                Situation s = situation.copy();
                s.moveToRoom(amphipod.num);
                energyUsed = Math.min(organize(s, seen), energyUsed);
            }
        }

        return energyUsed;
    }

    private static class Situation {

        static final int HALLWAY_START = 0;
        static final int HALLWAY_END = 10;

        final Map<Integer, Room> rooms = new HashMap<>();
        final Map<Integer, Integer> roomEntrances = new HashMap<>();
        final Map<Integer, Amphipod> hallwayAmphipods = new HashMap<>();
        final Map<Integer, Integer> amphipodPositions = new HashMap<>();

        Situation copy() {
            Situation s = new Situation();

            s.roomEntrances.putAll(roomEntrances);
            for (Map.Entry<Integer, Room> entry : rooms.entrySet()) {
                s.rooms.put(entry.getKey(), entry.getValue().copy());
            }

            s.amphipodPositions.putAll(amphipodPositions);
            for (Map.Entry<Integer, Amphipod> entry : hallwayAmphipods.entrySet()) {
                s.hallwayAmphipods.put(entry.getKey(), entry.getValue().copy());
            }

            return s;
        }

        void addRoom(Room room, int entrance) {
            this.rooms.put(room.num, room);
            this.roomEntrances.put(room.num, entrance);
        }

        boolean isOrganized() {
            for (Room r : rooms.values()) {
                if (!r.isOrganized()) {
                    return false;
                }
            }

            return true;
        }

        int totalEnergyUsed() {
            return hallwayAmphipods.values().stream().mapToInt(a -> a.energyUsed).sum()
                + rooms.values().stream().mapToInt(r -> r.amphipods().mapToInt(a -> a.energyUsed).sum()).sum();
        }

        boolean canMoveToHallway(int roomNum, int hallwayPosition) {
            Room fromRoom = rooms.get(roomNum);
            if (roomEntrances.values().contains(hallwayPosition)) {
                return false;
            }

            if (!fromRoom.canExit()) {
                return false;
            }

            int roomEntrance = roomEntrances.get(roomNum);
            int from = Math.min(roomEntrance, hallwayPosition);
            int to = Math.max(roomEntrance, hallwayPosition);

            for (int pos : amphipodPositions.values()) {
                if (pos >= from && pos <= to) {
                    return false;
                }
            }

            return true;
        }

        void moveToHallway(int roomNum, int hallwayPosition) {
            Room fromRoom = rooms.get(roomNum);
            int roomEntrance = roomEntrances.get(roomNum);

            Amphipod amphipod = fromRoom.exit();
            hallwayAmphipods.put(amphipod.num, amphipod);
            amphipodPositions.put(amphipod.num, hallwayPosition);
            amphipod.moved(Math.abs(roomEntrance - hallwayPosition));
        }

        boolean canMoveToRoom(int amphipodNum) {
            Amphipod amphipod = hallwayAmphipods.get(amphipodNum);
            Room toRoom = rooms.values().stream().filter(r -> r.type == amphipod.type).findFirst().get();
            if (!toRoom.canAccept(amphipod)) {
                return false;
            }

            int roomEntrance = roomEntrances.get(toRoom.num);
            int amphipodPosition = amphipodPositions.get(amphipod.num);
            int from = Math.min(roomEntrance, amphipodPosition);
            int to = Math.max(roomEntrance, amphipodPosition);

            for (int pos : amphipodPositions.values()) {
                if (pos > from && pos < to) {
                    return false;
                }
            }

            return true;
        }

        void moveToRoom(int amphipodNum) {
            Amphipod amphipod = hallwayAmphipods.get(amphipodNum);
            Room toRoom = rooms.values().stream().filter(r -> r.type == amphipod.type).findFirst().get();

            int roomEntrance = roomEntrances.get(toRoom.num);
            int amphipodPosition = amphipodPositions.get(amphipod.num);
            amphipod.moved(Math.abs(roomEntrance - amphipodPosition));

            hallwayAmphipods.remove(amphipod.num);
            amphipodPositions.remove(amphipod.num);
            toRoom.accept(amphipod);
        }

        // don't know which situations to consider equal to reduce number of paths to check
        // but using this approach it atleast could be computed within reasonable time
        Map<AmphipodType, Integer> byType;
        private Map<AmphipodType, Integer> byType() {
            if (byType != null) {
                return byType;
            }

            //Map<AmphipodType, Integer> byType = new HashMap<>();
            byType = new HashMap<>();
            for (Map.Entry<Integer, Integer> e : amphipodPositions.entrySet()) {
                byType.put(hallwayAmphipods.get(e.getKey()).type, e.getValue());
            }

            return byType;
        }

        @Override
        public int hashCode() {
            return 31 * rooms.hashCode() + byType().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Situation other = (Situation) obj;
            return rooms.equals(other.rooms) && byType().equals(other.byType());
        }
    }

    private static class Room {

        private static int LAST_NUM = 1;

        private AmphipodType type;
        private int num;
        private int headIdx;
        private Amphipod[] cells;

        Room(AmphipodType type, int size, Amphipod... initialAmphipods) {
            this(type, LAST_NUM++, size, initialAmphipods);
        }

        Room(AmphipodType type, int num, int size, Amphipod... initialAmphipods) {
            this.type = type;
            this.num = num;
            this.headIdx = -1;
            this.cells = new Amphipod[size];
            for (int i = initialAmphipods.length - 1; i >= 0; i--) {
                this.cells[++headIdx] = initialAmphipods[i];
            }
        }

        Stream<Amphipod> amphipods() {
            return Arrays.stream(cells).filter(a -> a != null);
        }

        Room copy() {
            Room r = new Room(type, num, cells.length);
            r.headIdx = headIdx;
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null) {
                    r.cells[i] = cells[i].copy();
                }
            }

            return r;
        }

        Amphipod peek() {
            if (headIdx < 0) {
                return null;
            }

            return cells[headIdx];
        }

        boolean isOrganized() {
            return headIdx == cells.length - 1
                && Arrays.stream(cells).allMatch(a -> a.type == type);
        }

        boolean canAccept(Amphipod amphipod) {
            return headIdx < cells.length - 1
                && type == amphipod.type
                && Arrays.stream(cells).filter(a -> a != null).allMatch(a -> a.type == type);
        }

        void accept(Amphipod amphipod) {
            headIdx++;
            cells[headIdx] = amphipod;
            amphipod.moved(cells.length - headIdx);
        }

        boolean canExit() {
            if (isOrganized() || headIdx < 0) {
                return false;
            }

            return true;
        }

        Amphipod exit() {
            Amphipod amphipod = cells[headIdx];
            amphipod.moved(cells.length - headIdx);
            cells[headIdx] = null;
            headIdx--;

            return amphipod;
        }

        @Override
        public int hashCode() {
            return 31 * type.hashCode() + headIdx + Arrays.hashCode(cells);
        }

        @Override
        public boolean equals(Object obj) {
            Room other = (Room) obj;
            return type == other.type && headIdx == other.headIdx && Arrays.equals(cells, other.cells);
        }
    }

    private static class Amphipod {

        private static int LAST_NUM = 1;

        final AmphipodType type;
        int num;
        int energyUsed;

        Amphipod(AmphipodType type) {
            this(type, LAST_NUM++);
        }

        Amphipod(AmphipodType type, int num) {
            this.type = type;
            this.num = num;
        }

        Amphipod copy() {
            Amphipod a = new Amphipod(type, num);
            a.energyUsed = energyUsed;

            return a;
        }

        void moved(int steps) {
            energyUsed += steps * type.energyRequired;
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Amphipod other = (Amphipod) obj;
            return type == other.type;
        }
    }

    private enum AmphipodType {

        A(1), B(10), C(100), D(1000);

        final int energyRequired;

        AmphipodType(int energyRequired) {
            this.energyRequired = energyRequired;
        }
    }
}

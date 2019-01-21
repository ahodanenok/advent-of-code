import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2018/day/15
 */
public class Day15 {

    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        Cave cave = getCave();
        fight(cave);
    }

    private static Cave getCave() {
        List<String> lines = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine().trim());
        }

        Cave cave = new Cave(lines.get(0).length(), lines.size());
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char ch = line.charAt(col);
                if (ch == '#') {
                    cave.walls.add(new Location(row, col));
                } else if (ch == 'G') {
                    cave.units.add(new Unit(UnitType.GOBLIN, UnitType.ELF, new Location(row, col), cave));
                } else if (ch == 'E') {
                    cave.units.add(new Unit(UnitType.ELF, UnitType.GOBLIN, new Location(row, col), cave));
                } else if (ch == '.') {
                    // no-op
                } else {
                    throw new IllegalStateException("Unknown cave character: " + ch);
                }
            }
        }

        return cave;
    }

    private static void fight(Cave cave) {
        int rounds = 0;

        game:
        while (true) {
            List<Unit> units = new ArrayList<Unit>(cave.units);
            Collections.sort(units, Unit.TURN_ORDER);

            if (DEBUG) {
                System.out.println("== ROUND: " + (rounds + 1));
                System.out.println("  -- Moving order --");
                for (Unit unit : units) {
                    System.out.println("  " + unit);
                }
                System.out.println();
            }

            if (DEBUG) System.out.println("  -- Unit actions --");
            for (Unit unit : units) {
                if (DEBUG) System.out.println("  " + unit);
                if (unit.hp <= 0) {
                    if (DEBUG) {
                        System.out.println("    Unit is dead, skipping");
                        System.out.println();
                    }
                    continue;
                }

                List<Unit> targets = cave.getUnits(unit.enemyType);
                if (targets.isEmpty()) {
                    if (DEBUG) {
                        System.out.println("    No targets, combat is over");
                        System.out.println();
                    }
                    break game;
                }

                if (!unit.attack()) {
                    unit.move();
                    unit.attack();
                }
            }

            rounds++;

            if (DEBUG) {
                System.out.println("  -- Cave after " + rounds + " round --");
                for (int row = 0; row < cave.height; row++) {
                    for (int col = 0; col < cave.width; col++) {
                        if (cave.walls.contains(new Location(row, col))) {
                            System.out.print('#');
                        } else {
                            Unit unit = null;
                            for (Unit u : cave.units) {
                                if (u.location.equals(new Location(row, col))) {
                                    unit = u;
                                    break;
                                }
                            }

                            if (unit != null) {
                                if (unit.type == UnitType.GOBLIN) {
                                    System.out.print('G');
                                } else {
                                    System.out.print('E');
                                }
                            } else {
                                System.out.print('.');
                            }
                        }
                    }
                    System.out.println();
                }

                System.out.println("  -- Remaining units --");
                for (Unit unit : cave.units) {
                    System.out.println("  " + unit);
                }

                System.out.println("===========================================");
                System.out.println();
            }
        }

        int hpSum = 0;
        for (Unit unit : cave.units) {
            hpSum += unit.hp;
        }

        if (DEBUG) {
            System.out.println("-- Units after game --");
            for (Unit unit : cave.units) {
                System.out.println(unit);
            }
            System.out.println();
            System.out.println("rounds=" + rounds);
            System.out.println("hpSum=" + hpSum);
        }

        System.out.println(hpSum * rounds);
    }

    private static class Cave {

        private final int width;
        private final int height;
        private Set<Location> walls = new HashSet<Location>();
        private List<Unit> units = new ArrayList<Unit>();

        Cave(int width, int height) {
            this.width = width;
            this.height = height;
        }

        private static class Step {

            Location location;
            int distance;
            Step prev;

            Step(Location location, int distance, Step prev) {
                this.location = location;
                this.distance = distance;
                this.prev = prev;
            }

            @Override
            public int hashCode() {
                return location.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                Step other = (Step) obj;
                return other.location.equals(location);
            }

            @Override
            public String toString() {
                return location.toString();
            }
        }

        Path pathTo(Location from, Location to) {
            Map<Step, Integer> distances = new HashMap<Step, Integer>();
            Map<Location, Step> best = new HashMap<Location, Step>();
            PriorityQueue<Step> queue = new PriorityQueue<Step>(new Comparator<Step>() {
                @Override
                public int compare(Step a, Step b) {
                    int distanceCmp = Integer.compare(a.distance, b.distance);
                    if (distanceCmp == 0) {
                        return a.location.compareTo(b.location);
                    } else {
                        return distanceCmp;
                    }
                }
            });
            queue.offer(new Step(from, 0, null));

            List<Step> result = new ArrayList<Step>();
            while (!queue.isEmpty()) {
                Step current = queue.poll();
                if (distances.containsKey(current)) {
                    if (distances.get(current) < current.distance) {
                        continue;
                    } else if (distances.get(current) == current.distance) {
                        if (best.get(current.location).prev.location.compareTo(current.prev.location) < 0) {
                            continue;
                        }
                    }
                }

                distances.put(current, current.distance);
                best.put(current.location, current);

                if (current.location.equals(to)) {
                    result.add(current);
                    continue;
                }

                for (Location adjacent : getAdjacentLocations(current.location, false)) {
                    queue.offer(new Step(adjacent, current.distance + 1, current));
                }
            }

            List<Path> paths = new ArrayList<Path>();
            for (Step current : result) {
                List<Location> steps = new ArrayList<Location>();
                while (current != null) {
                    steps.add(0, current.location);
                    current = current.prev;
                }

                paths.add(new Path(steps));
            }

            if (paths.isEmpty()) {
                return null;
            }

            Collections.sort(paths);
            Path path = paths.get(0);
            if (DEBUG) {
                System.out.println("    Choosing one path from " + from  + " to " + to);
                for (Path p : paths) {
                    System.out.println("      " + p);
                }
                System.out.println("    Chosen path: " + path);
                System.out.println();
            }

            return path;
        }

        List<Unit> getUnits(UnitType type) {
            List<Unit> result = new ArrayList<Unit>();
            for (Unit unit : units) {
                if (unit.type == type) {
                    result.add(unit);
                }
            }

            return result;
        }

        List<Location> getAdjacentLocations(List<Unit> units) {
            List<Location> locations = new ArrayList<Location>();
            for (Unit unit : units) {
                for (Location location : getAdjacentLocations(unit.location, false)) {
                    if (!locations.contains(location)) {
                        locations.add(location);
                    }
                }
            }

            return locations;
        }

        private List<Location> getAdjacentLocations(final Location location, boolean ignoreUnits) {
            List<Location> locations = new ArrayList<Location>();

            Location target = new Location(location.row - 1, location.col);
            if (isFree(target, ignoreUnits)) {
                locations.add(target);
            }

            target = new Location(location.row, location.col + 1);
            if (isFree(target, ignoreUnits)) {
                locations.add(target);
            }

            target = new Location(location.row + 1, location.col);
            if (isFree(target, ignoreUnits)) {
                locations.add(target);
            }

            target = new Location(location.row, location.col - 1);
            if (isFree(target, ignoreUnits)) {
                locations.add(target);
            }

            return locations;
        }

        private boolean isFree(Location location, boolean ignoreUnits) {
            if (walls.contains(location)) {
                return false;
            }

            if (ignoreUnits) {
                return true;
            }

            for (Unit unit : units) {
                if (unit.location.equals(location)) {
                    return false;
                }
            }

            return true;
        }
    }

    private enum UnitType {
        ELF, GOBLIN;
    }

    private static class Unit {

        private static final Comparator<Unit> TURN_ORDER = new Comparator<Unit>() {
            @Override
            public int compare(Unit a, Unit b) {
                return a.location.compareTo(b.location);
            }
        };

        private static final Comparator<Unit> TARGET_ORDER = new Comparator<Unit>() {
            @Override
            public int compare(Unit a, Unit b) {
                int hpCmp = Integer.compare(a.hp, b.hp);
                if (hpCmp == 0) {
                    return a.location.compareTo(b.location);
                } else {
                    return hpCmp;
                }
            }
        };

        private int hp;
        private Location location;
        private UnitType type;
        private UnitType enemyType;
        private Cave cave;

        Unit(UnitType type, UnitType enemyType, Location location, Cave cave) {
            this.hp = 200;
            this.type = type;
            this.enemyType = enemyType;
            this.location = location;
            this.cave = cave;
        }

        boolean attack() {
            if (DEBUG) System.out.println("    -- Attacking --");
            List<Unit> enemies = cave.getUnits(enemyType);

            List<Unit> possibleTargets = new ArrayList<Unit>();
            for (Location adjacent : cave.getAdjacentLocations(location, true)) {
                for (Unit enemy : enemies) {
                    if (enemy.location.equals(adjacent)) {
                        possibleTargets.add(enemy);
                    }
                }
            }

            if (possibleTargets.isEmpty()) {
                if (DEBUG) {
                    System.out.println("    Can't attack, no adjacent targets");
                    System.out.println();
                }
                return false;
            }

            Collections.sort(possibleTargets, TARGET_ORDER);
            if (DEBUG) {
                System.out.println("    Targets in range:");
                for (Unit target : possibleTargets) {
                    System.out.println("      " + target);
                }
                System.out.println();
            }

            Unit attackTarget = possibleTargets.get(0);
            attackTarget.hp -= 3;
            if (DEBUG) System.out.println("    Attacking target: " + attackTarget);
            if (attackTarget.hp <= 0) {
                cave.units.remove(attackTarget);
                if (DEBUG) System.out.println("    Unit is killed");
            }

            if (DEBUG) System.out.println();

            return true;
        }

        boolean move() {
            if (DEBUG) System.out.println("    -- Moving --");
            List<Unit> targets = cave.getUnits(enemyType);

            List<Location> locationsInRange = cave.getAdjacentLocations(targets);
            if (locationsInRange.isEmpty()) {
                if (DEBUG) {
                    System.out.println("    Can't move, no adjacent locations");
                    System.out.println();
                }
                return false;
            }

            if (DEBUG) {
                System.out.println("    Locations in range:");
                for (Location location : locationsInRange) {
                    System.out.println("      " + location);
                }
                System.out.println();
            }

            List<Path> possiblePaths = new ArrayList<Path>();
            for (Location locationInRange : locationsInRange) {
                Path p = cave.pathTo(location, locationInRange);
                if (p != null) {
                    possiblePaths.add(p);
                }
            }

            if (possiblePaths.isEmpty()) {
                if (DEBUG) {
                    System.out.println("    Can't move to targets, no paths");
                    System.out.println();
                }
                return false;
            }

            Collections.sort(possiblePaths);
            if (DEBUG) {
                System.out.println("    Possible paths:");
                for (Path path : possiblePaths) {
                    System.out.println("      " + path);
                }
                System.out.println();
            }

            Path path = possiblePaths.get(0);
            if (DEBUG) {
                System.out.println("    Chosen path: " + path);
                System.out.println("    Moved from " + location + " to " + path.next());
                System.out.println();
            }

            this.location = path.next();
            return true;
        }

        @Override
        public String toString() {
            return String.format("%s: %d HP, %s", type, hp, location);
        }
    }

    private static class Location implements Comparable<Location> {

        private final int row;
        private final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return other.row == row && other.col == col;
        }

        @Override
        public int compareTo(Location other) {
            int rowCmp = Integer.compare(row, other.row);
            if (rowCmp == 0) {
                return Integer.compare(col, other.col);
            } else {
                return rowCmp;
            }
        }

        @Override
        public String toString() {
            return row + "," + col;
        }
    }

    private static class Path implements Comparable<Path> {

        private final List<Location> steps;

        Path(List<Location> steps) {
            this.steps = steps;
        }

        Location next() {
            return steps.get(1);
        }

        int size() {
            return steps.size();
        }

        @Override
        public int compareTo(Path other) {
            int sizeCmp = Integer.compare(size(), other.size());
            if (sizeCmp == 0) {
                return next().compareTo(other.next());
            } else {
                return sizeCmp;
            }
        }

        @Override
        public String toString() {
            return String.format("%s -> %s, %s (%d)", 
                steps.get(0), steps.get(steps.size() - 1), steps.get(1), steps.size());
        }
    }
}

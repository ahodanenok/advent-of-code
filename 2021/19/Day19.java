import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;


/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2021/day/19
 */
public class Day19 {

    private static final int BEACONS_OVERLAP_THRESHOLD = 12;

    public static void main(String[] args) throws Exception {
        Trench map = buildMap(getInput());
        part1(map);
        part2(map);
    }

    private static void part1(Trench map) {
        System.out.println("Part 1: " + map.beacons.size());
    }

    private static void part2(Trench map) {
        int maxDistance = Integer.MIN_VALUE;
        List<Location> scannerLocations = new ArrayList<>(map.scanners);
        for (int i = 0; i < scannerLocations.size(); i++) {
            for (int j = i + 1; j < scannerLocations.size(); j++) {
                int d = scannerLocations.get(i).distanceTo(scannerLocations.get(j));
                if (d > maxDistance) {
                    maxDistance = d;
                }
            }
        }

        System.out.println("Part 2: " + maxDistance);
    }

    private static List<Orientation> getOrientations() {
        List<Orientation> orientations = new ArrayList<>();
        for (Axis x : Axis.values()) {
            for (Axis y : Axis.values()) {
                if (y == x || y == x.negate()) {
                    continue;
                }

                for (Axis z : Axis.values()) {
                    if (z == x || z == x.negate() || z == y || z == y.negate()) {
                        continue;
                    }

                    orientations.add(new Orientation(x, y, z));
                }
            }
        }

        return orientations;
    }

    private static Location getScannerLocation(Orientation orient, Location from, Orientation offsetOrient, Location offset) {
        Axis baseX = orient.baseFor(Axis.X_POS);
        Axis offsetX = offsetOrient.baseFor(Axis.X_POS);//offsetOrient.mappedBy(baseX);
        int x = baseX.get(from) - offsetX.get(offset);

        Axis baseY = orient.baseFor(Axis.Y_POS);
        Axis offsetY = offsetOrient.baseFor(Axis.Y_POS);//offsetOrient.mappedBy(baseY);
        int y = baseY.get(from) - offsetY.get(offset);

        Axis baseZ = orient.baseFor(Axis.Z_POS);
        Axis offsetZ = offsetOrient.baseFor(Axis.Z_POS);//offsetOrient.mappedBy(baseZ);
        int z = baseZ.get(from) - offsetZ.get(offset);

        return new Location(x, y, z);
    }

    private static Location getBeaconLocation(Orientation orient, Location scanner, Location offset) {
        return new Location(
            orient.baseFor(Axis.X_POS).get(offset) + scanner.x,
            orient.baseFor(Axis.Y_POS).get(offset) + scanner.y,
            orient.baseFor(Axis.Z_POS).get(offset) + scanner.z
        );
    }

    private static List<Scanner> getInput() throws Exception {
        List<Scanner> scanners = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Scanner current = null;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    if (current != null) {
                        scanners.add(current);
                        current = null;
                    }

                    continue;
                }

                if (line.startsWith("---")) {
                    current = new Scanner(Integer.parseInt(line.split(" ")[2].trim()));
                } else {
                    String[] parts = line.split(",");
                    current.beacons.add(new Location(
                        Integer.parseInt(parts[0].trim()),
                        Integer.parseInt(parts[1].trim()),
                        Integer.parseInt(parts[2].trim())
                    ));
                }
            }

            scanners.add(current);
        }

        return scanners;
    }

    private static Trench buildMap(List<Scanner> scanners) {
        // assuming the first is 0 scanner
        Trench map = new Trench();
        map.beacons.addAll(scanners.get(0).beacons);
        map.scanners.add(new Location(0, 0, 0));

        LinkedList<Scanner> queue = new LinkedList<>();
        for (int i = 1; i < scanners.size(); i++) {
            queue.offer(scanners.get(i));
        }

        List<Orientation> orientations = getOrientations();

        next:
        while (!queue.isEmpty()) {
            Scanner current = queue.poll();
            for (Orientation orientA : orientations) {
                for (Orientation orientB : orientations) {
                    for (Location ca : new HashSet<>(map.beacons)) {
                        for (Location cb : current.beacons) {
                            Location scannerLocation = getScannerLocation(orientA, ca, orientB, cb);

                            int overlapCount = 0;
                            for (Location b : current.beacons) {
                                Location beaconLocation = getBeaconLocation(orientB, scannerLocation, b);
                                if (map.beacons.contains(beaconLocation)) {
                                    overlapCount++;
                                }

                                if (overlapCount == BEACONS_OVERLAP_THRESHOLD) {
                                    break;
                                }
                            }

                            if (overlapCount == BEACONS_OVERLAP_THRESHOLD) {
                                map.scanners.add(scannerLocation);
                                for (Location b : current.beacons) {
                                    map.beacons.add(getBeaconLocation(orientB, scannerLocation, b));
                                }

                                continue next;
                            }
                        }
                    }
                }
            }

            queue.offer(current);
        }

        return map;
    }

    private static class Trench {

        final Set<Location> scanners = new HashSet<>();
        final Set<Location> beacons = new HashSet<>();
    }

    private static class Scanner {

        final int id;
        final Set<Location> beacons = new HashSet<>();

        Scanner(int id) {
            this.id = id;
        }
    }

    private static class Location {

        final int x;
        final int y;
        final int z;

        Location(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int distanceTo(Location other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return x == other.x && y == other.y && z == other.z;
        }

        @Override
        public int hashCode() {
            return 31 * 31 * x + 31 * y + z;
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }
    }

    private enum Axis {
        X_POS {
            @Override
            public int get(Location location) {
                return location.x;
            }

            @Override
            public Axis negate() {
                return Axis.X_NEG;
            }
        },
        X_NEG {
            @Override
            public int get(Location location) {
                return -location.x;
            }

            @Override
            public Axis negate() {
                return Axis.X_POS;
            }
        },
        Y_POS {
            @Override
            public int get(Location location) {
                return location.y;
            }

            @Override
            public Axis negate() {
                return Axis.Y_NEG;
            }
        },
        Y_NEG {
            @Override
            public int get(Location location) {
                return -location.y;
            }

            @Override
            public Axis negate() {
                return Axis.Y_POS;
            }
        },
        Z_POS {
            @Override
            public int get(Location location) {
                return location.z;
            }

            @Override
            public Axis negate() {
                return Axis.Z_NEG;
            }
        },
        Z_NEG {
            @Override
            public int get(Location location) {
                return -location.z;
            }

            @Override
            public Axis negate() {
                return Axis.Z_POS;
            }
        };

        abstract int get(Location location);
        abstract Axis negate();
    }

    private static class Orientation {

        private final Map<Axis, Axis> mappings = new HashMap<>();

        Orientation(Axis x, Axis y, Axis z) {
            mappings.put(Axis.X_POS, x);
            mappings.put(Axis.X_NEG, x.negate());
            mappings.put(Axis.Y_POS, y);
            mappings.put(Axis.Y_NEG, y.negate());
            mappings.put(Axis.Z_POS, z);
            mappings.put(Axis.Z_NEG, z.negate());
        }

        Axis baseFor(Axis axis) {
            return mappings.get(axis);
        }

        @Override
        public String toString() {
            return String.format("(%s, %s, %s)",
                mappings.get(Axis.X_POS), 
                mappings.get(Axis.Y_POS), 
                mappings.get(Axis.Z_POS));
        }
    }
}

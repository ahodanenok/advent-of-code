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

    /*private static final List<Orientation> ORIENTATIONS = Arrays.asList(

        new Orientation(Axis.X_NEG, Axis.Y_POS, Axis.Z_POS), // (-x, y, z)
        new Orientation(Axis.X_NEG, Axis.Y_NEG, Axis.Z_POS), // (-x, -y, z)
        new Orientation(Axis.X_NEG, Axis.Y_POS, Axis.Z_NEG), // (-x, y, -z)
        new Orientation(Axis.X_NEG, Axis.Y_NEG, Axis.Z_NEG), // (-x, -y, -z)
        new Orientation(Axis.X_NEG, Axis.Z_POS, Axis.Y_POS), // (-x, z, y)
        new Orientation(Axis.X_NEG, Axis.Z_POS, Axis.Y_NEG), // (-x, z, -y)
        new Orientation(Axis.X_NEG, Axis.Z_NEG, Axis.Y_POS), // (-x, -z, y)
        new Orientation(Axis.X_NEG, Axis.Z_NEG, Axis.Y_NEG), // (-x, -z, -y)
        
        new Orientation(Axis.Y_NEG, Axis.X_POS, Axis.Z_POS), // (-y, x, z)
        new Orientation(Axis.Y_NEG, Axis.X_NEG, Axis.Z_POS), // (-y, -x, z)
        new Orientation(Axis.Y_NEG, Axis.X_POS, Axis.Z_NEG), // (-y, x, -z)
        new Orientation(Axis.Y_NEG, Axis.X_NEG, Axis.Z_NEG), // (-y, -x, -z)
        new Orientation(Axis.Y_NEG, Axis.Z_POS, Axis.X_POS), // (-y, z, x)
        new Orientation(Axis.Y_NEG, Axis.Z_POS, Axis.X_NEG), // (-y, z, -x)
        new Orientation(Axis.Y_NEG, Axis.Z_NEG, Axis.X_POS), // (-y, -z, x)
        new Orientation(Axis.Y_NEG, Axis.Z_NEG, Axis.X_NEG), // (-y, -z, -x)

        new Orientation(Axis.Z_NEG, Axis.X_POS, Axis.Y_POS), // (-z, x, y)
        new Orientation(Axis.Z_NEG, Axis.X_NEG, Axis.Y_POS), // (-z, -x, y)
        new Orientation(Axis.Z_NEG, Axis.X_POS, Axis.Y_NEG), // (-z, x, -y)
        new Orientation(Axis.Z_NEG, Axis.X_NEG, Axis.Y_NEG), // (-z, -x, -y)
        new Orientation(Axis.Z_NEG, Axis.Y_POS, Axis.X_POS), // (-z, y, x)
        new Orientation(Axis.Z_NEG, Axis.Y_POS, Axis.X_NEG), // (-z, y, -x)
        new Orientation(Axis.Z_NEG, Axis.Y_NEG, Axis.X_POS), // (-z, -y, x)
        new Orientation(Axis.Z_NEG, Axis.Y_NEG, Axis.X_NEG),  // (-z, -y, -x)

        new Orientation(Axis.X_POS, Axis.Y_POS, Axis.Z_POS), // (x, y, z)
        new Orientation(Axis.X_POS, Axis.Y_NEG, Axis.Z_POS), // (x, -y, z)
        new Orientation(Axis.X_POS, Axis.Y_POS, Axis.Z_NEG), // (x, y, -z)
        new Orientation(Axis.X_POS, Axis.Y_NEG, Axis.Z_NEG), // (x, -y, -z)
        new Orientation(Axis.X_POS, Axis.Z_POS, Axis.Y_POS), // (x, z, y)
        new Orientation(Axis.X_POS, Axis.Z_POS, Axis.Y_NEG), // (x, z, -y)
        new Orientation(Axis.X_POS, Axis.Z_NEG, Axis.Y_POS), // (x, -z, y)
        new Orientation(Axis.X_POS, Axis.Z_NEG, Axis.Y_NEG), // (x, -z, -y)
        
        new Orientation(Axis.Y_POS, Axis.X_POS, Axis.Z_POS), // (y, x, z)
        new Orientation(Axis.Y_POS, Axis.X_NEG, Axis.Z_POS), // (y, -x, z)
        new Orientation(Axis.Y_POS, Axis.X_POS, Axis.Z_NEG), // (y, x, -z)
        new Orientation(Axis.Y_POS, Axis.X_NEG, Axis.Z_NEG), // (y, -x, -z)
        new Orientation(Axis.Y_POS, Axis.Z_POS, Axis.X_POS), // (y, z, x)
        new Orientation(Axis.Y_POS, Axis.Z_POS, Axis.X_NEG), // (y, z, -x)
        new Orientation(Axis.Y_POS, Axis.Z_NEG, Axis.X_POS), // (y, -z, x)
        new Orientation(Axis.Y_POS, Axis.Z_NEG, Axis.X_NEG), // (y, -z, -x)

        new Orientation(Axis.Z_POS, Axis.X_POS, Axis.Y_POS), // (z, x, y)
        new Orientation(Axis.Z_POS, Axis.X_NEG, Axis.Y_POS), // (z, -x, y)
        new Orientation(Axis.Z_POS, Axis.X_POS, Axis.Y_NEG), // (z, x, -y)
        new Orientation(Axis.Z_POS, Axis.X_NEG, Axis.Y_NEG), // (z, -x, -y)
        new Orientation(Axis.Z_POS, Axis.Y_POS, Axis.X_POS), // (z, y, x)
        new Orientation(Axis.Z_POS, Axis.Y_POS, Axis.X_NEG), // (z, y, -x)
        new Orientation(Axis.Z_POS, Axis.Y_NEG, Axis.X_POS), // (z, -y, x)
        new Orientation(Axis.Z_POS, Axis.Y_NEG, Axis.X_NEG)  // (z, -y, -x)
    );*/

    public static void main(String[] args) throws Exception {
        List<Scanner> scanners = getInput();
        part1(scanners);
    }

    private static void part1(List<Scanner> scanners) {
    getOrientations();
        Scanner result = buildMap(scanners);
        System.out.println("Part 1: " + result.beacons.size());
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

    private static Scanner buildMap(List<Scanner> scanners) {
        // assuming the first is 0 scanner
        Scanner result = scanners.get(0);

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
                    for (Location ca : new HashSet<>(result.beacons)) {
                        for (Location cb : current.beacons) {
                            Location scannerLocation = getScannerLocation(orientA, ca, orientB, cb);

                            int overlapCount = 0;
                            for (Location b : current.beacons) {
                                Location beaconLocation = getBeaconLocation(orientB, scannerLocation, b);
                                if (result.beacons.contains(beaconLocation)) {
                                    overlapCount++;
                                }

                                if (overlapCount == BEACONS_OVERLAP_THRESHOLD) {
                                    break;
                                }
                            }

                            if (overlapCount == BEACONS_OVERLAP_THRESHOLD) {
                                for (Location b : current.beacons) {
                                    result.beacons.add(getBeaconLocation(orientB, scannerLocation, b));
                                }

                                continue next;
                            }
                        }
                    }
                }
            }

            queue.offer(current);
        }

        return result;
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

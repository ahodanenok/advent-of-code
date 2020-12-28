import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2020/day/17
 */
public class Day17 {

    public static void main(String[] args) throws Exception {
        Map<Location, Boolean> cubes = getCubes();

        Map<Location, Boolean> currentCubes = new HashMap<>(cubes);
        for (int i = 0; i < 6; i++) {
            Map<Location, Boolean> considerCubes = new HashMap<>(currentCubes);
            for (Location location : currentCubes.keySet()) {
                for (Location neighbour : location.neighbours()) {
                    if (!considerCubes.containsKey(neighbour)) {
                        considerCubes.put(neighbour, false);
                    }
                }
            }

            Map<Location, Boolean> nextCubes = new HashMap<>();
            for (Map.Entry<Location, Boolean> cube : considerCubes.entrySet()) {
                int nc = 0;
                for (Location neighbour : cube.getKey().neighbours()) {
                    if (considerCubes.getOrDefault(neighbour, false)) {
                        nc++;
                    }
                }

                if (cube.getValue()) {
                    nextCubes.put(cube.getKey(), nc == 2 || nc == 3);
                } else {
                    nextCubes.put(cube.getKey(), nc == 3);
                }
            }

            currentCubes = nextCubes;
        }

        int activeCount = 0;
        for (Boolean active : currentCubes.values()) {
            if (active) {
                activeCount++;
            }
        }

        System.out.println("Part 1: " + activeCount);
    }

    private static Map<Location, Boolean> getCubes() throws Exception {
        Map<Location, Boolean> cubes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    cubes.put(new Location(x, y, 0), line.charAt(x) == '#');
                }

                y++;
            }
        }

        return cubes;
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

        List<Location> neighbours() {
            List<Location> list = new ArrayList<>();
            for (int dz = -1; dz < 2; dz++) {
                for (int dy = -1; dy < 2; dy++) {
                    for (int dx = -1; dx < 2; dx++) {
                        Location n = new Location(x + dx, y + dy, z + dz);
                        if (!n.equals(this)) {
                            list.add(n);
                        }
                    }
                }
            }

            return list;
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
            return String.format("(%d, %d, %d)", x, y, z);
        }
    }
}

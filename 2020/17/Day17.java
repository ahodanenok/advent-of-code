import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2020/day/17
 */
public class Day17 {

    public static void main(String[] args) throws Exception {
        Map<Location, Boolean> cubes = getCubes();
        part1(cubes);
        part2(cubes);
    }

    private static void part1(Map<Location, Boolean> cubes) {
        int activeCount = simulate(cubes, location -> {
            List<Location> list = new ArrayList<>();
            for (int dz = -1; dz < 2; dz++) {
                for (int dy = -1; dy < 2; dy++) {
                    for (int dx = -1; dx < 2; dx++) {
                        Location n = new Location(location.x + dx, location.y + dy, location.z + dz, location.w);
                        if (!n.equals(location)) {
                            list.add(n);
                        }
                    }
                }
            }

            return list;
        });

        System.out.println("Part 1: " + activeCount);
    }

    private static void part2(Map<Location, Boolean> cubes) {
        int activeCount = simulate(cubes, location -> {
            List<Location> list = new ArrayList<>();
            for (int dz = -1; dz < 2; dz++) {
                for (int dy = -1; dy < 2; dy++) {
                    for (int dx = -1; dx < 2; dx++) {
                        for (int dw = -1; dw < 2; dw++) {
                            Location n = new Location(location.x + dx, location.y + dy, location.z + dz, location.w + dw);
                            if (!n.equals(location)) {
                                list.add(n);
                            }
                        }
                    }
                }
            }

            return list;
        });

        System.out.println("Part 2: " + activeCount);
    }

    private static int simulate(Map<Location, Boolean> cubes, Function<Location, List<Location>> neighbours) {
        Map<Location, Boolean> currentCubes = cubes;
        for (int i = 0; i < 6; i++) {
            Map<Location, Boolean> considerCubes = new HashMap<>(currentCubes);
            for (Location location : currentCubes.keySet()) {
                for (Location neighbour : neighbours.apply(location)) {
                    if (!considerCubes.containsKey(neighbour)) {
                        considerCubes.put(neighbour, false);
                    }
                }
            }

            Map<Location, Boolean> nextCubes = new HashMap<>();
            for (Map.Entry<Location, Boolean> cube : considerCubes.entrySet()) {
                int nc = 0;
                for (Location neighbour : neighbours.apply(cube.getKey())) {
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

        return activeCount;
    }

    private static Map<Location, Boolean> getCubes() throws Exception {
        Map<Location, Boolean> cubes = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            int y = 0;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < line.length(); x++) {
                    cubes.put(new Location(x, y, 0, 0), line.charAt(x) == '#');
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
        final int w;

        Location(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return x == other.x && y == other.y && z == other.z && w == other.w;
        }

        @Override
        public int hashCode() {
            return 31 * 31 * 31 * x + 31 * 31 * y + 31 * z + w;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d, %d)", x, y, z, w);
        }
    }
}

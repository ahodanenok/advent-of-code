import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2022/day/18
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        List<Cube> scan = getScan();
        part1(scan);
    }

    private static void part1(List<Cube> scan) {
        int surfaceArea = 0;

        Set<Cube> cubes = new HashSet<>(scan);
        for (Cube cube : cubes) {
            if (!cubes.contains(new Cube(cube.x - 1, cube.y, cube.z))) {
                surfaceArea++;
            }

            if (!cubes.contains(new Cube(cube.x + 1, cube.y, cube.z))) {
                surfaceArea++;
            }

            if (!cubes.contains(new Cube(cube.x, cube.y - 1, cube.z))) {
                surfaceArea++;
            }

            if (!cubes.contains(new Cube(cube.x, cube.y + 1, cube.z))) {
                surfaceArea++;
            }

            if (!cubes.contains(new Cube(cube.x, cube.y, cube.z - 1))) {
                surfaceArea++;
            }

            if (!cubes.contains(new Cube(cube.x, cube.y, cube.z + 1))) {
                surfaceArea++;
            }
        }

        System.out.println("Part 1: " + surfaceArea);
    }

    private static List<Cube> getScan() throws Exception {
        List<Cube> cubes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                cubes.add(new Cube(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])));
            }
        }

        return cubes;
    }

    private static class Cube {

        final int x;
        final int y;
        final int z;

        Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            return 31 * 31 * x + 31 * y + z;
        }

        @Override
        public boolean equals(Object obj) {
            Cube other = (Cube) obj;
            return x == other.x && y == other.y && z == other.z;
        }
    }
}

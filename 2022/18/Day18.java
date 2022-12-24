import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2022/day/18
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        Set<Cube> scan = getScan();
        part1(scan);
        part2(scan);
    }

    private static void part1(Set<Cube> scan) {
        int surfaceArea = 0;
        for (Cube cube : scan) {
            for (Cube neighbor : List.of(
                    cube.left(), cube.right(), cube.top(), cube.bottom(), cube.front(), cube.back())) {

                if (!scan.contains(neighbor)) {
                    surfaceArea++;
                }
            }
        }

        System.out.println("Part 1: " + surfaceArea);
    }

    private static void part2(Set<Cube> scan) {
        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        int zMin = Integer.MAX_VALUE;
        int zMax = Integer.MIN_VALUE;
        for (Cube cube : scan) {
            xMin = Math.min(cube.x, xMin);
            xMax = Math.max(cube.x, xMax);
            yMin = Math.min(cube.y, yMin);
            yMax = Math.max(cube.y, yMax);
            zMin = Math.min(cube.z, zMin);
            zMax = Math.max(cube.z, zMax);
        }

        LinkedList<Cube> queue = new LinkedList<>();
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                Cube steamFront = new Cube(x, y, zMin);
                if (!scan.contains(steamFront)) {
                    queue.add(steamFront);
                }

                Cube steamBack = new Cube(x, y, zMax);
                if (!scan.contains(steamBack)) {
                    queue.add(steamBack);
                }
            }
        }
        for (int x = xMin; x <= xMax; x++) {
            for (int z = zMin; z <= zMax; z++) {
                Cube steamBottom = new Cube(x, yMin, z);
                if (!scan.contains(steamBottom)) {
                    queue.add(steamBottom);
                }

                Cube steamTop = new Cube(x, yMax, z);
                if (!scan.contains(steamTop)) {
                    queue.add(steamTop);
                }
            }
        }
        for (int y = yMin; y <= yMax; y++) {
            for (int z = zMin; z <= zMax; z++) {
                Cube steamLeft = new Cube(xMin, y, z);
                if (!scan.contains(steamLeft)) {
                    queue.add(steamLeft);
                }

                Cube steamRight = new Cube(xMax, y, z);
                if (!scan.contains(steamRight)) {
                    queue.add(steamRight);
                }
            }
        }

        Set<Cube> steam = new HashSet<>();
        while (!queue.isEmpty()) {
            Cube cube = queue.removeFirst();
            if (steam.contains(cube)) {
                continue;
            }

            steam.add(cube);
            for (Cube neighbor : List.of(
                    cube.left(), cube.right(), cube.top(), cube.bottom(), cube.front(), cube.back())) {

                if (neighbor.x < xMin - 1 || neighbor.x > xMax + 1) {
                    continue;
                }

                if (neighbor.y < yMin - 1 || neighbor.y > yMax + 1) {
                    continue;
                }

                if (neighbor.z < zMin - 1 || neighbor.z > zMax + 1) {
                    continue;
                }

                if (scan.contains(neighbor) || steam.contains(neighbor)) {
                    continue;
                }

                queue.add(neighbor);
            }
        }

        int surfaceArea = 0;
        for (Cube cube : scan) {
            for (Cube neighbor : List.of(
                    cube.left(), cube.right(), cube.top(), cube.bottom(), cube.front(), cube.back())) {

                if (steam.contains(neighbor)) {
                    surfaceArea++;
                }
            }
        }

        System.out.println("Part 2: " + surfaceArea);
    }

    private static Set<Cube> getScan() throws Exception {
        Set<Cube> cubes = new HashSet<>();
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

        Cube left() {
            return new Cube(x - 1, y, z);
        }

        Cube right() {
            return new Cube(x + 1, y, z);
        }

        Cube top() {
            return new Cube(x, y + 1, z);
        }

        Cube bottom() {
            return new Cube(x, y - 1, z);
        }

        Cube front() {
            return new Cube(x, y, z - 1);
        }

        Cube back() {
            return new Cube(x, y, z + 1);
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;
import java.util.Collections;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2021/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        List<Action> actions = getInput();
        part1(actions);
        part2(actions);
    }

    private static void part1(List<Action> actions) {
        List<Action> validActions = new ArrayList<>();
        for (Action a : actions) {
            if (a.cube.xStart >= -50 && a.cube.xEnd <= 50
                        && a.cube.yStart >= -50 && a.cube.yEnd <= 50
                        && a.cube.zStart >= -50 && a.cube.zEnd <= 50) {
                validActions.add(a);
            }
        }

        System.out.println("Part 1: " + calculateEnabledCount(validActions));
    }

    private static void part2(List<Action> actions) {
        System.out.println("Part 2: " + calculateEnabledCount(actions));
    }

    private static BigInteger calculateEnabledCount(List<Action> actions) {
        List<EnabledRegion> enabledRegions = new ArrayList<>();
        for (Action a : actions) {
            for (EnabledRegion r : enabledRegions) {
                r.exclude(a.cube);
            }

            if (a.on) {
                enabledRegions.add(new EnabledRegion(a.cube));
            }
        }

        BigInteger count = BigInteger.ZERO;
        for (EnabledRegion r : enabledRegions) {
            count = count.add(r.count);
        }

        return count;
    }

    private static List<Action> getInput() throws Exception {
        List<Action> actions = new ArrayList<Action>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                boolean on;
                if (line.startsWith("on")) {
                    on = true;
                    line = line.substring(3);
                } else if (line.startsWith("off")) {
                    on = false;
                    line = line.substring(4);
                } else {
                    throw new IllegalStateException("Unknown action: " + line);
                }

                String[] parts = line.split(",");

                String[] xParts = parts[0].split("\\.\\.");
                int xStart = Integer.parseInt(xParts[0].substring(2));
                int xEnd = Integer.parseInt(xParts[1]);

                String[] yParts = parts[1].split("\\.\\.");
                int yStart = Integer.parseInt(yParts[0].substring(2));
                int yEnd = Integer.parseInt(yParts[1]);

                String[] zParts = parts[2].split("\\.\\.");
                int zStart = Integer.parseInt(zParts[0].substring(2));
                int zEnd = Integer.parseInt(zParts[1]);

                actions.add(new Action(on,
                    Math.min(xStart, xEnd), Math.max(xStart, xEnd),
                    Math.min(yStart, yEnd), Math.max(yStart, yEnd),
                    Math.min(zStart, zEnd), Math.max(zStart, zEnd)));
            }
        }

        return actions;
    }

    private static class Action {

        final boolean on;
        final Cube cube;

        Action(boolean on, int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd) {
            this.on = on;
            this.cube = new Cube(xStart, xEnd, yStart, yEnd, zStart, zEnd);
        }

        @Override
        public String toString() {
            return String.format("%s %s", on ? "on" : "off", cube);
        }
    }

    private static class EnabledRegion {

        List<Cube> cubes;
        BigInteger count;

        EnabledRegion(Cube cube) {
            this.cubes = Collections.singletonList(cube);
            this.count = cube.area;
        }

        void exclude(Cube other) {
            List<Cube> newCubes = new ArrayList<>();
            for (Cube cube : cubes) {
                Cube common = cube.intersect(other);
                if (common != null) {
                    count = count.subtract(common.area);
                    newCubes.addAll(cube.exclude(common));
                } else {
                    newCubes.add(cube);
                }
            }

            cubes = newCubes;
        }
    }

    private static class Cube {

        final int xStart;
        final int xEnd;

        final int yStart;
        final int yEnd;

        final int zStart;
        final int zEnd;

        final BigInteger area;

        Cube(int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd) {
            this.xStart = xStart;
            this.xEnd = xEnd;
            this.yStart = yStart;
            this.yEnd = yEnd;
            this.zStart = zStart;
            this.zEnd = zEnd;
            this.area = calculateArea();
        }

        Cube intersect(Cube other) {
            int xIntStart;
            int xIntEnd;
            if (xStart <= other.xEnd && xEnd >= other.xStart) {
                xIntStart = Math.max(xStart, other.xStart);
                xIntEnd = Math.min(xEnd, other.xEnd);
            } else {
                return null;
            }

            int yIntStart;
            int yIntEnd;
            if (yStart <= other.yEnd && yEnd >= other.yStart) {
                yIntStart = Math.max(yStart, other.yStart);
                yIntEnd = Math.min(yEnd, other.yEnd);
            } else {
                return null;
            }

            int zIntStart;
            int zIntEnd;
            if (zStart <= other.zEnd && zEnd >= other.zStart) {
                zIntStart = Math.max(zStart, other.zStart);
                zIntEnd = Math.min(zEnd, other.zEnd);
            } else {
                return null;
            }

            return new Cube(xIntStart, xIntEnd, yIntStart, yIntEnd, zIntStart, zIntEnd);
        }

        List<Cube> exclude(Cube other) {
            List<Cube> remaining = new ArrayList<>();

            if (other.xStart > xStart) {
                remaining.add(new Cube(xStart, other.xStart - 1, yStart, yEnd, zStart, zEnd));
            }

            if (other.xEnd < xEnd) {
                remaining.add(new Cube(other.xEnd + 1, xEnd, yStart, yEnd, zStart, zEnd));
            }

            if (other.yStart > yStart) {
                remaining.add(new Cube(other.xStart, other.xEnd, yStart, other.yStart - 1, zStart, zEnd));
            }

            if (other.yEnd < yEnd) {
                remaining.add(new Cube(other.xStart, other.xEnd, other.yEnd + 1, yEnd, zStart, zEnd));
            }

            if (other.zStart > zStart) {
                remaining.add(new Cube(other.xStart, other.xEnd, other.yStart, other.yEnd, zStart, other.zStart - 1));
            }

            if (other.zEnd < zEnd) {
                remaining.add(new Cube(other.xStart, other.xEnd, other.yStart, other.yEnd, other.zEnd + 1, zEnd));
            }

            return remaining;
        }

        private BigInteger calculateArea() {
            int width = xEnd - xStart + 1;
            int length = yEnd - yStart + 1;
            int height = zEnd - zStart + 1;

            return BigInteger.valueOf(width)
                .multiply(BigInteger.valueOf(length))
                .multiply(BigInteger.valueOf(height));
        }

        @Override
        public String toString() {
            return String.format("x=%d..%d,y=%d..%d,z=%d..%d", xStart, xEnd, yStart, yEnd, zStart, zEnd);
        }
    }
}

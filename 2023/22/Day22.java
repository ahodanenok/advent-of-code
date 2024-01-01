import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.IdentityHashMap;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2023/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        List<Brick> bricks = getInput();
        part1(bricks);
    }

    private static void part1(List<Brick> bricks) {
        TreeMap<Integer, List<Brick>> bricksByZ = new TreeMap<>();
        for (Brick brick : bricks) {
            bricksByZ.computeIfAbsent(
                Math.min(brick.startPos.z, brick.endPos.z), __ -> new ArrayList<>()).add(brick);
        }

        List<Brick> fallenBricks = new ArrayList<>();
        for (Map.Entry<Integer, List<Brick>> entry : bricksByZ.entrySet()) {
            for (Brick fallingBrick : entry.getValue()) {
                int targetZ = 1;
                for (Brick stillBrick : fallenBricks) {
                    int z = Math.max(stillBrick.startPos.z, stillBrick.endPos.z) + 1;
                    if (fallingBrick.intersectXY(stillBrick) && z > targetZ) {
                        targetZ = z;
                    }
                }

                fallenBricks.add(fallingBrick.moveZ(targetZ));
            }
        }

        Map<Brick, List<Brick>> supportAboveByBrick = new IdentityHashMap<>();
        Map<Brick, List<Brick>> supportBelowByBrick = new IdentityHashMap<>();
        for (int i = 0; i < fallenBricks.size(); i++) {
            Brick a = fallenBricks.get(i);
            List<Brick> supportedBricks = new ArrayList<>();
            for (int j = 0; j < fallenBricks.size(); j++) {
                if (i == j) {
                    continue;
                }

                Brick b = fallenBricks.get(j);
                if (a.adjacentZ(b) && a.intersectXY(b)) {
                    supportedBricks.add(b);
                }
            }

            supportAboveByBrick.put(a, supportedBricks);
            for (Brick b : supportedBricks) {
                supportBelowByBrick.computeIfAbsent(b, __ -> new ArrayList<>()).add(a);
            }
        }

        int count = 0;
        for (Brick brick : fallenBricks) {
            List<Brick> supportedBricks = supportAboveByBrick.get(brick);
            if (supportedBricks.isEmpty()) {
                count++;
                continue;
            }

            int safe = 0;
            for (Brick b : supportedBricks) {
                if (supportBelowByBrick.get(b).size() > 1) {
                    safe++;
                }
            }

            if (safe == supportedBricks.size()) {
                count++;
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static List<Brick> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Brick> bricks = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split("~");
                String[] startParts = parts[0].trim().split(",");
                String[] endParts = parts[1].trim().split(",");
                bricks.add(new Brick(
                    new Position(
                        Integer.parseInt(startParts[0].trim()),
                        Integer.parseInt(startParts[1].trim()),
                        Integer.parseInt(startParts[2].trim())),
                    new Position(
                        Integer.parseInt(endParts[0].trim()),
                        Integer.parseInt(endParts[1].trim()),
                        Integer.parseInt(endParts[2].trim()))));
            }

            return bricks;
        }
    }

    private static class Brick {

        final Position startPos;
        final Position endPos;

        Brick(Position startPos, Position endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }

        boolean intersectXY(Brick other) {
            return Math.min(startPos.x, endPos.x) <= Math.max(other.startPos.x, other.endPos.x)
                && Math.max(startPos.x, endPos.x) >= Math.min(other.startPos.x, other.endPos.x)
                && Math.min(startPos.y, endPos.y) <= Math.max(other.startPos.y, other.endPos.y)
                && Math.max(startPos.y, endPos.y) >= Math.min(other.startPos.y, other.endPos.y);
        }

        boolean adjacentZ(Brick other) {
            int aboveZ = Math.max(startPos.z, endPos.z) + 1;
            return other.startPos.z == aboveZ || other.endPos.z == aboveZ;
        }

        Brick moveZ(int toZ) {
            return new Brick(
                new Position(startPos.x, startPos.y, toZ),
                new Position(endPos.x, endPos.y, toZ + Math.abs(startPos.z - endPos.z)));
        }
    }

    private static class Position {

        final int x;
        final int y;
        final int z;

        Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

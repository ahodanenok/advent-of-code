import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2023/day/17
 */
public class Day17 {

    public static void main(String[] args) throws Exception {
        City city = getInput();
        part1(city);
    }

    private static void part1(City city) {
        Map<Crucible, Integer> heatLosses = new HashMap<>();
        LinkedList<Crucible> queue = new LinkedList<>();
        queue.addLast(new Crucible(new Location(0, 0), Direction.DOWN, 0, 0));
        while (!queue.isEmpty()) {
            Crucible crucible = queue.removeFirst();
            if (crucible.heatLoss >= heatLosses.getOrDefault(crucible, Integer.MAX_VALUE)) {
                continue;
            }
            heatLosses.put(crucible, crucible.heatLoss);

            if (crucible.stepsStraight < 3 && city.contains(crucible.lookForward())) {
                queue.addLast(crucible.moveForward(city.heatLossAt(crucible.lookForward())));
            }
            if (city.contains(crucible.lookLeft())) {
                queue.addLast(crucible.moveLeft(city.heatLossAt(crucible.lookLeft())));
            }
            if (city.contains(crucible.lookRight())) {
                queue.addLast(crucible.moveRight(city.heatLossAt(crucible.lookRight())));
            }
        }

        Location destination = new Location(city.height - 1, city.width - 1);
        int heatLossMin = Integer.MAX_VALUE;
        for (Map.Entry<Crucible, Integer> entry : heatLosses.entrySet()) {
            if (entry.getKey().location.equals(destination)) {
                heatLossMin = Math.min(entry.getValue(), heatLossMin);
            }
        }

        System.out.println("Part 1: " + heatLossMin);
    }

    private static City getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }

            int[][] blocks = new int[lines.size()][lines.get(0).length()];
            for (int row = 0; row < lines.size(); row++) {
                for (int col = 0; col < lines.get(row).length(); col++) {
                    blocks[row][col] = lines.get(row).charAt(col) - '0';
                }
            }

            return new City(blocks.length, blocks[0].length, blocks);
        }
    }

    private static class City {

        final int width;
        final int height;
        final int[][] blocks;

        City(int width, int height, int[][] blocks) {
            this.width = width;
            this.height = height;
            this.blocks = blocks;
        }

        boolean contains(Location location) {
            return location.row >= 0 && location.row < height
                && location.col >= 0 && location.col < width;
        }

        int heatLossAt(Location location) {
            return blocks[location.row][location.col];
        }
    }

    private static class Crucible {

        final Location location;
        final Direction direction;

        final int heatLoss;
        final int stepsStraight;

        Crucible(Location location, Direction direction, int heatLoss, int stepsStraight) {
            this.location = location;
            this.direction = direction;
            this.heatLoss = heatLoss;
            this.stepsStraight = stepsStraight;
        }

        Location lookForward() {
            return direction.next(location);
        }

        Location lookLeft() {
            return direction.turnLeft().next(location);
        }

        Location lookRight() {
            return direction.turnRight().next(location);
        }

        Crucible moveForward(int blockHeatLoss) {
            return new Crucible(direction.next(location), direction, heatLoss + blockHeatLoss, stepsStraight + 1);
        }

        Crucible moveLeft(int blockHeatLoss) {
            Direction left = direction.turnLeft();
            return new Crucible(left.next(location), left, heatLoss + blockHeatLoss, 1);
        }

        Crucible moveRight(int blockHeatLoss) {
            Direction right = direction.turnRight();
            return new Crucible(right.next(location), right, heatLoss + blockHeatLoss, 1);
        }

        @Override
        public int hashCode() {
            return 31 * 31 * location.hashCode() + 31 * direction.hashCode() + stepsStraight;
        }

        @Override
        public boolean equals(Object obj) {
            Crucible other = (Crucible) obj;
            return location.equals(other.location)
                && direction.equals(other.direction)
                && stepsStraight == other.stepsStraight;
        }
    }

    private static class Location {

        final int row;
        final int col;

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
            return row == other.row && col == other.col;
        }
    }

    private enum Direction {

        UP {
            @Override
            public Direction turnLeft() {
                return Direction.LEFT;
            }

            @Override
            public Direction turnRight() {
                return Direction.RIGHT;
            }

            @Override
            public Location next(Location location) {
                return new Location(location.row - 1, location.col);
            }
        },
        DOWN {
            @Override
            public Direction turnLeft() {
                return Direction.RIGHT;
            }

            @Override
            public Direction turnRight() {
                return Direction.LEFT;
            }

            @Override
            public Location next(Location location) {
                return new Location(location.row + 1, location.col);
            }
        },
        LEFT {
            @Override
            public Direction turnLeft() {
                return Direction.DOWN;
            }

            @Override
            public Direction turnRight() {
                return Direction.UP;
            }

            @Override
            public Location next(Location location) {
                return new Location(location.row, location.col - 1);
            }
        },
        RIGHT {
            @Override
            public Direction turnLeft() {
                return Direction.UP;
            }

            @Override
            public Direction turnRight() {
                return Direction.DOWN;
            }

            @Override
            public Location next(Location location) {
                return new Location(location.row, location.col + 1);
            }
        };

        abstract Direction turnLeft();
        abstract Direction turnRight();
        abstract Location next(Location location);
    }
}

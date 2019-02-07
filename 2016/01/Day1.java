import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2016/day/1
 */
public class Day1 {

    public static void main(String[] args) {
        List<Instruction> instructions = getInstructions();
        Location startingLocation = new Location(0, 0);
        part1(startingLocation, instructions);
    }

    private static void part1(Location startingLocation, List<Instruction> instructions) {
        Destination dest = Destination.NORTH;
        Location location = new Location(0, 0);
        for (Instruction inst : instructions) {
            if ("L".equals(inst.turn)) {
                dest = dest.turnLeft();
            } else if ("R".equals(inst.turn)) {
                dest = dest.turnRight();
            } else {
                throw new IllegalArgumentException("Unknown turn: " + inst.turn);
            }

            location = dest.move(location, inst.steps);
        }

        System.out.println(startingLocation.distanceTo(location));
    }

    private static List<Instruction> getInstructions() {
        List<Instruction> instructions = new ArrayList<Instruction>();

        String line = new Scanner(System.in).nextLine();
        for (String str : line.split(", ")) {
            instructions.add(new Instruction("" + str.charAt(0), Integer.parseInt(str.substring(1))));
        }

        return instructions;
    }

    private enum Destination {
        NORTH {
            @Override
            Destination turnLeft() {
                return WEST;
            }

            @Override
            Destination turnRight() {
                return EAST;
            }

            @Override
            Location move(Location location, int steps) {
                return new Location(location.row - steps, location.col);
            }
        },
        EAST {
            @Override
            Destination turnLeft() {
                return NORTH;
            }

            @Override
            Destination turnRight() {
                return SOUTH;
            }

            @Override
            Location move(Location location, int steps) {
                return new Location(location.row, location.col + steps);
            }
        },
        SOUTH {
            @Override
            Destination turnLeft() {
                return EAST;
            }

            @Override
            Destination turnRight() {
                return WEST;
            }

            @Override
            Location move(Location location, int steps) {
                return new Location(location.row + steps, location.col);
            }
        },
        WEST {
            @Override
            Destination turnLeft() {
                return SOUTH;
            }

            @Override
            Destination turnRight() {
                return NORTH;
            }

            @Override
            Location move(Location location, int steps) {
                return new Location(location.row, location.col - steps);
            }
        };

        abstract Destination turnLeft();
        abstract Destination turnRight();
        abstract Location move(Location location, int steps);
    }

    private static class Instruction {

        private final String turn;
        private final int steps;

        Instruction(String turn, int steps) {
            this.turn = turn;
            this.steps = steps;
        }

        @Override
        public String toString() {
            return "Instruction: " + turn + steps;
        }
    }

    private static class Location {

        private final int row;
        private final int col;

        Location (int row, int col) {
            this.row = row;
            this.col = col;
        }

        private int distanceTo(Location location) {
            return Math.abs(row - location.row) + Math.abs(col - location.col);
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
    }
}

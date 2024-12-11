import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2024/day/10
 */
public class Day10 {

    public static void main(String... args) throws Exception {
        Map<Position, Integer> map = getInput();
        part1(map);
        part2(map);
    }

    private static void part1(Map<Position, Integer> map) {
        int totalScore = 0;
        for (Map.Entry<Position, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 0) {
                continue;
            }

            Set<Position> reachablePositions = new HashSet<>();
            LinkedList<Position> queue = new LinkedList<>();
            queue.add(entry.getKey());
            while (!queue.isEmpty()) {
                Position position = queue.poll();
                if (map.get(position) == 9) {
                    reachablePositions.add(position);
                    continue;
                }

                for (Position nextPosition : List.of(
                        new Position(position.row - 1, position.col),
                        new Position(position.row + 1, position.col),
                        new Position(position.row, position.col - 1),
                        new Position(position.row, position.col + 1))) {
                    if (map.getOrDefault(nextPosition, Integer.MAX_VALUE) == map.get(position) + 1) {
                        queue.addLast(nextPosition);
                    }
                }
            }

            totalScore += reachablePositions.size();
        }

        System.out.println("Part 1: " + totalScore);
    }
    
    private static void part2(Map<Position, Integer> map) {
        int totalRating = 0;
        for (Map.Entry<Position, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 0) {
                continue;
            }

            int rating = 0;
            LinkedList<Position> queue = new LinkedList<>();
            queue.add(entry.getKey());
            while (!queue.isEmpty()) {
                Position position = queue.poll();
                if (map.get(position) == 9) {
                    rating++;
                    continue;
                }

                for (Position nextPosition : List.of(
                        new Position(position.row - 1, position.col),
                        new Position(position.row + 1, position.col),
                        new Position(position.row, position.col - 1),
                        new Position(position.row, position.col + 1))) {
                    if (map.getOrDefault(nextPosition, Integer.MAX_VALUE) == map.get(position) + 1) {
                        queue.addLast(nextPosition);
                    }
                }
            }

            totalRating += rating;
        }

        System.out.println("Part 2: " + totalRating);
    }

    private static Map<Position, Integer> getInput() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));

        Map<Position, Integer> map = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            for (int col = 0; col < lines.get(row).length(); col++) {
                map.put(new Position(row, col), lines.get(row).charAt(col) - '0');
            }
        }

        return map;
    }

    private static class Position {

        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }
}

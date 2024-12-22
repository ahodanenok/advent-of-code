import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2024/day/20
 */
public class Day20 {

	public static void main(String... args) throws Exception {
        Racetrack track = getInput();
        part1(track);
        part2(track);
    }

    private static void part1(Racetrack track) {
        Map<Position, Integer> distances = race(track);
        int fairDistance = distances.get(track.end);

        Set<Cheat> cheats = new HashSet<>();
        for (Position wall : track.walls) {
            for (Position a : List.of(wall.top(), wall.bottom(),wall.left(), wall.right())) {
                for (Position b : List.of(wall.top(), wall.bottom(),wall.left(), wall.right())) {
                    if (a.equals(b)) {
                        continue;
                    }

                    if (!distances.containsKey(a) || !distances.containsKey(b)) {
                        continue;
                    }

                    int distanceA = distances.get(a);
                    int distanceB = distances.get(b);
                    if (distanceA < distanceB && (distanceB - distanceA - 2) >= 100) {
                        cheats.add(new Cheat(a, b));
                    }
                }
            }
        }

        System.out.println("Part 1: " + cheats.size());
    }

    private static void part2(Racetrack track) {
        Map<Position, Integer> distances = race(track);
        int fairDistance = distances.get(track.end);

        Set<Cheat> cheats = new HashSet<>();
        for (Position a : distances.keySet()) {
            for (Position b : distances.keySet()) {
                if (a.equals(b)) {
                    continue;
                }

                int distanceBetween = Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
                if (distanceBetween > 20) {
                    continue;
                }

                int distanceA = distances.get(a);
                int distanceB = distances.get(b);
                if (distanceA < distanceB && (distanceB - distanceA - distanceBetween) >= 100) {
                    cheats.add(new Cheat(a, b));
                }
            }
        }

        System.out.println("Part 2: " + cheats.size());
    }

    private static Map<Position, Integer> race(Racetrack track) {
        Map<Position, Integer> distances = new HashMap<>();
        distances.put(track.start, 0);

        LinkedList<Position> queue = new LinkedList<>();
        queue.add(track.start);
        while (!queue.isEmpty()) {
            Position position = queue.poll();
            if (position.equals(track.end)) {
                continue;
            }

            for (Position next : List.of(
                    position.top(), position.bottom(),
                    position.left(), position.right())) {
                if (distances.containsKey(next)) {
                    continue;
                }

                if (track.walls.contains(next)) {
                    continue;
                }

                distances.put(next, distances.get(position) + 1);
                queue.add(next);
            }
        }

        return distances;
    }

    private static Racetrack getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Set<Position> walls = new HashSet<>();
            Position start = null;
            Position end = null;
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    switch (line.charAt(col)) {
                        case '.' -> {}
                        case '#' -> walls.add(new Position(row, col));
                        case 'S' -> start = new Position(row, col);
                        case 'E' -> end = new Position(row, col);
                        default -> throw new IllegalStateException();
                    }
                }

                row++;
            }

            return new Racetrack(walls, start, end);
        }
    }

    private static class Racetrack {

        final Set<Position> walls;
        final Position start;
        final Position end;

        Racetrack(Set<Position> walls, Position start, Position end) {
            this.walls = walls;
            this.start = start;
            this.end = end;
        }
    }

    private static class Cheat {

        final Position from;
        final Position to;

        Cheat(Position from, Position to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object obj) {
            Cheat other = (Cheat) obj;
            return from.equals(other.from) && to.equals(other.to);
        }

        @Override
        public int hashCode() {
            return 31 * from.hashCode() + to.hashCode();
        }
    }

    private static class Position {

        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Position top() {
            return new Position(row - 1, col);
        }

        Position bottom() {
            return new Position(row + 1, col);
        }

        Position left() {
            return new Position(row, col - 1);
        }

        Position right() {
            return new Position(row, col + 1);
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

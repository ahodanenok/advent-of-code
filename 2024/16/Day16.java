import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2024/day/16
 */
public class Day16 {

    public static void main(String... args) throws Exception {
        Maze maze = getInput();
        part1(maze);
        part2(maze);
    }

    private static void part1(Maze maze) {
        int endScore = Integer.MAX_VALUE;
        Map<Reindeer, Integer> scores = new HashMap<>();
        LinkedList<Reindeer> queue = new LinkedList<>();
        queue.add(new Reindeer(maze.start, Direction.EAST, 0, null));
        while (!queue.isEmpty()) {
            Reindeer reindeer = queue.poll();
            if (reindeer.tile.equals(maze.end)) {
                endScore = Math.min(reindeer.score, endScore);
                continue;
            }

            int tileScore = scores.getOrDefault(reindeer, Integer.MAX_VALUE);
            if (reindeer.score >= tileScore) {
                continue;
            }

            scores.put(reindeer, reindeer.score);
            if (!maze.walls.contains(reindeer.facing.next(reindeer.tile))) {
                queue.add(new Reindeer(
                    reindeer.facing.next(reindeer.tile),
                    reindeer.facing,
                    reindeer.score + 1,
                    null));
            }
            queue.add(new Reindeer(
                reindeer.tile,
                reindeer.facing.clockwise(),
                reindeer.score + 1000,
                null));
            queue.add(new Reindeer(
                reindeer.tile,
                reindeer.facing.counterclockwise(),
                reindeer.score + 1000,
                null));
        }

        System.out.println("Part 1: " + endScore);
    }

    private static void part2(Maze maze) {
        LinkedList<Reindeer> endScores = new LinkedList<>();
        Map<Reindeer, Integer> scores = new HashMap<>();
        LinkedList<Reindeer> queue = new LinkedList<>();
        queue.add(new Reindeer(maze.start, Direction.EAST, 0, Set.of(maze.start)));
        while (!queue.isEmpty()) {
            Reindeer reindeer = queue.poll();
            if (reindeer.tile.equals(maze.end)) {
                endScores.add(reindeer);
                continue;
            }

            int tileScore = scores.getOrDefault(reindeer, Integer.MAX_VALUE);
            if (reindeer.score > tileScore) {
                continue;
            }

            scores.put(reindeer, reindeer.score);
            if (!maze.walls.contains(reindeer.facing.next(reindeer.tile))) {
                Tile nextTile = reindeer.facing.next(reindeer.tile);
                Set<Tile> newVisitedTiles;
                if (!reindeer.visitedTiles.contains(nextTile)) {
                    newVisitedTiles = new HashSet<>(reindeer.visitedTiles);
                    newVisitedTiles.add(nextTile);
                } else {
                    newVisitedTiles = reindeer.visitedTiles;
                }

                queue.add(new Reindeer(
                    nextTile,
                    reindeer.facing,
                    reindeer.score + 1,
                    newVisitedTiles));
            }
            queue.add(new Reindeer(
                reindeer.tile,
                reindeer.facing.clockwise(),
                reindeer.score + 1000,
                reindeer.visitedTiles));
            queue.add(new Reindeer(
                reindeer.tile,
                reindeer.facing.counterclockwise(),
                reindeer.score + 1000,
                reindeer.visitedTiles));
        }

        int minScore = endScores.stream()
            .mapToInt(r -> r.score)
            .min()
            .orElse(Integer.MAX_VALUE);

        Set<Tile> bestSpots = new HashSet<>();
        for (Reindeer reindeer : endScores) {
            if (reindeer.score == minScore) {
                bestSpots.addAll(reindeer.visitedTiles);
            }
        }

        System.out.println("Part 2: " + bestSpots.size());
    }

    private static Maze getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Set<Tile> walls = new HashSet<>();
            Tile start = null;
            Tile end = null;
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    switch (line.charAt(col)) {
                        case '.' -> {}
                        case '#' -> walls.add(new Tile(row, col));
                        case 'S' -> start = new Tile(row, col);
                        case 'E' -> end = new Tile(row, col);
                        default -> throw new IllegalStateException();
                    }
                }

                row++;
            }

            return new Maze(walls, start, end);
        }
    }

    private static class Maze {

        final Set<Tile> walls;
        final Tile start;
        final Tile end;

        Maze(Set<Tile> walls, Tile start, Tile end) {
            this.walls = walls;
            this.start = start;
            this.end = end;
        }
    }

    private static class Reindeer {

        final Tile tile;
        final Direction facing;
        final int score;
        final Set<Tile> visitedTiles;

        Reindeer(Tile tile, Direction facing, int score, Set<Tile> visitedTiles) {
            this.tile = tile;
            this.facing = facing;
            this.score = score;
            this.visitedTiles = visitedTiles;
        }

        @Override
        public boolean equals(Object obj) {
            Reindeer other = (Reindeer) obj;
            return tile.equals(other.tile) && facing == other.facing;
        }

        @Override
        public int hashCode() {
            return 31 * tile.hashCode() + facing.hashCode();
        }
    }

    private static class Tile {

        final int row;
        final int col;

        Tile(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Tile other = (Tile) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    private enum Direction {

        NORTH {
            Tile next(Tile tile) {
                return new Tile(tile.row - 1, tile.col);
            }

            Direction clockwise() {
                return Direction.EAST;
            }

            Direction counterclockwise() {
                return Direction.WEST;
            }
        },
        SOUTH {
            Tile next(Tile tile) {
                return new Tile(tile.row + 1, tile.col);
            }

            Direction clockwise() {
                return Direction.WEST;
            }

            Direction counterclockwise() {
                return Direction.EAST;
            }
        },
        EAST {
            Tile next(Tile tile) {
                return new Tile(tile.row, tile.col + 1);
            }

            Direction clockwise() {
                return Direction.SOUTH;
            }

            Direction counterclockwise() {
                return Direction.NORTH;
            }
        },
        WEST {
            Tile next(Tile tile) {
                return new Tile(tile.row, tile.col - 1);
            }

            Direction clockwise() {
                return Direction.NORTH;
            }

            Direction counterclockwise() {
                return Direction.SOUTH;
            }
        };

        abstract Tile next(Tile tile);
        abstract Direction clockwise();
        abstract Direction counterclockwise();
    }
}

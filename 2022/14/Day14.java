import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2022/day/14
 */
public class Day14 {

    public static void main(String[] args) throws Exception {
        List<List<Coordinate>> shapes = getShapes();
        part1(shapes);
        part2(shapes);
    }

    private static void part1(List<List<Coordinate>> shapes) {
        Coordinate sandSource = new Coordinate(500, 0);

        int xMin = sandSource.x;
        int xMax = sandSource.x;
        int yMax = Integer.MIN_VALUE;
        for (List<Coordinate> shape : shapes) {
            for (Coordinate coord : shape) {
                xMin = Math.min(coord.x, xMin);
                xMax = Math.max(coord.x, xMax);
                yMax = Math.max(coord.y, yMax);
            }
        }

        boolean [][] cave = new boolean[yMax + 1][xMax - xMin + 1];
        for (List<Coordinate> shape : shapes) {
            for (int i = 1; i < shape.size(); i++) {
                Coordinate prev = shape.get(i - 1);
                Coordinate curr = shape.get(i);
                if (prev.x != curr.x) {
                    int to = Math.max(prev.x, curr.x);
                    for (int x = Math.min(prev.x, curr.x); x < to; x++) {
                        cave[curr.y][x - xMin] = true;
                    }
                } else if (prev.y != curr.y) {
                    int to = Math.max(prev.y, curr.y);
                    for (int y = Math.min(prev.y, curr.y); y < to; y++) {
                        cave[y][curr.x - xMin] = true;
                    }
                } else {
                    throw new IllegalStateException("Bad shape!");
                }

                cave[prev.y][prev.x - xMin] = true;
                cave[curr.y][curr.x - xMin] = true;
            }
        }

        int sandCount = 0;

        flow:
        while (true) {
            int x = sandSource.x;
            int y = sandSource.y;
            while (true) {
                if (y + 1 > yMax) {
                    // flows into the abyss
                    break flow;
                } else if (!cave[y + 1][x - xMin]) {
                    y++;
                } else if (x - 1 < xMin) {
                    // flows into the abyss
                    break flow;
                } else if (!cave[y + 1][x - 1 - xMin]) {
                    x--;
                    y++;
                } else if (x + 1 > xMax) {
                    // flows into the abyss
                    break flow;
                } else if (!cave[y + 1][x + 1 - xMin]) {
                    x++;
                    y++;
                } else {
                    cave[y][x - xMin] = true;
                    sandCount++;
                    // comes to rest
                    continue flow;
                }
            }
        }

        System.out.println("Part 1: " + sandCount);
    }

    private static void part2(List<List<Coordinate>> shapes) {
        Coordinate sandSource = new Coordinate(500, 0);

        int width = sandSource.x * 2;
        int yMax = Integer.MIN_VALUE;
        for (List<Coordinate> shape : shapes) {
            for (Coordinate coord : shape) {
                yMax = Math.max(coord.y, yMax);
            }
        }
        yMax += 2;

        boolean [][] cave = new boolean[yMax + 1][width];
        for (List<Coordinate> shape : shapes) {
            for (int i = 1; i < shape.size(); i++) {
                Coordinate prev = shape.get(i - 1);
                Coordinate curr = shape.get(i);
                if (prev.x != curr.x) {
                    int to = Math.max(prev.x, curr.x);
                    for (int x = Math.min(prev.x, curr.x); x < to; x++) {
                        cave[curr.y][x] = true;
                    }
                } else if (prev.y != curr.y) {
                    int to = Math.max(prev.y, curr.y);
                    for (int y = Math.min(prev.y, curr.y); y < to; y++) {
                        cave[y][curr.x] = true;
                    }
                } else {
                    throw new IllegalStateException("Bad shape!");
                }

                cave[prev.y][prev.x] = true;
                cave[curr.y][curr.x] = true;
            }
        }
        // add floor
        for (int x = 0; x < width; x++) {
            cave[yMax][x] = true;
        }

        int sandCount = 0;

        flow:
        while (true) {
            int x = sandSource.x;
            int y = sandSource.y;
            while (true) {
                if (!cave[y + 1][x]) {
                    y++;
                } else if (!cave[y + 1][x - 1]) {
                    x--;
                    y++;
                } else if (!cave[y + 1][x + 1]) {
                    x++;
                    y++;
                } else {
                    cave[y][x] = true;
                    sandCount++;
                    if (x == sandSource.x && y == sandSource.y) {
                        // sand source is blocked
                        break flow;
                    } else {
                        // comes to rest
                        continue flow;
                    }
                }
            }
        }

        System.out.println("Part 2: " + sandCount);
    }

    private static List<List<Coordinate>> getShapes() throws Exception {
        List<List<Coordinate>> shapes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Coordinate> shape = new ArrayList<>();
                for (String coord : line.split(" -> ")) {
                    String[] parts = coord.split(",");
                    shape.add(new Coordinate(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                }

                shapes.add(shape);
            }
        }

        return shapes;
    }

    private static class Coordinate {

        final int x;
        final int y;

        Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

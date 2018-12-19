import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2018/day/10
 */
public class Day10 {

    public static void main(String[] args) {
        movePoints(getPoints());
    }

    private static void movePoints(List<Point> points) {
        int seconds = 0;
        while (true) {
            int rowStart = Integer.MAX_VALUE;
            int rowEnd = Integer.MIN_VALUE;
            int colStart = Integer.MAX_VALUE;
            int colEnd = Integer.MIN_VALUE;
            for (Point p : points) {
                rowStart = Math.min(p.y, rowStart);
                rowEnd = Math.max(p.y, rowEnd);
                colStart = Math.min(p.x, colStart);
                colEnd = Math.max(p.x, colEnd);
            }

            int height = rowEnd - rowStart + 1;
            int width = colEnd - colStart + 1;
            if (height > 0 && height < 20) {
                boolean[][] grid = new boolean[height][width];
                for (Point p : points) {
                    grid[p.y - rowStart][p.x - colStart] = true;
                }

                System.out.println();
                System.out.println(seconds);
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        if (grid[row][col]) {
                            System.out.print('#');
                        } else {
                            System.out.print('.');
                        }
                    }
                    System.out.println();
                }

                break;
            }

            for (Point p : points) {
                p.move();
            }
            
            seconds++;
        }
    }

    private static List<Point> getPoints() {
        List<Point> points = new ArrayList<Point>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            int idx;
            String[] parts;

            idx = line.indexOf('<');
            parts = line.substring(idx + 1, line.indexOf('>', idx)).trim().split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());

            idx = line.indexOf('<', idx + 1);
            parts = line.substring(idx + 1, line.indexOf('>', idx)).trim().split(",");
            int ax = Integer.parseInt(parts[0].trim());
            int ay = Integer.parseInt(parts[1].trim());

            points.add(new Point(x, y, ax, ay));
        }

        return points;
    }

    private static class Point {

        private int x;
        private int y;

        private int ax;
        private int ay;

        private Point(int x, int y, int ax, int ay) {
            this.x = x;
            this.y = y;
            this.ax = ax;
            this.ay = ay;
        }

        private void move() {
            x = x + ax;
            y = y + ay;
        }

        @Override
        public String toString() {
            return String.format("position=<%d, %d> velocity=<%d, %d>", x, y, ax, ay);
        }
    }
}

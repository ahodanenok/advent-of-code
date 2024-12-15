import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2024/day/14
 */
public class Day14 {

    public static void main(String... args) throws Exception {
        List<Robot> robots = getInput();
        part1(robots);
    }

    private static void part1(List<Robot> robots) {
        int height = 103;
        int width = 101;
        int seconds = 100;
        int[][] space = new int[height][width];
        for (Robot robot : robots) {
            int x = (seconds * width + robot.px + seconds * robot.vx) % width;
            int y = (seconds * height + robot.py + seconds * robot.vy) % height;
            space[y][x]++;
        }

        int sf1 = 0;
        for (int y = 0; y < height / 2; y++) {
            for (int x = 0; x < width / 2; x++) {
                sf1 += space[y][x];
            }
        }

        int sf2 = 0;
        for (int y = height / 2 + 1; y < height; y++) {
            for (int x = 0; x < width / 2; x++) {
                sf2 += space[y][x];
            }
        }

        int sf3 = 0;
        for (int y = 0; y < height / 2; y++) {
            for (int x = width / 2 + 1; x < width; x++) {
                sf3 += space[y][x];
            }
        }

        int sf4 = 0;
        for (int y = height / 2 + 1; y < height; y++) {
            for (int x = width / 2 + 1; x < width; x++) {
                sf4 += space[y][x];
            }
        }

        System.out.println("Part 1: " + (sf1 * sf2 * sf3 * sf4));
    }

    private static List<Robot> getInput() throws Exception {
        List<Robot> robots = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Pattern pattern = Pattern.compile("^p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)$");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                robots.add(new Robot(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4))));
            }
        }

        return robots;
    }

    private static class Robot {

        final int px;
        final int py;
        final int vx;
        final int vy;

        Robot(int px, int py, int vx, int vy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
        }
    }
}

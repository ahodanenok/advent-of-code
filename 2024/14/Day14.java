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

    private static final int SPACE_HEIGHT = 103;
    private static final int SPACE_WIDTH = 101;

    public static void main(String... args) throws Exception {
        List<Robot> robots = getInput();
        part1(robots);
        part2(robots);
    }

    private static void part1(List<Robot> robots) {
        int seconds = 100;
        int[][] space = new int[SPACE_HEIGHT][SPACE_WIDTH];
        for (Robot robot : robots) {
            int x = (seconds * SPACE_WIDTH + robot.px + seconds * robot.vx) % SPACE_WIDTH;
            int y = (seconds * SPACE_HEIGHT + robot.py + seconds * robot.vy) % SPACE_HEIGHT;
            space[y][x]++;
        }

        int sf1 = 0;
        for (int y = 0; y < SPACE_HEIGHT / 2; y++) {
            for (int x = 0; x < SPACE_WIDTH / 2; x++) {
                sf1 += space[y][x];
            }
        }

        int sf2 = 0;
        for (int y = SPACE_HEIGHT / 2 + 1; y < SPACE_HEIGHT; y++) {
            for (int x = 0; x < SPACE_WIDTH / 2; x++) {
                sf2 += space[y][x];
            }
        }

        int sf3 = 0;
        for (int y = 0; y < SPACE_HEIGHT / 2; y++) {
            for (int x = SPACE_WIDTH / 2 + 1; x < SPACE_WIDTH; x++) {
                sf3 += space[y][x];
            }
        }

        int sf4 = 0;
        for (int y = SPACE_HEIGHT / 2 + 1; y < SPACE_HEIGHT; y++) {
            for (int x = SPACE_WIDTH / 2 + 1; x < SPACE_WIDTH; x++) {
                sf4 += space[y][x];
            }
        }

        System.out.println("Part 1: " + (sf1 * sf2 * sf3 * sf4));
    }

    private static void part2(List<Robot> robots) throws Exception {
        int seconds = 0;
        int[][] space = new int[SPACE_HEIGHT][SPACE_WIDTH];
        for (Robot robot : robots) {
            space[robot.py][robot.px]++;
        }

        while (seconds < 10000) {
            List<Robot> newRobots = new ArrayList<>();
            for (Robot robot : robots) {
                int x = (SPACE_WIDTH + robot.px + robot.vx) % SPACE_WIDTH;
                int y = (SPACE_HEIGHT + robot.py + robot.vy) % SPACE_HEIGHT;
                space[y][x]++;
                space[robot.py][robot.px]--;
                newRobots.add(new Robot(x, y, robot.vx, robot.vy));
            }
            robots = newRobots;
            seconds++;

            for (int y = 0; y < SPACE_HEIGHT; y++) {
                for (int x = 0; x < SPACE_WIDTH; x++) {
                    if (space[y][x] > 0) {
                        System.out.print('x');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
            System.out.println(seconds);
            System.out.println();
        }
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

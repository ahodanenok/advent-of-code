import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2023/day/2
 */
public class Day2 {

    public static void main(String[] args) throws Exception {
        List<Game> games = getInput();
        part1(games);
        part2(games);
    }

    private static void part1(List<Game> games) {
        int sum = 0;

        next:
        for (Game game : games) {
            for (Cubes cubes : game.cubesRevealed) {
                if (cubes.red > 12 || cubes.green > 13 || cubes.blue > 14) {
                    continue next;
                }
            }

            sum += game.id;
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<Game> games) {
        int sum = 0;
        for (Game game : games) {
            int redMax = Integer.MIN_VALUE;
            int blueMax = Integer.MIN_VALUE;
            int greenMax = Integer.MIN_VALUE;
            for (Cubes cubes : game.cubesRevealed) {
                redMax = Math.max(cubes.red, redMax);
                blueMax = Math.max(cubes.blue, blueMax);
                greenMax = Math.max(cubes.green, greenMax);
            }

            sum += (redMax * blueMax * greenMax);
        }

        System.out.println("Part 2: " + sum);
    }

    private static List<Game> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Game> games = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                Game game = new Game();                

                String[] parts = line.split(":");
                game.id = Integer.parseInt(parts[0].split(" ")[1].trim());
                for (String cubesSet : parts[1].split(";")) {
                    Cubes cubes = new Cubes();
                    for (String cubesCount : cubesSet.split(",")) {
                        parts = cubesCount.trim().split(" ");
                        if (parts[1].trim().equals("red")) {
                            cubes.red = Integer.parseInt(parts[0].trim());
                        }
                        if (parts[1].trim().equals("blue")) {
                            cubes.blue = Integer.parseInt(parts[0].trim());
                        }
                        if (parts[1].equals("green")) {
                            cubes.green = Integer.parseInt(parts[0].trim());
                        }
                    }

                    game.cubesRevealed.add(cubes);
                }                

                games.add(game);
            }

            return games;
        }
    }

    private static class Game {

        int id;
        List<Cubes> cubesRevealed = new ArrayList<>();
    }

    private static class Cubes {

        int red;
        int blue;
        int green;
    }
}

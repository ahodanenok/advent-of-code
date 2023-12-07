import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2023/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        List<Race> races = getInput();
        part1(races);
    }

    private static void part1(List<Race> races) {
        int waysCountTotal = 1;
        for (Race race : races) {
            int waysCount = 0;
            for (int t = 1; t < race.timeAllowed; t++) {
                if (t * (race.timeAllowed - t) > race.bestDistance) {
                    waysCount++;
                }
            }

            waysCountTotal *= waysCount;
        }

        System.out.println("Part 1: " + waysCountTotal);
    }

    private static List<Race> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Race> races = new ArrayList<>();

            String[] times = reader.readLine().trim().split(":")[1].trim().split("\\s++");
            String[] distances = reader.readLine().trim().split(":")[1].trim().split("\\s++");
            for (int i = 0; i < times.length; i++) {
                races.add(new Race(
                    Integer.parseInt(times[i].trim()),
                    Integer.parseInt(distances[i].trim())));
            }

            return races;
        }
    }

    private static class Race {

        final int timeAllowed;
        final int bestDistance;

        Race(int timeAllowed, int bestDistance) {
            this.timeAllowed = timeAllowed;
            this.bestDistance = bestDistance;
        }
    }
}

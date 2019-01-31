import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2015/day/14
 */
public class Day14 {

    public static void main(String[] args) {
        List<Reindeer> deers = getReindeers();
        part1(deers);
    }

    private static void part1(List<Reindeer> deers) {
        int time = 2503;
        int maxDistance = Integer.MIN_VALUE;
        for (Reindeer deer : deers) {
            int cycleTime = deer.flyingTime + deer.restingTime;
            int cyclesCount = time / cycleTime;
            int totalFlyingTime = cyclesCount * deer.flyingTime + Math.min(time % cycleTime, deer.flyingTime);
            maxDistance = Math.max(deer.flyingSpeed * totalFlyingTime, maxDistance);
        }

        System.out.println(maxDistance);
    }

    private static List<Reindeer> getReindeers() {
        List<Reindeer> deers = new ArrayList<Reindeer>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            deers.add(new Reindeer(
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[6]),
                Integer.parseInt(parts[13])));
        }

        return deers;
    }

    private static class Reindeer {

        private int flyingSpeed;
        private int flyingTime;
        private int restingTime;

        Reindeer(int flyingSpeed, int flyingTime, int restingTime) {
            this.flyingSpeed = flyingSpeed;
            this.flyingTime = flyingTime;
            this.restingTime = restingTime;
        }
    }
}

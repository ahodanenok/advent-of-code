import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2015/day/14
 */
public class Day14 {

    private static final int TIME = 2503;

    public static void main(String[] args) {
        List<Reindeer> deers = getReindeers();
        part1(deers);
        part2(deers);
    }

    private static void part1(List<Reindeer> deers) {
        int maxDistance = Integer.MIN_VALUE;
        for (Reindeer deer : deers) {
            int cycleTime = deer.flyingTime + deer.restingTime;
            int cyclesCount = TIME / cycleTime;
            int totalFlyingTime = cyclesCount * deer.flyingTime + Math.min(TIME % cycleTime, deer.flyingTime);
            maxDistance = Math.max(deer.flyingSpeed * totalFlyingTime, maxDistance);
        }

        System.out.println(maxDistance);
    }

    private static void part2(List<Reindeer> deers) {
        int[] points = new int[deers.size()];
        int[] distance = new int[deers.size()];
        int[] startAt = new int[deers.size()];

        for (int t = 0; t < TIME; t++) {
            for (int i = 0; i < deers.size(); i++) {
                Reindeer deer = deers.get(i);
                if (startAt[i] < t - deer.flyingTime + 1) {
                    startAt[i] += deer.flyingTime + deer.restingTime;
                }

                if (t >= startAt[i]) {
                    distance[i] += deer.flyingSpeed;
                }
            }

            int maxDistance = Integer.MIN_VALUE;
            for (int i = 0; i < distance.length; i++) {
                maxDistance = Math.max(distance[i], maxDistance);
            }

            for (int i = 0; i < distance.length; i++) {
                if (distance[i] == maxDistance) {
                    points[i]++;
                }
            }
        }

        int maxPoints = Integer.MIN_VALUE;
        for (int i = 0; i < points.length; i++) {
            maxPoints = Math.max(points[i], maxPoints);
        }

        System.out.println(maxPoints);
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

        private final int flyingSpeed;
        private final int flyingTime;
        private final int restingTime;

        Reindeer(int flyingSpeed, int flyingTime, int restingTime) {
            this.flyingSpeed = flyingSpeed;
            this.flyingTime = flyingTime;
            this.restingTime = restingTime;
        }
    }
}

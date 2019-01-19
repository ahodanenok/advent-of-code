import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2018/day/23
 */
public class Day23 {

    public static void main(String[] args) {
        List<NanoBot> bots = getBots();
        part1(bots);
        part2(bots);
    }

    private static void part1(List<NanoBot> bots) {
        NanoBot strongestBot = bots.get(0);
        for (NanoBot bot : bots) {
            if (bot.radius > strongestBot.radius) {
                strongestBot = bot;
            }
        }

        int botsInRange = 0;
        for (NanoBot bot : bots) {
            if (bot.inRange(strongestBot)) {
                botsInRange++;
            }
        }

        System.out.println(botsInRange);
    }

    private static void part2(List<NanoBot> bots) {
        // with the help of this awesome solution
        // https://www.reddit.com/r/adventofcode/comments/a8s17l/2018_day_23_solutions/ecdqzdg

        Map<Long, Integer> intervals = new TreeMap<Long, Integer>();
        for (NanoBot bot : bots) {
            long d = Math.abs(bot.x) + Math.abs(bot.y) + Math.abs(bot.z);
            intervals.put(d - bot.radius, 1);
            intervals.put(d + bot.radius + 1, -1);
        }

        int overlapCount = 0;
        int maxOverlapCount = Integer.MIN_VALUE;
        long distanceToOrigin = Integer.MAX_VALUE;
        for (java.util.Map.Entry<Long, Integer> entry : intervals.entrySet()) {
            overlapCount += entry.getValue();
            if (overlapCount > maxOverlapCount) {
                maxOverlapCount = overlapCount;
                distanceToOrigin = entry.getKey();
            }
        }

        System.out.println(distanceToOrigin);
    }

    private static List<NanoBot> getBots() {
        List<NanoBot> bots = new ArrayList<NanoBot>();
 
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(">, ");
            String[] coords = parts[0].trim().substring(5).split(",");

            bots.add(new NanoBot(
                Long.parseLong(coords[0].trim()),
                Long.parseLong(coords[1].trim()),
                Long.parseLong(coords[2].trim()),
                Long.parseLong(parts[1].trim().substring(2))));
        }

        return bots;
    }

    private static class NanoBot {

        private final long x;
        private final long y;
        private final long z;

        private final long radius;

        NanoBot(long x, long y, long z, long radius) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.radius = radius;
        }

        boolean inRange(NanoBot other) {
            long distance = Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
            //System.out.println(this + " <-> " + other + ", distance=" + distance);
            return distance <= other.radius;
        }

        @Override
        public String toString() {
            return String.format("pos=<%d,%d,%d>, r=%d", x, y, z, radius);
        }
    }
}
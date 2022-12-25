import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2022/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        List<Blueprint> blueprints = getBlueprints();
        part1(blueprints);
    }

    private static void part1(List<Blueprint> blueprints) {
        int qualityLevel = 0;
        for (Blueprint b : blueprints) {
            qualityLevel += simulate(b, new Minute(0, 0, 0, 0, 0, 1, 0, 0, 0)) * b.num;
        }

        System.out.println("Part 1: " + qualityLevel);
    }

    private static int simulate(Blueprint b, Minute m) {
        if (m.num == 24) {
            return m.geodeOpened;
        }

        int geodeOpenedMax = Integer.MIN_VALUE;

        int oreCollected = m.oreRobots;
        int clayCollected = m.clayRobots;
        int obsidianCollected = m.obsidianRobots;
        int geodeOpened = m.geodeRobots;

        if (b.geodeRobot.enoughResources(m)) {
            geodeOpenedMax = Math.max(
                geodeOpenedMax,
                simulate(b, new Minute(
                    m.num + 1,
                    m.geodeOpened + geodeOpened,
                    m.oreCollected + oreCollected - b.geodeRobot.oreCost,
                    m.clayCollected + clayCollected - b.geodeRobot.clayCost,
                    m.obsidianCollected + obsidianCollected - b.geodeRobot.obsidianCost,
                    m.oreRobots,
                    m.clayRobots,
                    m.obsidianRobots,
                    m.geodeRobots + 1)));
        }

        if (b.obsidianRobot.enoughResources(m)) {
            geodeOpenedMax = Math.max(
                geodeOpenedMax,
                simulate(b, new Minute(
                    m.num + 1,
                    m.geodeOpened + geodeOpened,
                    m.oreCollected + oreCollected - b.obsidianRobot.oreCost,
                    m.clayCollected + clayCollected - b.obsidianRobot.clayCost,
                    m.obsidianCollected + obsidianCollected - b.obsidianRobot.obsidianCost,
                    m.oreRobots,
                    m.clayRobots,
                    m.obsidianRobots + 1,
                    m.geodeRobots)));
        }

        if (b.clayRobot.enoughResources(m)) {
            geodeOpenedMax = Math.max(
                geodeOpenedMax,
                simulate(b, new Minute(
                    m.num + 1,
                    m.geodeOpened + geodeOpened,
                    m.oreCollected + oreCollected - b.clayRobot.oreCost,
                    m.clayCollected + clayCollected - b.clayRobot.clayCost,
                    m.obsidianCollected + obsidianCollected - b.clayRobot.obsidianCost,
                    m.oreRobots,
                    m.clayRobots + 1,
                    m.obsidianRobots,
                    m.geodeRobots)));
        }
 
        if (m.oreRobots <= Math.max(Math.max(b.geodeRobot.oreCost, b.obsidianRobot.oreCost), b.clayRobot.oreCost) && b.oreRobot.enoughResources(m)) {
            geodeOpenedMax = Math.max(
                geodeOpenedMax,
                simulate(b, new Minute(
                    m.num + 1,
                    m.geodeOpened + geodeOpened,
                    m.oreCollected + oreCollected - b.oreRobot.oreCost,
                    m.clayCollected + clayCollected - b.oreRobot.clayCost,
                    m.obsidianCollected + obsidianCollected - b.oreRobot.obsidianCost,
                    m.oreRobots + 1,
                    m.clayRobots,
                    m.obsidianRobots,
                    m.geodeRobots)));
        }

        {
            geodeOpenedMax = Math.max(
                geodeOpenedMax,
                simulate(b, new Minute(
                    m.num + 1,
                    m.geodeOpened + geodeOpened,
                    m.oreCollected + oreCollected,
                    m.clayCollected + clayCollected,
                    m.obsidianCollected + obsidianCollected,
                    m.oreRobots,
                    m.clayRobots,
                    m.obsidianRobots,
                    m.geodeRobots)));
        }

        return geodeOpenedMax;
    }

    private static List<Blueprint> getBlueprints() throws Exception {
        List<Blueprint> blueprints = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Pattern p = Pattern.compile(
                "Blueprint ([0-9]+): Each ore robot costs ([0-9]+) ore. Each clay robot costs ([0-9]+) ore. Each obsidian robot costs ([0-9]+) ore and ([0-9]+) clay. Each geode robot costs ([0-9]+) ore and ([0-9]+) obsidian.");

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (!m.find()) {
                    throw new IllegalStateException(line);
                }

                Robot oreRobot = new Robot(Integer.parseInt(m.group(2)), 0, 0);
                Robot clayRobot = new Robot(Integer.parseInt(m.group(3)), 0, 0);
                Robot obsidianRobot = new Robot(Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), 0);
                Robot geodeRobot = new Robot(Integer.parseInt(m.group(6)), 0, Integer.parseInt(m.group(7)));

                blueprints.add(new Blueprint(
                    Integer.parseInt(m.group(1)), oreRobot, clayRobot, obsidianRobot, geodeRobot));
            }
        }

        return blueprints;
    }

    private static class Minute {

        final int num;

        final int geodeOpened;
        final int oreCollected;
        final int clayCollected;
        final int obsidianCollected;

        final int oreRobots;
        final int clayRobots;
        final int obsidianRobots;
        final int geodeRobots;

        Minute(int num, int geodeOpened, int oreCollected, int clayCollected, int obsidianCollected,
                int oreRobots, int clayRobots, int obsidianRobots, int geodeRobots) {
            this.num = num;
            this.geodeOpened = geodeOpened;
            this.oreCollected = oreCollected;
            this.clayCollected = clayCollected;
            this.obsidianCollected = obsidianCollected;
            this.oreRobots = oreRobots;
            this.clayRobots = clayRobots;
            this.obsidianRobots = obsidianRobots;
            this.geodeRobots = geodeRobots;
        }
    }

    private static class Blueprint {

        final int num;

        final Robot oreRobot;
        final Robot clayRobot;
        final Robot obsidianRobot;
        final Robot geodeRobot;

        Blueprint(int num, Robot oreRobot, Robot clayRobot, Robot obsidianRobot, Robot geodeRobot) {
            this.num = num;
            this.oreRobot = oreRobot;
            this.clayRobot = clayRobot;
            this.obsidianRobot = obsidianRobot;
            this.geodeRobot = geodeRobot;
        }
    }

    private static class Robot {

        final int oreCost;
        final int clayCost;
        final int obsidianCost;

        Robot(int oreCost, int clayCost, int obsidianCost) {
            this.oreCost = oreCost;
            this.clayCost = clayCost;
            this.obsidianCost = obsidianCost;
        }

        boolean enoughResources(Minute minute) {
            if (oreCost > minute.oreCollected) {
                return false;
            }

            if (clayCost > minute.clayCollected) {
                return false;
            }

            if (obsidianCost > minute.obsidianCollected) {
                return false;
            }

            return true;
        }
    }
}

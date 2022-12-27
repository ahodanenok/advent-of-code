import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
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
        part2(blueprints);
    }

    private static void part1(List<Blueprint> blueprints) {
        int qualityLevel = 0;
        for (Blueprint b : blueprints) {
            qualityLevel += simulate(b, 24, new Minute(0, 0, 0, 0, 0, 1, 0, 0, 0)) * b.num;
        }

        System.out.println("Part 1: " + qualityLevel);
    }

    private static void part2(List<Blueprint> blueprints) {
        int product = 1;
        for (int i = 0; i < 3; i++) {
            product *= simulate(blueprints.get(i), 32, new Minute(0, 0, 0, 0, 0, 1, 0, 0, 0));
        }

        System.out.println("Part 2: " + product);
    }

    private static int simulate(Blueprint b, int maxMinutes, Minute m) {
        if (m.num == maxMinutes) {
            return m.geodeOpened;
        }

        int geodeOpenedMax = Integer.MIN_VALUE;

        geodeOpenedMax = Math.max(geodeOpenedMax, simulate(b, maxMinutes, tryBuildRobot(m, maxMinutes, b.geodeRobot, 0, 0, 0, 1)));
        geodeOpenedMax = Math.max(geodeOpenedMax, simulate(b, maxMinutes, tryBuildRobot(m, maxMinutes, b.obsidianRobot, 0, 0, 1, 0)));
        if (m.clayRobots <= b.obsidianRobot.clayCost) {
            geodeOpenedMax = Math.max(geodeOpenedMax, simulate(b, maxMinutes, tryBuildRobot(m, maxMinutes, b.clayRobot, 0, 1, 0, 0)));
        }
        if (m.oreRobots <= Math.max(Math.max(b.geodeRobot.oreCost, b.obsidianRobot.oreCost), b.clayRobot.oreCost)) {
            geodeOpenedMax = Math.max(geodeOpenedMax, simulate(b, maxMinutes, tryBuildRobot(m, maxMinutes, b.oreRobot, 1, 0, 0, 0)));
        }

        return geodeOpenedMax;
    }

    private static Minute tryBuildRobot(Minute m, int maxMinutes, Robot robot, int oreRobotsInc, int clayRobotsInc, int obsidianRobotsInc, int geodeRobotsInc) {
        int n = m.num;
        int oreCollected = m.oreCollected;
        int clayCollected = m.clayCollected;
        int obsidianCollected = m.obsidianCollected;
        int geodeOpened = m.geodeOpened;
        while (n < maxMinutes && (robot.oreCost > oreCollected || robot.clayCost > clayCollected || robot.obsidianCost > obsidianCollected)) {
            n++;
            oreCollected += m.oreRobots;
            clayCollected += m.clayRobots;
            obsidianCollected += m.obsidianRobots;
            geodeOpened += m.geodeRobots;
        }

        if (n < maxMinutes && robot.oreCost <= oreCollected && robot.clayCost <= clayCollected && robot.obsidianCost <= obsidianCollected) {
            return new Minute(
                n + 1,
                geodeOpened + m.geodeRobots,
                oreCollected + m.oreRobots - robot.oreCost,
                clayCollected + m.clayRobots - robot.clayCost,
                obsidianCollected + m.obsidianRobots - robot.obsidianCost,
                m.oreRobots + oreRobotsInc,
                m.clayRobots + clayRobotsInc,
                m.obsidianRobots + obsidianRobotsInc,
                m.geodeRobots + geodeRobotsInc);
        } else {
            return new Minute(
                n,
                geodeOpened,
                oreCollected,
                clayCollected,
                obsidianCollected,
                m.oreRobots,
                m.clayRobots,
                m.obsidianRobots,
                m.geodeRobots);
        }
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
    }
}

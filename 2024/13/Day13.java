import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2024/day/13
 */
public class Day13 {

    public static void main(String... args) throws Exception {
        List<Machine> machines = getInput();
        part1(machines);
        part2(machines);
    }

    private static void part1(List<Machine> machines) {
        int totalCost = 0;
        for (Machine machine : machines) {
            int tokensCount = minTokensByBruteForce(machine, 0, 0, 0, 0, new java.util.HashMap<>());
            if (tokensCount != Integer.MAX_VALUE) {
                totalCost += tokensCount;
            }
        }

        System.out.println("Part 1: " + totalCost);
    }

    private static void part2(List<Machine> machines) {
        long totalCost = 0;
        for (Machine machine : machines) {
            int tokensCount = 0;

            long x = machine.px + 10000000000000L;
            long y = machine.py + 10000000000000L;
            long gcd = BigInteger.valueOf(machine.ax).gcd(BigInteger.valueOf(machine.ay)).longValueExact();
            long lcm = machine.ax * machine.ay / gcd;
            long n = lcm / machine.ax;
            long m = lcm / machine.ay;

            double b = (x * n - y * m) / (double) (machine.bx * n - machine.by * m);
            if (b < 0 || b % 1 != 0) {
                continue;
            }

            double a = (x - machine.bx * b) / (double) machine.ax;
            if (a < 0 || a % 1 != 0) {
                continue;
            }

            totalCost +=  a * Machine.BUTTON_A_COST + b * Machine.BUTTON_B_COST;
        }

        System.out.println("Part 2: " + totalCost);
    }

    private static int minTokensByBruteForce(
            Machine machine, int x, int y, int aPressedCount, int bPressedCount, Map<String, Integer> cache) {
        if (aPressedCount > 100 || bPressedCount > 100) {
            return Integer.MAX_VALUE;
        }

        if (x == machine.px && y == machine.py) {
            return aPressedCount * Machine.BUTTON_A_COST + bPressedCount * Machine.BUTTON_B_COST;
        }

        int aTokensCount;
        if (cache.containsKey(x + "_" + y + "_A")) {
            aTokensCount = cache.get(x + "_" + y + "_A");
        } else {
            aTokensCount = minTokensByBruteForce(
                machine, x + machine.ax, y + machine.ay, aPressedCount + 1, bPressedCount, cache);
            cache.put(x + "_" + y + "_A", aTokensCount);
        }

        int bTokensCount;
        if (cache.containsKey(x + "_" + y + "_B")) {
            bTokensCount = cache.get(x + "_" + y + "_B");
        } else {
            bTokensCount = minTokensByBruteForce(
                machine, x + machine.bx, y + machine.by, aPressedCount, bPressedCount + 1, cache);
            cache.put(x + "_" + y + "_B", bTokensCount);
        }

        return Math.min(aTokensCount, bTokensCount);
    }

    private static List<Machine> getInput() throws Exception {
        List<Machine> machines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Pattern buttonAPattern = Pattern.compile("^Button A: X\\+(\\d+), Y\\+(\\d+)$");
            Pattern buttonBPattern = Pattern.compile("^Button B: X\\+(\\d+), Y\\+(\\d+)$");
            Pattern prizePattern = Pattern.compile("^Prize: X=(\\d+), Y=(\\d+)$");

            String line;
            Matcher matcher;
            int ax, bx, ay, by, px, py;
            while ((line = reader.readLine()) != null) {
                matcher = buttonAPattern.matcher(line);
                matcher.find();
                ax = Integer.parseInt(matcher.group(1));
                ay = Integer.parseInt(matcher.group(2));

                line = reader.readLine();
                matcher = buttonBPattern.matcher(line);
                matcher.find();
                bx = Integer.parseInt(matcher.group(1));
                by = Integer.parseInt(matcher.group(2));

                line = reader.readLine();
                matcher = prizePattern.matcher(line);
                matcher.find();
                px = Integer.parseInt(matcher.group(1));
                py = Integer.parseInt(matcher.group(2));

                machines.add(new Machine(ax, ay, bx, by, px, py));
                reader.readLine();
            }
        }

        return machines;
    }

    private static class Machine {

        static final int BUTTON_A_COST = 3;
        static final int BUTTON_B_COST = 1;

        final int ax;
        final int ay;
        final int bx;
        final int by;
        final int px;
        final int py;

        Machine(int ax, int ay, int bx, int by, int px, int py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2020/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(Input input) {
        int targetBus = Integer.MAX_VALUE;
        long targetTimestamp = Long.MAX_VALUE;
        for (int bus : input.buses) {
            long t = bus * (input.timestamp / bus);
            if (t < input.timestamp) {
                t += bus;
            }

            if (t < targetTimestamp) {
                targetTimestamp = t;
                targetBus = bus;
            }
        }

        System.out.println("Part 1: " + (targetTimestamp - input.timestamp) * targetBus);
    }

    /**
     * With the help from 
     * https://www.reddit.com/r/adventofcode/comments/kc4njx/2020_day_13_solutions/
     * https://www.reddit.com/r/adventofcode/comments/kc4njx/2020_day_13_solutions/ggv5a0s?utm_source=share&utm_medium=web2x&context=3
     */
    private static void part2(Input input) {
        List<Integer> buses = input.buses;
        List<Integer> offsets = input.offsets;

        BigInteger p = BigInteger.ONE;
        for (Integer bus : buses) {
            p = p.multiply(BigInteger.valueOf(bus));
        }

        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < buses.size(); i++) {
            BigInteger b = BigInteger.valueOf(buses.get(i));
            BigInteger r = BigInteger.valueOf(buses.get(i) - offsets.get(i)).mod(b);
            BigInteger m = p.divide(b);
            BigInteger mi = m.modInverse(b);

            result = result.add(r.multiply(m).multiply(mi));
        }

        System.out.println("Part 2: " + result.mod(p));
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int timestamp = Integer.parseInt(reader.readLine());
            List<Integer> buses = new ArrayList<>();
            List<Integer> offsets = new ArrayList<>();

            int offset = 0;
            for (String part : reader.readLine().split(",")) {
                if (!"x".equals(part)) {
                    buses.add(Integer.parseInt(part));
                    offsets.add(offset);
                }

                offset++;
            }

            return new Input(timestamp, buses, offsets);
        }
    }

    private static class Input {

        final int timestamp;
        final List<Integer> buses;
        final List<Integer> offsets;

        Input(int timestamp, List<Integer> buses, List<Integer> offsets) {
            this.timestamp = timestamp;
            this.buses = buses;
            this.offsets = offsets;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2020/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();

        int targetBus = Integer.MAX_VALUE;
        int targetTimestamp = Integer.MAX_VALUE;
        for (int bus : input.buses) {
            int t = bus * (input.timestamp / bus);
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

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int timestamp = Integer.parseInt(reader.readLine());
            List<Integer> buses = new ArrayList<>();
            for (String part : reader.readLine().split(",")) {
                if (!"x".equals(part)) {
                    buses.add(Integer.parseInt(part));
                }
            }

            return new Input(timestamp, buses);
        }
    }

    private static class Input {

        final int timestamp;
        final List<Integer> buses;

        Input(int timestamp, List<Integer> buses) {
            this.timestamp = timestamp;
            this.buses = buses;
        }
    }
}

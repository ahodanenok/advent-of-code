import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2021/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        List<Action> actions = getInput();
        part1(actions);
    }

    private static void part1(List<Action> actions) {
        List<Action> validActions = new ArrayList<>();

        Map<String, Boolean> state = new HashMap<>();
        for (Action a : actions) {
            if (a.xStart >= -50 && a.xEnd <= 50
                    && a.yStart >= -50 && a.yEnd <= 50
                    && a.zStart >= -50 && a.zEnd <= 50) {

                for (int x = a.xStart; x <= a.xEnd; x++) {
                    for (int y = a.yStart; y <= a.yEnd; y++) {
                        for (int z = a.zStart; z <= a.zEnd; z++) {
                            state.put(x + " " + y + " " + z, a.on);
                        }
                    }
                }
            }
        }

        int count = 0;
        for (boolean on : state.values()) {
            if (on) {
                count++;
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static List<Action> getInput() throws Exception {
        List<Action> actions = new ArrayList<Action>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                boolean on;
                if (line.startsWith("on")) {
                    on = true;
                    line = line.substring(3);
                } else if (line.startsWith("off")) {
                    on = false;
                    line = line.substring(4);
                } else {
                    throw new IllegalStateException("Unknown action: " + line);
                }

                String[] parts = line.split(",");

                String[] xParts = parts[0].split("\\.\\.");
                int xStart = Integer.parseInt(xParts[0].substring(2));
                int xEnd = Integer.parseInt(xParts[1]);

                String[] yParts = parts[1].split("\\.\\.");
                int yStart = Integer.parseInt(yParts[0].substring(2));
                int yEnd = Integer.parseInt(yParts[1]);

                String[] zParts = parts[2].split("\\.\\.");
                int zStart = Integer.parseInt(zParts[0].substring(2));
                int zEnd = Integer.parseInt(zParts[1]);

                actions.add(new Action(on, xStart, xEnd, yStart, yEnd, zStart, zEnd));
            }
        }

        return actions;
    }

    private static class Action {

        final boolean on;

        final int xStart;
        final int xEnd;

        final int yStart;
        final int yEnd;

        final int zStart;
        final int zEnd;

        Action(boolean on, int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd) {
            this.on = on;
            this.xStart = xStart;
            this.xEnd = xEnd;
            this.yStart = yStart;
            this.yEnd = yEnd;
            this.zStart = zStart;
            this.zEnd = zEnd;
        }

        @Override
        public String toString() {
            return String.format("%s x=%d..%d,y=%d..%d,z=%d..%d",
                on ? "on" : "off", xStart, xEnd, yStart, yEnd, zStart, zEnd);
        }
    }
}

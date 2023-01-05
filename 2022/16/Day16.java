import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2022/day/16
 */
public class Day16 {

    public static void main(String[] args) throws Exception {
        List<Valve> valves = getValves();
        part1(valves);
    }

    private static void part1(List<Valve> valves) {
        Map<String, Valve> valvesByName = new HashMap<>();
        for (Valve v : valves) {
            valvesByName.put(v.name, v);
        }

        int totalMinutes = 30;
        int maxPressureReleased = Integer.MIN_VALUE;

        LinkedList<Minute> queue = new LinkedList<>();
        queue.add(new Minute(0, valvesByName.get("AA"), 0));
        while (!queue.isEmpty()) {
            Minute currentMinute = queue.removeFirst();
            if (currentMinute.num == totalMinutes) {
                maxPressureReleased = Math.max(currentMinute.pressureReleased, maxPressureReleased);
                continue;
            }

            int pressureReleased = currentMinute.openedValves.stream().map(v -> v.flow).reduce(0, Integer::sum);
            List<ReachableValve> r = reachable(currentMinute.valve, currentMinute.openedValves, valvesByName);

            boolean anyReachable = false;
            for (ReachableValve next : r) {
                if (currentMinute.num + next.minutes + 1 <= totalMinutes) {
                    anyReachable = true;
                    Minute m = new Minute(
                        currentMinute.num + next.minutes + 1,
                        next.valve,
                        currentMinute.pressureReleased + next.minutes * pressureReleased + pressureReleased);
                    m.openedValves.addAll(currentMinute.openedValves);
                    m.openedValves.add(next.valve);
                    queue.add(m);
                }
            }

            if (!anyReachable) {
                Minute m = new Minute(
                    currentMinute.num + 1,
                    currentMinute.valve,
                    currentMinute.pressureReleased + pressureReleased);
                m.openedValves.addAll(currentMinute.openedValves);
                queue.add(m);
            }
        }

        System.out.println("Part 1: " + maxPressureReleased);
    }

    private static List<ReachableValve> reachable(Valve fromValve, List<Valve> openedValves, Map<String, Valve> valvesByName) {
        List<ReachableValve> result = new ArrayList<>();

        List<String> seen = new ArrayList<>();
        seen.add(fromValve.name);

        LinkedList<ReachableValve> queue = new LinkedList<>();
        queue.add(new ReachableValve(fromValve, 0));

        while (!queue.isEmpty()) {
            ReachableValve currentValve = queue.removeFirst();
            if (currentValve.valve.flow > 0 
                    && !openedValves.contains(currentValve.valve)
                    && !currentValve.valve.equals(fromValve)) {
                result.add(currentValve);
            }

            for (String tunnel : currentValve.valve.tunnels) {
                if (!seen.contains(tunnel)) {
                    queue.add(new ReachableValve(valvesByName.get(tunnel), currentValve.minutes + 1));
                    seen.add(tunnel);
                }
            }
        }

        return result;
    }

    private static List<Valve> getValves() throws Exception {
        List<Valve> valves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Pattern pattern = Pattern.compile("([A-Z]{2})|([0-9]+)");

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                matcher.find();
                String name = matcher.group(0);

                matcher.find();
                int flow = Integer.parseInt(matcher.group(0));

                Valve valve = new Valve(name, flow);
                while (matcher.find()) {
                    valve.tunnels.add(matcher.group(0));
                }

                valves.add(valve);
            }
        }

        return valves;
    }

    private static class Minute {

        final int num;
        final Valve valve;
        final List<Valve> openedValves = new ArrayList<>();
        final int pressureReleased;

        Minute(int num, Valve valve, int pressureReleased) {
            this.num = num;
            this.valve = valve;
            this.pressureReleased = pressureReleased;
        }
    }

    private static class Valve {

        final String name;
        final int flow;
        final List<String> tunnels = new ArrayList<>();

        Valve(String name, int flow) {
            this.name = name;
            this.flow = flow;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Valve other = (Valve) obj;
            return name.equals(other.name);
        }
    }

    private static class ReachableValve {

        final int minutes;
        final Valve valve;

        ReachableValve(Valve valve, int minutes) {
            this.valve = valve;
            this.minutes = minutes;
        }

        @Override
        public String toString() {
            return String.format("(%d, %s)", minutes, valve.name);
        }
    }
}

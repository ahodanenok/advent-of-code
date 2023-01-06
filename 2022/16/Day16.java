import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.function.BiConsumer;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2022/day/16
 */
public class Day16 {

    public static void main(String[] args) throws Exception {
        Map<String, Valve> valves = getValves();
        part1(valves);
        part2(valves);
    }

    private static void part1(Map<String, Valve> valvesByName) {
        Map<Valve, List<ValveDistance>> valvesReachable = new HashMap<>();
        for (Valve v : valvesByName.values()) {
            valvesReachable.put(v, findReachableValves(v, List.of(), valvesByName));
        }

        Countdown countdown = new Countdown(30, valvesByName.get("AA"), 0);
        System.out.println("Part 1: " + openValves(countdown, valvesReachable, List.of()));
    }

    private static void part2(Map<String, Valve> valvesByName) {
        List<Valve> usefulValves = new ArrayList<>();
        Map<Valve, List<ValveDistance>> valvesReachable = new HashMap<>();
        for (Valve v : valvesByName.values()) {
            if (v.flow > 0) {
                usefulValves.add(v);
            }

            valvesReachable.put(v, findReachableValves(v, List.of(), valvesByName));
        }

        int[] n = new int[] { 1 };
        long[] maxPressureReleased = new long[] { Long.MIN_VALUE };
        splitValves(new ArrayList<>(), new ArrayList<>(), usefulValves, 0, (valvesForMe, valvesForElephant) -> {
            Countdown countdown = new Countdown(26, valvesByName.get("AA"), 0);
            maxPressureReleased[0] = Math.max(
                openValves(countdown, valvesReachable, valvesForElephant) + openValves(countdown, valvesReachable, valvesForMe),
                maxPressureReleased[0]);
        });

        System.out.println("Part 2: " + maxPressureReleased[0]);
    }

    private static void splitValves(List<Valve> valvesForMe, List<Valve> valvesForElephant, 
            List<Valve> valves, int valvesIdx, BiConsumer<List<Valve>, List<Valve>> consumer) {
        if (valvesForMe.size() + valvesForElephant.size() == valves.size()) {
            // calculate only for the first half, we've already seen remaining
            if (valvesForMe.size() >= valvesForElephant.size()) {
                consumer.accept(valvesForMe, valvesForElephant);
            }

            return;
        }

        valvesForMe.add(valves.get(valvesIdx));
        splitValves(valvesForMe, valvesForElephant, valves, valvesIdx + 1, consumer);
        valvesForMe.remove(valvesForMe.size() - 1);

        valvesForElephant.add(valves.get(valvesIdx));
        splitValves(valvesForMe, valvesForElephant, valves, valvesIdx + 1, consumer);
        valvesForElephant.remove(valvesForElephant.size() - 1);
    }

    private static int openValves(Countdown countdown, Map<Valve, List<ValveDistance>> valvesReachable, List<Valve> valvesIgnored) {
        if (countdown.minutesRemaining == 0) {
            return countdown.pressureReleased;
        }

        int pressureReleasedMax = Integer.MIN_VALUE;
        int pressureReleased = countdown.valvesOpened.stream().map(v -> v.flow).reduce(0, Integer::sum);
        for (ValveDistance next : valvesReachable.get(countdown.valveAt)) {
            if (valvesIgnored.contains(next.valve) || countdown.valvesOpened.contains(next.valve)) {
                continue;
            }

            int minutesRemaining = countdown.minutesRemaining - next.minutes - 1;
            if (minutesRemaining >= 0) {
                Countdown countdownNext = new Countdown(
                    minutesRemaining,
                    next.valve,
                    countdown.pressureReleased + next.minutes * pressureReleased + pressureReleased);
                countdownNext.valvesOpened.addAll(countdown.valvesOpened);
                countdownNext.valvesOpened.add(next.valve);

                pressureReleasedMax = Math.max(openValves(countdownNext, valvesReachable, valvesIgnored), pressureReleasedMax);
            }
        }

        if (pressureReleasedMax == Integer.MIN_VALUE) {
            // fast-forward till the end
            pressureReleasedMax = countdown.pressureReleased + countdown.minutesRemaining * pressureReleased;
        }

        return pressureReleasedMax;
    }

    private static List<ValveDistance> findReachableValves(Valve fromValve, List<Valve> valvesOpened, Map<String, Valve> valvesByName) {
        List<ValveDistance> distances = new ArrayList<>();

        List<String> seen = new ArrayList<>();
        seen.add(fromValve.name);

        LinkedList<ValveDistance> queue = new LinkedList<>();
        queue.add(new ValveDistance(fromValve, 0));

        while (!queue.isEmpty()) {
            ValveDistance currentDistance = queue.removeFirst();
            if (currentDistance.valve.flow > 0 
                    && !valvesOpened.contains(currentDistance.valve)
                    && !currentDistance.valve.equals(fromValve)) {
                distances.add(currentDistance);
            }

            for (String tunnel : currentDistance.valve.tunnels) {
                if (!seen.contains(tunnel)) {
                    queue.add(new ValveDistance(valvesByName.get(tunnel), currentDistance.minutes + 1));
                    seen.add(tunnel);
                }
            }
        }

        return distances;
    }

    private static Map<String, Valve> getValves() throws Exception {
        Map<String, Valve> valves = new HashMap<>();
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

                valves.put(valve.name, valve);
            }
        }

        return valves;
    }

    private static class Countdown {

        final int minutesRemaining;
        final Valve valveAt;
        final List<Valve> valvesOpened = new ArrayList<>();
        final int pressureReleased;

        Countdown(int minutesRemaining, Valve valveAt, int pressureReleased) {
            this.minutesRemaining = minutesRemaining;
            this.valveAt = valveAt;
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

    private static class ValveDistance {

        final Valve valve;
        final int minutes;

        ValveDistance(Valve valve, int minutes) {
            this.valve = valve;
            this.minutes = minutes;
        }

        @Override
        public String toString() {
            return String.format("(%d, %s)", minutes, valve.name);
        }
    }
}

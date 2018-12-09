import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2018/day/4
 */
public class Day4 {

    public static void main(String[] args) {
        List<Guard> guards = getGuards();
        part1(guards);
    }

    private static void part1(List<Guard> guards) {
        Guard maxAsleepGuard = null;
        for (Guard guard : guards) {
            if (maxAsleepGuard == null || guard.minutesAsleep > maxAsleepGuard.minutesAsleep) {
                maxAsleepGuard = guard;
            }
        }

        System.out.println(maxAsleepGuard.id * maxAsleepGuard.mostAsleepMinute());
    }

    private static List<Guard> getGuards() {
        List<String> records = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            records.add(scanner.nextLine().trim());
        }
        Collections.sort(records);

        int sleepFrom = -1;
        int sleepTo = -1;
        Guard currentGuard = null;
        List<Guard> guards = new ArrayList<Guard>();
        for (String record : records) {
            String action = record.substring(19);
            if (action.startsWith("Guard")) {
                int id = Integer.parseInt(action.split("\\s+")[1].substring(1));

                boolean exists = false;
                for (Guard guard : guards) {
                    if (guard.id == id) {
                        currentGuard = guard;
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    currentGuard = new Guard(id);
                }
            } else if ("falls asleep".equals(action)) {
                sleepFrom = Integer.parseInt(record.substring(15, 17));
            } else if ("wakes up".equals(action)) {
                sleepTo = Integer.parseInt(record.substring(15, 17));
                currentGuard.addSleepTime(sleepFrom, sleepTo);
                if (!guards.contains(currentGuard)) {
                    guards.add(currentGuard);
                }
            } else {
                throw new IllegalStateException();
            }
        }

        return guards;
    }

    private static class Guard {

        private int id;
        private int minutesAsleep;
        private Map<Integer, Integer> asleepByMinute;

        Guard(int id) {
            this.id = id;
            this.asleepByMinute = new HashMap<Integer, Integer>();
        }

        void addSleepTime(int from, int to) {
            minutesAsleep += (to - from);
            for (int minute = from; minute < to; minute++) {
                if (asleepByMinute.containsKey(minute)) {
                    asleepByMinute.put(minute, asleepByMinute.get(minute) + 1);
                } else {
                    asleepByMinute.put(minute, 1);
                }
            }
        }

        int mostAsleepMinute() {
            int minute = Integer.MIN_VALUE;
            int times = 0;
            for (Map.Entry<Integer, Integer> entry : asleepByMinute.entrySet()) {
                if (entry.getValue() > times) {
                    minute = entry.getKey();
                    times = entry.getValue();
                }
            }

            return minute;
        }

        public int hashCode() {
            return id;
        }

        public boolean equals(Object obj) {
            Guard other = (Guard) obj;
            return other.id == id;
        }
    }
}

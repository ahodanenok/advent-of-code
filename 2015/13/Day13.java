import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2015/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        List<Happiness> happiness = getHappiness();
        part1(happiness);
        part2(happiness);
    }

    private static void part1(List<Happiness> happiness) {
        Set<String> persons = new HashSet<String>(); 
        for (Happiness h : happiness) {
            persons.add(h.person);
        }

        System.out.println(maxHappinessChange(new LinkedList<String>(), persons, happiness));
    }

    private static void part2(List<Happiness> happiness) {
        List<Happiness> me = new ArrayList<Happiness>();
        for (Happiness h : happiness) {
            me.add(new Happiness("me", h.person, 0));
            me.add(new Happiness(h.person, "me", 0));
        }
        happiness.addAll(me);

        part1(happiness);
    }

    private static int maxHappinessChange(LinkedList<String> arrangement,
                                          Set<String> persons,
                                          List<Happiness> happiness) {
        if (arrangement.size() == persons.size()) {
            return happinessChange(arrangement, happiness);
        } else {
            int max = Integer.MIN_VALUE;
            for (String person : persons) {
                if (!arrangement.contains(person)) {
                    arrangement.addLast(person);
                    max = Math.max(maxHappinessChange(arrangement, persons, happiness), max);
                    arrangement.removeLast();
                }
            }

            return max;
        }
    }

    private static int happinessChange(List<String> arrangement, List<Happiness> happiness) {
        int val = happinessBetween(arrangement.get(arrangement.size() - 1), arrangement.get(0), happiness);
        for (int i = 1; i < arrangement.size(); i++) {
            val += happinessBetween(arrangement.get(i - 1), arrangement.get(i), happiness);
        }

        return val;
    }

    private static int happinessBetween(String person1, String person2, List<Happiness>  happiness) {
        int result = 0;
        for (Happiness h : happiness) {
            if (h.person.equals(person1) && h.neighbor.equals(person2)) {
                result += h.change;
            }
            if (h.person.equals(person2) && h.neighbor.equals(person1)) {
                result += h.change;
            }
        }

        return result;
    }

    private static List<Happiness> getHappiness() {
        List<Happiness> happiness = new ArrayList<Happiness>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            int change = Integer.parseInt(parts[3]);
            if ("lose".equals(parts[2])) {
                change = -change;
            }

            happiness.add(new Happiness(parts[0], parts[10].substring(0, parts[10].length() - 1), change));
        }

        return happiness;
    }

    private static class Happiness {

        private final String person;
        private final String neighbor;
        private final int change;

        Happiness(String person, String neighbor, int change) {
            this.person = person;
            this.neighbor = neighbor;
            this.change = change;
        }
    }
}

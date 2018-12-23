import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2018/day/12
 */
public class Day12 {

    public static void main(String[] args) {
        Pots pots = getPots();

        // part 1
        pots.nextGenerations(20);
        System.out.println(pots.sum());
 
        // part 2
        pots.nextGenerations(50000000000L - 20);
        System.out.println(pots.sum());
    }

    private static Pots getPots() {
        Pots pots = new Pots();

        Scanner scanner = new Scanner(System.in);
        String potsDef = scanner.nextLine().trim().substring(15).trim();
        for (int i = 0; i < potsDef.length(); i++) {
            if (potsDef.charAt(i) == '#') {
                pots.addPotWithPlant(i);
            }
        }

        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("=>");
            String patternDef = parts[0].trim();
            String resultDef = parts[1].trim();

            boolean[] pattern = new boolean[patternDef.length()];
            for (int i = 0; i < patternDef.length(); i++) {
                pattern[i] = patternDef.charAt(i) == '#';
            }

            pots.addRule(new Rule(pattern, resultDef.charAt(0) == '#'));
        }

        return pots;
    }

    private static class Pots {

        private TreeSet<Long> pots;
        private List<Rule> spreadRules;

        Pots() {
            this.pots = new TreeSet<Long>();
            this.spreadRules = new ArrayList<Rule>();
        }

        void addPotWithPlant(long pot) {
            pots.add(pot);
        }

        void addRule(Rule rule) {
            spreadRules.add(rule);
        }

        long firstPot() {
            return pots.first() - Rule.PATTERN_LENGTH;
        }

        long lastPot() {
            return pots.last() + Rule.PATTERN_LENGTH;
        }

        boolean hasPlant(long pot) {
            return pots.contains(pot);
        }

        void nextGenerations(long count) {
            for (int g = 0; g < count; g++) {
                TreeSet<Long> newPots = new TreeSet<Long>();
                for (long pot = firstPot(); pot <= lastPot(); pot++) {
                    boolean hasPlant = false;
                    for (Rule rule : spreadRules) {
                        if (rule.match(this, pot)) {
                            hasPlant = rule.result;
                            break;
                        }
                    }

                    if (hasPlant) {
                        newPots.add(pot);
                    }
                }

                // checking for sliding:
                // the same pot configuration as in the last generation,
                // but starting from different pot
                if (newPots.size() == pots.size()) {
                    long offset = newPots.first() - pots.first();
                    Iterator<Long> potsIterator = pots.iterator();
                    Iterator<Long> newPotsIterator = newPots.iterator();

                    boolean equal = true;
                    while (potsIterator.hasNext()) {
                        if (potsIterator.next() + offset != newPotsIterator.next()) {
                            equal = false;
                            break;
                        }
                    }

                    if (equal) {
                        // fast-forward to the last generation
                        long generationsLeft = count - g;
                        long resultOffset = generationsLeft * offset;
                        newPots = new TreeSet<Long>();
                        for (long pot : pots) {
                            newPots.add(pot + resultOffset);
                        }

                        pots = newPots;
                        break;
                    }
                }

                pots = newPots;
            }
        }

        long sum() {
            long sum = 0;
            for (long pot : pots) {
                sum += pot;
            }

            return sum;
        }
    }

    private static class Rule {

        static final int PATTERN_LENGTH = 5;

        private boolean[] pattern;
        private boolean result;

        Rule(boolean[] pattern, boolean result) {
            this.pattern = pattern;
            this.result = result;
        }

        boolean match(Pots pots, long pot) {
            long fromPot = pot - PATTERN_LENGTH / 2;
            for (int i = 0; i < PATTERN_LENGTH; i++) {
                if (pattern[i] != pots.hasPlant(fromPot + i)) {
                    return false;
                }
            }

            return true;
        }
    }
}

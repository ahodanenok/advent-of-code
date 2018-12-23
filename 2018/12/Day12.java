import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2018/day/12
 */
public class Day12 {

    public static void main(String[] args) {
        Pots pots = getPots();
        part1(pots);
    }

    private static void part1(Pots pots) {
        for (int i = 0; i < 20; i++) {
            pots.nextGeneration();
        }

        int sum = 0;
        for (int pot = pots.firstPot(); pot <= pots.lastPot(); pot++) {
            if (pots.hasPlant(pot)) {
                sum += pot;
            }
        }

        System.out.println(sum);
    }

    private static Pots getPots() {
        Pots pots = new Pots();

        Scanner scanner = new Scanner(System.in);
        String potsDef = scanner.nextLine().trim().substring(15).trim();
        for (int i = 0; i < potsDef.length(); i++) {
            pots.addPot(potsDef.charAt(i) == '#');
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

        private List<Boolean> pots;
        private int zeroPotIdx = 0;
        private List<Rule> spreadRules;

        Pots() {
            this.pots = new ArrayList<Boolean>();
            spreadRules = new ArrayList<Rule>();
        }

        void addPot(boolean withPlant) {
            pots.add(withPlant);
        }

        void addRule(Rule rule) {
            spreadRules.add(rule);
        }

        int firstPot() {
            return -zeroPotIdx;
        }

        int lastPot() {
            return pots.size() - zeroPotIdx - 1;
        }

        boolean hasPlant(int pot) {
            int potIdx = pot + zeroPotIdx;
            while (potIdx < 0 || potIdx >= pots.size()) {
                return false;
            }

            return pots.get(potIdx);
        }

        void nextGeneration() {
            zeroPotIdx += Rule.PATTERN_LENGTH;
            for (int i = 0; i < Rule.PATTERN_LENGTH; i++) pots.add(0, false);
            for (int i = 0; i < Rule.PATTERN_LENGTH; i++) pots.add(false);

            List<Boolean> newPots = new ArrayList<Boolean>(pots.size());
            for (int pot = firstPot(); pot <= lastPot(); pot++) {
                boolean hasPlant = false;
                for (Rule rule : spreadRules) {
                    if (rule.match(this, pot)) {
                        hasPlant = rule.result;
                        break;
                    }
                }

                newPots.add(hasPlant);
            }

            pots = newPots;
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

        boolean match(Pots pots, int pot) {
            int fromPot = pot - PATTERN_LENGTH / 2;
            for (int i = 0; i < PATTERN_LENGTH; i++) {
                if (pattern[i] != pots.hasPlant(fromPot + i)) {
                    return false;
                }
            }

            return true;
        }
    }
}

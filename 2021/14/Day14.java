import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2021/day/14
 */
public class Day14 {

    public static void main(String[] args) throws Exception {
        Manual manual = getInput();
        part1(manual);
        part2(manual);
    }

    private static Manual getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Manual manual = new Manual(reader.readLine().trim());

            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                manual.rules.put(parts[0].trim(), parts[1].trim());
            }

            return manual;
        }
    }

    private static void part1(Manual manual) {
        String polymer = manual.template;
        for (int i = 0; i < 10; i++) {
            polymer = polymerize(polymer, 0, polymer.length(), manual.rules);
        }

        Map<Character, Integer> occurrences = new HashMap<>();
        for (int i = 0; i < polymer.length(); i++) {
            occurrences.merge(polymer.charAt(i), 1, Integer::sum);
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int count : occurrences.values()) {
            min = Math.min(count, min);
            max = Math.max(count, max);
        }

        System.out.println("Part 1: " + (max - min));
    }

    private static void part2(Manual manual) {
        Map<Character, BigInteger> occurrences = countElements(manual.template, manual.rules, 40, new HashMap<>());
        BigInteger min = occurrences.values().stream().min((a, b) -> a.compareTo(b)).get();
        BigInteger max = occurrences.values().stream().max((a, b) -> a.compareTo(b)).get();

        System.out.println("Part 2: " + max.subtract(min));
    }

    private static Map<Character, BigInteger> countElements(
            String template, Map<String, String> rules, int stepsRemaining, Map<State, Map<Character, BigInteger>> memo) {

        if (stepsRemaining == 0) {
            Map<Character, BigInteger> result = new HashMap<>();
            for (int i = 0; i < template.length(); i++) {
                result.merge(template.charAt(i), BigInteger.ONE, (a, b) -> a.add(b));
            }

            return result;
        }

        State state = new State(template, stepsRemaining);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        Map<Character, BigInteger> result = new HashMap<>();
        for (int i = 1, len = template.length() + 1; i < len; i++) {
            String p = polymerize(template, i - 1, Math.min(i + 1, template.length()), rules);

            Map<Character, BigInteger> r = countElements(p, rules, stepsRemaining - 1, memo);
            for (Map.Entry<Character, BigInteger> entry : r.entrySet()) {
                result.merge(entry.getKey(), entry.getValue(), (a, b) -> a.add(b));
            }

            // don't remove for the first pair, but for all remaining
            // the first element has been already counted the step before
            if (i > 1) {
                result.merge(p.charAt(0), BigInteger.ONE, (a, b) -> a.subtract(b));
            }
        }
        memo.put(state, result);

        return result;
    }

    private static String polymerize(String template, int from, int to, Map<String, String> rules) {
        StringBuilder sb = new StringBuilder();
        for (int i = from + 1, len = to + 1; i < len; i++) {
            String pair = template.substring(i - 1, Math.min(i + 1, to));
            String insertion = rules.get(pair);
            if (insertion != null) {
                sb.append(pair.charAt(0));
                sb.append(insertion.charAt(0));
            } else {
                sb.append(pair.charAt(0));
            }
        }

        return sb.toString();
    }

    private static class Manual {

        final String template;
        final Map<String, String> rules = new HashMap<>();

        Manual(String template) {
            this.template = template;
        }
    }

    private static class State {

        final String template;
        final int step;

        State(String template, int step) {
            this.template = template;
            this.step = step;
        }

        @Override
        public int hashCode() {
            return 31 * template.hashCode() + step;
        }

        @Override
        public boolean equals(Object obj) {
            State other = (State) obj;
            return template.equals(other.template) && step == other.step;
        }
    }
}

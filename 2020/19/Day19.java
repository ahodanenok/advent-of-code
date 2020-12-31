import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2020/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        System.out.println("Part 1: " + getMatchedCount("input_1.txt"));
        System.out.println("Part 2: " + getMatchedCount("input_2.txt"));
    }

    private static int getMatchedCount(String inputName) throws Exception {
        Input input = getInput(inputName);

        int matchedCount = 0;
        Rule rule = input.rules.get(0);
        for (String msg : input.messages) {
            List<Integer> result = rule.matches(msg, 0);
            if (!result.isEmpty() && result.stream().anyMatch(pos -> pos == msg.length())) {
                matchedCount++;
            }
        }

        return matchedCount;
    }

    private static Input getInput(String name) throws Exception {
        Input input = new Input();
        try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                int separatorIdx = line.indexOf(':');
                int id = Integer.parseInt(line.substring(0, separatorIdx));

                String def = line.substring(separatorIdx + 1).trim();
                if (def.charAt(0) == '"') {
                    input.rules.put(id, new ExactRule(def.charAt(1)));
                    continue;
                }

                String[] orParts = def.trim().split("\\|");
                if (orParts.length == 1) {
                    input.rules.put(id, parseAnd(orParts[0], input.rules));
                    continue;
                }

                if (orParts.length == 2) {
                    Rule left = parseAnd(orParts[0], input.rules);
                    Rule right = parseAnd(orParts[1], input.rules);
                    input.rules.put(id, new OrRule(left, right));
                    continue;
                }

                throw new IllegalStateException(def);
            }

            while ((line = reader.readLine()) != null) {
                input.messages.add(line.trim());
            }
        }

        return input;
    }

    private static AndRule parseAnd(String def, Map<Integer, Rule> rules) {
        List<Rule> andRules = new ArrayList<>();

        String[] andParts = def.trim().split("\\s");
        for (String andPart : andParts) {
            andRules.add(new RefRule(Integer.parseInt(andPart.trim()), rules));
        }

        return new AndRule(andRules);
    }

    private static class Input {

        final Map<Integer, Rule> rules = new HashMap<>();
        final List<String> messages = new ArrayList<>();
    }

    private interface Rule {
        List<Integer> matches(String str, int pos);
    }

    private static class ExactRule implements Rule {

        private final char ch;

        ExactRule(char ch) {
            this.ch = ch;
        }

        public List<Integer> matches(String str, int pos) {
            return pos < str.length() && str.charAt(pos) == ch 
                ? Collections.singletonList(pos + 1)
                : Collections.emptyList();
        }
    }

    private static class RefRule implements Rule {

        private final int id;
        private final Map<Integer, Rule> rules;

        RefRule(int id, Map<Integer, Rule> rules) {
            this.id = id;
            this.rules = rules;
        }

        public List<Integer> matches(String str, int pos) {
            return rules.get(id).matches(str, pos);
        }
    }

    private static class AndRule implements Rule {

        private final List<Rule> rules;

        AndRule(List<Rule> rules) {
            this.rules = rules;
        }

        public List<Integer> matches(String str, int pos) {
            List<Integer> result = Collections.singletonList(pos);
            for (Rule rule : rules) {
                List<Integer> next = new ArrayList<>();
                for (Integer r : result) {
                    next.addAll(rule.matches(str, r));
                }

                if (next.isEmpty()) {
                    return Collections.emptyList();
                }

                result = next;
            }

            return result;
        }
    }

    private static class OrRule implements Rule {

        private final Rule left;
        private final Rule right;

        OrRule(Rule left, Rule right) {
            this.left = left;
            this.right = right;
        }

        public List<Integer> matches(String str, int pos) {
            List<Integer> result = new ArrayList<>();
            result.addAll(left.matches(str, pos));
            result.addAll(right.matches(str, pos));

            return result;
        }
    }
}

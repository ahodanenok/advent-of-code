import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2020/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();

        int matchedCount = 0;
        Rule rule = input.rules.get(0);
        for (String msg : input.messages) {
            //System.out.println(msg + ": " + rule.matches(msg, 0));
            if (rule.matches(msg, 0) == msg.length()) {
                matchedCount++;
            }
        }

        System.out.println("Part 1: " + matchedCount);
    }

    private static Input getInput() throws Exception {
        Input input = new Input();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
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
        int matches(String str, int pos);
    }

    private static class ExactRule implements Rule {

        private final char ch;

        ExactRule(char ch) {
            this.ch = ch;
        }

        public int matches(String str, int pos) {
            return pos < str.length() && str.charAt(pos) == ch ? pos + 1 : -1;
        }
    }

    private static class RefRule implements Rule {

        private final int id;
        private final Map<Integer, Rule> rules;

        RefRule(int id, Map<Integer, Rule> rules) {
            this.id = id;
            this.rules = rules;
        }

        public int matches(String str, int pos) {
            return rules.get(id).matches(str, pos);
        }
    }

    private static class AndRule implements Rule {

        private final List<Rule> rules;

        AndRule(List<Rule> rules) {
            this.rules = rules;
        }

        public int matches(String str, int pos) {
            int nextPos = pos;
            for (Rule rule : rules) {
                nextPos = rule.matches(str, nextPos);
                if (nextPos == -1) {
                    break;
                }
            }

            return nextPos;
        }
    }

    private static class OrRule implements Rule {

        private final Rule left;
        private final Rule right;

        OrRule(Rule left, Rule right) {
            this.left = left;
            this.right = right;
        }

        public int matches(String str, int pos) {
            int nextPos = left.matches(str, pos);
            return nextPos == -1 && right != null ? right.matches(str, pos) : nextPos;
        }
    }
}

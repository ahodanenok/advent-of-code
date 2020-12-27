import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2020/day/16
 */
public class Day16 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();

        int errorRate = 0;
        for (Ticket ticket : input.nearbyTickets) {
            nextValue:
            for (Integer fieldValue : ticket.fieldValues) {
                for (Rule rule : input.rules) {
                    if (rule.isValid(fieldValue)) {
                        continue nextValue;
                    }
                }

                errorRate += fieldValue;
            }
        }

        System.out.println("Part 1: " + errorRate);
    }

    private static Input getInput() throws Exception {
        Input input = new Input();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] ruleParts = line.split(":");
                String[] rangesParts = ruleParts[1].split("or");

                Rule rule = new Rule(ruleParts[0].trim());
                for (String rp : rangesParts) {
                    String[] parts = rp.split("-");
                    rule.addRange(
                        Integer.parseInt(parts[0].trim()),
                        Integer.parseInt(parts[1].trim()));
                }

                input.rules.add(rule);
            }

            reader.readLine();
            for (String part : reader.readLine().split(",")) {
                input.myTicket.fieldValues.add(Integer.parseInt(part.trim()));
            }

            reader.readLine();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Ticket ticket = new Ticket();
                for (String part : line.split(",")) {
                    ticket.fieldValues.add(Integer.parseInt(part.trim()));
                }

                input.nearbyTickets.add(ticket);
            }
        }

        return input;
    }

    private static class Input {

        final List<Rule> rules = new ArrayList<>();
        final Ticket myTicket = new Ticket();
        final List<Ticket> nearbyTickets = new ArrayList<>();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            for (Rule rule : rules) {
                sb.append(rule).append('\n');
            }
            sb.append('\n');

            sb.append("my ticket: " + myTicket.fieldValues).append('\n');
            sb.append('\n');

            for (Ticket t : nearbyTickets) {
                sb.append(t.fieldValues).append('\n');
            }

            return sb.toString();
        }
    }

    private static class Rule {

        private final String fieldName;
        private final List<Integer> ranges = new ArrayList<>();

        Rule(String fieldName) {
            this.fieldName = fieldName;
        }

        void addRange(int from, int to) {
            ranges.add(from);
            ranges.add(to);
        }

        boolean isValid(int value) {
            for (int i = 0; i < ranges.size(); i += 2) {
                if (ranges.get(i) <= value && value <= ranges.get(i + 1)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(fieldName);
            sb.append(": ");
            for (int i = 0; i < ranges.size(); i += 2) {
                sb.append(ranges.get(i)).append('-').append(ranges.get(i + 1));
                if (i < ranges.size() - 2) {
                    sb.append(" or ");
                }
            }

            return sb.toString();
        }
    }

    private static class Ticket {

        final List<Integer> fieldValues = new ArrayList<>();
    }
}

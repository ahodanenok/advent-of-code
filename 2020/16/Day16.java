import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2020/day/16
 */
public class Day16 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(Input input) {
        int errorRate = 0;
        for (Ticket ticket : input.nearbyTickets) {
            int ticketErrorRate = ticket.errorRate(input.rules);
            if (ticketErrorRate != -1) {
                errorRate += ticketErrorRate;
            }
        }

        System.out.println("Part 1: " + errorRate);
    }

    private static void part2(Input input) {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(input.myTicket);

        // collect only valid tickets
        for (Ticket ticket : input.nearbyTickets) {
            if (ticket.errorRate(input.rules) == -1) {
                tickets.add(ticket);
            }
        }

        int fieldCount = input.myTicket.fieldValues.size();
        Map<Integer, List<Rule>> fields = new HashMap<>();
        // collect matching rules by field
        for (int i = 0; i < fieldCount; i++) {
            List<Rule> rules = new ArrayList<>();

            nextRule:
            for (Rule rule : input.rules) {
                for (Ticket ticket : tickets) {
                    if (!rule.isValid(ticket.field(i))) {
                        continue nextRule;
                    }
                }

                rules.add(rule);
            }

            if (fields.containsKey(i)) {
                fields.get(i).retainAll(rules);
            } else {
                fields.put(i, rules);
            }
        }

        //for (Map.Entry<Integer, List<Rule>> entry : fields.entrySet()) {
        //    System.out.println(entry.getKey() + ": " + entry.getValue().stream().map(r -> r.fieldName).collect(java.util.stream.Collectors.toList()));
        //}

        // resolve fields
        while (true) {
            boolean anyChanged = false;
            for (Map.Entry<Integer, List<Rule>> current : fields.entrySet()) {
                if (current.getValue().size() == 1) {
                    for (Map.Entry<Integer, List<Rule>> next : fields.entrySet()) {
                        if (next.getValue().size() > 1 && next.getValue().remove(current.getValue().get(0))) {
                            anyChanged = true;
                        }
                    }
                }
            }

            if (!anyChanged) {
                break;
            }
        }

        //System.out.println();
        //for (Map.Entry<Integer, List<Rule>> entry : fields.entrySet()) {
        //    System.out.println(entry.getKey() + ": " + entry.getValue().stream().map(r -> r.fieldName).collect(java.util.stream.Collectors.toList()));
        //}

        long mul = 1;
        for (Map.Entry<Integer, List<Rule>> entry : fields.entrySet()) {
            if (entry.getValue().get(0).fieldName.startsWith("departure")) {
                mul *= input.myTicket.field(entry.getKey());
            }
        }

        System.out.println("Part 2: " + mul);
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
        public boolean equals(Object obj) {
            Rule other = (Rule) obj;
            return fieldName.equals(other.fieldName);
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

        int field(int idx) {
            return fieldValues.get(idx);
        }

        int errorRate(List<Rule> rules) {
            boolean valid = true;
            int errorRate = 0;

            next:
            for (Integer fieldValue : fieldValues) {
                for (Rule rule : rules) {
                    if (rule.isValid(fieldValue)) {
                        continue next;
                    }
                }

                errorRate += fieldValue;
                valid = false;
            }

            if (!valid) {
                return errorRate;
            } else {
                return -1;
            }
        }
    }
}

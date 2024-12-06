import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2024/day/5
 */
public class Day5 {

    public static void main(String... args) throws Exception {
        Input input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(Input input) {
        int result = 0;
        for (Update update : input.updates) {
            if (isRightOrder(update.pages, input.rules)) {
                result += update.pages.get(update.pages.size() / 2);
            }
        }

        System.out.println("Part 1: " + result);
    }

    private static void part2(Input input) {
        int result = 0;
        for (Update update : input.updates) {
            if (!isRightOrder(update.pages, input.rules)) {
                List<Integer> correctedPages = correctOrder(update.pages, input.rules);
                result += correctedPages.get(correctedPages.size() / 2);
            }
        }

        System.out.println("Part 2: " + result);
    }

    private static boolean isRightOrder(List<Integer> pages, Set<OrderRule> rules) {
        for (int i = 0; i < pages.size(); i++) {
            for (int j = i + 1; j < pages.size(); j++) {
                if (rules.contains(new OrderRule(pages.get(j), pages.get(i)))) {
                    return false;
                }
            }
        }

        return true;
    }

    private static List<Integer> correctOrder(List<Integer> pages, Set<OrderRule> rules) {
        return pages.stream()
            .sorted((p0, p1) -> {
                if (rules.contains(new OrderRule(p0, p1))) {
                    return -1;
                } else if (rules.contains(new OrderRule(p1, p0))) {
                    return 1;
                } else {
                    return 0;
                }
            })
            .collect(Collectors.toList());
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;

            Set<OrderRule> rules = new HashSet<>();
            while (!(line = reader.readLine()).isEmpty()) {
                String[] parts = line.trim().split("\\|");
                rules.add(new OrderRule(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1])));
            }

            List<Update> updates = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                updates.add(new Update(
                    Arrays.stream(parts)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())));
            }

            return new Input(rules, updates);
        }
    }

    private static class Input {

        final Set<OrderRule> rules;
        final List<Update> updates;

        Input(Set<OrderRule> rules, List<Update> updates) {
            this.rules = rules;
            this.updates = updates;
        }
    }

    private static class OrderRule {

        final int pageBefore;
        final int page;

        OrderRule(int pageBefore, int page) {
            this.pageBefore = pageBefore;
            this.page = page;
        }

        @Override
        public boolean equals(Object obj) {
            OrderRule other = (OrderRule) obj;
            return pageBefore == other.pageBefore && page == other.page;
        }

        @Override
        public int hashCode() {
            return 31 * pageBefore + page;
        }
    }

    private static class Update {

        final List<Integer> pages;

        Update(List<Integer> pages) {
            this.pages = pages;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2025/day/6
 */
public class Day6 {

    public static void main(String... args) throws Exception {
        List<Problem> problems = getInput();
        part1(problems);
        part2(problems);
    }

    private static List<Problem> getInput() throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line + " ~");
            }
        }

        List<Problem> problems = new ArrayList<>();
        int problemStart = 0;
        int idx = 1;
        String operationsLine = lines.get(lines.size() - 1);
        while (idx < operationsLine.length()) {
            if (operationsLine.charAt(idx) != ' ') {
                List<String> numbers = new ArrayList<>();
                for (int i = 0; i < lines.size() - 1; i++) {
                    numbers.add(lines.get(i).substring(problemStart, idx - 1));
                }

                problems.add(new Problem(numbers, operationsLine.charAt(problemStart) + ""));
                problemStart = idx;
            }

            idx++;
        }

        return problems;
    }

    private static void part1(List<Problem> problems) {
        long sum = 0;
        for (Problem problem : problems) {
            List<Long> numbers = new ArrayList<>();
            for (String str : problem.numbers) {
                numbers.add(Long.parseLong(str.trim()));
            }

            sum += calculate(numbers, problem.operation);
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<Problem> problems) {
        long sum = 0;
        for (Problem problem : problems) {
            List<Long> numbers = new ArrayList<>();
            for (int i = 0, length = problem.numbers.get(0).length(); i < length; i++) {
                String number = "";
                for (String str : problem.numbers) {
                    if (str.charAt(i) != ' ') {
                        number += str.charAt(i);
                    }
                }

                numbers.add(Long.parseLong(number));
            }

            sum += calculate(numbers, problem.operation);
        }

        System.out.println("Part 2: " + sum);
    }

    private static long calculate(List<Long> numbers, String operation) {
        long answer;
        if (operation.equals("*")) {
            answer = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                answer *= numbers.get(i);
            }
        } else if (operation.equals("+")) {
            answer = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                answer += numbers.get(i);
            }
        } else {
            throw new IllegalStateException(operation);
        }

        return answer;
    }

    private static class Problem {

        final List<String> numbers;
        final String operation;

        Problem(List<String> numbers, String operation) {
            this.numbers = numbers;
            this.operation = operation;
        }

        @Override
        public String toString() {
            return "Problem(%s, %s)".formatted(numbers, operation);
        }
    }
}

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
    }

    private static List<Problem> getInput() throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        List<List<Long>> numberLines = new ArrayList<>();
        for (int i = 0; i < lines.size() - 1; i++) {
            String[] parts = lines.get(i).split("[ ]+");
            List<Long> numbers = new ArrayList<>();
            for (String part : parts) {
                numbers.add(Long.parseLong(part));
            }

            numberLines.add(numbers);
        }

        String[] operations = lines.get(lines.size() - 1).split("[ ]+");
        List<Problem> problems = new ArrayList<>();
        for (int i = 0; i < operations.length; i++) {
            List<Long> numbers = new ArrayList<>();
            for (List<Long> numberLine : numberLines) {
                numbers.add(numberLine.get(i));
            }

            problems.add(new Problem(numbers, operations[i]));
        }

        return problems;
    }

    private static void part1(List<Problem> problems) {
        long sum = 0;
        for (Problem problem : problems) {
            long answer;
            if (problem.operation.equals("*")) {
                answer = problem.numbers.get(0);
                for (int i = 1; i < problem.numbers.size(); i++) {
                    answer *= problem.numbers.get(i);
                }
            } else if (problem.operation.equals("+")) {
                answer = problem.numbers.get(0);
                for (int i = 1; i < problem.numbers.size(); i++) {
                    answer += problem.numbers.get(i);
                }
            } else {
                throw new IllegalStateException(problem.operation);
            }

            sum += answer;
        }

        System.out.println("Part 1: " + sum);
    }

    private static class Problem {

        final List<Long> numbers;
        final String operation;

        Problem(List<Long> numbers, String operation) {
            this.numbers = numbers;
            this.operation = operation;
        }

        @Override
        public String toString() {
            return "Problem(%s, %s)".formatted(numbers, operation);
        }
    }
}

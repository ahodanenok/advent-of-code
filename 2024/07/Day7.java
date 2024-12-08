import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2024/day/7
 */
public class Day7 {

    public static void main(String... args) throws Exception {
        List<Equation> equations = getInput();
        part1(equations);
    }

    private static void part1(List<Equation> equations) {
        long sum = 0;
        for (Equation equation : equations) {
            if (findOperators(equation, new ArrayList<>())) {
                sum += equation.testValue;
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static boolean findOperators(Equation equation, List<BiFunction<Long, Long, Long>> operators) {
        if (operators.size() == equation.numbers.size() - 1) {
            long value = equation.numbers.get(0);
            for (int i = 1; i < equation.numbers.size(); i++) {
                value = operators.get(i - 1).apply(value, equation.numbers.get(i));
            }

            return value == equation.testValue;
        }

        operators.add(Day7::sum);
        if (findOperators(equation, operators)) {
            return true;
        }
        operators.remove(operators.size() - 1);

        operators.add(Day7::mul);
        if (findOperators(equation, operators)) {
            return true;
        }
        operators.remove(operators.size() - 1);

        return false;
    }

    private static long sum(long a, long b) {
        return a + b;
    }

    private static long mul(long a, long b) {
        return a * b;
    }

    private static List<Equation> getInput() throws Exception {
        List<Equation> equations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(":");
                equations.add(new Equation(
                    Long.parseLong(parts[0]),
                    Arrays.stream(parts[1].trim().split("\\s+"))
                        .map(Long::parseLong)
                        .collect(Collectors.toList())));
            }
        }

        return equations;
    }

    private static class Equation {

        final long testValue;
        final List<Long> numbers;

        Equation(long testValue, List<Long> numbers) {
            this.testValue = testValue;
            this.numbers = numbers;
        }
    }
}

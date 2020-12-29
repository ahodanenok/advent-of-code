import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2020/day/18
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        List<String> expressions = getExpressions();
        part1(expressions);
        part2(expressions);
    }

    private static void part1(List<String> expressions) {
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('*', 1);
        precedence.put('+', 1);

        long resultSum = 0;
        for (String expr : expressions) {
            resultSum += evaluate(expr, 0, precedence).value;
        }

        System.out.println("Part 1: " + resultSum);
    }

    private static void part2(List<String> expressions) {
        Map<Character, Integer> precedence = new HashMap<>();
        precedence.put('*', 1);
        precedence.put('+', 2);

        long resultSum = 0;
        for (String expr : expressions) {
            resultSum += evaluate(expr, 0, precedence).value;
        }

        System.out.println("Part 2: " + resultSum);
    }

    private static Result evaluate(String expr, int from, Map<Character, Integer> precedence) {
        int pos = from;
        LinkedList<Character> operators = new LinkedList<>();
        LinkedList<Long> stack = new LinkedList<>();
        while (pos < expr.length()) {
            char ch = expr.charAt(pos);
            if (Character.isWhitespace(ch)) {
                pos++;
            } else if (ch >= '0' && ch <= '9') {
                StringBuilder sb = new StringBuilder();
                while (pos < expr.length()) {
                    char digit = expr.charAt(pos);
                    if (digit < '0' || digit > '9') {
                        break;
                    }

                    sb.append(digit);
                    pos++;
                }

                stack.addLast(Long.parseLong(sb.toString()));
            } else if (ch == '+' || ch == '*') {
                while (!operators.isEmpty() && precedence.get(operators.peekLast()) >= precedence.get(ch)) {
                    evaluateOperator(operators.removeLast(), stack);
                }

                operators.addLast(ch);
                pos++;
            } else if (ch == '(') {
                Result result = evaluate(expr, pos + 1, precedence);
                stack.addLast(result.value);
                pos = result.pos + 1;
            } else if (ch == ')') {
                evaluateOperator(operators.removeLast(), stack);
                break;
            } else {
                throw new IllegalStateException(ch + "");
            }
        }

        while (!operators.isEmpty()) {
            evaluateOperator(operators.removeLast(), stack);
        }

        if (stack.size() > 1) {
            throw new IllegalStateException(stack.toString());
        }

        return new Result(stack.removeLast(), pos);
    }

    private static void evaluateOperator(char op, LinkedList<Long> stack) {
        long a = stack.removeLast();
        long b = stack.removeLast();
        if (op == '+') {
            stack.addLast(a + b);
        } else if (op == '*') {
            stack.addLast(a * b);
        } else {
            throw new IllegalStateException(op + "");
        }
    }

    private static class Result {

        final long value;
        final int pos;

        Result(long value, int pos) {
            this.value = value;
            this.pos = pos;
        }
    }

    private static List<String> getExpressions() throws Exception {
        List<String> expressions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                expressions.add(line.trim());
            }
        }

        return expressions;
    }
}

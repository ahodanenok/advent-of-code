import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2022/day/13
 */
public class Day13 {

    public static void main(String[] args) throws Exception {
        List<Pair> pairs = getPairs();
        part1(pairs);
    }

    private static void part1(List<Pair> pairs) {
        int sum = 0;
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            Object left = parseValue(new ValueReader(pair.left));
            Object right = parseValue(new ValueReader(pair.right));
            if (compareValues(left, right) < 0) {
                sum += (i + 1);
            }
        }

        System.out.println("Part 1: " + sum);
    }

    private static Object parseValue(ValueReader reader) {
        char token = reader.consume();
        if (Character.isDigit(token)) {
            String num = token + "";
            while (reader.hasNext() && Character.isDigit(token = reader.peek())) {
                num += reader.consume();
            }
        
            return Integer.parseInt(num);
        } else if (token == '[') {
            List<Object> list = new ArrayList<>();
            while (reader.peek() != ']') {
                list.add(parseValue(reader));
                if (reader.peek() == ',') {
                    reader.consume();
                }
            }
            reader.consume(); // ]

            return list;
        } else {
            throw new IllegalStateException(String.format(
                "Unknown token '%c' at pos %d in '%s'", token, reader.pos, reader.data));
        }
    }

    @SuppressWarnings("unchecked")
    private static int compareValues(Object left, Object right) {
        if (left instanceof Integer && right instanceof Integer) {
            return ((Integer) left).compareTo((Integer) right);
        } else if (left instanceof Integer) {
            return compareValues(List.of(left), right);
        } else if (right instanceof Integer) {
            return compareValues(left, List.of(right));
        } else if (left instanceof List && right instanceof List) {
            Iterator<Object> leftIterator = ((List<Object>) left).iterator();
            Iterator<Object> rightIterator = ((List<Object>) right).iterator();
            while (leftIterator.hasNext() || rightIterator.hasNext()) {
                if (!leftIterator.hasNext() && rightIterator.hasNext()) {
                    return -1;
                }

                if (leftIterator.hasNext() && !rightIterator.hasNext()) {
                    return 1;
                }

                int res = compareValues(leftIterator.next(), rightIterator.next());
                if (res != 0) {
                    return res;
                }
            }

            return 0;
        } else {
            throw new IllegalStateException(String.format(
                "Unknown value types combination: left=%s, right=%s", left.getClass(), right.getClass()));
        }
    }

    private static class ValueReader {

        private String data;
        private int pos;

        ValueReader(String data) {
            this.data = data;
        }

        boolean hasNext() {
            return pos < data.length();
        }

        char consume() {
            return data.charAt(pos++);
        }

        char peek() {
            return data.charAt(pos);
        }
    }

    private static List<Pair> getPairs() throws Exception {
        List<Pair> pairs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            do {
                pairs.add(new Pair(reader.readLine(), reader.readLine()));
            } while (reader.readLine() != null);
        }

        return pairs;
    }

    private static class Pair {

        final String left;
        final String right;

        Pair(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
}

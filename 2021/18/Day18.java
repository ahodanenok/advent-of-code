import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2021/day/18
 */
public class Day18 {

    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        List<Fish> fish = getInput();
        Fish result = fish.get(0);
        for (int i = 1; i < fish.size(); i++) {
            result = add(result, fish.get(i));
        }

        System.out.println("Part 1: " + result.magnitude());
    }

    private static void part2() throws Exception {
        int maxMagnitude = Integer.MIN_VALUE;
        int size = getInput().size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }

                // the simplest, but not the fastest approach...
                List<Fish> fish = getInput();

                int magnitude = add(fish.get(i), fish.get(j)).magnitude();
                maxMagnitude = Math.max(magnitude, maxMagnitude);
            }
        }

        System.out.println("Part 2: " + maxMagnitude);
    }

    private static Fish add(Fish a, Fish b) {
        Fish result = combine(a, b);
        while (true) {
            if (explode(result, 1)) {
                continue;
            }

            if (split(result)) {
                continue;
            }

            break;
        }

        return result;
    }

    private static Fish combine(Fish a, Fish b) {
        Fish result = new Fish(a, b);

        Number an = a.lastNumber();
        Number bn = b.firstNumber();

        an.right = bn;
        bn.left = an;

        return result;
    }

    private static boolean explode(Fish fish, int level) {
        if (level > 3) {
            if (fish.left.isExplodable()) {
                fish.left = explodeSingle((Fish) fish.left);
                return true;
            } else if (fish.right.isExplodable()) {
                fish.right = explodeSingle((Fish) fish.right);
                return true;
            }
        }

        if (fish.left instanceof Fish && explode((Fish) fish.left, level + 1)) {
            return true;
        }

        if (fish.right instanceof Fish && explode((Fish) fish.right, level + 1)) {
            return true;
        }

        return false;
    }

    private static Number explodeSingle(Fish fish) {
        Number a = (Number) fish.left;
        if (a.left != null) {
            a.left.value += a.value;
        }

        Number b = (Number) fish.right;
        if (b.right != null) {
            b.right.value += b.value;
        }

        Number n = new Number(0);

        n.left = a.left;
        if (n.left != null) {
            n.left.right = n;
        }

        n.right = b.right;
        if (n.right != null) {
            n.right.left = n;
        }

        return n;
    }

    private static boolean split(Fish fish) {
        if (fish.left instanceof Fish && split((Fish) fish.left)) {
            return true;
        }

        if (fish.left.isSplitable()) {
            fish.left = splitSingle((Number) fish.left);
            return true;
        }

        if (fish.right instanceof Fish && split((Fish) fish.right)) {
            return true;
        }

        if (fish.right.isSplitable()) {
            fish.right = splitSingle((Number) fish.right);
            return true;
        }

        return false;
    }

    private static Fish splitSingle(Number n) {
        Number a = new Number((int) (Math.floor(n.value / 2.0)));
        Number b = new Number((int) (Math.ceil(n.value / 2.0)));

        a.left = n.left;
        if (a.left != null) {
            a.left.right = a;
        }

        a.right = b;
        b.left = a;

        b.right = n.right;
        if (b.right != null) {
            b.right.left = b;
        }

        return new Fish(a, b);
    }

    private static List<Fish> getInput() throws Exception {
        List<Fish> fish = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ParsingState state = new ParsingState(line);
                fish.add((Fish) parseElement(state));
            }
        }

        return fish;
    }

    private static Element parseElement(ParsingState state) {
        char ch = state.nextChar();
        if (ch == '[') {
            Element left = parseElement(state);
            state.advance(',');
            Element right = parseElement(state);
            state.advance(']');
            return new Fish(left ,right);
        } else if (ch >= '0' && ch <= '9') {
            Number n = new Number(ch - '0');
            if (state.lastNumber != null) {
                n.left = state.lastNumber;
                state.lastNumber.right = n;
            }
            state.lastNumber = n;

            return n;
        } else {
            throw new IllegalStateException("Unknown character: " + ch);
        }
    }

    private static class ParsingState {

        private final String str;
        private int pos;
        Number lastNumber;

        ParsingState(String str) {
            this.str = str;
        }

        char nextChar() {
            return str.charAt(pos++);
        }

        void advance(char ch) {
            if (str.charAt(pos) != ch) {
                throw new IllegalStateException(String.format(
                    "Unexpected character '%s' at %d, expected: %s", str.charAt(pos), pos, ch));
            }

            pos++;
        }
    }

    private static interface Element {

        int magnitude();

        boolean isSplitable();

        boolean isExplodable();
    }

    private static class Fish implements Element {

        Element left;
        Element right;

        Fish(Element left, Element right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int magnitude() {
            return 3 * left.magnitude() + 2 * right.magnitude();
        }

        @Override
        public boolean isSplitable() {
            return false;
        }

        @Override
        public boolean isExplodable() {
            return left instanceof Number && right instanceof Number;
        }

        Number firstNumber() {
            if (left instanceof Number) {
                return (Number) left;
            } else {
                return ((Fish) left).firstNumber();
            }
        }

        Number lastNumber() {
            if (right instanceof Number) {
                return (Number) right;
            } else {
                return ((Fish) right).lastNumber();
            }
        }

        @Override
        public String toString() {
            return "[" + left + "," + right + "]";
        }
    }

    private static class Number implements Element {

        int value;
        Number left;
        Number right;

        Number(int value) {
            this.value = value;
        }

        @Override
        public int magnitude() {
            return value;
        }

        @Override
        public boolean isSplitable() {
            return value > 9;
        }

        @Override
        public boolean isExplodable() {
            return false;
        }

        @Override
        public String toString() {
            return value + "";
        }
    }
}

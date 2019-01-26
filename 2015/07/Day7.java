import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2015/day/7/input
 */
public class Day7 {

    private static final int BIT16_MASK = 0xFFFF;

    public static void main(String[] args) {
        Map<String, Gate> gates = getGates();
        System.out.println(gates.get("a").propagate(gates, new HashMap<String, Integer>())); // part 1
    }

    private static Map<String, Gate> getGates() {
        Map<String, Gate> gates = new HashMap<String, Gate>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            if ("NOT".equals(parts[0])) {
                gates.put(parts[3], new NotGate(parts[3], parseInput(parts[1])));
            } else if ("AND".equals(parts[1])) {
                gates.put(parts[4], new AndGate(parts[4], parseInput(parts[0]), parseInput(parts[2])));
            } else if ("OR".equals(parts[1])) {
                gates.put(parts[4], new OrGate(parts[4], parseInput(parts[0]), parseInput(parts[2])));
            } else if ("LSHIFT".equals(parts[1])) {
                gates.put(parts[4], new LShiftGate(parts[4], parseInput(parts[0]), Integer.parseInt(parts[2])));
            } else if ("RSHIFT".equals(parts[1])) {
                gates.put(parts[4], new RShiftGate(parts[4], parseInput(parts[0]), Integer.parseInt(parts[2])));
            } else if ("->".equals(parts[1])) {
                gates.put(parts[2], new ForwardGate(parts[2], parseInput(parts[0])));
            } else {
                throw new RuntimeException("Unknown gate: " + line);
            }
        }

        return gates;
    }

    private static Input parseInput(String str) {
        try {
            return new NumberInput(Integer.parseInt(str));
        } catch (Exception e) {
            return new WireInput(str);
        }
    }

    private abstract static class Gate {

        private final String out;

        Gate(String out) {
            this.out = out;
        }

        int propagate(Map<String, Gate> gates, Map<String, Integer> env) {
            if (!env.containsKey(out)) {
                env.put(out, getOutput(gates, env));
            }

            return env.get(out);
        }
    
        protected abstract int getOutput(Map<String, Gate> gates, Map<String, Integer> env);
    }

    private static class AndGate extends Gate {

        private final Input a;
        private final Input b;

        AndGate(String out, Input a, Input b) {
            super(out);
            this.a = a;
            this.b = b;
        }

        @Override
        protected int getOutput(Map<String, Gate> gates, Map<String, Integer> env) {
            return (a.get(gates, env) & b.get(gates, env)) & BIT16_MASK;
        }
    }

    private static class OrGate extends Gate {

        private final Input a;
        private final Input b;

        OrGate(String out, Input a, Input b) {
            super(out);
            this.a = a;
            this.b = b;
        }

        @Override
        protected int getOutput(Map<String, Gate> gates, Map<String, Integer> env) {
            return (a.get(gates, env) | b.get(gates, env)) & BIT16_MASK;
        }
    }

    private static class NotGate extends Gate {

        private final Input a;

        NotGate(String out, Input a) {
            super(out);
            this.a = a;
        }

        @Override
        protected int getOutput(Map<String, Gate> gates, Map<String, Integer> env) {
            return ~a.get(gates, env) & BIT16_MASK;
        }
    }

    private static class LShiftGate extends Gate {

        private final Input a;
        private final int shift;

        LShiftGate(String out, Input a, int shift) {
            super(out);
            this.a = a;
            this.shift = shift;
        }

        @Override
        protected int getOutput(Map<String, Gate> gates, Map<String, Integer> env) {
            return (a.get(gates, env) << shift) & BIT16_MASK;
        }
    }

    private static class RShiftGate extends Gate {

        private final Input a;
        private final int shift;

        RShiftGate(String out, Input a, int shift) {
            super(out);
            this.a = a;
            this.shift = shift;
        }

        @Override
        protected int getOutput(Map<String, Gate> gates, Map<String, Integer> env) {
            return (a.get(gates, env) >> shift) & BIT16_MASK;
        }
    }

    private static class ForwardGate extends Gate {

        private final Input a;

        ForwardGate(String out, Input a) {
            super(out);
            this.a = a;
        }

        @Override
        protected int getOutput(Map<String, Gate> gates, Map<String, Integer> env) {
            return a.get(gates, env);
        }
    }

    private abstract static class Input {
        abstract int get(Map<String, Gate> gates, Map<String, Integer> env);
    }

    private static class NumberInput extends Input {

        private final int number;

        NumberInput(int number) {
            this.number = number;
        }

        @Override
        int get(Map<String, Gate> gates, Map<String, Integer> env) {
            return number;
        }
    }

    private static class WireInput extends Input {

        private final String wire;

        WireInput(String wire) {
            this.wire = wire;
        }

        @Override
        int get(Map<String, Gate> gates, Map<String, Integer> env) {
            return gates.get(wire).propagate(gates, env);
        }
    }
}

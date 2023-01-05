import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2022/day/11/input
 */
public class Day11 {

    public static void main(String[] args) throws Exception {
        part1(getMonkeys());
        part2(getMonkeys());
    }

   private static void part1(List<Monkey> monkeys) {
        BigInteger three = BigInteger.valueOf(3);
        int[] inspectedCount = new int[monkeys.size()];
        for (int n = 0; n < 20; n++) {
            for (int i = 0; i < monkeys.size(); i++) {
                Monkey m = monkeys.get(i);
                Iterator<BigInteger> items = m.items.iterator();
                while (items.hasNext()) {
                    BigInteger item = items.next();
                    // that's some scary stuff when using modulo
                    // don't know how to handle inverse, so no common procedure
                    BigInteger worry = m.operation.apply(item).divide(three);
                    if (worry.remainder(m.divisor).equals(BigInteger.ZERO)) {
                        monkeys.get(m.monkeyOnTrue).items.add(worry);
                    } else {
                        monkeys.get(m.monkeyOnFalse).items.add(worry);
                    }

                    inspectedCount[i]++;
                    items.remove();
                }
            }
        }

        Arrays.sort(inspectedCount);
        int monkeyBusiness = inspectedCount[monkeys.size() - 2] * inspectedCount[monkeys.size() - 1];
        System.out.println("Part 1: " + monkeyBusiness);
    }

    private static void part2(List<Monkey> monkeys) {
        BigInteger modulo = BigInteger.ONE;
        for (Monkey m : monkeys) {
            modulo = modulo.multiply(m.divisor);
        }

        long[] inspectedCount = new long[monkeys.size()];
        for (int n = 0; n < 10000; n++) {
            for (int i = 0; i < monkeys.size(); i++) {
                Monkey m = monkeys.get(i);
                Iterator<BigInteger> items = m.items.iterator();
                while (items.hasNext()) {
                    BigInteger item = items.next();
                    BigInteger worry = m.operation.apply(item).mod(modulo);
                    if (worry.remainder(m.divisor).equals(BigInteger.ZERO)) {
                        monkeys.get(m.monkeyOnTrue).items.add(worry);
                    } else {
                        monkeys.get(m.monkeyOnFalse).items.add(worry);
                    }

                    inspectedCount[i]++;
                    items.remove();
                }
            }
        }

        Arrays.sort(inspectedCount);
        long monkeyBusiness = inspectedCount[monkeys.size() - 2] * inspectedCount[monkeys.size() - 1];
        System.out.println("Part 2: " + monkeyBusiness);
    }

    private static List<Monkey> getMonkeys() throws Exception {
        List<Monkey> monkeys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Monkey monkey = new Monkey();

                String[] items = reader.readLine().split("[,:] ");
                for (int i = 1; i < items.length; i++) {
                    monkey.items.add(new BigInteger(items[i]));
                }

                String op = reader.readLine();
                if (op.contains("old * old")) {
                    monkey.operation = worry -> worry.multiply(worry);
                } else if (op.contains(" * ")) {
                    BigInteger multiplier = new BigInteger(op.split(" \\* ")[1]);
                    monkey.operation = worry -> worry.multiply(multiplier);
                } else if (op.contains(" + ")) {
                    BigInteger add = new BigInteger(op.split(" \\+ ")[1]);
                    monkey.operation = worry -> worry.add(add);
                } else {
                    throw new IllegalStateException(op);
                }

                BigInteger divisor = new BigInteger(reader.readLine().split("by ")[1]);
                monkey.divisor = divisor;
                monkey.monkeyOnTrue = Integer.parseInt(reader.readLine().split("monkey ")[1]);
                monkey.monkeyOnFalse = Integer.parseInt(reader.readLine().split("monkey ")[1]);
                reader.readLine();

                monkeys.add(monkey);
            }
        }

        return monkeys;
    }

    private static class Monkey {

        List<BigInteger> items = new ArrayList<>();
        Function<BigInteger, BigInteger> operation;
        BigInteger divisor;
        int monkeyOnTrue;
        int monkeyOnFalse;
    }
}

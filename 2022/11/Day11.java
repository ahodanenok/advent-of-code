import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2022/day/11/input
 */
public class Day11 {

    public static void main(String[] args) throws Exception {
        List<Monkey> monkeys = getMonkeys();
        part1(monkeys);
    }

    private static void part1(List<Monkey> monkeys) {
        int[] inspectedCount = new int[monkeys.size()];
        for (int n = 0; n < 20; n++) {
            for (int i = 0; i < monkeys.size(); i++) {
                Monkey m = monkeys.get(i);
                Iterator<Long> items = m.items.iterator();
                while (items.hasNext()) {
                    long item = items.next();
                    long worry = m.operation.apply(item);
                    worry /= 3;
                    if (m.test.test(worry)) {
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

    private static List<Monkey> getMonkeys() throws Exception {
        List<Monkey> monkeys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Monkey monkey = new Monkey();

                String[] items = reader.readLine().split("[,:] ");
                for (int i = 1; i < items.length; i++) {
                    monkey.items.add(Long.parseLong(items[i]));
                }

                String op = reader.readLine();
                if (op.contains("old * old")) {
                    monkey.operation = worry -> worry * worry;
                } else if (op.contains(" * ")) {
                    long multiplier = Long.parseLong(op.split(" \\* ")[1]);
                    monkey.operation = worry -> worry * multiplier;
                } else if (op.contains(" + ")) {
                    long add = Long.parseLong(op.split(" \\+ ")[1]);
                    monkey.operation = worry -> worry + add;
                } else {
                    throw new IllegalStateException(op);
                }

                long divisor = Long.parseLong(reader.readLine().split("by ")[1]);
                monkey.test = worry -> worry % divisor == 0;
                monkey.monkeyOnTrue = Integer.parseInt(reader.readLine().split("monkey ")[1]);
                monkey.monkeyOnFalse = Integer.parseInt(reader.readLine().split("monkey ")[1]);
                reader.readLine();

                monkeys.add(monkey);
            }
        }

        return monkeys;
    }

    private static class Monkey {

        List<Long> items = new ArrayList<>();
        Function<Long, Long> operation;
        Predicate<Long> test;
        int monkeyOnTrue;
        int monkeyOnFalse;
    }
}

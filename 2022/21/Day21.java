import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2022/day/21
 */
public class Day21 {

    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        List<Monkey> monkeys = getMonkeys("input-1.txt");

        Map<String, Monkey> context = new HashMap<>();
        for (Monkey m : monkeys) {
            context.put(m.name, m);
        }

        System.out.println("Part 1: " + context.get("root").job.apply(context));
    }

    private static void part2() throws Exception {
        List<Monkey> monkeys = getMonkeys("input-2.txt");

        Map<String, Monkey> context = new HashMap<>();
        for (Monkey m : monkeys) {
            context.put(m.name, m);
        }

        long lo = 0;
        long hi = 1;
        // works only for the decreasing case, as it is in my input :)
        while (true) {
            if (evalWithInput(hi, context) < 0) {
                break;
            }

            lo = hi;
            hi *= 2;
        }

        long n;
        while (true) {
            n = lo + (hi - lo) / 2;

            long res = evalWithInput(n, context);
            if (res == 0) {
                break;
            }

            if (res < 0) {
                hi = n - 1;
            } else {
                lo = n + 1;
            }
        }

        while (evalWithInput(n - 1, context) == 0) {
            n--;
        }

        System.out.println("Part 2: " + n);
    }

    private static long evalWithInput(long n, Map<String, Monkey> context) {
        context.put("humn", new Monkey("humn", new NumberJob(n)));
        return context.get("root").job.apply(context);
    }

    private static List<Monkey> getMonkeys(String inputFile) throws Exception {
        List<Monkey> monkeys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            Pattern numPattern = Pattern.compile("^([a-z]+): ([0-9]+)$");
            Pattern sumPattern = Pattern.compile("^([a-z]+): ([a-z]+) \\+ ([a-z]+)$");
            Pattern subPattern = Pattern.compile("^([a-z]+): ([a-z]+) - ([a-z]+)$");
            Pattern divPattern = Pattern.compile("^([a-z]+): ([a-z]+) / ([a-z]+)$");
            Pattern mulPattern = Pattern.compile("^([a-z]+): ([a-z]+) \\* ([a-z]+)$");
            Pattern eqPattern = Pattern.compile("^([a-z]+): ([a-z]+) = ([a-z]+)$");

            String line;
            Matcher m;
            while ((line = reader.readLine()) != null) {
                m = numPattern.matcher(line);
                if (m.find()) {
                    monkeys.add(new Monkey(m.group(1), new NumberJob(Long.parseLong(m.group(2)))));
                    continue;
                }

                m = sumPattern.matcher(line);
                if (m.find()) {
                    monkeys.add(new Monkey(m.group(1), new SumJob(m.group(2), m.group(3))));
                    continue;
                }

                m = subPattern.matcher(line);
                if (m.find()) {
                    monkeys.add(new Monkey(m.group(1), new SubtractJob(m.group(2), m.group(3))));
                    continue;
                }

                m = divPattern.matcher(line);
                if (m.find()) {
                    monkeys.add(new Monkey(m.group(1), new DivideJob(m.group(2), m.group(3))));
                    continue;
                }

                m = mulPattern.matcher(line);
                if (m.find()) {
                    monkeys.add(new Monkey(m.group(1), new MultiplyJob(m.group(2), m.group(3))));
                    continue;
                }

                m = eqPattern.matcher(line);
                if (m.find()) {
                    monkeys.add(new Monkey(m.group(1), new EqualsJob(m.group(2), m.group(3))));
                    continue;
                }

                throw new IllegalStateException(line);
            }
        }

        return monkeys;
    }

    private static class Monkey {

        final String name;
        final Job job;

        Monkey(String name, Job job) {
            this.name = name;
            this.job = job;
        }
    }

    private interface Job {

        long apply(Map<String, Monkey> context);
    }

    private static class NumberJob implements Job {

        final long num;

        NumberJob(long num) {
            this.num = num;
        }

        @Override
        public long apply(Map<String, Monkey> context) {
            return num;
        }
    }

    private static class SumJob implements Job {

        final String leftMonkeyName;
        final String rightMonkeyName;

        SumJob(String leftMonkeyName, String rightMonkeyName) {
            this.leftMonkeyName = leftMonkeyName;
            this.rightMonkeyName = rightMonkeyName;
        }

        @Override
        public long apply(Map<String, Monkey> context) {
            Monkey leftMonkey = context.get(leftMonkeyName);
            Monkey rightMonkey = context.get(rightMonkeyName);

            return leftMonkey.job.apply(context) + rightMonkey.job.apply(context);
        }
    }

    private static class SubtractJob implements Job {

        final String leftMonkeyName;
        final String rightMonkeyName;

        SubtractJob(String leftMonkeyName, String rightMonkeyName) {
            this.leftMonkeyName = leftMonkeyName;
            this.rightMonkeyName = rightMonkeyName;
        }

        @Override
        public long apply(Map<String, Monkey> context) {
            Monkey leftMonkey = context.get(leftMonkeyName);
            Monkey rightMonkey = context.get(rightMonkeyName);

            return leftMonkey.job.apply(context) - rightMonkey.job.apply(context);
        }
    }

    private static class MultiplyJob implements Job {

        final String leftMonkeyName;
        final String rightMonkeyName;

        MultiplyJob(String leftMonkeyName, String rightMonkeyName) {
            this.leftMonkeyName = leftMonkeyName;
            this.rightMonkeyName = rightMonkeyName;
        }

        @Override
        public long apply(Map<String, Monkey> context) {
            Monkey leftMonkey = context.get(leftMonkeyName);
            Monkey rightMonkey = context.get(rightMonkeyName);

            return leftMonkey.job.apply(context) * rightMonkey.job.apply(context);
        }
    }

    private static class DivideJob implements Job {

        final String leftMonkeyName;
        final String rightMonkeyName;

        DivideJob(String leftMonkeyName, String rightMonkeyName) {
            this.leftMonkeyName = leftMonkeyName;
            this.rightMonkeyName = rightMonkeyName;
        }

        @Override
        public long apply(Map<String, Monkey> context) {
            Monkey leftMonkey = context.get(leftMonkeyName);
            Monkey rightMonkey = context.get(rightMonkeyName);

            return leftMonkey.job.apply(context) / rightMonkey.job.apply(context);
        }
    }

    private static class EqualsJob implements Job {

        final String leftMonkeyName;
        final String rightMonkeyName;

        EqualsJob(String leftMonkeyName, String rightMonkeyName) {
            this.leftMonkeyName = leftMonkeyName;
            this.rightMonkeyName = rightMonkeyName;
        }

        @Override
        public long apply(Map<String, Monkey> context) {
            Monkey leftMonkey = context.get(leftMonkeyName);
            Monkey rightMonkey = context.get(rightMonkeyName);

            long left = leftMonkey.job.apply(context);
            long right = rightMonkey.job.apply(context);

            return Long.compare(left, right);
        }
    }
}

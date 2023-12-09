import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2023/day/8
 */
public class Day8 {

    public static void main(String[] args) throws Exception {
        Maps maps = getInput();
        part1(maps);
        part2(maps);
    }

    private static void part1(Maps maps) {
        System.out.println("Part 1: " + countSteps(maps, "AAA", node -> node.equals("ZZZ")));
    }

    private static void part2(Maps maps) {
        List<Integer> periods = new ArrayList<>();
        for (String node : maps.nodeLeft.keySet()) {
            if (node.endsWith("A")) {
                // the number of steps needed to reach the first node ending with 'Z'is the period for that node,
                // that is a node ending with 'Z' will be visited every `period` steps
                periods.add(countSteps(maps, node, n -> n.endsWith("Z")));
            }
        }

        List<Integer> commonFactors = new ArrayList<>();
        for (Integer p : periods) {
            List<Integer> factors = factorize(p);
            for (Integer f : factors) {
                long neededCount = factors.stream().filter(n -> n.equals(f)).count();
                long currentCount = commonFactors.stream().filter(n -> n.equals(f)).count();
                while (currentCount < neededCount) {
                    commonFactors.add(f);
                    currentCount++;
                }
            }
        }

        BigInteger steps = commonFactors.stream()
            .map(BigInteger::valueOf)
            .reduce(BigInteger.ONE, BigInteger::multiply);
        System.out.println("Part 2: " + steps);
    }

    private static int countSteps(Maps maps, String node, Predicate<String> condition) {
        int steps = 0;
        int pos = 0;
        String currentNode = node;
        while (!condition.test(currentNode)) {
            char inst = maps.instructions.charAt(pos);
            if (inst == 'R') {
                currentNode = maps.nodeRight.get(currentNode);
            } else if (inst == 'L') {
                currentNode = maps.nodeLeft.get(currentNode);
            } else {
                throw new IllegalStateException("inst: " + inst);
            }

            pos = (pos + 1) % maps.instructions.length();
            steps++;
        }

        return steps;
    }

    private static List<Integer> factorize(int n) {
        List<Integer> factors = new ArrayList<>();
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            int f = queue.removeLast();
            boolean divisable = false;
            for (int i = 2; i < f / 2; i++) {
                if (f % i == 0) {
                    queue.addLast(i);
                    queue.addLast(f / i);
                    divisable = true;
                    break;
                }
            }

            if (!divisable) {
                factors.add(f);
            }
        }

        return factors;
    }

    private static Maps getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Maps maps = new Maps();
            maps.instructions = reader.readLine().trim();

            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=");
                String[] nodes = parts[1].trim().split(",");
                maps.nodeLeft.put(parts[0].trim(), nodes[0].trim().substring(1));
                maps.nodeRight.put(parts[0].trim(), nodes[1].trim().substring(0, nodes[1].trim().length() - 1));
            }

            return maps;
        }
    }

    private static class Maps {

        String instructions;
        Map<String, String> nodeLeft = new HashMap<>();
        Map<String, String> nodeRight = new HashMap<>();
    }
}

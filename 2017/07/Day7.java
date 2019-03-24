import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2017/day/7
 */
public class Day7 {

    public static void main(String[] args) {
        List<Program> programs = getPrograms();
        System.out.println(getRoot(programs).name);
        System.out.println(balanceWeight(getRoot(programs), programs));
    }

    private static void part1(List<Program> programs) {
        System.out.println(getRoot(programs).name);
    }

    private static void part2(List<Program> programs) {
        System.out.println(balanceWeight(getRoot(programs), programs));
    }

    private static Program getRoot(List<Program> programs) {
        for (int i = 0; i < programs.size(); i++) {
            boolean hasRef = false;
            for (int j = 0; j < programs.size(); j++) {
                if (programs.get(j).holding.contains(programs.get(i).name)) {
                    hasRef = true;
                    break;
                }
            }

            if (!hasRef) {
                return programs.get(i);
            }
        }

        return null;
    }

    private static int balanceWeight(Program root, List<Program> programs) {
        int[] weights = new int[root.holding.size()];
        for (int i = 0; i < root.holding.size(); i++) {
            weights[i] = weight(root.holding.get(i), programs);
        }

        for (int i = 0; i < weights.length; i++) {
            int count = 0;
            for (int j = 0; j < weights.length; j++) {
                if (weights[i] == weights[j]) {
                    count++;
                }
            }

            // only one weight different from others, others are equal
            if (count == 1) {
                Program p = getProgram(root.holding.get(i), programs);
                // going down while there is no balance
                int result = balanceWeight(p, programs);
                if (result == 0) {
                    int unbalanced = weights[i];
                    int balanced = weights[(i + 1) % weights.length];
                    if (unbalanced > balanced) {
                        return p.weight - (unbalanced - balanced);
                    } else if (unbalanced < balanced) {
                        return p.weight + (balanced - unbalanced);
                    } else {
                        throw new IllegalStateException("weights are equal");
                    }
                } else {
                    return result;
                }
            }
        }

        return 0;
    }

    private static int weight(String programName, List<Program> programs) {
        Program p = getProgram(programName, programs);
        if (p.holding.size() == 0) {
            return p.weight;
        } else {
            int w = p.weight;
            for (String name : p.holding) {
                w += weight(name, programs);
            }

            return w;
        }
    }

    private static Program getProgram(String programName, List<Program> programs) {
        for (Program p : programs) {
            if (p.name.equals(programName)) {
                return p;
            }
        }

        return null;
    }

    private static List<Program> getPrograms() {
        List<Program> programs = new ArrayList<Program>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("\\s+");
            List<String> holding = new ArrayList<String>();
            for (int i = 3; i < parts.length; i++) {
                holding.add(parts[i].substring(0, i != parts.length - 1 ? parts[i].length() - 1 : parts[i].length()));
            }

            programs.add(new Program(
                parts[0],
                Integer.parseInt(parts[1].substring(1, parts[1].length() - 1)),
                holding));
        }

        return programs;
    }

    private static class Program {

        private final String name;
        private final int weight;
        private final List<String> holding;

        private Program(String name, int weight, List<String> holding) {
            this.name = name;
            this.weight = weight;
            this.holding = holding;
        }

        @Override
        public String toString() {
            return name + " (" + weight + ") -> " + holding; 
        }
    }
}

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2015/day/24
 */
public class Day24 {

    public static void main(String[] args) {
        List<Integer> weights = getWeights();
        part1(weights);
    }

    private static List<Integer> getWeights() {
        List<Integer> weights = new ArrayList<Integer>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            weights.add(Integer.parseInt(scanner.nextLine().trim()));
        }

        return weights;
    }

    private static void part1(List<Integer> weights) {
        int weightSum = 0;
        for (Integer w : weights) {
            weightSum += w;
        }

        System.out.println(qe(balanceGroup(weights, new ArrayList<Integer>(), 0, weightSum / 3)));
    }

    private static List<Integer> balanceGroup(List<Integer> weights, List<Integer> group, int start, int groupWeight) {
        int w = weight(group);
        if (w > groupWeight) {
            return null;
        } else if (w == groupWeight) {
            return new ArrayList<Integer>(group);
        } else {
            List<Integer> resultGroup = null;
            for (int i = start; i < weights.size(); i++) {
                group.add(weights.get(i));
                List<Integer> foundGroup = balanceGroup(weights, group, i + 1, groupWeight);
                if (foundGroup != null
                      && (resultGroup == null
                          || foundGroup.size() < resultGroup.size()
                          || (foundGroup.size() == resultGroup.size() && qe(foundGroup) < qe(resultGroup)))) {
                    resultGroup = foundGroup;
                }
                group.remove(group.size() - 1);
            }

            return resultGroup;
        }
    }

    private static int weight(List<Integer> group) {
        int value = 0;
        for (Integer w : group) {
            value += w;
        }

        return value;
    }

    private static long qe(List<Integer> group) {
        long value = 1;
        for (Integer w : group) {
            value *= w;
        }

        return value;
    }
}

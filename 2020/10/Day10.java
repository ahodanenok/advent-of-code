import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2020/day/10
 */
public class Day10 {

    public static void main(String[] args) throws Exception {
        List<Integer> adapters = getAdapters();
        adapters.sort(Comparator.naturalOrder());
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        int oneCount = 0;
        int threeCount = 0;
        int prevJolt = 0;
        for (int n : adapters) {
            if (n - prevJolt == 1) {
                oneCount++;
            } else if (n - prevJolt == 3) {
                threeCount++;
            }

            prevJolt = n;
        }

        System.out.println("Part 1: " + oneCount * threeCount);
    }

    private static List<Integer> getAdapters() throws Exception {
        List<Integer> adapters = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                adapters.add(Integer.parseInt(line));
            }
        }

        return adapters;
    }
}

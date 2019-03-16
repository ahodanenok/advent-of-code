import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2017/day/2
 */
public class Day2 {

    public static void main(String[] args) {
        List<List<Integer>> data = getData();
        part1(data);
        part2(data);
    }

    private static void part1(List<List<Integer>> data) {
        int sum = 0;
        for (List<Integer> row : data) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int b : row) {
                min = Math.min(b, min);
                max = Math.max(b, max);
            }

            sum += max - min;
        }

        System.out.println(sum);
    }

    private static void part2(List<List<Integer>> data) {
        int sum = 0;
        for (List<Integer> row : data) {
            int div = 0;

            divisableSearch: 
            for (int i = 0; i < row.size(); i++) {
                for (int j = 0; j < row.size(); j++) {
                    if (i == j) continue;
                    if (row.get(i) % row.get(j) == 0) {
                        div = row.get(i) / row.get(j);
                        break divisableSearch;
                    }
                }
            }

            sum += div;
        }

        System.out.println(sum);
    }

    private static List<List<Integer>> getData() {
        List<List<Integer>> data = new ArrayList<List<Integer>>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            List<Integer> row = new ArrayList<Integer>(parts.length);
            for (String part : parts) {
                row.add(Integer.parseInt(part));
            }

            data.add(row);
        }

        return data;
    }
}

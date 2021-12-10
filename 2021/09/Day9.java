import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2021/day/9
 */
public class Day9 {

    public static void main(String[] args) throws Exception {
        List<String> map = getInput();
        part1(map);
    }

    private static List<String> getInput() throws Exception {
        List<String> map = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                map.add(line.trim());
            }
        }

        return map;
    }

    private static void part1(List<String> map) {
        int sum = 0;
        for (int i = 0; i < map.size(); i++) {
            String row = map.get(i);
            for (int j = 0; j < row.length(); j++) {
                char col = row.charAt(j);

                // top-left
                if (i > 0 && j > 0 && map.get(i - 1).charAt(j - 1) <= col) {
                    continue;
                }

                // top
                if (i > 0 && map.get(i - 1).charAt(j) <= col) {
                    continue;
                }

                // top-right
                if (i > 0 && j < row.length() - 1 && map.get(i - 1).charAt(j + 1) <= col) {
                    continue;
                }

                // left
                if (j > 0 && row.charAt(j - 1) <= col) {
                    continue;
                }

                // right
                if (j < row.length() - 1 && row.charAt(j + 1) <= col) {
                    continue;
                }

                // bottom-left
                if (i < map.size() - 1 && j > 0 && map.get(i + 1).charAt(j - 1) <= col) {
                    continue;
                }

                // bottom
                if (i < map.size() - 1 && map.get(i + 1).charAt(j) <= col) {
                    continue;
                }

                // bottom-right
                if (i < map.size() - 1 && j < row.length() - 1 && map.get(i + 1).charAt(j + 1) <= col) {
                    continue;
                }

                sum += (col - '0') + 1;
            }
        }

        System.out.println("Part 1: " + sum);
    }
}

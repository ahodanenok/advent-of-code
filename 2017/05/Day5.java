import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2017/day/5
 */
public class Day5 {

    public static void main(String[] args)  {
        List<Integer> offsets = getOffsets();
        part1(offsets);
    }

    private static void part1(List<Integer> offsets) {
        int steps = 0;
        int idx = 0;
        while (idx >= 0 && idx < offsets.size()) {
            int offset = offsets.get(idx);
            offsets.set(idx, offset + 1);
            idx += offset;
            steps++;
        }

        System.out.println(steps);
    }

    private static List<Integer> getOffsets() {
        List<Integer> offsets = new ArrayList<Integer>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            offsets.add(Integer.parseInt(scanner.nextLine()));
        }

        return offsets;
    }
}

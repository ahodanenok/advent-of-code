import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2017/day/17
 */
public class Day17 {

    public static void main(String[] args) {
        part1(386);
        part2(386);
    }

    private static void part1(int step) {
        int currPos = 0;
        List<Integer> buf = new ArrayList<Integer>();
        buf.add(0);

        for (int n = 1; n <= 2017; n++) {
            currPos = (currPos + step) % buf.size() + 1;
            buf.add(currPos, n);
        }

        System.out.println(buf.get((currPos + 1) % buf.size()));
    }

    private static void part2(int step) {
        int size = 1;
        int pos = 0;
        int value = 0;

        for (int n = 1; n <= 50_000_000; n++) {
            pos = (pos + step) % size + 1;
            if (pos == 1) {
                value = n;
            }

            size++;
        }

        System.out.println(value);
    }
}

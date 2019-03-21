import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2017/day/6
 */
public class Day6 {

    public static void main(String[] args) {
        int[] blocks = new int[] { 10, 3, 15, 10, 5, 15, 5, 15, 9, 2, 5, 8, 5, 2, 3, 6 };
        //int[] blocks = new int[] { 0, 2, 7, 0 };
        part1(blocks);
        part2(blocks);
    }

    private static void part1(int[] blocks) {
        int steps = 0;
        int[] state = new int[blocks.length];
        System.arraycopy(blocks, 0, state, 0, blocks.length);

        Set<String> seen = new HashSet<String>();
        do {
            seen.add(Arrays.toString(state));
            move(state);
            steps++;
        } while (!seen.contains(Arrays.toString(state)));

        System.out.println(steps);
    }

    private static void part2(int[] blocks) {
        int[] state = new int[blocks.length];
        System.arraycopy(blocks, 0, state, 0, blocks.length);

        Set<String> seen = new HashSet<String>();
        do {
            seen.add(Arrays.toString(state));
            move(state);
        } while (!seen.contains(Arrays.toString(state)));

        int[] toFind = new int[state.length];
        System.arraycopy(state, 0, toFind, 0, toFind.length);

        int steps = 0;
        do {
            move(state);
            steps++;
        } while (!Arrays.equals(toFind, state));

        System.out.println(steps);
    }

    private static void move(int[] blocks) {
        int maxIdx = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] > blocks[maxIdx]) {
                maxIdx = i;
            }
        }

        int idx = (maxIdx + 1) % blocks.length;
        int blocksLeft = blocks[maxIdx];
        blocks[maxIdx] = 0;

        while (blocksLeft > 0) {
            blocks[idx]++;
            blocksLeft--;
            idx = (idx + 1) % blocks.length;
        }
    }
}

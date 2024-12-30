import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2024/day/25
 */
public class Day25 {

	public static void main(String... args) throws Exception {
        Input input = getInput();
        part1(input);
    }

    private static void part1(Input input) {
        int fitCount = 0;
        for (int[] lock : input.locks) {
            for (int[] key : input.keys) {
                boolean fit = true;
                for (int i = 0; i < key.length; i++) {
                    if (key[i] + lock[i] > input.height) {
                        fit = false;
                        break;
                    }
                }

                if (fit) {
                    fitCount++;
                }
            }
        }

        System.out.println("Part 1: " + fitCount);
    }

    private static Input getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<int[]> locks = new ArrayList<>();
            List<int[]> keys = new ArrayList<>();
            int height = 0;
            boolean heightSet = false;
            int[] buf = null;
            boolean lock = true;
            String line;
            while (true) {            
                line = reader.readLine();
                if (line == null || line.isBlank()) {
                    for (int i = 0; i < buf.length; i++) {
                        buf[i]--;
                    }

                    if (lock) {
                        locks.add(buf);
                    } else {
                        keys.add(buf);
                    }

                    buf = null;
                    heightSet = true;
                }

                if (line == null) {
                    break;
                }
                if (line.isBlank()) {
                    continue;
                }

                if (buf == null) {
                    buf = new int[line.length()];
                    lock = line.charAt(0) == '#';
                }

                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '#') {
                        buf[i]++;
                    }
                }

                if (!heightSet) {
                    height++;
                }
            }

            return new Input(height - 2, locks, keys);
        }
    }

    private static class Input {

        final int height;
        final List<int[]> locks;
        final List<int[]> keys;

        Input(int height, List<int[]> locks, List<int[]> keys) {
            this.height = height;
            this.locks = locks;
            this.keys = keys;
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2022/day/20
 */
public class Day20 {

    public static void main(String[] args) throws Exception {
        List<Integer> file = getEncryptedFile();
        part1(file);
    }

    private static void part1(List<Integer> file) {
        Num[] numbers = file.stream().map(Num::new).toArray(n -> new Num[n]);

        int movedCount = 0;
        int nIdx = 0;
        while (movedCount < numbers.length) {
            Num n = numbers[nIdx];
            if (n.moved) {
                nIdx = (nIdx + 1) % numbers.length;
                continue;
            }

            nIdx = move(numbers, nIdx);
            movedCount++;
        }

        int zeroIdx = -1;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i].value == 0) {
                zeroIdx = i;
                break;
            }
        }

        int a = numbers[(zeroIdx + 1000) % numbers.length].value;
        int b = numbers[(zeroIdx + 2000) % numbers.length].value;
        int c = numbers[(zeroIdx + 3000) % numbers.length].value;
        System.out.println("Part 1: " + (a + b + c));
    }

    private static int move(Num[] numbers, int idx) {
        Num n = numbers[idx];
        int t = numbers.length - 1;

        int toIdx;
        // black magic begins!
        if (n.value > 0) {
            toIdx = (idx + n.value) % t;
        } else if (n.value < 0) {
            toIdx = (t + ((idx + n.value) % t)) % t;
        } else {
            toIdx = idx;
        }

        int nextIdx;
        if (toIdx > idx) {
            System.arraycopy(numbers, idx + 1, numbers, idx, toIdx - idx);
            nextIdx = idx;
        } else if (toIdx < idx) {
            System.arraycopy(numbers, toIdx, numbers, toIdx + 1, idx - toIdx);
            nextIdx = (idx + 1) % numbers.length;
        } else {
            nextIdx = (idx + 1) % numbers.length;
        }

        numbers[toIdx] = n;
        n.moved = true;

        return nextIdx;
    }

    private static List<Integer> getEncryptedFile() throws Exception {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(Integer.parseInt(line));
            }
        }

        return data;
    }

    private static class Num {

        final int value;
        boolean moved;

        Num(int value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return value + "";
        }
    }
}

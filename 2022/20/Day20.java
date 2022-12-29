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
        part2(file);
    }

    private static void part1(List<Integer> file) {
        Num[] numbers = new Num[file.size()];
        for (int i = 0; i < file.size(); i++) {
            numbers[i] = new Num(i, file.get(i));
        }

        mix(numbers);

        int zeroIdx = 0;
        while (numbers[zeroIdx].value != 0) {
            zeroIdx++;
        }

        long a = numbers[(zeroIdx + 1000) % numbers.length].value;
        long b = numbers[(zeroIdx + 2000) % numbers.length].value;
        long c = numbers[(zeroIdx + 3000) % numbers.length].value;
        System.out.println("Part 1: " + (a + b + c));
    }

    private static void part2(List<Integer> file) {
        Num[] numbers = new Num[file.size()];
        for (int i = 0; i < file.size(); i++) {
            numbers[i] = new Num(i, file.get(i) * 811589153L);
        }

        for (int t = 0; t < 10; t++) {
            mix(numbers);
        }

        int zeroIdx = 0;
        while (numbers[zeroIdx].value != 0) {
            zeroIdx++;
        }

        long a = numbers[(zeroIdx + 1000) % numbers.length].value;
        long b = numbers[(zeroIdx + 2000) % numbers.length].value;
        long c = numbers[(zeroIdx + 3000) % numbers.length].value;
        System.out.println("Part 2: " + (a + b + c));
    }

    private static void mix(Num[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            int idx = 0;
            while (numbers[idx].idx != i) {
                idx++;
            }

            move(numbers, idx);
        }
    }

    private static void move(Num[] numbers, int idx) {
        Num n = numbers[idx];
        int t = numbers.length - 1;

        int toIdx;
        // black magic begins!
        if (n.value > 0) {
            toIdx = (int) ((idx + n.value) % t);
        } else if (n.value < 0) {
            toIdx = (int) ((t + ((idx + n.value) % t)) % t);
        } else {
            toIdx = idx;
        }

        if (toIdx > idx) {
            System.arraycopy(numbers, idx + 1, numbers, idx, toIdx - idx);
        } else if (toIdx < idx) {
            System.arraycopy(numbers, toIdx, numbers, toIdx + 1, idx - toIdx);
        }

        numbers[toIdx] = n;
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

        final int idx;
        final long value;

        Num(int idx, long value) {
            this.idx = idx;
            this.value = value;
        }

        @Override
        public String toString() {
            return value + "";
        }
    }
}

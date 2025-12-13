import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2025/day/3
 */
public class Day3 {

    public static void main(String... args) throws Exception {
        List<Bank> banks = getInput();
        part1(banks);
    }

    private static List<Bank> getInput() throws Exception {
        List<Bank> banks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<Integer> batteries = new ArrayList<>();
                for (int i = 0; i < line.length(); i++) {
                    batteries.add(line.charAt(i) - '0');
                }

                banks.add(new Bank(batteries));
            }
        }

        return banks;
    }

    private static void part1(List<Bank> banks) {
        int sum = 0;
        for (Bank bank : banks) {
            int idx1 = bank.maxBatteryIndex(0, bank.size() - 1);
            int idx2 = bank.maxBatteryIndex(idx1 + 1, bank.size());
            sum += bank.joltage(idx1) * 10 + bank.joltage(idx2);
        }

        System.out.println("Part 1: " + sum);
    }

    private static class Bank {

        final List<Integer> batteries;

        Bank(List<Integer> batteries) {
            this.batteries = batteries;
        }

        int joltage(int index) {
            return batteries.get(index);
        }

        int size() {
            return batteries.size();
        }

        int maxBatteryIndex(int fromIndex, int toIndex) {
            int maxBattery = batteries.get(fromIndex);
            int maxIndex = fromIndex;
            for (int i = fromIndex + 1; i < toIndex; i++) {
                if (batteries.get(i) > maxBattery) {
                    maxBattery = batteries.get(i);
                    maxIndex = i;
                }
            }

            return maxIndex;
        }
    }
}

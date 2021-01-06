import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.StringJoiner;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2020/day/23
 */
public class Day23 {

    public static void main(String[] args) {
        Cups cups = new Cups("523764819");

        for (int t = 0; t < 100; t++) {
            cups.makeTurn();
        }

        StringJoiner str = new StringJoiner("");
        Cup currentCup = cups.cups.get(1).next;
        while (currentCup.num != 1) {
            str.add(currentCup.num + "");
            currentCup = currentCup.next;
        }

        System.out.println("Part 1: " + str);
    }

    private static class Cup {

        int num;
        Cup prev;
        Cup next;

        Cup(int num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return String.format("Cup %d (prev=%d, next=%d)", num, prev.num, next.num);
        }
    }

    private static class Cups {

        private Map<Integer, Cup> cups;
        private int currentNum;
        private int minNum;
        private int maxNum;

        Cups(String def) {
            minNum = Integer.MAX_VALUE;
            maxNum = Integer.MIN_VALUE;
            cups = new HashMap<>();
            for (int i = 0; i < def.length(); i++) {
                Cup current = cups.computeIfAbsent(def.charAt(i) - '0', Cup::new);
                Cup next = cups.computeIfAbsent(def.charAt((i + 1) % def.length()) - '0', Cup::new);

                current.next = next;
                next.prev = current;

                minNum = Math.min(current.num, minNum);
                maxNum = Math.max(current.num, maxNum);
            }

            currentNum = def.charAt(0) - '0';
        }

        void makeTurn() {
            Cup currentCup = cups.get(currentNum);
            Cup firstCupToRemove = currentCup.next;
            Cup lastCupToRemove = firstCupToRemove.next.next;

            currentCup.next = lastCupToRemove.next;
            lastCupToRemove.next.prev = currentCup;

            int destinationNum = currentNum;
            do {
                destinationNum--;
                if (destinationNum < minNum) {
                    destinationNum = maxNum;
                }
            } while (destinationNum == currentNum
                    || firstCupToRemove.num == destinationNum
                    || firstCupToRemove.next.num == destinationNum
                    || firstCupToRemove.next.next.num == destinationNum);

            Cup destinationCup = cups.get(destinationNum);
            lastCupToRemove.next = destinationCup.next;
            firstCupToRemove.prev = destinationCup;
            destinationCup.next.prev = lastCupToRemove;
            destinationCup.next = firstCupToRemove;

            currentNum = currentCup.next.num;
        }
    }
}
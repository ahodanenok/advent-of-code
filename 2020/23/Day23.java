import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.Iterator;
import java.math.BigInteger;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2020/day/23
 */
public class Day23 {

    public static void main(String[] args) {
        String input = "523764819";
        part1(input);
        part2(input);
    }

    private static void part1(String input) {
        GameOfCrab game = new GameOfCrab(new CupsIterator(input));
        for (int t = 0; t < 100; t++) {
            game.makeTurn();
        }

        StringJoiner str = new StringJoiner("");
        Cup currentCup = game.cups.get(1).next;
        while (currentCup.num != 1) {
            str.add(currentCup.num + "");
            currentCup = currentCup.next;
        }

        System.out.println("Part 1: " + str);
    }

    private static void part2(String input) {
        GameOfCrab game = new GameOfCrab(new CupsIterator(input, 1_000_000));
        for (int t = 0; t < 10_000_000; t++) {
            game.makeTurn();
        }

        Cup oneCup = game.cups.get(1);
        BigInteger result = BigInteger.valueOf(oneCup.next.num)
                .multiply(BigInteger.valueOf(oneCup.next.next.num));
        System.out.println("Part 2: " + result);
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

    private static class GameOfCrab {

        Map<Integer, Cup> cups;
        Cup currentCup;
        int minNum;
        int maxNum;

        GameOfCrab(CupsIterator iterator) {
            cups = new HashMap<>();

            Cup firstCup = iterator.next();
            cups.put(firstCup.num, firstCup);
            minNum = firstCup.num;
            maxNum = firstCup.num;

            Cup prevCup = firstCup;
            while (iterator.hasNext()) {
                Cup cup = iterator.next();
                cups.put(cup.num, cup);
                prevCup.next = cup;
                cup.prev = prevCup;
                prevCup = cup;

                minNum = Math.min(cup.num, minNum);
                maxNum = Math.max(cup.num, maxNum);
            }

            firstCup.prev = prevCup;
            prevCup.next = firstCup;

            currentCup = firstCup;
        }

        void makeTurn() {
            Cup firstCupToRemove = currentCup.next;
            Cup lastCupToRemove = firstCupToRemove.next.next;

            currentCup.next = lastCupToRemove.next;
            lastCupToRemove.next.prev = currentCup;

            int destinationNum = currentCup.num;
            do {
                destinationNum--;
                if (destinationNum < minNum) {
                    destinationNum = maxNum;
                }
            } while (destinationNum == currentCup.num
                    || firstCupToRemove.num == destinationNum
                    || firstCupToRemove.next.num == destinationNum
                    || firstCupToRemove.next.next.num == destinationNum);

            Cup destinationCup = cups.get(destinationNum);
            lastCupToRemove.next = destinationCup.next;
            firstCupToRemove.prev = destinationCup;
            destinationCup.next.prev = lastCupToRemove;
            destinationCup.next = firstCupToRemove;

            currentCup = currentCup.next;
        }
    }

    private static class CupsIterator implements Iterator<Cup> {

        private String input;
        private int idx;
        private int limit;

        CupsIterator(String input) {
            this(input, input.length());
        }

        CupsIterator(String input, int limit) {
            this.idx = 1;
            this.input = input;
            this.limit = limit;
        }

        @Override
        public Cup next() {
            Integer result;
            if (idx <= input.length()) {
                result = input.charAt(idx - 1) - '0';
            } else {
                result = idx;
            }

            idx++;
            return new Cup(result);
        }

        @Override
        public boolean hasNext() {
            return idx <= limit;
        }
    }
}
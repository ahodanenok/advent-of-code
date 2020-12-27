import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2020/day/15
 */
public class Day15 {

    public static void main(String[] args) throws Exception {
        List<Integer> input = Arrays.asList(1, 17, 0, 10, 18, 11, 6);
        System.out.println("Part 1: " + numberSpokenAt(input, 2020));
        System.out.println("Part 2: " + numberSpokenAt(input, 30000000));
    }

    private static int numberSpokenAt(List<Integer> startingNumbers, final int turn) {
        Map<Integer, Integer> lastMentioned = new HashMap<>();
        for (int i = 0; i < startingNumbers.size(); i++) {
            lastMentioned.put(startingNumbers.get(i), i + 1);
        }

        int numberSpoken = startingNumbers.get(startingNumbers.size() - 1);
        int prevTurn = startingNumbers.size();
        while (true) {
            Integer spokenAtTurn = lastMentioned.getOrDefault(numberSpoken, prevTurn);
            //System.out.printf("turn=%d, number=%d, spokenAtTurn=%d%n", prevTurn, numberSpoken, spokenAtTurn);

            int nextNumber;
            if (spokenAtTurn != prevTurn) {
                nextNumber = prevTurn - spokenAtTurn;
            } else {
                nextNumber = 0;
            }

            lastMentioned.put(numberSpoken, prevTurn);
            if (prevTurn == turn) {
                break;
            }

            numberSpoken = nextNumber;
            prevTurn++;
        }

        return numberSpoken;
    }
}

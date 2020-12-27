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
        int targetTurn = 2020;

        Map<Integer, Integer> lastMentioned = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            lastMentioned.put(input.get(i), i + 1);
        }

        int numberSpoken = input.get(input.size() - 1);
        int prevTurn = input.size();
        while (true) {
            Integer spokenAtTurn = lastMentioned.get(numberSpoken);
            //System.out.printf("turn=%d, number=%d, spokenAtTurn=%d%n", prevTurn, numberSpoken, spokenAtTurn);

            int nextNumber;
            if (spokenAtTurn == null || spokenAtTurn == prevTurn) {
                nextNumber = 0;
            } else {
                nextNumber = prevTurn - spokenAtTurn;
            }

            lastMentioned.put(numberSpoken, prevTurn);
            if (prevTurn == targetTurn) {
                break;
            }

            numberSpoken = nextNumber;
            prevTurn++;
        }

        System.out.println("Part 1: " + numberSpoken);
    }
}

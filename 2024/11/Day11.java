import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2024/day/11
 */
public class Day11 {

    public static void main(String... args) throws Exception {
        LinkedList<Long> stones = getInput();
        part1(stones);
    }

    private static void part1(LinkedList<Long> stones) {
        LinkedList<Long> currentStones = new LinkedList<>(stones);
        LinkedList<Long> nextStones = new LinkedList<>();
        for (int n = 0; n < 25; n++) {
            while (!currentStones.isEmpty()) {
                Long stone = currentStones.poll();
                if (stone == 0) {
                    nextStones.add(stone + 1);
                    continue;
                }

                String s = Long.toString(stone);
                if (s.length() % 2 == 0) {
                    nextStones.add(Long.parseLong(s.substring(0, s.length() / 2)));
                    nextStones.add(Long.parseLong(s.substring(s.length() / 2, s.length())));
                } else {
                    nextStones.add(stone * 2024);
                }
            }

            LinkedList<Long> tmp = currentStones;
            currentStones = nextStones;
            nextStones = tmp;
        }

        System.out.println("Part 1: " + currentStones.size());
    }

    private static LinkedList<Long> getInput() throws Exception {
        String line = new String(Files.readAllBytes(Paths.get("input.txt")));

        LinkedList<Long> stones = new LinkedList<>();
        for (String part : line.trim().split("\\s+")) {
            stones.add(Long.parseLong(part));
        }

        return stones;
    }
}

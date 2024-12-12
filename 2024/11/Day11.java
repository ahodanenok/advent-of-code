import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2024/day/11
 */
public class Day11 {

    public static void main(String... args) throws Exception {
        LinkedList<Long> stones = getInput();
        part1(stones);
        part2(stones);
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

    private static void part2(LinkedList<Long> stones) {
        long count = 0;
        Map<CacheKey, Long> cache = new HashMap<>();
        for (long stone : stones) {
            count += countStones(stone, 0, cache);
        }

        System.out.println("Part 2: " + count);
    }

    private static long countStones(long stone, int n, Map<CacheKey, Long> cache) {
        if (n >= 75) {
            return 1;
        }

        CacheKey cacheKey = new CacheKey(stone, n);
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        if (stone == 0) {
            long count = countStones(stone + 1, n + 1, cache);
            cache.put(cacheKey, count);
            return count;
        }

        String s = Long.toString(stone);
        if (s.length() % 2 == 0) {
            long count = 0;
            count += countStones(
                Long.parseLong(s.substring(0, s.length() / 2)), n + 1, cache);
            count += countStones(
                Long.parseLong(s.substring(s.length() / 2, s.length())), n + 1, cache);
            cache.put(cacheKey, count);
            return count;
        } else {
            long count = countStones(stone * 2024, n + 1, cache);
            cache.put(cacheKey, count);
            return count;
        }
    }

    private static LinkedList<Long> getInput() throws Exception {
        String line = new String(Files.readAllBytes(Paths.get("input.txt")));

        LinkedList<Long> stones = new LinkedList<>();
        for (String part : line.trim().split("\\s+")) {
            stones.add(Long.parseLong(part));
        }

        return stones;
    }

    private static class CacheKey {

        final long stone;
        final int step;

        CacheKey(long stone, int step) {
            this.stone = stone;
            this.step = step;
        }

        @Override
        public boolean equals(Object obj) {
            CacheKey other = (CacheKey) obj;
            return stone == other.stone && step == other.step;
        }

        @Override
        public int hashCode() {
            return 31 * (int) stone + step;
        }
    }
}

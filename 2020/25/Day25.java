/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2020/day/25
 */
public class Day25 {

    public static void main(String[] args) {
        long doorKey = 11239946;
        long cardKey = 10464955;
        System.out.println("Part 1: " + getEncryptionKey(doorKey, determineLoopSize(cardKey)));
    }

    private static int determineLoopSize(long publicKey) {
        int loopSize = 0;
        long value = 1;
        while (value != publicKey) {
            value = (value * 7) % 20201227;
            loopSize++;
        }

        return loopSize;
    }

    private static long getEncryptionKey(long publicKey, int loopSize) {
        long key = 1;
        for (int i = 0; i < loopSize; i++) {
            key = (key * publicKey) % 20201227;
        }

        return key;
    }
}

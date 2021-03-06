import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigInteger;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2019/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        List<Shuffle> shuffles = getInput();

        part1(shuffles);
        part2(shuffles);
    }

    private static void part1(List<Shuffle> shuffles) {
        long pos = shuffle(shuffles, 10007, 2019);
        System.out.println("Part 1: " + pos);
    }

    // Thanks to: 
    // https://www.reddit.com/r/adventofcode/comments/ee0rqi/2019_day_22_solutions/fbnifwk/
    private static void part2(List<Shuffle> shuffles) {
        BigInteger n = BigInteger.valueOf(101741582076661L);
        BigInteger deckSize = BigInteger.valueOf(119315717514047L);
        BigInteger x = BigInteger.valueOf(2020);
        BigInteger y = BigInteger.valueOf(shuffle(shuffles, deckSize.longValue(), x.longValue())); 
        BigInteger z = BigInteger.valueOf(shuffle(shuffles, deckSize.longValue(), y.longValue()));
        BigInteger a = y.subtract(z).multiply(x.subtract(y).modInverse(deckSize)).mod(deckSize); 
        BigInteger b = y.subtract(a.multiply(x)).mod(deckSize);


        // f^n(x) = A^n * x + (A^n - 1) * modinv(A - 1, D) * B
        // inverse: (x - (A^n - 1) * modinv(A - 1, D) * B) * modinv(A^n)
        BigInteger an = a.modPow(n, deckSize);
        BigInteger pos = x.subtract(
               an.subtract(BigInteger.ONE)
                   .multiply(a.subtract(BigInteger.ONE).modInverse(deckSize))
                   .multiply(b)).multiply(an.modInverse(deckSize)).mod(deckSize);

        System.out.println("Part 2: " + pos);
    }

    private static long shuffle(List<Shuffle> shuffles, long deckSize, long trackPos) {
        long pos = trackPos;
        for (Shuffle sh : shuffles) {
            pos = sh.apply(deckSize, pos);
        }

        return pos; 
    } 

    private static List<Shuffle> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Shuffle> shuffles = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if ("cut".equals(parts[0])) {
                    shuffles.add(new CutShuffle(Integer.parseInt(parts[1])));
                } else if ("deal".equals(parts[0]) && "with".equals(parts[1])) {
                    shuffles.add(new IncShuffle(Integer.parseInt(parts[3])));
                } else if ("deal".equals(parts[0]) && "into".equals(parts[1])) {
                    shuffles.add(new NewStackShuffle());
                } else {
                    throw new IllegalStateException();
                }
            } 

            return shuffles;
        }
    }

    private interface Shuffle {
        long apply(long deckSize, long pos); 
    }

    private static class CutShuffle implements Shuffle {

        private int count;

        CutShuffle(int count) {
            this.count = count;
        }

        @Override
        public long apply(long deckSize, long pos) {
            return (pos + deckSize - count) % deckSize;
        }
    } 

    private static class IncShuffle implements Shuffle {

        private int step;

        IncShuffle(int step) {
            this.step = step;
        }

        @Override
        public long apply(long deckSize, long pos) {
           return (pos * step) % deckSize;
        }
    } 

    private static class NewStackShuffle implements Shuffle {

        @Override
        public long apply(long deckSize, long pos) {
            return deckSize - pos - 1; 
        }
    } 
}


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2022/day/25
 */
public class Day25 {

    private static final BigInteger SNAFU_BASE = BigInteger.valueOf(5);
    private static final Map<Character, BigInteger> DIGIT_SNAFU_TO_DECIMAL = new HashMap<>();
    private static final char[] DIGIT_DECIMAL_TO_SNAFU = new char[] { '0', '1', '2', '=', '-' }; // integer overflow!

    static {
        DIGIT_SNAFU_TO_DECIMAL.put('=', BigInteger.valueOf(-2));
        DIGIT_SNAFU_TO_DECIMAL.put('-', BigInteger.valueOf(-1));
        DIGIT_SNAFU_TO_DECIMAL.put('0',  BigInteger.valueOf(0));
        DIGIT_SNAFU_TO_DECIMAL.put('1',  BigInteger.valueOf(1));
        DIGIT_SNAFU_TO_DECIMAL.put('2',  BigInteger.valueOf(2));
    }

    public static void main(String[] args) throws Exception {
        List<String> numbers = getNumbers();
        part1(numbers);
    }

    private static void part1(List<String> numbers) {
        BigInteger sum = BigInteger.ZERO;
        for (String n : numbers) {
            sum = sum.add(toDecimal(n));
        }

        System.out.println("Part 1: " + toSnafu(sum));
    }

    private static BigInteger toDecimal(String snafu) {
        BigInteger n = BigInteger.ZERO;
        BigInteger p = BigInteger.ONE;
        for (int i = snafu.length() - 1; i >= 0; i--) {
            n = n.add(DIGIT_SNAFU_TO_DECIMAL.get(snafu.charAt(i)).multiply(p));
            p = p.multiply(SNAFU_BASE);
        }

        return n;
    }

    private static String toSnafu(BigInteger decimal) {
        StringBuilder sb = new StringBuilder();
        BigInteger n = decimal;
        BigInteger m;
        char d;
        while (n.compareTo(BigInteger.ZERO) > 0) {
            m = n.mod(SNAFU_BASE);
            d = DIGIT_DECIMAL_TO_SNAFU[m.intValue()];
            n = n.subtract(DIGIT_SNAFU_TO_DECIMAL.get(d)).divide(SNAFU_BASE);
            sb.append(d);
        }

        return sb.reverse().toString();
    }

    private static List<String> getNumbers() throws Exception {
        List<String> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(line);
            }
        }

        return numbers;
    }
}

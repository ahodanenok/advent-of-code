import java.util.Arrays;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2019/day/4
 */
public class Day4 {

    public static void main(String[] args) {
        int from = 359282;
        int to = 820401;

        part1(from, to);    
    }

    private static void part1(int from, int to) {
        int count = 0;

        next:
        for (int p = from; p < to; p++) {
            String s = p + "";

            boolean hasAdjacentSame = false;
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i - 1) == s.charAt(i)) {
                    hasAdjacentSame = true;
                    break;
                } 
            }

            if (!hasAdjacentSame) continue;

            char[] chars = Arrays.copyOf(s.toCharArray(), s.length());
            Arrays.sort(chars); 
            if (!Arrays.equals(s.toCharArray(), chars)) {
                continue;
            } 

            count++;
        }

        System.out.println("Part 1: " + count);
    }
}


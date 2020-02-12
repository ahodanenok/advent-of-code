import java.util.Arrays;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2019/day/4
 */
public class Day4 {

    public static void main(String[] args) {
        int from = 359282;
        int to = 820401;

        int count_1 = 0;
        int count_2 = 0;

        for (int p = from; p < to; p++) {
            String s = p + "";

            char[] chars = Arrays.copyOf(s.toCharArray(), s.length());
            Arrays.sort(chars); 
            if (!Arrays.equals(s.toCharArray(), chars)) {
                continue;
            } 

            int groupSize = 1;
            boolean hasTwoDigitsGroup = false; 
            boolean hasAdjacentSame = false;
            for (int i = 1; i <= chars.length; i++) {
                if (i >= chars.length || chars[i - 1] != chars[i]) {
                    if (groupSize > 1) hasAdjacentSame = true;
                    if (groupSize == 2) hasTwoDigitsGroup = true;
                    groupSize = 1;
                } else {
                    groupSize++;
                }
            }


            if (hasAdjacentSame) count_1++;
            if (hasTwoDigitsGroup) count_2++;
        }

        System.out.println("Part 1: " + count_1);
        System.out.println("Part 2: " + count_2);
    }
}


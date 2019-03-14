import java.util.Scanner;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2017/day/1
 */
public class Day1 {

    public static void main(String[] args) {
        String seq = new Scanner(System.in).nextLine();
        System.out.println(sum(seq, 1));
        System.out.println(sum(seq, seq.length() / 2));
    }

    private static long sum(String seq, int offset) {
        int result = 0;
        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) == seq.charAt((i + offset) % seq.length())) {
                result += Integer.parseInt(Character.toString(seq.charAt(i)));
            }
        }

        return result;
    }
}

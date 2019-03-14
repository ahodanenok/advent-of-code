import java.util.Scanner;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2017/day/1
 */
public class Day1 {

    public static void main(String[] args) {
        String seq = new Scanner(System.in).nextLine();
        part1(seq);
    }

    private static void part1(String seq) {
        int result = 0;
        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) == seq.charAt((i + 1) % seq.length())) {
                result += Integer.parseInt(Character.toString(seq.charAt(i)));
            }
        }

        System.out.println(result);
    }
}

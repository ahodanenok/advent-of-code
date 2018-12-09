import java.util.Scanner;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2018/day/5
 */
public class Day5 {

    public static void main(String[] args) {
        String polymer = getPolymer();
        if (polymer != null) {
            part1(polymer);
            part2(polymer);
        }
    }

    private static void part1(String polymer) {
        StringBuilder sb = new StringBuilder(polymer);
        react(sb);
        System.out.println(sb.length());
    }

    private static void part2(String polymer) {
        int minLength = Integer.MAX_VALUE;
        for (char ch = 'a'; ch <= 'z'; ch = (char) (ch + 1)) {
            StringBuilder sb = new StringBuilder(polymer);
            for (int i = sb.length() - 1; i >= 0; i--) {
                if (sb.charAt(i) == ch || Character.toLowerCase(sb.charAt(i)) == ch) {
                    sb.deleteCharAt(i);
                }
            }

            react(sb);
            minLength = Math.min(sb.length(), minLength);
        }

        System.out.println(minLength);
    }

    private static void react(StringBuilder polymer) {
        int pos = 1;
        while (pos < polymer.length()) {
            char a = polymer.charAt(pos - 1);
            char b = polymer.charAt(pos);
            if (a != b && Character.toLowerCase(a) == Character.toLowerCase(b)) {
                polymer.delete(pos - 1, pos + 1);
                pos = 1;
            } else {
                pos++;
            }
        }
    }

    private static String getPolymer() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            return null;
        }
    }
}

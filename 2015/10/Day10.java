/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2015/day/10
 */
public class Day10 {

    public static void main(String[] args) {
        String input = "1113222113";
        part1(input);
        part2(input);

        //System.out.println(lookAndSay("1"));
        //System.out.println(lookAndSay("11"));
        //System.out.println(lookAndSay("21"));
        //System.out.println(lookAndSay("1211"));
        //System.out.println(lookAndSay("111221"));
    }

    private static void part1(String input) {
        String output = input;
        for (int n = 0; n < 40; n++) {
            output = lookAndSay(output);
        }

        System.out.println(output.length());
    }

    private static void part2(String input) {
        String output = input;
        for (int n = 0; n < 50; n++) {
            output = lookAndSay(output);
        }

        System.out.println(output.length());
    }

    private static String lookAndSay(String digits) {
        StringBuilder sb = new StringBuilder();

        int i = 0;
        while (i < digits.length()) {
            int n = 0;
            char prev = digits.charAt(i);
            while (i < digits.length() && digits.charAt(i) == prev) {
                n++;
                i++;
            }

            sb.append(n);
            sb.append(prev);
        }

        return sb.toString();
    }
}

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
        }
    }

    private static void part1(String polymer) {
        StringBuilder sb = new StringBuilder(polymer);

        int pos = 1;
        while (pos < sb.length()) {
            char a = sb.charAt(pos - 1);
            char b = sb.charAt(pos);
            if (a != b && Character.toLowerCase(a) == Character.toLowerCase(b)) {
                sb.delete(pos - 1, pos + 1);
                pos = 1;
            } else {
                pos++;
            }
        }

        System.out.println(sb.length());
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

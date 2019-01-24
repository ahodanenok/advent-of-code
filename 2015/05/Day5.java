import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2015/day/5
 */
public class Day5 {

    private static char[] VOWELS = new char[] { 'a', 'e', 'i', 'o', 'u' };
    private static String[] BLACKLIST_STRINGS = new String[] { "ab", "cd", "pq", "xy" };

    public static void main(String[] args) throws Exception {
        List<String> strings = getStrings();
        part1(strings);
        part2(strings);
    }

    private static void part1(List<String> strings) {
        int count = 0;
        for (String str : strings) {
            if (containsChars(str, VOWELS, 3)
                    && !containsStrings(str, BLACKLIST_STRINGS)
                    && hasTwoConsecutive(str)) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static void part2(List<String> strings) {
        int count = 0;
        for (String str : strings) {
            if (hasRepeatedLetterWithLetterBetween(str) && hasRepeatedPair(str)) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static boolean containsStrings(String str, String[] strings) {
        for (String searchedStr : strings) {
            if (str.contains(searchedStr)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsChars(String str, char[] chars, int count) {
        int foundCount = 0;
        for (int i = 0; i < str.length() && foundCount < count; i++) {
            char ch = str.charAt(i);
            for (int j = 0; j < chars.length; j++) {
                if (ch == chars[j]) {
                    foundCount++;
                    break;
                }
            }
        }

        return foundCount == count;
    }

    private static boolean hasTwoConsecutive(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasRepeatedPair(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (str.indexOf(str.substring(i - 1, i + 1), i + 1) > 0) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasRepeatedLetterWithLetterBetween(String str) {
        for (int i = 2; i < str.length(); i++) {
            if (str.charAt(i - 2) == str.charAt(i)) {
                return true;
            }
        }

        return false;
    }

    private static List<String> getStrings() {
        List<String> strings = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            strings.add(scanner.nextLine().trim());
        }

        return strings;
    }
}

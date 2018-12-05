import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2018/day/2
 */
public class Day2 {

    public static void main(String[] args) {
        List<String> ids = getIds();
        part1(ids);
        part2(ids);
    }

    private static void part1(List<String> ids) {
        int twoLettersCount = 0;
        int threeLettersCount = 0;
        for (String id : ids) {
            Map<Character, Integer> lettersFrequency = new HashMap<Character, Integer>();
            for (int i = 0; i < id.length(); i++) {
                char ch = id.charAt(i);
                if (lettersFrequency.containsKey(ch)) {
                    lettersFrequency.put(ch, lettersFrequency.get(ch) + 1);
                } else {
                    lettersFrequency.put(ch, 1);
                }
            }

            boolean hasTwoLetters = false;
            boolean hasThreeLetters = false;
            for (Map.Entry<Character, Integer> entry : lettersFrequency.entrySet()) {
                if (entry.getValue() == 3) {
                    hasThreeLetters = true;
                } else if (entry.getValue() == 2) {
                    hasTwoLetters = true;
                }
            }

            if (hasTwoLetters) {
                twoLettersCount++;
            }

            if (hasThreeLetters) {
                threeLettersCount++;
            }
        }

        int checksum = twoLettersCount * threeLettersCount;
        System.out.println(checksum);
    }

    private static void part2(List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                String commonLetters = diff(ids.get(i), ids.get(j));
                if (commonLetters != null) {
                    System.out.println(commonLetters);
                }
            }
        }
    }

    private static String diff(String a, String b) {
        int mismatchIdx = -1;
        for (int i = 0; i < a.length(); i++) {
            char ac = a.charAt(i);
            char bc = b.charAt(i);
            if (ac != bc && mismatchIdx == -1) {
                mismatchIdx = i;
            } else if (ac != bc) {
                return null;
            }
        }

        if (mismatchIdx != -1) {
            return a.substring(0, mismatchIdx) + a.substring(mismatchIdx + 1);
        } else {
            return null;
        }
    }

    private static List<String> getIds() {
        List<String> ids = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            ids.add(scanner.nextLine().trim());
        }

        return ids;
    }
}

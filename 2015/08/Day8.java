import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2015/day/8
 */
public class Day8 {

    public static void main(String[] args) {
        List<String> strings = getStrings();
        part1(strings);
        part2(strings);
    }

    private static void part1(List<String> strings) {
        int literalCharactersCount = 0;
        int memoryCharactersCount = 0;

        for (String str : strings) {
            int strLength = 0;

            int pos = 1;
            while (pos < str.length() - 1) {
                memoryCharactersCount++;

                char ch = str.charAt(pos);
                if (ch == '\\') {
                    char nextChar = str.charAt(pos + 1);
                    if (nextChar == '"' || nextChar == '\\') {
                        pos += 2;
                    } else if (nextChar == 'x') {
                        pos += 4;
                    } else {
                        throw new IllegalArgumentException("Unknown escape sequence: " + ch + nextChar);
                    }
                } else {
                    pos++;
                }
            }

            literalCharactersCount += str.length();
        }

        System.out.println(literalCharactersCount - memoryCharactersCount);
    }

    private static void part2(List<String> strings) {
        int literalCharactersCount = 0;
        int encodedCharactersCount = 0;

        for (String str : strings) {
            for (int pos = 0; pos < str.length(); pos++) {
                char ch = str.charAt(pos);
                if (ch == '"' || ch == '\\') {
                    encodedCharactersCount += 2;
                } else {
                    encodedCharactersCount++;
                }
            }

            encodedCharactersCount += 2;
            literalCharactersCount += str.length();
        }

        System.out.println(encodedCharactersCount - literalCharactersCount);
    }

    private static List<String> getStrings() {
        List<String> strings = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            strings.add(scanner.nextLine());
        }

        return strings;
    }
}

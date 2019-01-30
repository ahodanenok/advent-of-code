/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2015/day/11
 */
public class Day11 {

    public static void main(String[] args) {
        String input = "hepxcrrq";

        String password = input;
        // part 1
        do {
            password = increment(password);
        } while (!isValid(password));
        System.out.println(password);

        // part 2
        do {
            password = increment(password);
        } while (!isValid(password));
        System.out.println(password);

        //System.out.println("hijklmmn: " + isValid("hijklmmn"));
        //System.out.println("abbceffg: " + isValid("abbceffg"));
        //System.out.println("abbcegjk: " + isValid("abbcegjk"));
        //System.out.println("abcddgdd: " + isValid("abcddgdd"));
        //System.out.println("abcddgss: " + isValid("abcddgss"));
        //System.out.println("ddabckka: " + isValid("ddabckka"));
        //System.out.println("gwwaaabc: " + isValid("gwwaaabc"));

        //System.out.println("abcdefgh: " + increment("abcdefgh"));
        //System.out.println("abcdefgz: " + increment("abcdefgz"));
        //System.out.println("zzzzzzzz: " + increment("zzzzzzzz"));
    }

    private static boolean isValid(String password) {
        boolean straightFound = false;
        for (int i = 2; !straightFound && i < password.length(); i++) {
            int a = (int) password.charAt(i);
            int b = (int) password.charAt(i - 1);
            int c = (int) password.charAt(i - 2);
            if (a == b + 1 && a == c + 2) {
                straightFound = true;
            }
        }

        if (!straightFound) {
            return false;
        }

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch == 'i' || ch == 'o' || ch == 'l') {
                return false;
            }
        }

        char prevPair = '\0';
        boolean pairsFound = false;
        for (int i = 1; !pairsFound && i < password.length(); i++) {
            char a = password.charAt(i);
            char b = password.charAt(i - 1);
            if (a == b && prevPair == '\0') {
                prevPair = a;
            } else if (a == b && a != prevPair) {
                pairsFound = true;
            }
        }

        if (!pairsFound) {
            return false;
        }

        return true;
    }

    private static String increment(String password) {
        char[] chars = password.toCharArray();

        boolean carry = true;
        for (int i = chars.length - 1; carry && i >= 0; i--) {
            char ch = chars[i];
            ch = (char) (ch + 1);
            if (ch > 'z') {
                ch = 'a';
            } else {
                carry = false;
            }

            chars[i] = ch;
        }

        return String.valueOf(chars);
    }
}

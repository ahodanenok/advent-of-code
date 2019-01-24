import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2015/day/4
 */
public class Day4 {

    private static final MessageDigest MD;
    static {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }

        MD = md;
    }

    public static void main(String[] args) {
        String key = "iwrupvqb";
        System.out.println(mine(key, 5)); // part 1
        System.out.println(mine(key, 6)); // part 2
    }

    private static int mine(String key, int leadingZeroesCount) {
        int num = 1;
        while (!hasLeading(hash(key, num), '0', leadingZeroesCount)) {
            num++;
        }

        return num;
    }

    private static boolean hasLeading(String str, char ch, int count) {
        for (int i = 0; i < count && i < str.length(); i++) {
            if (str.charAt(i) != ch) {
                return false;
            }
        }

        return true;
    }

    private static String hash(String key, int num) {
        try {
            String s = new BigInteger(1, MD.digest((key + num).getBytes("UTF-8"))).toString(16);
            while (s.length() < 32) {
                s = "0" + s;
            }

            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
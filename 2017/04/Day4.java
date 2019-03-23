import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2017/day/4
 */
public class Day4 {

    public static void main(String[] args) {
        List<String> passwords = getPasswords();
        System.out.println(passphrasesCount(passwords, false));
        System.out.println(passphrasesCount(passwords, true));
    }

    private static List<String> getPasswords() {
        List<String> phrases = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            phrases.add(line);
        }

        return phrases;
    }

    private static int passphrasesCount(List<String> passwords, boolean withAnagrams) {
        int count = 0;
        for (int i = 0; i < passwords.size(); i++) {
            if (isPassphrase(passwords.get(i), withAnagrams)) {
                count++;
            }
        }

        return count;
    }

    private static boolean isPassphrase(String password, boolean withAnagrams) {
        String[] parts = password.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            for (int j = i + 1; j < parts.length; j++) {
                if (parts[i].equals(parts[j]) || withAnagrams && isAnagramEquals(parts[i], parts[j])) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isAnagramEquals(String a, String b) {
        char[] aa = a.toCharArray();
        char[] bb = b.toCharArray();

        Arrays.sort(aa);
        Arrays.sort(bb);

        return Arrays.equals(aa, bb);
    }
}

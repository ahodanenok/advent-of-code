import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2017/day/4
 */
public class Day4 {

    public static void main(String[] args) {
        List<String> passwords = getPasswords();
        part1(passwords);
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

    private static void part1(List<String> passwords) {
        int count = 0;
        for (int i = 0; i < passwords.size(); i++) {
            if (isPassphrase(passwords.get(i))) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static boolean isPassphrase(String password) {
        String[] parts = password.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            for (int j = i + 1; j < parts.length; j++) {
                if (parts[i].equals(parts[j])) {
                    return false;
                }
            }
        }

        return true;
    }
}

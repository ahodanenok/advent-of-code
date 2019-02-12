import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2016/day/7
 */
public class Day7 {

    public static void main(String[] args) throws Exception {
        List<IP7> addresses = getAddresses();
        part1(addresses);
        part2(addresses);
    }

    private static void part1(List<IP7> addresses) {
        int count = 0;
        for (IP7 ip7 : addresses) {
            if (ip7.hasABBA()) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static void part2(List<IP7> addresses) {
        int count = 0;
        for (IP7 ip7 : addresses) {
            if (ip7.hasSSL()) {
                count++;
            }
        }

        System.out.println(count);
    }

    private static List<IP7> getAddresses() {
        List<IP7> addresses = new ArrayList<IP7>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            addresses.add(new IP7(scanner.nextLine().trim()));
        }

        return addresses;
    }

    private static class IP7 {

        private final List<String> parts;
        private final List<String> sequences;

        IP7(String ip) {
            this.parts = new ArrayList<String>();
            this.sequences = new ArrayList<String>();

            for (String str : ip.split("\\[")) {
                int idx = str.indexOf(']');
                if (idx > -1) {
                    sequences.add(str.substring(0, idx));
                    if (idx < str.length() - 1) {
                        parts.add(str.substring(idx + 1));
                    }
                } else {
                    parts.add(str);
                }
            }

            //System.out.print(parts);
            //System.out.print("   ");
            //System.out.println(sequences);
        }

        boolean hasABBA() {
            for (String seq : sequences) {
                for (int i = 3; i < seq.length(); i++) {
                    char a = seq.charAt(i - 3);
                    char b = seq.charAt(i - 2);
                    char c = seq.charAt(i - 1);
                    char d = seq.charAt(i - 0);
                    if (isABBA(a, b, c, d)) {
                        return false;
                    }
                }
            }

            for (String part : parts) {
                for (int i = 3; i < part.length(); i++) {
                    char a = part.charAt(i - 3);
                    char b = part.charAt(i - 2);
                    char c = part.charAt(i - 1);
                    char d = part.charAt(i - 0);
                    if (isABBA(a, b, c, d)) {
                        return true;
                    }
                }
            }

            return false;
        }

        private boolean isABBA(char a, char b, char c, char d) {
            return a != b && a == d && b == c;
        }

        private boolean hasSSL() {
            return hasBAB(getABA());
        }

        private List<String> getABA() {
            List<String> aba = new ArrayList<String>();
            for (String part : parts) {
                for (int i = 2; i < part.length(); i++) {
                    char a = part.charAt(i - 2);
                    char b = part.charAt(i - 1);
                    char c = part.charAt(i);
                    if (a == c && a != b) {
                        aba.add("" + a + b + c);
                    }
                }
            }

            return aba;
        }

        private boolean hasBAB(List<String> aba) {
            if (aba.size() == 0) {
                return false;
            }

            for (String seq : sequences) {
                for (int i = 2; i < seq.length(); i++) {
                    char a = seq.charAt(i - 2);
                    char b = seq.charAt(i - 1);
                    char c = seq.charAt(i);
                    if (a == c && a != b && aba.contains("" + b + a + b)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}

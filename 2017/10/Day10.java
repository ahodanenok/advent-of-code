import java.util.Scanner;
import java.util.Arrays;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2017/day/10
 */
public class Day10 {

    public static void main(String[] args) {
        String input = getInput();
        part1(input);
        part2(input);
    }

    private static void part1(String str) {
        String[] parts = str.split(",");
        int[] lengths = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            lengths[i] = Integer.parseInt(parts[i]);
        }

        int[] list = new int[256];
        for (int i = 0; i < list.length; i++) {
            list[i] = i;
        }

        int pos = 0;
        int skip = 0;
        for (int i = 0; i < lengths.length; i++) {
            int length = lengths[i];
            //System.out.println(Arrays.toString(list) + ", pos=" + pos + ", skip=" + skip + ", length=" + length);

            // reverse
            int left = pos;
            int right = (pos + length - 1) % list.length;
            int toMove = length / 2;
            while (toMove > 0) {
                int tmp = list[left];
                list[left] = list[right];
                list[right] = tmp;

                left = (left + 1) % list.length;
                right = (list.length + right - 1) % list.length;
                toMove--;
            }

            pos = (pos + length + skip) % list.length;
            skip++;
        }

        System.out.println(list[0] * list[1]);
    }

    private static void part2(String str) {
        int[] list = new int[256];
        for (int i = 0; i < list.length; i++) {
            list[i] = i;
        }

        int[] lengths = new int[str.length() + 5];
        for (int i = 0; i < str.length(); i++) {
            lengths[i] = (int) str.charAt(i);
        }

        // standard length suffix
        lengths[lengths.length - 5] = 17;
        lengths[lengths.length - 4] = 31;
        lengths[lengths.length - 3] = 73;
        lengths[lengths.length - 2] = 47;
        lengths[lengths.length - 1] = 23;

        int pos = 0;
        int skip = 0;
        // calculating sparse hash
        for (int round = 0; round < 64; round++) {
            for (int i = 0; i < lengths.length; i++) {
                int length = lengths[i];
                //System.out.println(Arrays.toString(list) + ", pos=" + pos + ", skip=" + skip + ", length=" + length);

                // reverse
                int left = pos;
                int right = (pos + length - 1) % list.length;
                int toMove = length / 2;
                while (toMove > 0) {
                    int tmp = list[left];
                    list[left] = list[right];
                    list[right] = tmp;

                    left = (left + 1) % list.length;
                    right = (list.length + right - 1) % list.length;
                    toMove--;
                }

                pos = (pos + length + skip) % list.length;
                skip++;
            }
        }

        // calculating dense hash
        int blockLength = 16;
        int blockCount = list.length / blockLength;
        int[] denseHash = new int[blockCount];
        for (int block = 0; block < list.length / blockLength; block++) {
            int n = list[block * blockLength];
            for (int i = block * blockLength + 1; i < (block + 1) * blockLength; i++) {
                n = n ^ list[i];
            }

            denseHash[block] = n;
        }

        // hexadecimal hash
        String hash = "";
        for (int i = 0; i < denseHash.length; i++) {
            String hex = Integer.toString(denseHash[i], 16);
            if (hex.length() == 1) {
                hash += "0";
            }
            hash += hex;
        }

        System.out.println(hash);
    }

    private static String getInput() {
        return new Scanner(System.in).nextLine();
    }
}

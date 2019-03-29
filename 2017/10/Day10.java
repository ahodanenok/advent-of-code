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

    private static String getInput() {
        return new Scanner(System.in).nextLine();
    }
}

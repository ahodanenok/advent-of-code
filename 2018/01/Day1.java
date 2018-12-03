import java.util.Scanner;

/**
 * Advent of Code - Day 1
 * https://adventofcode.com/2018/day/1
 */
public class Day1 {

    public static void main(String[] args) {
        int frequence = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int change = Integer.parseInt(scanner.nextLine().trim());
            frequence += change;
        }

        System.out.println(frequence);
    }
}
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2015/day/12
 */
public class Day12 {

    public static void main(String[] args) {
        String json = new Scanner(System.in).nextLine();
        part1(json);
    }

    private static void part1(String json) {
        int sum = 0;

        Pattern p = Pattern.compile("-?\\d+");
        Matcher m = p.matcher(json);
        while (m.find()) {
            sum += Integer.parseInt(m.group());
        }

        System.out.println(sum);
    }
}
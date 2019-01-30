import java.util.Scanner;
import java.util.LinkedList;
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
        part2(json);
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

    private static void part2(String json) {
        Pattern p = Pattern.compile(":\"red\"");

        int i = 0;
        LinkedList<Integer> stack = new LinkedList<Integer>();
        while (i < json.length()) {
            char ch = json.charAt(i);
            if (ch == '{') {
                stack.push(i);
            } else if (ch == '}') {
                int start = stack.pop();
                Matcher m = p.matcher(json);
                m.region(start, i);
                if (m.find()) {
                    json = json.substring(0, start) + json.substring(i + 1);
                    i = start - 1;
                }
            }

            i++;
        }

        part1(json);
    }
}
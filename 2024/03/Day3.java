import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2024/day/3
 */
public class Day3 {

    public static void main(String... args) throws Exception {
        String memory = getInput();
        part1(memory);
        part2(memory);
    }

    private static void part1(String memory) {
        long result = 0;

        Pattern mul = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = mul.matcher(memory);
        while (matcher.find()) {
            result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
        }

        System.out.println("Part 1: " + result);
    }

    private static void part2(String memory) {
        long result = 0;

        boolean enabled = true;
        Pattern grammar = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)");
        Matcher matcher = grammar.matcher(memory);
        while (matcher.find()) {
            String op = matcher.group(0);
            if (op.equals("do()")) {
                enabled = true;
            } else if (op.equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
            }
        }

        System.out.println("Part 2: " + result);
    }

    private static String getInput() throws Exception {
        return new String(Files.readAllBytes(Paths.get("input.txt")));
    }
}

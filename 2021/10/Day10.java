import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2021/day/10
 */
public class Day10 {

    private static final Map<Character, Character> CLOSINGS;
    private static final Map<Character, Integer> SCORES;

    static {
        CLOSINGS = new HashMap<>();
        CLOSINGS.put('(', ')');
        CLOSINGS.put('[', ']');
        CLOSINGS.put('{', '}');
        CLOSINGS.put('<', '>');

        SCORES = new HashMap<>();
        SCORES.put(')', 3);
        SCORES.put(']', 57);
        SCORES.put('}', 1197);
        SCORES.put('>', 25137);
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = getInput();
        part1(lines);
    }

    private static List<String> getInput() throws Exception {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        return lines;
    }

    private static void part1(List<String> lines) {
        int score = 0;
        for (String line : lines) {
            LinkedList<Character> stack = new LinkedList<>();
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (CLOSINGS.containsKey(ch)) {
                    stack.push(ch);
                } else if (!stack.isEmpty()) {
                    char opening = stack.pop();
                    if (CLOSINGS.get(opening) != ch) {
                        score += SCORES.get(ch);
                        break;
                    }
                }
            }
        }

        System.out.println("Part 1: " + score);
    }
}

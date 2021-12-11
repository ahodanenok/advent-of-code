import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2021/day/10
 */
public class Day10 {

    private static final Map<Character, Character> CLOSINGS;

    static {
        CLOSINGS = new HashMap<>();
        CLOSINGS.put('(', ')');
        CLOSINGS.put('[', ']');
        CLOSINGS.put('{', '}');
        CLOSINGS.put('<', '>');
    }

    public static void main(String[] args) throws Exception {
        score(getInput());
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

    private static void score(List<String> lines) {
        int corruptedScore = 0;
        List<Long> incompletedScores = new ArrayList<>();

        nextLine:
        for (String line : lines) {
            LinkedList<Character> stack = new LinkedList<>();

            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);
                if (CLOSINGS.containsKey(ch)) {
                    stack.push(ch);
                } else if (!stack.isEmpty()) {
                    if (CLOSINGS.get(stack.pop()) != ch) {
                        if (ch == ')') {
                            corruptedScore += 3;
                        } else if (ch == ']') {
                            corruptedScore += 57;
                        } else if (ch == '}') {
                            corruptedScore += 1197;
                        } else if (ch == '>') {
                            corruptedScore += 25137;
                        } else {
                            throw new IllegalStateException("Unknown closing: " + ch);
                        }

                        continue nextLine;
                    }
                }
            }

            if (!stack.isEmpty()) {
                long score = 0;
                while (!stack.isEmpty()) {
                    score *= 5;

                    char closing = CLOSINGS.get(stack.pop());
                    if (closing == ')') {
                        score += 1;
                    } else if (closing == ']') {
                        score += 2;
                    } else if (closing == '}') {
                        score += 3;
                    } else if (closing == '>') {
                        score += 4;
                    } else {
                        throw new IllegalStateException("Unknown closing: " + closing);
                    }
                }

                incompletedScores.add(score);
            }
        }

        System.out.println("Part 1: " + corruptedScore);

        incompletedScores.sort(Comparator.naturalOrder());
        long incompletedScore = incompletedScores.get(incompletedScores.size() / 2);
        System.out.println("Part 2: " + incompletedScore);
    }
}

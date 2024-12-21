import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2024/day/19
 */
public class Day19 {

	public static void main(String... args) throws Exception {
        Input input = getInput();
        part1(input);
    }

    private static void part1(Input input) {
        int possibleCount = 0;
        for (String design : input.designs) {
            if (isPossible(design, input.patterns, 0)) {
                possibleCount++;
            }
        }

        System.out.println("Part 1: " + possibleCount);
    }

    private static boolean isPossible(String design, List<String> patterns, int pos) {
        if (pos == design.length()) {
            return true;
        }

        for (String pattern : patterns) {
            if (design.startsWith(pattern, pos)
                    && isPossible(design, patterns, pos + pattern.length())) {
                return true;
            }
        }

        return false;
    }

    private static Input getInput() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));
        List<String> patterns = Arrays.asList(lines.get(0).split(", "));
        List<String> designs = lines.subList(2, lines.size());

        return new Input(patterns, designs);
    }

    private static class Input {

        final List<String> patterns;
        final List<String> designs;

        Input(List<String> patterns, List<String> designs) {
            this.patterns = patterns;
            this.designs = designs;
        }
    }
}

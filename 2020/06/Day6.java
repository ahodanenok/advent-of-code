import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2020/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        List<Group> groups = getGroups();
        part1(groups);
        part2(groups);
    }

    private static void part1(List<Group> groups) {
        int sum = 0;
        for (Group g : groups) {
            Set<Character> uniqueAnswers = new HashSet<>();
            for (String line : g.answers) {
                for (int i = 0; i < line.length(); i++) {
                    uniqueAnswers.add(line.charAt(i));
                }
            }

            sum += uniqueAnswers.size();
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<Group> groups) {
        int sum = 0;
        for (Group g : groups) {
            Map<Character, Integer> answersCount = new HashMap<>();
            for (String line : g.answers) {
                for (int i = 0; i < line.length(); i++) {
                    answersCount.merge(line.charAt(i), 1, Integer::sum);
                }
            }

            for (Integer count : answersCount.values()) {
                if (count == g.answers.size()) {
                    sum++;
                }
            }
        }

        System.out.println("Part 2: " + sum);
    }

    private static List<Group> getGroups() throws Exception {
        List<Group> groups = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Group currentGroup = new Group();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    groups.add(currentGroup);
                    currentGroup = new Group();
                    continue;
                }

                currentGroup.answers.add(line);
            }

            groups.add(currentGroup);
        }

        return groups;
    }

    private static class Group {
        List<String> answers = new ArrayList<>();
    }
}

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Day6 {

    public static void main(String[] args) throws Exception {
        List<Group> groups = getGroups();

        int sum = 0;
        for (Group g : groups) {
            sum += g.yesAnswers.size();
        }

        System.out.println("Part 1: " + sum);
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

                for (int i = 0; i < line.length(); i++) {
                    currentGroup.yesAnswers.add(line.charAt(i));
                }
            }

            groups.add(currentGroup);
        }

        return groups;
    }

    private static class Group {
        Set<Character> yesAnswers = new HashSet<>();
    }
}

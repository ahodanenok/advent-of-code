import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2023/day/25
 */
public class Day25 {

    public static void main(String[] args) throws Exception {
        Map<String, List<String>> connections = getInput();
        Map<String, Integer> counts = new HashMap<>();
        for (String component : connections.keySet()) {
            for (Path p : findPaths(connections, component)) {
                for (int i = 1; i < p.steps.size() - 1; i++) {
                    counts.merge(p.steps.get(i), 1, Integer::sum);
                }
            }
        }

        List<Map.Entry<String, Integer>> countsDesc = new ArrayList<>(counts.entrySet());
        countsDesc.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        List<String> candidates = new ArrayList<>();
        // 3 edges to remove are between the 6 most visited vertices
        for (int i = 0; i < 6; i++) {
            candidates.add(countsDesc.get(i).getKey());
        }

        Map<String, String> pairs = new HashMap<>();
        for (int i = 0; i < candidates.size(); i++) {
            String a = candidates.get(i);
            for (int j = 0; j <candidates.size(); j++) {
                String b = candidates.get(j);
                if (connections.get(a).contains(b)) {
                    pairs.put(a, b);
                    break;
                }
            }
        }

        List<Map.Entry<String, String>> edges = new ArrayList<>(pairs.entrySet());
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                for (int k = j + 1; k < edges.size(); k++) {
                    connections = getInput();

                    connections.get(edges.get(i).getKey()).remove(edges.get(i).getValue());
                    connections.get(edges.get(i).getValue()).remove(edges.get(i).getKey());

                    connections.get(edges.get(j).getKey()).remove(edges.get(j).getValue());
                    connections.get(edges.get(j).getValue()).remove(edges.get(j).getKey());

                    connections.get(edges.get(k).getKey()).remove(edges.get(k).getValue());
                    connections.get(edges.get(k).getValue()).remove(edges.get(k).getKey());

                    Map<String, String> groups = group(connections);
                    Map<String, Integer> groupSizes = new HashMap<>();
                    for (String component : groups.keySet()) {
                        groupSizes.merge(getGroupName(groups, component), 1, Integer::sum);
                    }

                    if (groupSizes.size() == 2) {
                        System.out.println("Part 1: " + groupSizes.values().stream().reduce(1, (a, b) -> a * b));
                        return;
                    }
                }
            }
        }

        throw new IllegalStateException("No answer!");
    }

    private static List<Path> findPaths(Map<String, List<String>> connections, String componentFrom) {
        List<Path> paths = new ArrayList<>();

        Set<String> visited = new HashSet<>();
        visited.add(componentFrom);

        LinkedList<Path> queue = new LinkedList<>();
        queue.add(new Path(componentFrom, List.of(componentFrom)));
        while (!queue.isEmpty()) {
            Path path = queue.removeFirst();
            paths.add(path);
            for (String to : connections.get(path.component)) {
                if (visited.add(to)) {
                    List<String> steps = new ArrayList<>(path.steps);
                    steps.add(to);
                    queue.addLast(new Path(to, steps));
                }
            }
        }

        return paths;
    }

    private static Map<String, String> group(Map<String, List<String>> connections) {
        Map<String, String> groups = new HashMap<>();
        for (String component : connections.keySet()) {
            groups.put(component, component);
        }

        for (Map.Entry<String, List<String>> entry : connections.entrySet()) {
            String groupNameFrom = getGroupName(groups, entry.getKey());
            for (String componentTo : entry.getValue()) {
                String groupNameTo = getGroupName(groups, componentTo);
                if (!groupNameTo.equals(groupNameFrom)) {
                    groups.put(groupNameTo, groupNameFrom);
                }
            }
        }

        return groups;
    }

    private static String getGroupName(Map<String, String> groups, String component) {
        String groupName = component;
        while (!groups.get(groupName).equals(groupName)) {
            groupName = groups.get(groupName);
        }

        return groupName;
    }

    private static Map<String, List<String>> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Map<String, List<String>> connections = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(":");
                String componentFrom = parts[0].trim();
                for (String componentTo : parts[1].trim().split(" ")) {
                    connections.computeIfAbsent(componentFrom, __ -> new ArrayList<>()).add(componentTo.trim());
                    connections.computeIfAbsent(componentTo.trim(), __ -> new ArrayList<>()).add(componentFrom);
                }
            }

            return connections;
        }
    }

    private static class Path {

        final List<String> steps;
        final String component;

        Path(String component, List<String> steps) {
            this.component = component;
            this.steps = steps;
        }
    }
}

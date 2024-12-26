import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2024/day/23
 */
public class Day23 {

	public static void main(String... args) throws Exception {
        List<Link> links = getInput();
        part1(links);
        part2(links);
    }

    private static void part1(List<Link> links) {
        Map<String, List<String>> computerConnections = new HashMap<>();
        for (Link link : links) {
            computerConnections.computeIfAbsent(link.left, __ -> new ArrayList<>()).add(link.right);
            computerConnections.computeIfAbsent(link.right, __ -> new ArrayList<>()).add(link.left);
        }

        int count = 0;
        List<String> computers = new ArrayList<>(computerConnections.keySet());
        for (int i = 0; i < computers.size(); i++) {
            String a = computers.get(i);
            for (int j = i + 1; j < computers.size(); j++) {
                String b = computers.get(j);
                for (int k = j + 1; k < computers.size(); k++) {
                    String c = computers.get(k);
                    if (a.charAt(0) != 't' && b.charAt(0) != 't' && c.charAt(0) != 't') {
                        continue;
                    }

                    if (computerConnections.get(a).contains(b)
                            && computerConnections.get(b).contains(c)
                            && computerConnections.get(c).contains(a)) {
                        count++;
                    }
                }
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static void part2(List<Link> links) {
        Map<String, List<String>> computerConnections = new HashMap<>();
        for (Link link : links) {
            computerConnections.computeIfAbsent(link.left, __ -> new ArrayList<>()).add(link.right);
            computerConnections.computeIfAbsent(link.right, __ -> new ArrayList<>()).add(link.left);
        }

        Set<String> maxCluster = Set.of();
        List<String> computers = new ArrayList<>(computerConnections.keySet());
        for (String computer : computers) {
            Set<String> maxLocalCluster = Set.of();
            List<String> connections = computerConnections.get(computer);
            for (int i = 0; i < connections.size(); i++) {
                String computerA = connections.get(i);
                List<String> connectionsA = computerConnections.get(computerA);

                Set<String> cluster = new HashSet<>();
                cluster.add(computer);
                cluster.add(computerA);

                for (int j = i + 1; j < connections.size(); j++) {
                    String computerB = connections.get(j);
                    List<String> connectionsB = computerConnections.get(computerB);
                    if (cluster.stream().allMatch(connectionsB::contains)) {
                        cluster.add(computerB);
                    }
                }

                if (cluster.size() > maxLocalCluster.size()) {
                    maxLocalCluster = cluster;
                }
            }

            if (maxLocalCluster.size() > maxCluster.size()) {
                maxCluster = maxLocalCluster;
            }
        }

        String password = maxCluster.stream()
            .sorted((a, b) -> a.compareTo(b))
            .collect(Collectors.joining(","));

        System.out.println("Part 2: " + password);
    }

    private static List<Link> getInput() throws Exception {
        List<Link> links = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                links.add(new Link(parts[0], parts[1]));
            }
        }

        return links;
    }

    private static class Link {

        final String left;
        final String right;

        Link(String left, String right) {
            this.left = left;
            this.right = right;
        }
    }
}

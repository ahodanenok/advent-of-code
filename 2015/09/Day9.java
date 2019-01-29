import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2015/day/9
 */
public class Day9 {

    public static void main(String[] args) {
        List<Path> paths = getPaths();
        part1(paths);
    }

    private static void part1(List<Path> paths) {
        Set<String> places = new HashSet<String>();
        for (Path path : paths) {
            places.add(path.from);
            places.add(path.to);
        }

        LinkedList<Route> queue = new LinkedList<Route>();
        for (String place : places) {
            queue.offer(new Route(place, 0, 1, null));
        }

        int min = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            Route current = queue.poll();
            if (current.length == places.size()) {
                min = Math.min(current.distance, min);
                continue;
            }

            for (Path path : paths) {
                if (path.to.equals(current.location) && !current.includes(path.from)) {
                    queue.offer(new Route(path.from, current.distance + path.distance, current.length + 1, current));
                }

                if (path.from.equals(current.location) && !current.includes(path.to)) {
                    queue.offer(new Route(path.to, current.distance + path.distance, current.length + 1, current));
                }
            }
        }

        System.out.println(min);
    }

    private static List<Path> getPaths() {
        List<Path> paths = new ArrayList<Path>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            paths.add(new Path(parts[0], parts[2], Integer.parseInt(parts[4])));
        }

        return paths;
    }

    private static class Route {

        private final String location;
        private final int distance;
        private final int length;
        private final Route prev;

        Route(String location, int distance, int length, Route prev) {
            this.location = location;
            this.distance = distance;
            this.length = length;
            this.prev = prev;
        }

        boolean includes(String location) {
            if (this.location.equals(location)) {
                return true;
            } else if (prev == null) {
                return false;
            } else {
                return prev.includes(location);
            }
        }
    }

    private static class Path {

        private final String from;
        private final String to;
        private final int distance;

        Path(String from, String to, int distance) {
            this.from = from;
            this.to = to;
            this.distance = distance;
        }
    }
}
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2022/day/15
 */
public class Day15 {

    public static void main(String[] args) throws Exception {
        List<Link> links = getLinks();
        part1(links);
        part2(links);
    }

    private static void part1(List<Link> links) {
        int yTarget = 2_000_000;
        int coveredCount = getCoverage(links, yTarget).stream()
            .map(Range::length).reduce(0, Integer::sum);
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);
            if (link.yBeacon != yTarget) {
                continue;
            }

            boolean seen = false;
            for (int j = 0; j < i; j++) {
                Link prev = links.get(j);
                if (prev.yBeacon == yTarget && prev.xBeacon == link.xBeacon) {
                    seen = true;
                    break;
                }
            }

            if (!seen) {
                coveredCount--;
            }
        }

        System.out.println("Part 1: " + coveredCount);
    }

    private static void part2(List<Link> links) {
        for (int y = 0; y < 4_000_000; y++) {
            List<Range> coverage = getCoverage(links, y);
            // being lazy and hoping that the beacon is not at the edge :)

            // because there is a single possible position for the beacon
            // there will be at most one split on two disjoint regions
            if (coverage.size() == 2) {
                coverage.sort((a, b) -> Integer.compare(a.from, b.from));
                long frequency = (coverage.get(0).to + 1) * 4000000L + y;
                System.out.println("Part 2: " + frequency);
                break;
            }
        }
    }

    private static List<Range> getCoverage(List<Link> links, int yTarget) {
        List<Range> ranges = new ArrayList<>();
        for (Link link : links) {
            int distance = Math.abs(link.xSensor - link.xBeacon) + Math.abs(link.ySensor - link.yBeacon);

            int yDiff = Math.abs(link.ySensor - yTarget);
            if (yDiff > distance) {
                continue;
            }

            int xDiff = distance - yDiff;
            ranges.add(new Range(link.xSensor - xDiff, link.xSensor + xDiff));
        }

        List<Range> coverage = new ArrayList<>();
        while (!ranges.isEmpty()) {
            Range finalRange = ranges.remove(ranges.size() - 1);
            while (true) {
                int sizeBefore = ranges.size();
                for (int i = ranges.size() - 1; i >= 0; i--) {
                    Range range = ranges.get(i);
                    if (range.to >= finalRange.from && finalRange.to >= range.from) {
                        finalRange = new Range(
                            Math.min(range.from, finalRange.from),
                            Math.max(range.to, finalRange.to));
                        ranges.remove(i);
                    }
                }

                if (sizeBefore == ranges.size()) {
                    break;
                }
            }

            coverage.add(finalRange);
        }

        return coverage;
    }

    private static List<Link> getLinks() throws Exception {
        List<Link> links = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Pattern pattern = Pattern.compile(
                "^Sensor at x=(-?[0-9]+), y=(-?[0-9]+): closest beacon is at x=(-?[0-9]+), y=(-?[0-9]+)$");

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    throw new IllegalStateException("Input not matched: " + line);
                }

                links.add(new Link(
                    Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
            }
        }

        return links;
    }

    private static class Range {

        final int from;
        final int to;

        Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        int length() {
            return to - from + 1;
        }
    }

    private static class Link {

        final int xSensor;
        final int ySensor;

        final int xBeacon;
        final int yBeacon;

        Link(int xSensor, int ySensor, int xBeacon, int yBeacon) {
            this.xSensor = xSensor;
            this.ySensor = ySensor;
            this.xBeacon = xBeacon;
            this.yBeacon = yBeacon;
        }
    }
}

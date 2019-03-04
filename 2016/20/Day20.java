import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2016/day/20
 */
public class Day20 {

    private static final long MAX_IP_VALUE = ((long) 1 << 32) - 1;

    public static void main(String[] args) throws Exception {
        List<IpRange> blacklist = normalize(getBlacklist());
        part1(blacklist);
    }

    private static void part1(List<IpRange> blacklist) {
        long ip = 0;
        while (!isAllowed(ip, blacklist) && ip <= MAX_IP_VALUE) ip++;
        System.out.println(ip);
    }

    private static boolean isAllowed(long ip, List<IpRange> blacklist) {
        //if (ip % 10000 == 0) System.out.println(ip);

        for (IpRange range : blacklist) {
            if (range.from > ip) {
                break;
            }
        
            if (range.contains(ip)) {
                return false;
            }
        }

        return true;
    }

    private static List<IpRange> normalize(List<IpRange> blacklist) {
        List<IpRange> copy = new ArrayList<IpRange>(blacklist);
        Collections.sort(copy);

        List<IpRange> result = new ArrayList<IpRange>();
        int i = 0;
        while (i < copy.size()) {
            IpRange a = copy.get(i);

            int j = i + 1;
            while (j < copy.size()) {
                IpRange b = copy.get(j);
                if (a.to < b.from) {
                    break;
                }

                a = new IpRange(Math.min(a.from, b.from), Math.max(a.to, b.to));
                j++;
            }

            result.add(a);
            i = j;
        }

        return result;
    }

    private static List<IpRange> getBlacklist() {
        List<IpRange> blacklist = new ArrayList<IpRange>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("-");
            blacklist.add(new IpRange(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }

        return blacklist;
    }

    private static class IpRange implements Comparable<IpRange> {

        private final long from;
        private final long to;

        IpRange(long from, long to) {
            this.from = from;
            this.to = to;
        }

        boolean contains(long ip) {
            return ip >= from && ip <= to;
        }

        boolean intersects(IpRange other) {
            return other.from <= to;
        }

        @Override
        public int compareTo(IpRange other) {
            if (from < other.from) {
                return -1;
            } else if (from > other.from) {
                return 1;
            } else{
                if (to < other.to) {
                    return -1;
                } else if (to > other.to) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }

        @Override
        public String toString() {
            return from + "-" + to;
        }
    }
}

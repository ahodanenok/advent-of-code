import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2022/day/7
 */
public class Day7 {

    public static void main(String[] args) throws Exception {
        Dir root = recreateFilesystem(getLog());
        part1(root);
        part2(root);
    }

    private static void part1(Dir root) {
        List<Integer> sizes = new ArrayList<>();
        collectSizesInRange(root, 0, 100_000, sizes);
        System.out.println("Part 1: " + sizes.stream().reduce(0, Integer::sum));
    }

    private static void part2(Dir root) {
        List<Integer> sizes = new ArrayList<>();
        collectSizesInRange(root, 0, Integer.MAX_VALUE, sizes);
        sizes.sort(Comparator.reverseOrder());
        int emptySize = 70_000_000 - sizes.get(0); // root is the largest
        int needSize = 30_000_000 - emptySize;
        System.out.println("Part 2: " + sizes.stream().filter(s -> s >= needSize).min(Comparator.naturalOrder()).orElse(-1));
    }

    private static int collectSizesInRange(Dir current, int minSize, int maxSize, List<Integer> sizes) {
        int totalSize = current.size;
        for (Dir dir : current.subdirs.values()) {
            totalSize += collectSizesInRange(dir, minSize, maxSize, sizes);
        }

        if (totalSize >= minSize && totalSize <= maxSize) {
            sizes.add(totalSize);
        }

        return totalSize;
    }

    private static List<Object> getLog() throws Exception {
        List<Object> log = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            Ls ls = null;
            while ((line = reader.readLine()) != null) {
                if (ls != null && line.startsWith("$")) {
                    log.add(ls);
                    ls = null;
                } 

                if (ls != null) {
                    ls.list.add(line);
                } else if (line.equals("$ ls")) {
                    ls = new Ls();
                } else if (line.startsWith("$ cd")) {
                    log.add(new Cd(line.substring(5)));
                } else {
                    throw new IllegalStateException(line);
                }
            }

            if (ls != null) {
                log.add(ls);
            }
        }

        return log;
    }

    private static Dir recreateFilesystem(List<Object> log) {
        Dir root = new Dir();
        Dir current = root;
        for (Object item : log) {
            if (item instanceof Cd) {
                String target = ((Cd) item).target;
                if (target.equals("/")) {
                    current = root;
                } else if (target.equals("..")) {
                    current = current.parent;
                } else {
                    Dir dir = new Dir();
                    dir.parent = current;
                    current.subdirs.put(target, dir);
                    current = dir;
                }
            } else if (item instanceof Ls) {
                if (current.size != 0) {
                    continue; // already listed
                }

                for (String line : ((Ls) item).list) {
                    if (!line.startsWith("dir")) {
                        int size = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                        current.size += size;
                    }
                }
            }
        }

        return root;
    }

    private static class Dir {

        Dir parent;
        int size;
        final Map<String, Dir> subdirs = new HashMap<>();
    }

    private static class Cd {

        final String target;

        Cd(String target) {
            this.target = target;
        }
    }

    private static class Ls {

        final List<String> list = new ArrayList<>();
    }
}

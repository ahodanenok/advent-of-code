import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2023/day/9
 */
public class Day9 {

    public static void main(String[] args) throws Exception {
        List<List<Integer>> sequences = getInput();
        part1(sequences);
        part2(sequences);
    }

    private static void part1(List<List<Integer>> sequences) {
        long sum = 0;
        for (List<Integer> seq : sequences) {
            List<List<Integer>> history = getHistory(seq);

            int n = 0;
            for (int i = history.size() - 1; i > 0; i--) {
                n = n + history.get(i - 1).get(history.get(i - 1).size() - 1);
            }

            sum += n;
        }

        System.out.println("Part 1: " + sum);
    }

    private static void part2(List<List<Integer>> sequences) {
        long sum = 0;
        for (List<Integer> seq : sequences) {
            List<List<Integer>> history = getHistory(seq);

            int n = 0;
            for (int i = history.size() - 1; i > 0; i--) {
                n = history.get(i - 1).get(0) - n;
            }

            sum += n;
        }

        System.out.println("Part 2: " + sum);
    }

    private static List<List<Integer>> getHistory(List<Integer> seq) {
        List<List<Integer>> history = new ArrayList<>();
        history.add(seq);

        List<Integer> currSeq = seq;
        while (!currSeq.stream().allMatch(n -> n == 0)) {
            List<Integer> nextSeq = new ArrayList<>();
            for (int i = 0; i < currSeq.size() - 1; i++) {
                nextSeq.add(currSeq.get(i + 1) - currSeq.get(i));
            }

            history.add(nextSeq);
            currSeq = nextSeq;
        }

        return history;
    }

    private static List<List<Integer>> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<List<Integer>> sequences = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                List<Integer> seq = new ArrayList<>();
                for (String n : line.trim().split("\\s+")) {
                    seq.add(Integer.parseInt(n.trim()));
                }

                sequences.add(seq);
            }

            return sequences;
        }
    }
}

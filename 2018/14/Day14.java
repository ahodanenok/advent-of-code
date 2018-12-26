import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2018/day/14
 */
public class Day14 {

    public static void main(String[] args) {
        part1(190221);
        part2(190221);
    }

    private static void part1(int n) {
        ScoreBoard sb = new ScoreBoard();
        for (int i = 0; i < n; i++) sb.next();
        for (int i = 0; i < 10; i++) {
            System.out.print(sb.next());
        }
        System.out.println();
    }

    private static void part2(int n) {
        ScoreBoard sb = new ScoreBoard();
        List<Integer> targetDigits = new ArrayList<Integer>();
        for (char ch : Integer.toString(n).toCharArray()) {
            targetDigits.add(Integer.parseInt(ch + ""));
        }

        List<Integer> currentDigits = new ArrayList<Integer>();

        int count = 0;
        while (!targetDigits.equals(currentDigits)) {
            currentDigits.add(sb.next());
            if (currentDigits.size() > targetDigits.size()) {
                currentDigits.remove(0);
            }

            count++;
        }

        System.out.println(count - targetDigits.size());
    }

    private static class ScoreBoard {

        private int firstElfPos;
        private int secondElfPos;
        private int nextScorePos;
        private List<Integer> scores;

        ScoreBoard() {
            this.firstElfPos = 0;
            this.secondElfPos = 1;
            this.nextScorePos = 0;
            this.scores = new ArrayList<Integer>();
            this.scores.add(3);
            this.scores.add(7);
        }

        int next() {
            if (nextScorePos >= scores.size()) {
                generateScores();
            }

            return scores.get(nextScorePos++);
        }

        private void generateScores() {
            int sum = scores.get(firstElfPos) + scores.get(secondElfPos);
            if (sum < 10) {
                scores.add(sum);
            } else {
                scores.add(sum / 10);
                scores.add(sum % 10);
            }

            firstElfPos = (firstElfPos + scores.get(firstElfPos) + 1) % scores.size();
            secondElfPos = (secondElfPos + scores.get(secondElfPos) + 1) % scores.size();
        }
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2023/day/4
 */
public class Day4 {

    public static void main(String[] args) throws Exception {
        List<Card> cards = getInput();
        part1(cards);
        part2(cards);
    }

    public static void part1(List<Card> cards) {
        int pointsTotal = 0;
        for (Card card : cards) {
            int shift = -1;
            for (Integer n : card.selectedNumbers) {
                if (card.winningNumbers.contains(n)) {
                    shift++;
                }
            }

            if (shift != -1) {
                pointsTotal += (1 << shift);
            }
        }

        System.out.println("Part 1: " + pointsTotal);
    }

    private static void part2(List<Card> cards) {
        int[] cardsCount = new int[cards.size()];
        Arrays.fill(cardsCount, 1);
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);

            int matchedCount = 0;
            for (Integer n : card.selectedNumbers) {
                if (card.winningNumbers.contains(n)) {
                    matchedCount++;
                }
            }

            if (matchedCount == 0) {
                continue;
            }

            for (int j = 1; j <= matchedCount; j++) {
                cardsCount[i + j] += cardsCount[i];
            }
        }

        int cardsTotal = 0;
        for (int count : cardsCount) {
            cardsTotal += count;
        }

        System.out.println("Part 2: " + cardsTotal);
    }

    private static List<Card> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Card> cards = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                Card card = new Card();

                String[] parts = line.trim().split(":");

                card.id = Integer.parseInt(parts[0].trim().split("\\s+")[1].trim());

                String[] numbers = parts[1].trim().split("\\|");
                for (String n : numbers[0].trim().split("\\s+")) {
                    card.winningNumbers.add(Integer.parseInt(n.trim()));
                }
                for (String n : numbers[1].trim().split("\\s+")) {
                    card.selectedNumbers.add(Integer.parseInt(n.trim()));
                }

                cards.add(card);
            }

            return cards;
        }
    }

    private static class Card {

        int id;
        List<Integer> winningNumbers = new ArrayList<>();
        List<Integer> selectedNumbers = new ArrayList<>();
    }
}

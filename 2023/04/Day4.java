import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2023/day/4
 */
public class Day4 {

    public static void main(String[] args) throws Exception {
        List<Card> cards = getInput();
        part1(cards);
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

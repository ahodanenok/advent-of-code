import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2020/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        Decks decks = getDecks();

        LinkedList<Integer> santa = new LinkedList<>(decks.santa);
        LinkedList<Integer> crab = new LinkedList<>(decks.crab);
        while (!santa.isEmpty() && !crab.isEmpty()) {
            Integer santaCard = santa.removeFirst();
            Integer crabCard = crab.removeFirst();
            if (santaCard > crabCard) {
                santa.addLast(santaCard);
                santa.addLast(crabCard);
            } else {
                crab.addLast(crabCard);
                crab.addLast(santaCard);
            }
        }

        LinkedList<Integer> winner = !santa.isEmpty() ? santa : crab;
        int score = 0;
        int multiplier = winner.size();
        for (Integer card : winner) {
            score += card * multiplier;
            multiplier--;
        }

        System.out.println("Part 1: " + score);
    }

    private static Decks getDecks() throws Exception {
        Decks decks = new Decks();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            readCards(reader, decks.santa);
            readCards(reader, decks.crab);
        }

        return decks;
    }

    private static void readCards(BufferedReader reader, List<Integer> cards) throws Exception {
        reader.readLine(); // skip Player definition

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            cards.add(Integer.parseInt(line));
        }
    }

    private static class Decks {
        List<Integer> santa = new ArrayList<>();
        List<Integer> crab = new ArrayList<>();
    }
}

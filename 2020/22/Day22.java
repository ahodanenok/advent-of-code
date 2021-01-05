import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2020/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        Decks decks = getDecks();
        System.out.println("Part 1: " + playCombat(decks).score());
        System.out.println("Part 2: " + playRecursiveCombat(decks).score());
    }

    private static GameResult playCombat(Decks decks) {
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

        if (!santa.isEmpty()) {
            return new GameResult(true, santa);
        } else {
            return new GameResult(false, crab);
        }
    }

    private static GameResult playRecursiveCombat(Decks decks) {
        Set<Decks> seen = new HashSet<>();
        LinkedList<Integer> santa = new LinkedList<>(decks.santa);
        LinkedList<Integer> crab = new LinkedList<>(decks.crab);
        while (!santa.isEmpty() && !crab.isEmpty()) {
            if (!seen.add(new Decks(santa, crab))) {
                return new GameResult(true, santa);
            }

            Integer santaCard = santa.removeFirst();
            Integer crabCard = crab.removeFirst();

            boolean santaWon;
            if (santa.size() >= santaCard && crab.size() >= crabCard) {
                santaWon = playRecursiveCombat(new Decks(
                        santa.subList(0, santaCard), crab.subList(0, crabCard))).santaWon;
            } else {
                santaWon = santaCard > crabCard;
            }

            if (santaWon) {
                santa.addLast(santaCard);
                santa.addLast(crabCard);
            } else {
                crab.addLast(crabCard);
                crab.addLast(santaCard);
            }
        }

        if (!santa.isEmpty()) {
            return new GameResult(true, santa);
        } else {
            return new GameResult(false, crab);
        }
    }

    private static class GameResult {

        boolean santaWon;
        List<Integer> deck;

        GameResult(boolean santaWon, List<Integer> deck) {
            this.santaWon = santaWon;
            this.deck = deck;
        }

        int score() {
            int score = 0;
            int multiplier = deck.size();
            for (Integer card : deck) {
                score += card * multiplier;
                multiplier--;
            }

            return score;
        }
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
        List<Integer> santa;
        List<Integer> crab;

        Decks() {
            this.santa = new ArrayList<>();
            this.crab = new ArrayList<>();
        }

        Decks(List<Integer> santa, List<Integer> crab) {
            this.santa = new ArrayList<>(santa);
            this.crab = new ArrayList<>(crab);
        }

        @Override
        public int hashCode() {
            return 31 * santa.hashCode() + crab.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Decks other = (Decks) obj;
            return other.santa.equals(santa) && other.crab.equals(crab);
        }
        
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2023/day/7
 */
public class Day7 {

    private static final int FIVE_KIND_SCORE  = 100;
    private static final int FOUR_KIND_SCORE  = 90;
    private static final int FULL_HOUSE_SCORE = 80;
    private static final int THREE_KIND_SCORE = 70;
    private static final int TWO_PAIR_SCORE   = 60;
    private static final int ONE_PAIR_SCORE   = 50;
    private static final int HIGH_CARD_SCORE  = 1;

    public static void main(String[] args) throws Exception {
        List<Bid> bids = getInput();
        part1(bids);
        part2(bids);
    }

    private static void part1(List<Bid> bids) {
        System.out.println("Part 1: " + calculateWinnings(
            new ArrayList<>(bids),
            List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'), 
            null));
    }

    private static void part2(List<Bid> bids) {
        System.out.println("Part 2: " + calculateWinnings(
            new ArrayList<>(bids),
            List.of('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'),
            'J'));
    }

    private static long calculateWinnings(List<Bid> bids, List<Character> cardStrength, Character joker) {
        bids.sort((a, b) -> {
            int n = Integer.compare(
                getHandStrength(a.hand, joker),
                getHandStrength(b.hand, joker));
            if (n != 0) {
                return n;
            }

            for (int i = 0; i < a.hand.length(); i++) {
                n = Integer.compare(
                    cardStrength.indexOf(a.hand.charAt(i)),
                    cardStrength.indexOf(b.hand.charAt(i)));
                if (n != 0) {
                    return n;
                }
            }

            throw new IllegalStateException("Equivalent!");
        });

        long totalWinnings = 0;
        for (int i = 0; i < bids.size(); i++) {
            totalWinnings += (i + 1) * bids.get(i).amount;
        }

        return totalWinnings;
    }

    private static int getHandStrength(String hand, Character joker) {
        int jokerCount = 0;
        Map<Character, Integer> cardCount = new HashMap<>();
        for (int i = 0; i < hand.length(); i++) {
            if (joker != null && hand.charAt(i) == joker) {
                jokerCount++;
            } else {
                cardCount.merge(hand.charAt(i), 1, Integer::sum);
            }
        }

        List<Integer> fingerprint = new ArrayList<>(cardCount.values());
        fingerprint.sort(java.util.Comparator.naturalOrder());
        if (fingerprint.isEmpty()) {
            fingerprint.add(jokerCount);
        } else if (jokerCount > 0) {
            fingerprint.set(fingerprint.size() - 1, fingerprint.get(fingerprint.size() - 1) + jokerCount);
        }

        if (fingerprint.size() == 1) {
            return FIVE_KIND_SCORE;
        }

        if (fingerprint.size() == 2) {
            if (fingerprint.get(0) == 1) {
                return FOUR_KIND_SCORE;
            } else {
                return FULL_HOUSE_SCORE;
            }
        }

        if (fingerprint.size() == 3) {
            if (fingerprint.get(fingerprint.size() - 1) == 3) {
                return THREE_KIND_SCORE;
            } else {
                return TWO_PAIR_SCORE;
            }
        }

        if (fingerprint.size() == 4) {
            return ONE_PAIR_SCORE;
        }

        return HIGH_CARD_SCORE;
    }

    private static List<Bid> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Bid> bids = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s++");
                bids.add(new Bid(parts[0].trim(), Long.parseLong(parts[1].trim())));
            }

            return bids;
        }
    }

    private static class Bid {

        final String hand;
        final long amount;

        Bid(String hand, long amount) {
            this.hand = hand;
            this.amount = amount;
        }
    }
}

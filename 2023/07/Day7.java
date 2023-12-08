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

    private static final List<Character> CARD_STRENGTH = List.of(
        '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'
    );

    public static void main(String[] args) throws Exception {
        List<Bid> bids = getInput();
        part1(bids);
    }

    private static void part1(List<Bid> bids) {
        bids.sort((a, b) -> {
            int n = Integer.compare(
                getHandStrength(a.hand),
                getHandStrength(b.hand));
            if (n != 0) {
                return n;
            }

            for (int i = 0; i < a.hand.length(); i++) {
                n = Integer.compare(
                    CARD_STRENGTH.indexOf(a.hand.charAt(i)),
                    CARD_STRENGTH.indexOf(b.hand.charAt(i)));
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

        System.out.println("Part 1: " + totalWinnings);
    }

    private static int getHandStrength(String hand) {
        Map<Character, Integer> cardCount = new HashMap<>();
        for (int i = 0; i < hand.length(); i++) {
            cardCount.merge(hand.charAt(i), 1, Integer::sum);
        }

        if (cardCount.size() == 1) {
            // five of a kind
            return 100;
        }

        if (cardCount.size() == 2) {
            int n = cardCount.values().iterator().next();
            if (n == 4 || n == 1) {
                // four of a kind
                return 90;
            } else {
                // full house
                return 80;
            }
        }

        if (cardCount.size() == 3) {
            if (cardCount.values().contains(3)) {
                // three of a kind
                return 70;
            } else {
                // two pair
                return 60;
            }
        }

        if (cardCount.size() == 4) {
            // one pair
            return 50;
        }

        // high card
        return 1;
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

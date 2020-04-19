import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2019/day/22
 */
public class Day22 {

    private static final int DECK_SIZE = 10007;

    public static void main(String[] args) throws Exception {
        List<Shuffle> shuffles = getInput();

        short[] deck = new short[DECK_SIZE];
        for (short i = 0; i < deck.length; i++) deck[i] = i;

        shuffle(deck, shuffles);
        //System.out.println(Arrays.toString(deck));

        part1(deck);
    }

    private static void part1(short[] deck) {
        int pos = -1;
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] == 2019) {
                pos = i;
                break; 
            }
        }

        System.out.println("Part 1: " + pos);
    }

    private static void shuffle(short[] deck, List<Shuffle> shuffles) {
        for (Shuffle sh : shuffles) {
            sh.apply(deck);
        }  
    } 

    private static List<Shuffle> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            List<Shuffle> shuffles = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if ("cut".equals(parts[0])) {
                    shuffles.add(new CutShuffle(Integer.parseInt(parts[1])));
                } else if ("deal".equals(parts[0]) && "with".equals(parts[1])) {
                    shuffles.add(new IncShuffle(Integer.parseInt(parts[3])));
                } else if ("deal".equals(parts[0]) && "into".equals(parts[1])) {
                    shuffles.add(new NewStackShuffle());
                } else {
                    throw new IllegalStateException();
                }
            } 

            return shuffles;
        }
    }

    private interface Shuffle {
        void apply(short[] deck);
    }

    private static class CutShuffle implements Shuffle {

        private int count;

        CutShuffle(int count) {
            this.count = count;
        }

        @Override
        public void apply(short[] deck) {
            if (count < 0) {
                short[] cut = Arrays.copyOfRange(deck, deck.length + count, deck.length);
                System.arraycopy(deck, 0, deck, -count, deck.length + count); 
                System.arraycopy(cut, 0, deck, 0, -count);
            } else {
                short[] cut = Arrays.copyOf(deck, count);
                System.arraycopy(deck, count, deck, 0, deck.length - count);
                System.arraycopy(cut, 0, deck, deck.length - count, count);
            }
        }
    } 

    private static class IncShuffle implements Shuffle {

        private int step;

        IncShuffle(int step) {
            this.step = step;
        }

        @Override
        public void apply(short[] deck) {
            short[] stack = Arrays.copyOf(deck, deck.length);
            Arrays.fill(deck, (short) -1);

            int j = 0;
            for (int i = 0; i < stack.length; i++) {
                while (deck[j] != -1) j = (j + step) % deck.length;    
                deck[j] = stack[i];
            }
        }
    } 

    private static class NewStackShuffle implements Shuffle {

        @Override
        public void apply(short[] deck) {
            short[] stack = Arrays.copyOf(deck, deck.length); 
            for (int i = 0; i < deck.length; i++) {
                deck[i] = stack[deck.length - i - 1];
            }
        }
    } 
}


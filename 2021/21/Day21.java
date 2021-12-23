import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2021/day/21
 */
public class Day21 {

    public static void main(String[] args) {
        part1(10, 4);
        part2(10, 4);
    }

    private static void part1(int pos1, int pos2) {
        int targetScore = 1000;
        int dice = 1;
        int diceRolls = 0;

        Player currentPlayer = new Player(pos1);
        Player otherPlayer = new Player(pos2);
        while (currentPlayer.score < targetScore && otherPlayer.score < targetScore) {
            int rolled = 0;
            for (int i = 0; i < 3; i++) {
                rolled += dice;
                dice = dice % 100 + 1;
            }

            diceRolls += 3;

            currentPlayer.makeTurn(rolled);

            Player tmp = currentPlayer;
            currentPlayer = otherPlayer;
            otherPlayer = tmp;
        }

        int lostScore;
        if (currentPlayer.score < targetScore) {
            lostScore = currentPlayer.score;
        } else if(otherPlayer.score < targetScore) {
            lostScore = otherPlayer.score;
        } else {
            throw new IllegalStateException("Nobody lost!");
        }

        System.out.println("Part 1: " + (lostScore * diceRolls));
    }

    private static void part2(int pos1, int pos2) {
        int targetScore = 21;
        Player p1 = new Player(pos1);
        Player p2 = new Player(pos2);

        Universes result = quantumGame(p1, p2, true, 21, new HashMap<>());
        System.out.println("Part 2: " + result.p1Wins.max(result.p2Wins));
    }

    private static Universes quantumGame(Player p1, Player p2, boolean p1Turn, int targetScore, Map<GameState, Universes> memo) {
        if (p1.score >= targetScore) {
            return new Universes(BigInteger.ONE, BigInteger.ZERO);
        }

        if (p2.score >= targetScore) {
            return new Universes(BigInteger.ZERO, BigInteger.ONE);
        }

        GameState state = new GameState(p1, p2, p1Turn);
        if (memo.containsKey(state)) {
            return memo.get(state);
        }

        BigInteger p1Wins = BigInteger.ZERO;
        BigInteger p2Wins = BigInteger.ZERO;
        for (int r1 = 1; r1 < 4; r1++) {
            for (int r2 = 1; r2 < 4; r2++) {
                for (int r3 = 1; r3 < 4; r3++) {
                    int rolled = r1 + r2 + r3;

                    Universes result;
                    if (p1Turn) {
                        Player p = p1.copy();
                        p.makeTurn(rolled);
                        result = quantumGame(p, p2, false, targetScore, memo);
                    } else {
                        Player p = p2.copy();
                        p.makeTurn(rolled);
                        result = quantumGame(p1, p, true, targetScore, memo);
                    }

                    p1Wins = p1Wins.add(result.p1Wins);
                    p2Wins = p2Wins.add(result.p2Wins);
                }
            }
        }

        Universes result = new Universes(p1Wins, p2Wins);
        memo.put(state, result);

        return result;
    }

    private static class Player {

        int score;
        int pos;

        Player(int pos) {
            this.pos = pos;
        }

        void makeTurn(int rolled) {
            pos = (pos + rolled - 1) % 10 + 1;
            score += pos;
        }

        Player copy() {
            Player p = new Player(pos);
            p.score = score;

            return p;
        }

        @Override
        public int hashCode() {
            return 31 * score + pos;
        }

        @Override
        public boolean equals(Object obj) {
            Player other = (Player) obj;
            return score == other.score && pos == other.pos;
        }
    }

    private static class Universes {

        BigInteger p1Wins;
        BigInteger p2Wins;

        Universes(BigInteger p1Wins, BigInteger p2Wins) {
            this.p1Wins = p1Wins;
            this.p2Wins = p2Wins;
        }
    }

    private static class GameState {

        Player p1;
        Player p2;
        boolean p1Turn;

        GameState(Player p1, Player p2, boolean p1Turn) {
            this.p1 = p1.copy();
            this.p2 = p2.copy();
            this.p1Turn = p1Turn;
        }

        @Override
        public int hashCode() {
            return 31 * 31 * p1.hashCode() + 31 * p2.hashCode() + (p1Turn ? 1 : 0);
        }

        @Override
        public boolean equals(Object obj) {
            GameState other = (GameState) obj;
            return p1.equals(other.p1) && p2.equals(other.p2) && p1Turn == other.p1Turn;
        }
    }
}

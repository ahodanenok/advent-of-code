/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2021/day/21
 */
public class Day21 {

    public static void main(String[] args) {
        part1(10, 4);
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

            currentPlayer.pos = (currentPlayer.pos + rolled - 1) % 10 + 1;
            currentPlayer.score += currentPlayer.pos;

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

    private static class Player {

        int score;
        int pos;

        Player(int pos) {
            this.pos = pos;
        }
    }
}

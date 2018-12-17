/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2018/day/9
 */
public class Day9 {

    public static void main(String[] args) {
        maxScore(459, 71790);
        maxScore(459, 7179000);
    }

    private static void maxScore(int playersCount, int marblesCount) {
        Game g = new Game(playersCount, marblesCount);
        while (!g.isOver()) {
            g.nextTurn();
        }

        System.out.println(g.maxScore());
    }

    private static class Game {

        long[] playersScore;
        int currentPlayer;

        Marble currentMarble;
        int nextMarbleNum;
        int marblesCount;

        Game(int playersCount, int marblesCount) {
            this.playersScore = new long[playersCount];
            this.currentPlayer = 0;
            this.nextMarbleNum = 1;
            this.marblesCount = marblesCount;

            this.currentMarble = new Marble(0);
            this.currentMarble.left = this.currentMarble;
            this.currentMarble.right = this.currentMarble;
        }

        boolean isOver() {
            return nextMarbleNum > marblesCount;
        }

        long maxScore() {
            long max = Integer.MIN_VALUE;
            for (int i = 0; i < playersScore.length; i++) {
                max = Math.max(playersScore[i], max);
            }

            return max;
        }

        void nextTurn() {
            int placedMarbleNum = nextMarbleNum;
            nextMarbleNum++;

            if (placedMarbleNum % 23 == 0) {
                Marble removedMarble = currentMarble;
                for (int i = 0; i < 7; i++) {
                    removedMarble = removedMarble.left;
                }
                currentMarble = removedMarble.right;

                playersScore[currentPlayer] += placedMarbleNum;
                playersScore[currentPlayer] += removedMarble.num;

                // removing marble from circle
                removedMarble.left.right = removedMarble.right;
                removedMarble.right.left = removedMarble.left;
                removedMarble.left = null;
                removedMarble.right = null;
            } else {
                Marble leftNeighbour = currentMarble.right;
                Marble rightNeighbour = currentMarble.right.right;

                Marble marble = new Marble(placedMarbleNum);
                marble.left = leftNeighbour;
                marble.right = rightNeighbour;

                leftNeighbour.right = marble;
                rightNeighbour.left = marble;

                currentMarble = marble;
            }

            currentPlayer = (currentPlayer + 1) % playersScore.length;
        }
    }

    private static class Marble {

        final int num;
        Marble left;
        Marble right;

        Marble(int num) {
            this.num = num;
        }
    }
}
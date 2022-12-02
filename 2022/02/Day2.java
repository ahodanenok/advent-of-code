import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2022/day/2
 */
public class Day2 {

    public static void main(String[] args) throws Exception {
        List<Round> strategy = getStrategy();
        part1(strategy);
    }

    private static List<Round> getStrategy() throws Exception {
        List<Round> strategy = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");

                Shape opponentShape;
                switch (parts[0]) {
                    case "A":
                        opponentShape = Shape.ROCK;
                        break;
                    case "B":
                        opponentShape = Shape.PAPER;
                        break;
                    case "C":
                        opponentShape = Shape.SCISSORS;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown opponent shape: " + parts[0]);
                }

                Shape myShape;
                switch (parts[1]) {
                    case "X":
                        myShape = Shape.ROCK;
                        break;
                    case "Y":
                        myShape = Shape.PAPER;
                        break;
                    case "Z":
                        myShape = Shape.SCISSORS;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown my shape: " + parts[1]);
                }

                strategy.add(new Round(opponentShape, myShape));
            }
        }

        return strategy;
    }

    private static void part1(List<Round> strategy) {
        int totalScore = 0;
        for (Round round : strategy) {
            totalScore += round.myShape.score;

            if (round.myShape == Shape.ROCK) {
                if (round.opponentShape == Shape.SCISSORS) {
                    totalScore += 6;
                } else if (round.opponentShape == Shape.ROCK) {
                    totalScore += 3;
                }
            } else if (round.myShape == Shape.SCISSORS) {
                if (round.opponentShape == Shape.PAPER) {
                    totalScore += 6;
                } else if (round.opponentShape == Shape.SCISSORS) {
                    totalScore += 3;
                }
            } else if (round.myShape == Shape.PAPER) {
                if (round.opponentShape == Shape.ROCK) {
                    totalScore += 6;
                } else if (round.opponentShape == Shape.PAPER) {
                    totalScore += 3;
                }
            }
        }

        System.out.println("Part 1: " + totalScore);
    }

    private static class Round {

        final Shape opponentShape;
        final Shape myShape;

        Round(Shape opponentShape, Shape myShape) {
            this.opponentShape = opponentShape;
            this.myShape = myShape;
        }
    }

    private enum Shape {

        ROCK(1), PAPER(2), SCISSORS(3);

        final int score;

        Shape(int score) {
            this.score = score;
        }
    }
}

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
        part2(strategy);
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
                Outcome expectedOutcome;
                switch (parts[1]) {
                    case "X":
                        myShape = Shape.ROCK;
                        expectedOutcome = Outcome.LOSE;
                        break;
                    case "Y":
                        myShape = Shape.PAPER;
                        expectedOutcome = Outcome.DRAW;
                        break;
                    case "Z":
                        myShape = Shape.SCISSORS;
                        expectedOutcome = Outcome.WIN;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown my shape/outcome: " + parts[1]);
                }

                strategy.add(new Round(opponentShape, myShape, expectedOutcome));
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
                    totalScore += Outcome.WIN.score;
                } else if (round.opponentShape == Shape.ROCK) {
                    totalScore += Outcome.DRAW.score;
                }
            } else if (round.myShape == Shape.SCISSORS) {
                if (round.opponentShape == Shape.PAPER) {
                    totalScore += Outcome.WIN.score;
                } else if (round.opponentShape == Shape.SCISSORS) {
                    totalScore += Outcome.DRAW.score;
                }
            } else if (round.myShape == Shape.PAPER) {
                if (round.opponentShape == Shape.ROCK) {
                    totalScore += Outcome.WIN.score;
                } else if (round.opponentShape == Shape.PAPER) {
                    totalScore += Outcome.DRAW.score;
                }
            }
        }

        System.out.println("Part 1: " + totalScore);
    }

    private static void part2(List<Round> strategy) {
        int totalScore = 0;
        for (Round round : strategy) {
            totalScore += round.expectedOutcome.score;
            if (round.expectedOutcome == Outcome.LOSE) {
                if (round.opponentShape == Shape.ROCK) {
                    totalScore += Shape.SCISSORS.score;
                } else if (round.opponentShape == Shape.SCISSORS) {
                    totalScore += Shape.PAPER.score;
                } else if (round.opponentShape == Shape.PAPER) {
                    totalScore += Shape.ROCK.score;
                }
            } else if (round.expectedOutcome == Outcome.WIN) {
                if (round.opponentShape == Shape.ROCK) {
                    totalScore += Shape.PAPER.score;
                } else if (round.opponentShape == Shape.SCISSORS) {
                    totalScore += Shape.ROCK.score;
                } else if (round.opponentShape == Shape.PAPER) {
                    totalScore += Shape.SCISSORS.score;
                }
            } else if (round.expectedOutcome == Outcome.DRAW) {
                totalScore += round.opponentShape.score;
            }
        }

        System.out.println("Part 2: " + totalScore);
    }

    private static class Round {

        final Shape opponentShape;
        final Shape myShape;
        final Outcome expectedOutcome;

        Round(Shape opponentShape, Shape myShape, Outcome expectedOutcome) {
            this.opponentShape = opponentShape;
            this.myShape = myShape;
            this.expectedOutcome = expectedOutcome;
        }
    }

    private enum Shape {

        ROCK(1), PAPER(2), SCISSORS(3);

        final int score;

        Shape(int score) {
            this.score = score;
        }
    }

    private enum Outcome {

        LOSE(0), DRAW(3), WIN(6);

        final int score;

        Outcome(int score) {
            this.score = score;
        }
    }
}

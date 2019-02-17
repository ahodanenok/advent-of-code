import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 10
 * https://adventofcode.com/2016/day/10
 */
public class Day10 {

    public static void main(String[] args) {
        Factory factory = getFactory();
        part1(factory);
    }

    private static void part1(Factory factory) {
        for (Map.Entry<Integer, Integer> entry : factory.values.entrySet()) {
            factory.getBot(entry.getValue()).addChip(entry.getKey(), new BigBrother() {
                @Override
                public void auditGive(Bot owner) {
                    if (owner.chipA == 17 && owner.chipB == 61 || owner.chipA == 61 && owner.chipB == 17) {
                        System.out.println(owner.num);
                    }
                }
            });
        }
    }

    private static Factory getFactory() {
        Factory factory = new Factory();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            if ("value".equals(parts[0])) {
                factory.values.put(Integer.parseInt(parts[1]), Integer.parseInt(parts[5]));
            } else if ("bot".equals(parts[0])) {
                Destination loDest;
                if ("bot".equals(parts[5])) {
                    loDest = new BotDestination(Integer.parseInt(parts[6]), factory);
                } else if ("output".equals(parts[5])) {
                    loDest = new OutputDestination(Integer.parseInt(parts[6]));
                } else {
                    throw new IllegalArgumentException("Unknown command: " + line);
                }

                Destination hiDest;
                if ("bot".equals(parts[10])) {
                    hiDest = new BotDestination(Integer.parseInt(parts[11]), factory);
                } else if ("output".equals(parts[10])) {
                    hiDest = new OutputDestination(Integer.parseInt(parts[11]));
                } else {
                    throw new IllegalArgumentException("Unknown command: " + line);
                }

                factory.bots.add(new Bot(Integer.parseInt(parts[1]), loDest, hiDest));
            } else {
                throw new IllegalArgumentException("Unknown command: " + line);
            }
        }

        return factory;
    }

    private static class Factory {

        private final List<Bot> bots = new ArrayList<Bot>();
        private final Map<Integer, Integer> values = new HashMap<Integer, Integer>();

        private Bot getBot(int num) {
            for (Bot bot : bots) {
                if (bot.num == num) {
                    return bot;
                }
            }

            throw new IllegalStateException("Bot " + num + " wasn't found");
        }
    }

    private interface Destination {
        void give(int chip, BigBrother obs);
    }

    private static class OutputDestination implements Destination {

        private final int num;

        OutputDestination(int num) {
            this.num = num;
        }

        public void give(int chip, BigBrother obs) { }
    }

    private static class BotDestination implements Destination {

        private final int botNum;
        private final Factory factory;

        BotDestination(int botNum, Factory factory) {
            this.botNum = botNum;
            this.factory = factory;
        }

        public void give(int chip, BigBrother obs) {
            factory.getBot(botNum).addChip(chip, obs);
        }
    }

    private static abstract class BigBrother {
        void auditGive(Bot owner) { }
    }

    private static class Bot {

        private final int num;
        private final Destination loDest;
        private final Destination hiDest;

        private int chipA = -1;
        private int chipB = -1;

        Bot(int num, Destination loDest, Destination hiDest) {
            this.num = num;
            this.loDest = loDest;
            this.hiDest = hiDest;
        }

        void addChip(int chip, BigBrother obs) {
            if (chipA == -1) {
                chipA = chip;
            } else if (chipB == -1) {
                chipB = chip;
            } else {
                throw new IllegalStateException("Bot " + num + " already has two chips");
            }

            if (chipA != -1 && chipB != -1) {
                giveChips(obs);
            }
        }

        private void giveChips(BigBrother obs) {
            obs.auditGive(this);
            int lo = Math.min(chipA, chipB);
            int hi = Math.max(chipA, chipB);
            chipA = -1;
            chipB = -1;
            loDest.give(lo, obs);
            hiDest.give(hi, obs);
        }
    }
}
import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.WIn;
import ahodanenok.aoc.intcode.Out;

import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 7
 * https://adventofcode.com/2019/day/7
 */
public class Day7 {

    public static void main(String[] args) throws Exception {
        long[] ams = IntcodeComputer.load("input.txt");
        part1(ams);
        part2(ams);
    }

    private static void part1(long[] ams) {
        List<Integer> availablePhases = new ArrayList<>();
        availablePhases.add(0);
        availablePhases.add(1);
        availablePhases.add(2);
        availablePhases.add(3);
        availablePhases.add(4);

        List<List<Integer>> allPhases = new ArrayList<>();
        perms(availablePhases, new ArrayList<>(), allPhases);

        long currentSignal = Long.MIN_VALUE;
        for (List<Integer> phases : allPhases) {
            long in = 0;
            for (int i = 0; i < phases.size(); i++) {
                IntcodeComputer pc = new IntcodeComputer(ams);
                pc.setIn(new WIn(phases.get(i), in));
                AmpOut out = new AmpOut();
                pc.setOut(out);
                pc.run();
                in = out.result;
            }
 
            currentSignal = Math.max(in, currentSignal);
        }

        System.out.println("Part 1: " + currentSignal);
    }

    private static void part2(long[] ams) {
        List<Integer> availablePhases = new ArrayList<>();
        availablePhases.add(5);
        availablePhases.add(6);
        availablePhases.add(7);
        availablePhases.add(8);
        availablePhases.add(9);

        List<List<Integer>> allPhases = new ArrayList<>();
        perms(availablePhases, new ArrayList<>(), allPhases);

        long currentSignal = Long.MIN_VALUE;
        for (List<Integer> phases : allPhases) {
            List<IntcodeComputer> computers = new ArrayList<>();
    
            for (int i = 0; i < phases.size(); i++) {
                IntcodeComputer pc = new IntcodeComputer(ams);
                pc.setStopOnOut(true);
                pc.setIn(new WIn(phases.get(i)));
                computers.add(pc);
            }

            long in = 0;
            int currentAmp = 0;
            while (true) {
                IntcodeComputer pc = computers.get(currentAmp);
                ((WIn) pc.getIn()).add(in);

                AmpOut out = new AmpOut();
                pc.setOut(out);

                pc.run();
                if (!out.hasResult) {
                    break;
                }

                in = out.result;
                currentAmp = (currentAmp + 1) % 5;
            }

            currentSignal = Math.max(in, currentSignal);
        }

        System.out.println("Part 2: " + currentSignal);
    }

    private static void perms(List<Integer> remaining, List<Integer> current, List<List<Integer>> all) {
        if (remaining.isEmpty()) {
           all.add(new ArrayList<>(current)); 
        } else {
            for (int i = 0; i < remaining.size(); i++) {
               current.add(remaining.remove(i));
               perms(remaining, current, all); 
               remaining.add(i, current.remove(current.size() - 1));
            }
        }
    }

    private static class AmpOut implements Out {

        long result;
        boolean hasResult;

        @Override
        public void write(long n) {
            result = n;
            hasResult = true;
        }
    }
}


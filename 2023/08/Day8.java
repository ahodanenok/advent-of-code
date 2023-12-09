import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2023/day/8
 */
public class Day8 {

    public static void main(String[] args) throws Exception {
        Maps maps = getInput();
        part1(maps);
    }

    private static void part1(Maps maps) {
        int steps = 0;
        int pos = 0;
        String node = "AAA";
        while (!node.equals("ZZZ")) {
            char inst = maps.instructions.charAt(pos);
            if (inst == 'R') {
                node = maps.nodeRight.get(node);
            } else if (inst == 'L') {
                node = maps.nodeLeft.get(node);
            } else {
                throw new IllegalStateException("inst: " + inst);
            }

            pos = (pos + 1) % maps.instructions.length();
            steps++;
        }

        System.out.println("Part 1: " + steps);
    }

    private static Maps getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Maps maps = new Maps();
            maps.instructions = reader.readLine().trim();

            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=");
                String[] nodes = parts[1].trim().split(",");
                maps.nodeLeft.put(parts[0].trim(), nodes[0].trim().substring(1));
                maps.nodeRight.put(parts[0].trim(), nodes[1].trim().substring(0, nodes[1].trim().length() - 1));
            }

            return maps;
        }
    }

    private static class Maps {

        String instructions;
        Map<String, String> nodeLeft = new HashMap<>();
        Map<String, String> nodeRight = new HashMap<>();
    }
}

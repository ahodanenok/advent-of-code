import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2017/day/12
 */
public class Day12 {

    public static void main(String[] args) {
        List<Program> programs = getPrograms();
        part1(programs);
        part2(programs);
    }

    private static void part1(List<Program> programs) {
        int programId = 0;
        Set<Integer> seen = new HashSet<Integer>();

        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(programId);
        while (queue.size() > 0) {
            int id = queue.poll();
            if (seen.contains(id)) {
                continue;
            }

            seen.add(id);
            Program p = getProgram(id, programs);
            for (Integer pipe : p.pipesTo) {
                queue.offer(pipe);
            }
        }

        System.out.println(seen.size());
    }

    private static void part2(List<Program> programs) {
        int[] programGroups = new int[programs.size()];
        for (int i = 0; i < programGroups.length; i++) {
            programGroups[i] = i;
        }

        int groupsCount = programGroups.length;
        for (Program p : programs) {
            for (Integer pipeTo : p.pipesTo) {
                int g1 = programGroups[p.id];
                int g2 = programGroups[pipeTo];
                if (g1 != g2) {
                    for (int i = 0; i < programGroups.length; i++) {
                        if (programGroups[i] == g2) {
                            programGroups[i] = g1;
                        }
                    }

                    groupsCount--;
                }
            }
        }

        System.out.println(groupsCount);
    }

    private static List<Program> getPrograms() {
        List<Program> programs = new ArrayList<Program>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("<->");
            String[] pipeParts = parts[1].trim().split(", ");
            List<Integer> pipesTo = new ArrayList<Integer>(pipeParts.length);
            for (String pipe : pipeParts) {
                pipesTo.add(Integer.parseInt(pipe));
            }

            Program p = new Program();
            p.id = Integer.parseInt(parts[0].trim());
            p.pipesTo = pipesTo;
            programs.add(p);
        }

        return programs;
    }

    private static Program getProgram(int id, List<Program> programs) {
        for (Program p : programs) {
            if (p.id == id) {
                return p;
            }
        }

        return null;
    }

    private static class Program {

        private int id;
        private List<Integer> pipesTo;
    }
}

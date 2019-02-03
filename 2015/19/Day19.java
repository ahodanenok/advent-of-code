import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2015/day/19
 */
public class Day19 {

    public static void main(String[] args) throws Exception {
        Input input = getInput();
        part1(input.molecule, input.replacements);
        part2(input.molecule);
    }

    private static void part1(String molecule, List<Replacement> replacements) {
        int count = 0;
        Set<String> seen = new HashSet<String>();

        for (Replacement r : replacements) {
            for (int i = r.from.length(); i < molecule.length(); i++) {
                if (molecule.substring(i - r.from.length(), i).equals(r.from)) {
                    int pos = i - r.from.length();
                    String replaced = molecule.substring(0, pos) + r.to + molecule.substring(pos + r.from.length());
                    if (seen.add(replaced)) {
                        count++;
                    }
                }
            }
        }

        System.out.println(count);
    }

    private static void part2(String molecule) {
        List<String> elements = toElements(molecule);
        int wrapperCount = 0;
        int separatorCount = 0;

        for (String element : elements) {
            if (element.equals("Rn") || element.equals("Ar")) {
                wrapperCount++;
            } else if (element.equals("Y")) {
                separatorCount++;
            }
        }

        //System.out.println(elements.size());
        //System.out.println(wrapperCount);
        //System.out.println(separatorCount);

        System.out.println(elements.size() - wrapperCount - 2 * separatorCount - 1);
    }

    private static List<String> toElements(String molecule) {
        List<String> elements = new ArrayList<String>();
        String currentElement = "";
        for (int i = 0; i < molecule.length(); i++) {
            char ch = molecule.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (currentElement.length() > 0) {
                    elements.add(currentElement);
                }

                currentElement = "" + ch;
            } else {
                currentElement += ch;
            }
        }

        if (currentElement.length() > 0) {
            elements.add(currentElement);
        }

        return elements;
    }

    private static Input getInput() {
        Input input = new Input();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!"".equals(line)) {
                String[] parts = line.split("=>");
                input.replacements.add(new Replacement(parts[0].trim(), parts[1].trim()));
            } else {
                input.molecule = scanner.nextLine().trim();
            }
        }

        return input;
    }

    private static class Input {

        String molecule;
        List<Replacement> replacements = new ArrayList<Replacement>();
    }

    private static class Replacement {

        private final String from;
        private final String to;

        Replacement(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return from + " => " + to;
        }
    }
}

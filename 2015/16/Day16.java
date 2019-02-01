import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2015/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        List<Sue> aunts = getAunts();

        Sue targetSue = new Sue();
        targetSue.children = 3;
        targetSue.cats = 7;
        targetSue.samoyeds = 2;
        targetSue.pomeranians = 3;
        targetSue.akitas = 0;
        targetSue.vizslas = 0;
        targetSue.goldfish = 5;
        targetSue.trees = 3;
        targetSue.cars = 2;
        targetSue.perfumes = 1;

        part1(aunts, targetSue);
    }

    private static List<Sue> getAunts() {
        List<Sue> aunts = new ArrayList<Sue>();

        Pattern namePattern = Pattern.compile("(\\w+\\s+\\d+):");
        Pattern valuesPattern = Pattern.compile("\\s+(\\w+):\\s+(\\d+),?");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            Sue sue = new Sue();

            Matcher nameMatcher = namePattern.matcher(line);
            nameMatcher.find();
            sue.name = nameMatcher.group(1);

            Matcher valuesMatcher = valuesPattern.matcher(line);
            while (valuesMatcher.find()) {
                String valueName = valuesMatcher.group(1);
                Integer value = Integer.parseInt(valuesMatcher.group(2));

                if ("children".equals(valueName)) {
                    sue.children = value;
                } else if ("cats".equals(valueName)) {
                    sue.cats = value;
                } else if ("samoyeds".equals(valueName)) {
                    sue.samoyeds = value;
                } else if ("pomeranians".equals(valueName)) {
                    sue.pomeranians = value;
                } else if ("akitas".equals(valueName)) {
                    sue.akitas = value;
                } else if ("vizslas".equals(valueName)) {
                    sue.vizslas = value;
                } else if ("goldfish".equals(valueName)) {
                    sue.goldfish = value;
                } else if ("trees".equals(valueName)) {
                    sue.trees = value;
                } else if ("cars".equals(valueName)) {
                    sue.cars = value;
                } else if ("perfumes".equals(valueName)) {
                    sue.perfumes = value;
                } else {
                    throw new IllegalArgumentException("Unknown value name: " + valueName);
                }
            }

            aunts.add(sue);
        }

        return aunts;
    }

    private static void part1(List<Sue> aunts, Sue target) {
        for (Sue aunt : aunts) {
            if (valueMatches(aunt.children, target.children)
                    && valueMatches(aunt.cats, target.cats)
                    && valueMatches(aunt.samoyeds, target.samoyeds)
                    && valueMatches(aunt.pomeranians, target.pomeranians)
                    && valueMatches(aunt.akitas, target.akitas)
                    && valueMatches(aunt.vizslas, target.vizslas)
                    && valueMatches(aunt.goldfish, target.goldfish)
                    && valueMatches(aunt.trees, target.trees)
                    && valueMatches(aunt.cars, target.cars)
                    && valueMatches(aunt.perfumes, target.perfumes)) {
                System.out.println(aunt);
                return;
            }
        }

        throw new IllegalStateException("Not found");
    }

    private static boolean valueMatches(Integer value, Integer target) {
        return value == null || value == target || (value != null && value.equals(target));
    }

    private static class Sue {

        private String name;
        private Integer children;
        private Integer cats;
        private Integer samoyeds;
        private Integer pomeranians;
        private Integer akitas;
        private Integer vizslas;
        private Integer goldfish;
        private Integer trees;
        private Integer cars;
        private Integer perfumes;

        @Override
        public String toString() {
            return name;
        }
    }
}

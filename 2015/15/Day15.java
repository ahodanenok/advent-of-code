import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 15
 * https://adventofcode.com/2015/day/15
 */
public class Day15 {

    public static void main(String[] args) {
        List<Ingridient> ingridients = getIngridients();
        System.out.println(part1(new int[ingridients.size()], 100, 0, ingridients));
        System.out.println(part2(new int[ingridients.size()], 100, 0, ingridients));
    }

    private static int part1(int[] recipe, int spoonsLeft, int ingridient, List<Ingridient> ingridients) {
        if (ingridient == ingridients.size()) {
            return calculateScore(recipe, ingridients);
        } else {
            int max = Integer.MIN_VALUE;
            for (int s = 1; s <= spoonsLeft; s++) {
                recipe[ingridient] = s;
                max = Math.max(part1(recipe, spoonsLeft - s, ingridient + 1, ingridients), max);
            }

            recipe[ingridient] = 0;
            return max;
        }
    }

    private static int part2(int[] recipe, int spoonsLeft, int ingridient, List<Ingridient> ingridients) {
        if (ingridient == ingridients.size()) {
            return caloriesCount(recipe, ingridients) == 500 
                ? calculateScore(recipe, ingridients) 
                : Integer.MIN_VALUE;
        } else {
            int max = Integer.MIN_VALUE;
            for (int s = 1; s <= spoonsLeft; s++) {
                recipe[ingridient] = s;
                max = Math.max(part2(recipe, spoonsLeft - s, ingridient + 1, ingridients), max);
            }

            recipe[ingridient] = 0;
            return max;
        }
    }

    private static int calculateScore(int[] recipe, List<Ingridient> ingridients) {
        //System.out.println(java.util.Arrays.toString(recipe));

        int capacity = 0;
        int durability = 0;
        int flavor = 0;
        int texture = 0;
        for (int i = 0; i < ingridients.size(); i++) {
            Ingridient ingridient = ingridients.get(i);
            capacity += recipe[i] * ingridient.capacity;
            durability += recipe[i] * ingridient.durability;
            flavor += recipe[i] * ingridient.flavor;
            texture += recipe[i] * ingridient.texture;
        }

        capacity = Math.max(0, capacity);
        durability = Math.max(0, durability);
        flavor = Math.max(0, flavor);
        texture = Math.max(0, texture);

        return capacity * durability * flavor * texture;
    }

    private static int caloriesCount(int[] recipe, List<Ingridient> ingridients) {
        int calories = 0;
        for (int i = 0; i < ingridients.size(); i++) {
            calories += recipe[i] * ingridients.get(i).calories;
        }

        return calories;
    }

    private static List<Ingridient> getIngridients() {
        List<Ingridient> ingridients = new ArrayList<Ingridient>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            ingridients.add(new Ingridient(
                parts[0].substring(0, parts[0].length() - 1),
                Integer.parseInt(parts[2].substring(0, parts[2].length() - 1)),
                Integer.parseInt(parts[4].substring(0, parts[4].length() - 1)),
                Integer.parseInt(parts[6].substring(0, parts[6].length() - 1)),
                Integer.parseInt(parts[8].substring(0, parts[8].length() - 1)),
                Integer.parseInt(parts[10])));
        }

        return ingridients;
    }

    private static class Ingridient {

        private final String name;
        private final int capacity;
        private final int durability;
        private final int flavor;
        private final int texture;
        private final int calories;

        Ingridient(String name, int capacity, int durability, int flavor, int texture, int calories) {
            this.name = name;
            this.capacity = capacity;
            this.durability = durability;
            this.flavor = flavor;
            this.texture = texture;
            this.calories = calories;
        }
    }

}
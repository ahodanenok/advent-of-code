import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.StringJoiner;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2020/day/21
 */
public class Day21 {

    public static void main(String[] args) throws Exception {
        List<Food> foods = getFoods();
        part1(foods);
        part2(foods);
    }

    private static void part1(List<Food> foods) {
        Map<String, String> allergenIngridients = getAllergenIngridients(foods);
        Set<String> dangerous = new HashSet<>(allergenIngridients.values());

        int cleanCount = 0;
        for (Food food : foods) {
            for (String ingridient : food.ingridients) {
                if (!dangerous.contains(ingridient)) {
                    cleanCount++;
                }
            }
        }

        System.out.println("Part 1: " + cleanCount);
    }

    private static void part2(List<Food> foods) {
        StringJoiner joiner = new StringJoiner(",");
        for (String ingridient : new TreeMap<>(getAllergenIngridients(foods)).values()) {
            joiner.add(ingridient);
        }

        System.out.println("Part 2: " + joiner);
    }

    private static Map<String, String> getAllergenIngridients(List<Food> foods) {
        Map<String, Set<String>> allergenIngridients = new HashMap<>();
        for (Food food : foods) {
            for (String allergen : food.allergens) {
                if (!allergenIngridients.containsKey(allergen)) {
                    allergenIngridients.put(allergen, new HashSet<>(food.ingridients));
                } else {
                    allergenIngridients.get(allergen).retainAll(food.ingridients);
                }
            }
        }

        while (true) {
            boolean anyChanged = false;
            for (Map.Entry<String, Set<String>> entry : allergenIngridients.entrySet()) {
                if (entry.getValue().size() > 1) {
                    continue;
                }

                for (Map.Entry<String, Set<String>> other : allergenIngridients.entrySet()) {
                    if (entry.getKey().equals(other.getKey())) {
                        continue;
                    }

                    if (other.getValue().removeAll(entry.getValue())) {
                        anyChanged = true;
                    }
                }
            }

            if (!anyChanged) {
                break;
            }
        }

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : allergenIngridients.entrySet()) {
            if (entry.getValue().size() != 1) {
                throw new IllegalStateException(entry.toString());
            }

            result.put(entry.getKey(), entry.getValue().iterator().next());
        }

        return result;
    }

    private static List<Food> getFoods() throws Exception {
        List<Food> foods = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Food food = new Food();

                int allergensStartIdx = line.indexOf('(');
                for (String allergen : line.substring(allergensStartIdx + 9, line.length() - 1).split(",\\s")) {
                    food.allergens.add(allergen.trim());
                }

                for (String ingridient : line.substring(0, allergensStartIdx).split("\\s")) {
                    food.ingridients.add(ingridient.trim());
                }

                foods.add(food);
            }
        }

        return foods;
    }

    private static class Food {

        final List<String> ingridients = new ArrayList<>();
        final List<String> allergens = new ArrayList<>();

        @Override
        public String toString() {
            return String.format("Food(ingridients: %s, allergens: %s)", ingridients, allergens);
        }
    }
}

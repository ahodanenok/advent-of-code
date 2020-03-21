import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2019/day/14
 */
public class Day14 {

    public static void main(String[] args) throws Exception {
        NF factory = new NF(getReactions());
        System.out.println("Part 1: " + factory.oreNeeded(new Chemical("FUEL", 1), new Storage()));
    }

    private static Map<Chemical, List<Chemical>> getReactions() throws Exception {
        Map<Chemical, List<Chemical>> reactions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=>");
                Chemical chemical = parseChemical(parts[1]);
                List<Chemical> needed = new ArrayList<>();
                for (String str : parts[0].split(",")) {
                    needed.add(parseChemical(str));
                } 

                reactions.put(chemical, needed);
            }
        }

        return reactions;
    }

    private static Chemical parseChemical(String str) {
        String[] parts = str.trim().split(" ");
        return new Chemical(parts[1].trim(), Integer.parseInt(parts[0].trim()));
    }

    private static class Chemical {

        private final int quantity;
        private final String name;

        Chemical(String name, int quantity) {
            this.quantity = quantity;
            this.name = name;
        } 

        @Override
        public boolean equals(Object obj) {
            Chemical other = (Chemical) obj;
            return quantity == other.quantity && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + quantity;
        }

        @Override
        public String toString() {
            return quantity + " " + name;
        }
    }

    private static class NF {

        private Map<Chemical, List<Chemical>> reactions;

        NF(Map<Chemical, List<Chemical>> reactions) {
            this.reactions = reactions;
        }

        long oreNeeded(Chemical chemical, Storage storage) {
            long ore = 0;

            Chemical produced = chemical;
            List<Chemical> needed = reactions.get(chemical);
            if (needed == null) {
                for (Map.Entry<Chemical, List<Chemical>> entry : reactions.entrySet()) {
                    if (entry.getKey().name.equals(chemical.name) 
                            && entry.getKey().quantity > chemical.quantity) {
                       needed = entry.getValue();
                       produced = entry.getKey(); 
                       break;
                    }

                    if (entry.getKey().name.equals(chemical.name) 
                            && entry.getKey().quantity < chemical.quantity) {
                       needed = new ArrayList<>();

                       int q = 0;
                       if (storage.availableAny(chemical.name)) {
                           q = storage.takeAll(chemical.name);
                       }

                       while (q < chemical.quantity) {
                           q += entry.getKey().quantity;
                           for (int i = 0; i < entry.getValue().size(); i++) {
                               if (needed.size() <= i) {
                                   needed.add(entry.getValue().get(i));
                               } else {
                                   Chemical n = needed.get(i);
                                   needed.set(i,
                                       new Chemical(n.name, n.quantity + entry.getValue().get(i).quantity));
                               }
                           } 
                       } 

                       produced = new Chemical(chemical.name, q);
                       break;
                    }
                } 
            }

            if (needed == null || needed.isEmpty()) {
                throw new IllegalStateException("Can't produce " + chemical);
            }

            for (Chemical n : needed) {
                if (n.name.equals("ORE")) {
                    ore += n.quantity;
                } else if (storage.available(n)) {
                    storage.take(n);
                } else {
                    ore += oreNeeded(n, storage);
                }
            }

            //System.out.println("Consume " + needed + " to produce " + produced);

            if (produced.quantity > chemical.quantity) {
                storage.put(new Chemical(produced.name, produced.quantity - chemical.quantity));
            }

            return ore;
        }
    }

    private static class Storage {

        private Map<String, Integer> stored = new HashMap<>();

        Storage() { } 

        boolean availableAny(String chemicalName) {
            return stored.containsKey(chemicalName) && stored.get(chemicalName) > 0;
        }

        boolean available(Chemical chemical) {
            return stored.containsKey(chemical.name) && stored.get(chemical.name) >= chemical.quantity;
        }

        void put(Chemical chemical) {
            if (stored.containsKey(chemical.name)) {
                stored.put(chemical.name, stored.get(chemical.name) + chemical.quantity);
            } else {
                stored.put(chemical.name, chemical.quantity);
            }
        }

        int takeAll(String chemicalName) {
            return stored.remove(chemicalName);
        }

        void take(Chemical chemical) {
           stored.put(chemical.name, stored.get(chemical.name) - chemical.quantity);
        }
    }
}


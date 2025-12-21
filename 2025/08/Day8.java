import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2025/day/8
 */
public class Day8 {

    public static void main(String... args) throws Exception {
        List<Box> boxes = getInput();
        part1(boxes);
    }

    private static List<Box> getInput() throws Exception {
        try (BufferedReader reader =
                new BufferedReader(new FileReader("input.txt"))) {
            List<Box> boxes = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                boxes.add(new Box(
                    Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2])));
            }

            return boxes;
        }
    }

    private static void part1(List<Box> boxes) {
        int[] circuits = new int[boxes.size()];
        for (int i = 0; i < circuits.length; i++) {
            circuits[i] = i;
        }

        Set<String> connected = new HashSet<>();
        for (int n = 0; n < 1000; n++) {
            double distanceMin = Double.MAX_VALUE;
            int connectFromIdx = -1;
            int connectToIdx = -1;
            for (int i = 0; i < boxes.size(); i++) {
                Box box = boxes.get(i);
                for (int j = 0; j < boxes.size(); j++) {
                    if (i == j || connected.contains(i + "_" + j) || connected.contains(j + "_" + i)) {
                        continue;
                    }

                    double distance = box.distanceTo(boxes.get(j));
                    if (distance < distanceMin) {
                        distanceMin = distance;
                        connectFromIdx = i;
                        connectToIdx = j;
                    }
                }
            }

            if (connectFromIdx == -1 || connectToIdx == -1) {
                break;
            }

            int circuitNum = circuits[connectToIdx];
            for (int m = 0; m < circuits.length; m++) {
                if (circuits[m] == circuitNum) {
                    circuits[m] = circuits[connectFromIdx];
                }
            }

            connected.add(connectFromIdx + "_" + connectToIdx);
        }

        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < circuits.length; i++) {
            counts.merge(circuits[i], 1, (a, b) -> a + b);
        }

        System.out.println("Part 1: "
            + counts.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b));
    }
    
    private static class Box {
    
        final int x;
        final int y;
        final int z;
        
        Box(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        double distanceTo(Box box) {
            return Math.sqrt(Math.pow(x - box.x, 2) + Math.pow(y - box.y, 2) + Math.pow(z - box.z, 2));
        }
        
        @Override
        public int hashCode() {
            return 31 * 31 * x + 31 * y + z;
        }
        
        @Override
        public boolean equals(Object obj) {
            Box other = (Box) obj;
            return x == other.x && y == other.y && z == other.z;
        }
        
        @Override
        public String toString() {
            return "(%s, %s, %s)".formatted(x, y, z);
        }
    }
}

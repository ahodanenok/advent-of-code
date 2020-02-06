import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2019/day/2  
 */
public class Day2 {

    public static void main(String[] args) throws Exception {
        List<Integer> input = getInput();
        Memory memory_1 = new Memory(input);
        memory_1.set(1, 12);
        memory_1.set(2, 2);
        run(input, memory_1);
        System.out.println("part 1: " + memory_1.get(0));
    } 

    private static List<Integer> getInput() throws Exception {
        List<Integer> input = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line = reader.readLine();
            for (String n : line.split(",")) {
                input.add(Integer.parseInt(n.trim()));
            }
        }

        return input;
    }

    private static void run(List<Integer> input, Memory memory) {
        int pos = 0;
        while (pos < input.size() && input.get(pos) != 99) {
            if (input.get(pos) == 1) {
                int a = memory.get(input.get(pos + 1));
                int b = memory.get(input.get(pos + 2));
                memory.set(input.get(pos + 3), a + b);
                pos += 4;
            } else if (input.get(pos) == 2) {
                int a = memory.get(input.get(pos + 1));
                int b = memory.get(input.get(pos + 2));
                memory.set(input.get(pos + 3), a * b);
                pos += 4;
            } else {
                throw new IllegalStateException("Unknown command: " + input.get(pos)); 
            } 
        }
    }

    private static class Memory {


        private List<Integer> data; 

        Memory(List<Integer> initialData) {
            this.data = new ArrayList<>(initialData);
        }

        void set(int idx, int value) {
            data.set(idx, value);
        }

        int get(int idx) {
            return data.get(idx);
        }
    }
}


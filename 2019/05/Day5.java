import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2019/day/5  
 */
public class Day5 {

    public static void main(String[] args) throws Exception {
        List<Integer> input = getInput();

        Memory memory_1 = new Memory(input);
        run(input, memory_1);
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
        while (pos < input.size() && memory.get(pos) != 99) {
            int cmd = memory.get(pos);
            int opcode = opcode(cmd);
            if (opcode == 1) {
                int a = value(memory, cmd, pos, 1);
                int b = value(memory, cmd, pos, 2);
                memory.set(memory.get(pos + 3), a + b);
                pos += 4;
            } else if (opcode == 2) {
                int a = value(memory, cmd, pos, 1);
                int b = value(memory, cmd, pos, 2);
                memory.set(memory.get(pos + 3), a * b);
                pos += 4;
            } else if (opcode == 3) {
                int in = 1;
                memory.set(memory.get(pos + 1), in);
                pos += 2;
            } else if (opcode == 4) {
                System.out.println(value(memory, cmd, pos, 1));
                pos += 2;
            } else {
                throw new IllegalStateException("Unknown command: " + opcode); 
            } 
        }
    }

    private static int opcode(int instruction) {
        return instruction % 100;
    }

    private static int value(Memory memory, int instruction, int pos, int idx) {
        int mode = instruction / (int) Math.pow(10, idx + 1) % 10;
        if (mode == 0) {
            return memory.get(memory.get(pos + idx));
        } else {
            return memory.get(pos + idx);
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


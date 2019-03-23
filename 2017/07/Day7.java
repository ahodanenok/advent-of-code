import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Day7 {

    public static void main(String[] args) {
        List<Program> programs = getPrograms();
        part1(programs);
    }

    private static void part1(List<Program> programs) {
        Program root = null;
        for (int i = 0; i < programs.size(); i++) {
            boolean hasRef = false;
            for (int j = 0; j < programs.size(); j++) {
                if (programs.get(j).holding.contains(programs.get(i).name)) {
                    hasRef = true;
                    break;
                }
            }

            if (!hasRef) {
                root = programs.get(i);
                break;
            }
        }

        System.out.println(root.name);
    }

    private static List<Program> getPrograms() {
        List<Program> programs = new ArrayList<Program>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("\\s+");
            List<String> holding = new ArrayList<String>();
            for (int i = 3; i < parts.length; i++) {
                holding.add(parts[i].substring(0, i != parts.length - 1 ? parts[i].length() - 1 : parts[i].length()));
            }

            programs.add(new Program(
                parts[0],
                Integer.parseInt(parts[1].substring(1, parts[1].length() - 1)),
                holding));
        }

        return programs;
    }

    private static class Program {

        private final String name;
        private final int weight;
        private final List<String> holding;

        private Program(String name, int weight, List<String> holding) {
            this.name = name;
            this.weight = weight;
            this.holding = holding;
        }

        @Override
        public String toString() {
            return name + " (" + weight + ") -> " + holding; 
        }
    }
}

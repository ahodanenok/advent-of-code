import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Day8 {

    public static void main(String[] args) throws Exception {
        List<Command> commands = getCommands();

        Set<Integer> executed = new HashSet<>();

        int acc = 0;
        int pos = 0;
        while (pos >= 0 && pos < commands.size()) {
            Command cmd = commands.get(pos);
            if (!executed.add(pos)) {
                break;
            }

            if ("acc".equals(cmd.name)) {
                acc += cmd.arg;
                pos++;
            } else if ("jmp".equals(cmd.name)) {
                pos += cmd.arg;
            } else if ("nop".equals(cmd.name)) {
                pos++;
            } else {
                throw new IllegalStateException(cmd.name);
            }
            
        }

        System.out.println("Part 1: " + acc);
    }

    private static List<Command> getCommands() throws Exception {
        List<Command> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                commands.add(new Command(parts[0], Integer.parseInt(parts[1])));
            }
        }

        return commands;
    }

    private static class Command {

        final String name;
        final int arg;

        Command(String name, int arg) {
            this.name = name;
            this.arg = arg;
        }
    }
}

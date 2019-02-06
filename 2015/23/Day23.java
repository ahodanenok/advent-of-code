import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2015/day/23
 */
public class Day23 {

    public static void main(String[] args) {
        List<Command> commands = getCommands();
        part1(commands);
    }

    private static void part1(List<Command> commands) {
        Env env1 = new Env();
        execute(commands, env1);
        System.out.println(env1.b);
    }

    private static List<Command> getCommands() {
        List<Command> commands = new ArrayList<Command>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");

            Command c;
            if (parts.length == 2) {
                c = new Command(parts[0], parts[1]);
            } else if (parts.length == 3) {
                c = new Command(parts[0], parts[1].substring(0, parts[1].length() - 1), parts[2]);
            } else {
                throw new IllegalArgumentException(line);
            }

            commands.add(c);
       }

       return commands;
    }

    private static void execute(List<Command> commands, Env env) {
        while (env.pc < commands.size()) {
            Command c = commands.get(env.pc);
            //System.out.println("Executing '" + c + "', pc=" + env.pc + ", a=" + env.a + ", b=" + env.b);
            if ("hlf".equals(c.cmd)) {
                env.setRegisterValue(c.arg1, env.getRegisterValue(c.arg1) / 2);
                env.pc++;
            } else if ("tpl".equals(c.cmd)) {
                env.setRegisterValue(c.arg1, env.getRegisterValue(c.arg1) * 3);
                env.pc++;
            } else if ("inc".equals(c.cmd)) {
                env.setRegisterValue(c.arg1, env.getRegisterValue(c.arg1) + 1);
                env.pc++;
            } else if ("jmp".equals(c.cmd)) {
                env.pc += Long.parseLong(c.arg1);
            } else if ("jie".equals(c.cmd)) {
                if (env.getRegisterValue(c.arg1) % 2 == 0) {
                    env.pc += Long.parseLong(c.arg2);
                } else {
                    env.pc++;
                }
            } else if ("jio".equals(c.cmd)) {
                if (env.getRegisterValue(c.arg1) == 1) {
                    env.pc += Long.parseLong(c.arg2);
                } else {
                    env.pc++;
                }
            } else {
                throw new IllegalArgumentException("Unknown command: " + c.cmd);
            }
        }
    }

    private static class Env {

        private long a;
        private long b;
        private int pc;

        long getRegisterValue(String name) {
            if ("a".equals(name)) {
                return a;
            } else if ("b".equals(name)) {
                return b;
            } else {
                throw new IllegalArgumentException("Unknown register: " + name);
            }
        }

        void setRegisterValue(String name, long value) {
            if ("a".equals(name)) {
                a = value;
            } else if ("b".equals(name)) {
                b = value;
            } else {
                throw new IllegalArgumentException("Unknown register: " + name);
            }
        }
    }

    private static class Command {

        private final String cmd;
        private final String arg1;
        private final String arg2;

        Command(String cmd, String arg1) {
            this(cmd, arg1, null);
        }

        Command(String cmd, String arg1, String arg2) {
            this.cmd = cmd;
            this.arg1 = arg1;
            this.arg2 = arg2;
        }

        @Override
        public String toString() {
            String str = cmd + " " + arg1;
            if (arg2 != null) {
                str += ", " + arg2;
            }

            return str;
        }
    }
}

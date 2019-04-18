import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2017/day/23
 */
public class Day23 {

    public static void main(String[] args) throws Exception {
        List<Command> commands = getCommands();
        part1(commands);
        part2();
    }

    private static void part1(List<Command> commands) {
        int mulInvokedCount = 0;
        Env env = new Env();
        while (env.pc < commands.size()) {
            Command cmd = commands.get(env.pc);
            if ("set".equals(cmd.cmd)) {
                env.setRegisterValue(cmd.arg1, getNumberOrRegisterValue(cmd.arg2, env));
                env.pc++;
            } else if ("sub".equals(cmd.cmd)) {
                env.setRegisterValue(cmd.arg1, env.getRegisterValue(cmd.arg1) - getNumberOrRegisterValue(cmd.arg2, env));
                env.pc++;
            } else if ("mul".equals(cmd.cmd)) {                
                env.setRegisterValue(cmd.arg1, env.getRegisterValue(cmd.arg1) * getNumberOrRegisterValue(cmd.arg2, env));
                mulInvokedCount++;
                env.pc++;
            } else if ("jnz".equals(cmd.cmd)) {
                if (getNumberOrRegisterValue(cmd.arg1, env) != 0) {
                    env.pc += getNumberOrRegisterValue(cmd.arg2, env);
                } else {
                    env.pc++;
                }
            } else {
                throw new IllegalStateException("Unknown command: " + cmd.cmd);
            }
        }

        System.out.println(mulInvokedCount);
    }

    private static void part2() {
        long a = 0, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;

        a = 1;

        b = 93;
        c = b;
        b = b * 100;
        b = b + 100000;
        c = b;
        c = c + 17000;

        g = b; // initialize g to the starting value
               // because it also must be checked for primality by 'check' loop

        // [b = 109300, c = 126300]
        loop: while (true) {
            f = 1;
            d = 2;
            // this loop checks if value of b is not prime
            check: while (g != 0) {
                e = b % d;
                if (e == 0) {
                    f = 0;
                    break;
                }

                d = d + 1;
                g = d;
                g = g - b;
            }

            if (f == 0) {
                h = h + 1;
            }

            g = b;
            g = g - c;
            if (g == 0) {
                break loop;
            }
            b = b + 17;
        }

        System.out.println(h);
    }

    private static boolean isRegisterName(String str) {
        return str.length() == 1 && Character.isLetter(str.charAt(0));
    }

    private static long getNumberOrRegisterValue(String str, Env env) {
        if (isRegisterName(str)) {
            return env.getRegisterValue(str);
        } else {
            return Long.parseLong(str);
        }
    }

    private static List<Command> getCommands() {
        List<Command> commands = new ArrayList<Command>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("\\s+");
            Command c = new Command();
            c.cmd = parts[0];
            c.arg1 = parts[1];
            if (parts.length == 3) {
                c.arg2 = parts[2];
            }

            commands.add(c);
        }

        return commands;
    }

    private static class Command {

        private String cmd;
        private String arg1;
        private String arg2;
    }

    private static class Env {

        private int pc;
        private Map<String, Long> registers = new HashMap<String, Long>();

        long getRegisterValue(String name) {
            if (!registers.containsKey(name)) {
                registers.put(name, 0L);
            }

            return registers.get(name);
        }

        void setRegisterValue(String name, long value) {
            registers.put(name, value);
        }
    }
}

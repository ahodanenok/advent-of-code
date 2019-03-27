import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2017/day/8
 */
public class Day8 {

    public static void main(String[] args) {
        List<Command> commands = getCommands();
        part1(commands);
        part2(commands);
    }

    private static void part1(List<Command> commands) {
        Env env = new Env();
        execute(commands, env);
        System.out.println(env.maxCurrentRegisterValue());
    }

    private static void part2(List<Command> commands) {
        Env env = new Env();
        execute(commands, env);
        System.out.println(env.maxSeenRegisterValue());
    }

    private static List<Command> getCommands()  {
        List<Command> commands = new ArrayList<Command>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            Command c = new Command();
            c.register = parts[0];
            c.op = parts[1];
            c.delta = Long.parseLong(parts[2]);
            c.conditionRegister = parts[4];
            c.conditionOp = parts[5];
            c.conditionValue = Long.parseLong(parts[6]);
            commands.add(c);
        }

        return commands;
    }

    private static void execute(List<Command> commands, Env env) {
        for (Command c : commands) {
            if (conditionHolds(c, env)) {
                execute(c, env);
            }
        }
    }

    private static boolean conditionHolds(Command c, Env env) {
        if (c.conditionOp.equals(">")) {
            return env.getRegisterValue(c.conditionRegister) > c.conditionValue;
        } else if (c.conditionOp.equals("<")) {
            return env.getRegisterValue(c.conditionRegister) < c.conditionValue;
        } else if (c.conditionOp.equals(">=")) {
            return env.getRegisterValue(c.conditionRegister) >= c.conditionValue;
        } else if (c.conditionOp.equals("<=")) {
            return env.getRegisterValue(c.conditionRegister) <= c.conditionValue;
        } else if (c.conditionOp.equals("==")) {
            return env.getRegisterValue(c.conditionRegister) == c.conditionValue;
        } else if (c.conditionOp.equals("!=")) {
            return env.getRegisterValue(c.conditionRegister) != c.conditionValue;
        } else {
            throw new IllegalStateException("unknown condition: " + c.conditionOp);
        }
    }

    private static void execute(Command c, Env env) {
        if (c.op.equals("inc")) {
            env.setRegisterValue(c.register, env.getRegisterValue(c.register) + c.delta);
        } else if (c.op.equals("dec")) {
            env.setRegisterValue(c.register, env.getRegisterValue(c.register) - c.delta);
        } else {
            throw new IllegalStateException("unknown command: " + c.op);
        }
    }

    private static class Env {

        private long max = Long.MIN_VALUE;
        private Map<String, Long> registers = new HashMap<String, Long>();

        long getRegisterValue(String name) {
            if (!registers.containsKey(name)) {
                registers.put(name, 0L);
            }

            return registers.get(name);
        }

        void setRegisterValue(String name, long value) {
            registers.put(name, value);
            max = Math.max(value, max);
        }

        long maxCurrentRegisterValue() {
            return Collections.max(registers.values());
        }

        long maxSeenRegisterValue() {
            return max;
        }
    }

    private static class Command {

        String register;
        String op;
        long delta;

        String conditionRegister;
        String conditionOp;
        long conditionValue;

        @Override
        public String toString() {
            return register + " " + op + " " + delta + " if " + conditionRegister + " " + conditionOp + " " + conditionValue;
        }
    }
}

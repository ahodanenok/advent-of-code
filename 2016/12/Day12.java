import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2016/day/12
 */
public class Day12 {

    public static void main(String[] args) {
        List<Instruction> instructions = getInstructions();
        part1(instructions);
    }

    private static void part1(List<Instruction> instructions) {
        Env env1 = new Env();
        execute(instructions, env1);
        System.out.println(env1.a);
    }

    private static List<Instruction> getInstructions() {
        List<Instruction> instructions = new ArrayList<Instruction>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            Instruction c;
            if (parts.length == 2) {
                c = new Instruction(parts[0], parts[1]);
            } else if (parts.length == 3) {
                c = new Instruction(parts[0], parts[1], parts[2]);
            } else {
                throw new IllegalArgumentException(line);
            }

            instructions.add(c);
        }

        return instructions;
    }

    private static void execute(List<Instruction> instructions, Env env) {
        while (env.pc < instructions.size()) {
            Instruction instruction = instructions.get(env.pc);
            //System.out.println("Executing '" + c + "', pc=" + env.pc + ", a=" + env.a + ", b=" + env.b);
            if ("cpy".equals(instruction.cmd)) {
                if (isRegisterName(instruction.arg1)) {
                    env.setRegisterValue(instruction.arg2, env.getRegisterValue(instruction.arg1));
                } else {
                    env.setRegisterValue(instruction.arg2, Long.parseLong(instruction.arg1));
                }
                env.pc++;
            } else if ("inc".equals(instruction.cmd)) {
                env.setRegisterValue(instruction.arg1, env.getRegisterValue(instruction.arg1) + 1);
                env.pc++;
            } else if ("dec".equals(instruction.cmd)) {
                env.setRegisterValue(instruction.arg1, env.getRegisterValue(instruction.arg1) - 1);
                env.pc++;
            } else if ("jnz".equals(instruction.cmd)) {
                long value = isRegisterName(instruction.arg1)
                    ? env.getRegisterValue(instruction.arg1)
                    : Long.parseLong(instruction.arg1);
                if (value != 0) {
                    env.pc += Long.parseLong(instruction.arg2);
                } else {
                    env.pc++;
                }
            } else {
                throw new IllegalArgumentException("Unknown command: " + instruction.cmd);
            }
        }
    }

    private static boolean isRegisterName(String arg) {
        return arg.length() == 1 && Character.isLetter(arg.charAt(0));
    }

    private static class Env {

        private long a;
        private long b;
        private long c;
        private long d;
        private int pc;

        long getRegisterValue(String name) {
            if ("a".equals(name)) {
                return a;
            } else if ("b".equals(name)) {
                return b;
            } else if ("c".equals(name)) {
                return c;
            } else if ("d".equals(name)) {
                return d;
            } else {
                throw new IllegalArgumentException("Unknown register: " + name);
            }
        }

        void setRegisterValue(String name, long value) {
            if ("a".equals(name)) {
                a = value;
            } else if ("b".equals(name)) {
                b = value;
            } else if ("c".equals(name)) {
                c = value;
            } else if ("d".equals(name)) {
                d = value;
            } else {
                throw new IllegalArgumentException("Unknown register: " + name);
            }
        }
    }

    private static class Instruction {

        private final String cmd;
        private final String arg1;
        private final String arg2;

        Instruction(String cmd, String arg1) {
            this(cmd, arg1, null);
        }

        Instruction(String cmd, String arg1, String arg2) {
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

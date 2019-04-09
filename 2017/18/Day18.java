import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2017/day/18
 */
public class Day18 {

    private static final String SND_CMD = "snd";
    private static final String SET_CMD = "set";
    private static final String ADD_CMD = "add";
    private static final String MUL_CMD = "mul";
    private static final String MOD_CMD = "mod";
    private static final String RCV_CMD = "rcv";
    private static final String JGZ_CMD = "jgz";

    public static void main(String[] args) {
        List<Command> commands = getCommands();
        part1(commands);
        part2(commands);
    }

    private static void part1(List<Command> commands) {
        long lastPlayed = 0;
        Env env = new Env();
        while (env.pc < commands.size()) {
            Command cmd = commands.get(env.pc);
            if (SND_CMD.equals(cmd.cmd)) {
                lastPlayed = getNumberOrRegisterValue(cmd.arg1, env);
                env.pc++;
            } else if (SET_CMD.equals(cmd.cmd)) {
                env.setRegisterValue(cmd.arg1, getNumberOrRegisterValue(cmd.arg2, env));
                env.pc++;
            } else if (ADD_CMD.equals(cmd.cmd)) {
                env.setRegisterValue(cmd.arg1, env.getRegisterValue(cmd.arg1) + getNumberOrRegisterValue(cmd.arg2, env));
                env.pc++;
            } else if (MUL_CMD.equals(cmd.cmd)) {
                env.setRegisterValue(cmd.arg1, env.getRegisterValue(cmd.arg1) * getNumberOrRegisterValue(cmd.arg2, env));
                env.pc++;
            } else if (MOD_CMD.equals(cmd.cmd)) {
                env.setRegisterValue(cmd.arg1, env.getRegisterValue(cmd.arg1) % getNumberOrRegisterValue(cmd.arg2, env));
                env.pc++;
            } else if (RCV_CMD.equals(cmd.cmd)) {
                long value = getNumberOrRegisterValue(cmd.arg1, env);
                if (value != 0) {
                    break;
                }
                env.pc++;
            } else if (JGZ_CMD.equals(cmd.cmd)) {
                if (getNumberOrRegisterValue(cmd.arg1, env) > 0) {
                    env.pc += Integer.parseInt(cmd.arg2);
                } else {
                    env.pc++;
                }
            } else {
                throw new IllegalArgumentException("Unknown command: " + cmd.cmd);
            }
        }

        System.out.println(lastPlayed);
    }

    private static void part2(List<Command> commands) {
        Program p0 = new Program(0);
        Program p1 = new Program(1);

        p0.linkedProgram = p1;
        p1.linkedProgram = p0;

        Program activeProgram = p0;

        int sendCount = 0;
        while (true) {
            if (!activeProgram.canExecuteNext(commands)) {
                activeProgram = activeProgram.linkedProgram;
                if (!activeProgram.canExecuteNext(commands)) {
                    break;
                }
            }

            Command cmd = commands.get(activeProgram.env.pc);
            if (SND_CMD.equals(cmd.cmd)) {
                activeProgram.send(getNumberOrRegisterValue(cmd.arg1, activeProgram.env));
                if (activeProgram.id == 1) {
                    sendCount++;
                }
                activeProgram.env.pc++;
            } else if (SET_CMD.equals(cmd.cmd)) {
                activeProgram.env.setRegisterValue(cmd.arg1, getNumberOrRegisterValue(cmd.arg2, activeProgram.env));
                activeProgram.env.pc++;
            } else if (ADD_CMD.equals(cmd.cmd)) {
                activeProgram.env.setRegisterValue(cmd.arg1, 
                        activeProgram.env.getRegisterValue(cmd.arg1) + getNumberOrRegisterValue(cmd.arg2, activeProgram.env));
                activeProgram.env.pc++;
            } else if (MUL_CMD.equals(cmd.cmd)) {
                activeProgram.env.setRegisterValue(cmd.arg1, 
                        activeProgram.env.getRegisterValue(cmd.arg1) * getNumberOrRegisterValue(cmd.arg2, activeProgram.env));
                activeProgram.env.pc++;
            } else if (MOD_CMD.equals(cmd.cmd)) {
                activeProgram.env.setRegisterValue(cmd.arg1, 
                        activeProgram.env.getRegisterValue(cmd.arg1) % getNumberOrRegisterValue(cmd.arg2, activeProgram.env));
                activeProgram.env.pc++;
            } else if (RCV_CMD.equals(cmd.cmd)) {
                if (activeProgram.receive(cmd.arg1)) {
                    activeProgram.env.pc++;
                }
            } else if (JGZ_CMD.equals(cmd.cmd)) {
                if (getNumberOrRegisterValue(cmd.arg1, activeProgram.env) > 0) {
                    activeProgram.env.pc += getNumberOrRegisterValue(cmd.arg2, activeProgram.env);
                } else {
                    activeProgram.env.pc++;
                }
            } else {
                throw new IllegalArgumentException("Unknown command: " + cmd.cmd);
            }
        }

        System.out.println(sendCount);
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

    private static class Program {

        private int id;
        private Env env;
        private boolean waiting;
        private LinkedList<Long> queue;
        private Program linkedProgram;

        Program(int id) {
            this.id = id;
            this.env = new Env();
            this.env.setRegisterValue("p", id);
            this.queue = new LinkedList<Long>();
        }

        void send(long value) {
            linkedProgram.queue.offer(value);
            linkedProgram.waiting = false;
        }

        boolean receive(String registerName) {
            if (queue.size() == 0) {
                waiting = true;
                return false;
            } else {
                env.setRegisterValue(registerName, queue.poll());
                return true;
            }
        }

        boolean canExecuteNext(List<Command> commands) {
            return env.pc < commands.size() && !waiting;
        }
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

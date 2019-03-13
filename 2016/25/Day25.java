import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Day25 {

    private static final String CPY_CMD = "cpy";
    private static final String INC_CMD = "inc";
    private static final String DEC_CMD = "dec";
    private static final String JNZ_CMD = "jnz";
    private static final String OUT_CMD = "out";
    private static final String TGL_CMD = "tgl";

    public static void main(String[] args) {
        lowestA(getInstructions());
    }

    private static void lowestA(List<Instruction> instructions) {
        int a = 0;
        while (true) {
            Env env = new Env();
            env.a = a;
            Transmitter t = new Transmitter();
            execute(instructions, env, t);
            if (t.transmitted()) {
                break;
            }

            a++;
        }

        System.out.println(a);
    }

    private static List<Instruction> getInstructions() {
        List<Instruction> instructions = new ArrayList<Instruction>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
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

    private static void execute(List<Instruction> instructions, Env env, Transmitter t) {
        while (env.pc < instructions.size()) {
            Instruction instruction = instructions.get(env.pc);
            if (CPY_CMD.equals(instruction.cmd)) {
                if (isRegisterName(instruction.arg2)) {
                    if (isRegisterName(instruction.arg1)) {
                        env.setRegisterValue(instruction.arg2, env.getRegisterValue(instruction.arg1));
                    } else {
                        env.setRegisterValue(instruction.arg2, Long.parseLong(instruction.arg1));
                    }
                }
                env.pc++;
            } else if (INC_CMD.equals(instruction.cmd)) {
                if (isRegisterName(instruction.arg1)) {
                    env.setRegisterValue(instruction.arg1, env.getRegisterValue(instruction.arg1) + 1);
                }
                env.pc++;
            } else if (DEC_CMD.equals(instruction.cmd)) {
                if (isRegisterName(instruction.arg1)) {
                    env.setRegisterValue(instruction.arg1, env.getRegisterValue(instruction.arg1) - 1);
                }
                env.pc++;
            } else if (OUT_CMD.equals(instruction.cmd)) {
                t.transmit(isRegisterName(instruction.arg1) ? env.getRegisterValue(instruction.arg1) : Long.parseLong(instruction.arg1));
                if (t.enough()) {
                    break;
                }
                env.pc++;
            } else if (JNZ_CMD.equals(instruction.cmd)) {
                long value = isRegisterName(instruction.arg1) ? env.getRegisterValue(instruction.arg1) : Long.parseLong(instruction.arg1);
                if (value != 0) {
                    env.pc += isRegisterName(instruction.arg2) ? env.getRegisterValue(instruction.arg2) : Long.parseLong(instruction.arg2);
                } else {
                    env.pc++;
                }
            } else if (TGL_CMD.equals(instruction.cmd)) {
                if (isRegisterName(instruction.arg1)) {
                    int tglIdx = env.pc + (int) env.getRegisterValue(instruction.arg1);
                    if (tglIdx >= 0 && tglIdx < instructions.size()) {
                        Instruction tglInstruction = instructions.get(tglIdx);
                        if (INC_CMD.equals(tglInstruction.cmd)) {
                            instructions.set(tglIdx, new Instruction(DEC_CMD, tglInstruction.arg1));
                        } else if (DEC_CMD.equals(tglInstruction.cmd)) {
                            instructions.set(tglIdx, new Instruction(INC_CMD, tglInstruction.arg1));
                        } else if (CPY_CMD.equals(tglInstruction.cmd)) {
                            instructions.set(tglIdx, new Instruction(JNZ_CMD, tglInstruction.arg1, tglInstruction.arg2));
                        } else if (JNZ_CMD.equals(tglInstruction.cmd)) {
                            instructions.set(tglIdx, new Instruction(CPY_CMD, tglInstruction.arg1, tglInstruction.arg2));
                        } else if (TGL_CMD.equals(tglInstruction.cmd)) {
                            instructions.set(tglIdx, new Instruction(INC_CMD, tglInstruction.arg1));
                        } else {
                            throw new IllegalArgumentException("Unknown operation for toggle: " + tglInstruction.cmd);
                        }
                    }
                }

                env.pc++;
            } else {
                throw new IllegalArgumentException("Unknown command: " + instruction.cmd);
            }
        }
    }

    private static boolean isRegisterName(String arg) {
        return arg.length() == 1 && Character.isLetter(arg.charAt(0));
    }

    private static class Transmitter {

        List<Long> signals = new ArrayList<Long>();
        long prevSignal = 1;
        int n;

        void transmit(long signal) {
            signals.add(signal);
            if ((signal == 0 || signal == 1) && prevSignal != signal) {
                prevSignal = signal;
                n++;
            } else {
                n = -1;
            }
        }

        boolean enough() {
            return n == -1 || transmitted();
        }

        boolean transmitted() {
            return n == 10;
        }
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

        private String cmd;
        private String arg1;
        private String arg2;

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

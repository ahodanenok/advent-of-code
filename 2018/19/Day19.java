import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2018/day/19
 */
public class Day19 {

    public static void main(String[] args) {
        Program prg = getProgram();
        part1(prg);
        part2();
    }

    private static void part1(Program prg) {
        Context ctx = new Context();
        int pcReg = prg.pcReg;
        while (ctx.get(pcReg) < prg.statements.size()) {
            Statement stmt = prg.statements.get(ctx.get(pcReg));
            stmt.instruction.execute(stmt.arg1, stmt.arg2, stmt.arg3, ctx);
            ctx.set(pcReg, ctx.get(pcReg) + 1);
        }

        System.out.println(ctx.get(0));
    }

    private static void part2() {
        // sum of all factors of r1

        int r0 = 0;
        int r1 = 10551260;

        for (int r3 = 1; r3 * r3 <= r1; r3++) {
            if (r1 % r3 == 0) {
                r0 += r3;
                r0 += r1 / r3;
            }
        }

        System.out.println(r0);
    }

    private static class Program {

        private int pcReg;
        private List<Statement> statements = new ArrayList<Statement>();
    }

    private static class Statement {

        private Instruction instruction;
        private int arg1;
        private int arg2;
        private int arg3;

        Statement(Instruction instruction, int arg1, int arg2, int arg3) {
            this.instruction = instruction;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }
    }

    private static Program getProgram() {
        Program prg = new Program();

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        prg.pcReg = Integer.parseInt(line.substring(4).trim());

        while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            prg.statements.add(new Statement(
                Instruction.byName(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim())));
        }

        return prg;
    }

    private enum Instruction {

        ADDR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a) && context.isValid(b)) {
                    context.set(c, context.get(a) + context.get(b));
                }
            }
        },
        ADDI {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a) + b);
                }
            }
        },
        MULR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a) && context.isValid(b)) {
                    context.set(c, context.get(a) * context.get(b));
                }
            }
        },
        MULI {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a) * b);
                }
            }
        },
        BANR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a) && context.isValid(b)) {
                    context.set(c, context.get(a) & context.get(b));
                }
            }
        },
        BANI {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a) & b);
                }
            }
        },
        BORR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a) && context.isValid(b)) {
                    context.set(c, context.get(a) | context.get(b));
                }
            }
        },
        BORI {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a) | b);
                }
            }
        },
        SETR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a));
                }
            }
        },
        SETI {
            @Override
            void execute(int a, int b, int c, Context context) {
                context.set(c, a);
            }
        },
        GTIR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(b)) {
                    context.set(c, a > context.get(b) ? 1 : 0);
                }
            }
        },
        GTRI {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a) > b ? 1 : 0);
                }
            }
        },
        GTRR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a) && context.isValid(b)) {
                    context.set(c, context.get(a) > context.get(b) ? 1 : 0);
                }
            }
        },
        EQIR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(b)) {
                    context.set(c, a == context.get(b) ? 1 : 0);
                }
            }
        },
        EQRI {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a)) {
                    context.set(c, context.get(a) == b ? 1 : 0);
                }
            }
        },
        EQRR {
            @Override
            void execute(int a, int b, int c, Context context) {
                if (context.isValid(a) && context.isValid(b)) {
                    context.set(c, context.get(a) == context.get(b) ? 1 : 0);
                }
            }
        };

        abstract void execute(int a, int b, int c, Context context);

        static Instruction byName(String name) {
            for (Instruction instruction : values()) {
                if (instruction.name().toLowerCase().equals(name)) {
                    return instruction;
                }
            }

            throw new IllegalStateException();
        }
    }

    private static class Context {

        private int[] registers;

        Context() {
            this.registers = new int[6];
        }

        void set(int register, int value) {
            registers[(int) register] = value;
        }

        int get(int register) {
            return registers[(int) register];
        }

        boolean isValid(int register) {
            return register >= 0 && register < registers.length;
        }
    }
}

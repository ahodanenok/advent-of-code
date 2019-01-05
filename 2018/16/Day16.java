import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2018/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        List<Sample> samples = getSamples();
        part1(samples);
    }

    private static void part1(List<Sample> samples) {
        int threeOrMoreCount = 0;
        for (Sample sample : samples) {
            int matchesCount = 0;
            for (Instruction inst : Instruction.values()) {
                Context before = sample.before.copy();
                inst.execute(sample.arg1, sample.arg2, sample.arg3, before);
                if (before.equals(sample.after)) {
                    matchesCount++;
                }
            }

            if (matchesCount >= 3) {
                threeOrMoreCount++;
            }
        }

        System.out.println(threeOrMoreCount);
    }

    private static List<Sample> getSamples() {
        List<Sample> samples = new ArrayList<Sample>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String beforeDef = scanner.nextLine().trim();
            String instDef = scanner.nextLine().trim();
            String afterDef = scanner.nextLine().trim();
            String[] parts;

            Sample sample = new Sample();

            parts = instDef.split("\\s+");
            sample.command = Integer.parseInt(parts[0].trim());
            sample.arg1 = Integer.parseInt(parts[1].trim());
            sample.arg2 = Integer.parseInt(parts[2].trim());
            sample.arg3 = Integer.parseInt(parts[3].trim());

            parts = beforeDef.substring(9, beforeDef.length() - 1).split(",");
            sample.before = new Context(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()));

            parts = afterDef.substring(9, afterDef.length() - 1).split(",");
            sample.after = new Context(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()));

            samples.add(sample);

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }

        return samples;
    }

    private static class Sample {

        private Context before;
        private Context after;
        private int command;
        private int arg1;
        private int arg2;
        private int arg3;
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
    }

    private static class Context {

        private int[] registers;

        Context(int a, int b, int c, int d) {
            this.registers = new int[] { a, b, c, d };
        }

        void set(int register, int value) {
            registers[register] = value;
        }

        int get(int register) {
            return registers[register];
        }
        
        boolean isValid(int register) {
            return register >= 0 && register < registers.length;
        }

        Context copy() {
            return new Context(registers[0], registers[1], registers[2], registers[3]);
        }

        @Override
        public boolean equals(Object obj) {
            Context other = (Context) obj;
            return Arrays.equals(registers, other.registers);
        }
    }
}

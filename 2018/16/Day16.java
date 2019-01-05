import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2018/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        List<Sample> samples = getSamples();
        part1(samples);
        part2(samples);
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

    private static void part2(List<Sample> samples) {
        Map<Instruction, Integer> instructionOpcodes = new HashMap<Instruction, Integer>();
        while (instructionOpcodes.size() < Instruction.values().length) {
            for (int i = 0; i < samples.size(); i++) {
                Sample sample = samples.get(i);
                List<Instruction> matches = new ArrayList<Instruction>();
                for (Instruction inst : Instruction.values()) {
                    Context before = sample.before.copy();
                    inst.execute(sample.arg1, sample.arg2, sample.arg3, before);
                    if (before.equals(sample.after) && !instructionOpcodes.containsKey(inst)) {
                        matches.add(inst);
                    }
                }

                if (matches.size() == 1) {
                    instructionOpcodes.put(matches.get(0), sample.command);
                }
            }
        }

        Context context = new Context(0, 0, 0, 0);
        for (Statement stmt : getProgram()) {
            for (Map.Entry<Instruction, Integer> entry : instructionOpcodes.entrySet()) {
                if (entry.getValue() == stmt.command) {
                    entry.getKey().execute(stmt.arg1, stmt.arg2, stmt.arg3, context);
                }
            }
        }

        System.out.println(context.get(0));
    }

    private static List<Sample> getSamples() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("samples.txt"), Charset.forName("utf-8"));
        } catch (Exception e) {
            lines = Collections.emptyList();
        }

        List<Sample> samples = new ArrayList<Sample>();
        for (int i = 0; i < lines.size(); i += 4) {
            String beforeDef = lines.get(i).trim();
            String instDef = lines.get(i + 1).trim();
            String afterDef = lines.get(i + 2).trim();
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

    private static List<Statement> getProgram() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("prg.txt"), Charset.forName("utf-8"));
        } catch (Exception e) {
            lines = Collections.emptyList();
        }

        List<Statement> statements = new ArrayList<Statement>();
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            statements.add(new Statement(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim())));
        }

        return statements;
    }

    private static class Statement {

        private final int command;
        private final int arg1;
        private final int arg2;
        private final int arg3;

        Statement(int command, int arg1, int arg2, int arg3) {
            this.command = command;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }
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

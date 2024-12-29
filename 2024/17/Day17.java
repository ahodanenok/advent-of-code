import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2024/day/17
 */
public class Day17 {

    public static void main(String... args) throws Exception {
        Computer computer = getInput();
        part1(computer);
        part2(computer);
    }

    private static void part1(Computer computer) {
        StringJoiner output = new StringJoiner(",");
        simulate(computer, output::add);
        System.out.println("Part 1: " + output);
    }

    private static void part2(Computer computer) {
        /*
            2,4 --> bst 4 --> registerB = registerA % 8
            1,1 --> bxl 1 --> registerB = registerB ^ 1
            7,5 --> cdv 5 --> registerC = registerA / pow(2, registerB)
            1,5 --> bxl 5 --> registerB = registerB ^ 5
            4,0 --> bxc 0 --> registerB = registerB ^ registerC
            0,3 --> adv 3 --> registerA = registerA / pow(2, 3)
            5,5 --> out 5 --> print registerB
            3,0 --> jnz 0 --> goto start if registerA != 0
        */
        long a = findA(computer, computer.program.size() - 1, 0);
        System.out.println("Part 2: " + a);
    }

    private static long findA(Computer computer, int pos, long a) {
        if (pos < 0) {
            return a;
        }

        for (long d = 0; d < 8; d++) {
            long na = a | (d << (pos * 3));

            List<String> output = new ArrayList<>();
            simulate(new Computer(na | ((1L << (pos * 3)) - 1), 0, 0, computer.program), output::add);
            if (output.size() > pos  && Integer.valueOf(output.get(pos)) == computer.program.get(pos)) {
                long ra = findA(computer, pos - 1, na);
                if (ra != -1) {
                    return ra;
                }
            }
        }

        return -1;
    }

    private static void simulate(Computer computer, java.util.function.Consumer<String> output) {
        long registerA = computer.registerA;
        long registerB = computer.registerB;
        long registerC = computer.registerC;
        int pc = 0;
        while (pc < computer.program.size()) {
            int arg = computer.program.get(pc + 1);
            switch (computer.program.get(pc)) {
                case 0 /* adv */ -> {
                    registerA = registerA
                        / (long) Math.pow(2, comboOperand(arg, registerA, registerB, registerC));
                    pc += 2;
                }
                case 1 /* bxl */ -> {
                    registerB = registerB ^ arg;
                    pc += 2;
                }
                case 2 /* bst */ -> {
                    registerB = comboOperand(arg, registerA, registerB, registerC) % 8;
                    pc += 2;
                }
                case 3 /* jnz */ -> {
                    if (registerA != 0) {
                        pc = arg;
                    } else {
                        pc += 2;
                    }
                }
                case 4 /* bxc */ -> {
                    registerB = registerB ^ registerC;
                    pc += 2;
                }
                case 5 /* out */ -> {
                    output.accept("" + comboOperand(arg, registerA, registerB, registerC) % 8);
                    pc += 2;
                }
                case 6 /* bdv */ -> {
                    registerB = registerA
                        / (long) Math.pow(2, comboOperand(arg, registerA, registerB, registerC));
                    pc += 2;
                }
                case 7 /* cdv */ -> {
                    registerC = registerA
                        / (long) Math.pow(2, comboOperand(arg, registerA, registerB, registerC));
                    pc += 2;
                }
                default -> throw new IllegalStateException();                
            }
        }
    }

    private static long comboOperand(int value, long registerA, long registerB, long registerC) {
        return switch (value) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> registerA;
            case 5 -> registerB;
            case 6 -> registerC;
            case 7 -> throw new IllegalStateException();
            default -> throw new IllegalStateException();
        };
    }

    private static Computer getInput() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));

        Pattern numberPattern = Pattern.compile("\\d+");
        Matcher matcher;

        matcher = numberPattern.matcher(lines.get(0));
        matcher.find();
        long registerA = Long.parseLong(matcher.group(0));

        matcher = numberPattern.matcher(lines.get(1));
        matcher.find();
        long registerB = Long.parseLong(matcher.group(0));

        matcher = numberPattern.matcher(lines.get(2));
        matcher.find();
        long registerC = Long.parseLong(matcher.group(0));

        List<Integer> program = new ArrayList<>();
        matcher = numberPattern.matcher(lines.get(4));
        while (matcher.find()) {
            program.add(Integer.parseInt(matcher.group(0)));
        }

        return new Computer(registerA, registerB, registerC, program);
    }

    private static class Computer {

        final long registerA;
        final long registerB;
        final long registerC;
        final List<Integer> program;

        Computer(long registerA, long registerB, long registerC, List<Integer> program) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = program;
        }
    }
}

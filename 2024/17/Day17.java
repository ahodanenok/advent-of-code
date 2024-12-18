import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2024/day/17
 */
public class Day17 {

    public static void main(String... args) throws Exception {
        Computer computer = getInput();
        part1(computer);
    }

    private static void part1(Computer computer) {
        StringJoiner output = new StringJoiner(",");
        simulate(computer, output);

        System.out.println("Part 1: " + output);
    }

    private static void simulate(Computer computer, StringJoiner output) {
        int registerA = computer.registerA;
        int registerB = computer.registerB;
        int registerC = computer.registerC;
        int pc = 0;
        while (pc < computer.program.size()) {
            int arg = computer.program.get(pc + 1);
            switch (computer.program.get(pc)) {
                case 0 /* adv */ -> {
                    registerA = registerA
                        / (int) Math.pow(2, comboOperand(arg, registerA, registerB, registerC));
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
                    output.add("" + comboOperand(arg, registerA, registerB, registerC) % 8);
                    pc += 2;
                }
                case 6 /* bdv */ -> {
                    registerB = registerA
                        / (int) Math.pow(2, comboOperand(arg, registerA, registerB, registerC));
                    pc += 2;
                }
                case 7 /* cdv */ -> {
                    registerC = registerA
                        / (int) Math.pow(2, comboOperand(arg, registerA, registerB, registerC));
                    pc += 2;
                }
                default -> throw new IllegalStateException();                
            }
        }
    }

    private static int comboOperand(int value, int registerA, int registerB, int registerC) {
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
        int registerA = Integer.parseInt(matcher.group(0));

        matcher = numberPattern.matcher(lines.get(1));
        matcher.find();
        int registerB = Integer.parseInt(matcher.group(0));

        matcher = numberPattern.matcher(lines.get(2));
        matcher.find();
        int registerC = Integer.parseInt(matcher.group(0));

        List<Integer> program = new ArrayList<>();
        matcher = numberPattern.matcher(lines.get(4));
        while (matcher.find()) {
            program.add(Integer.parseInt(matcher.group(0)));
        }

        return new Computer(registerA, registerB, registerC, program);
    }

    private static class Computer {

        final int registerA;
        final int registerB;
        final int registerC;
        final List<Integer> program;

        Computer(int registerA, int registerB, int registerC, List<Integer> program) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.program = program;
        }
    }
}

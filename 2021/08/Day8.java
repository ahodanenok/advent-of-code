import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2021/day/8
 */
public class Day8 {

    private static final int A_MASK = 0x40;
    private static final int B_MASK = 0x20;
    private static final int C_MASK = 0x10;
    private static final int D_MASK = 0x8;
    private static final int E_MASK = 0x4;
    private static final int F_MASK = 0x2;
    private static final int G_MASK = 0x1;

    private static final List<Integer> DIGIT_MASKS = Arrays.asList(
        A_MASK | B_MASK | C_MASK | E_MASK | F_MASK | G_MASK,          // 0
        C_MASK | F_MASK,                                              // 1
        A_MASK | C_MASK | D_MASK | E_MASK | G_MASK,                   // 2
        A_MASK | C_MASK | D_MASK | F_MASK | G_MASK,                   // 3
        B_MASK | C_MASK | D_MASK | F_MASK,                            // 4
        A_MASK | B_MASK | D_MASK | F_MASK | G_MASK,                   // 5
        A_MASK | B_MASK | D_MASK | E_MASK | F_MASK | G_MASK,          // 6
        A_MASK | C_MASK | F_MASK,                                     // 7
        A_MASK | B_MASK | C_MASK | D_MASK | E_MASK | F_MASK | G_MASK, // 8
        A_MASK | B_MASK | C_MASK | D_MASK | F_MASK | G_MASK           // 9
    );

    public static void main(String[] args) throws Exception {
        List<Signal> signals = getInput();
        part1(signals);
        part2(signals);
    }

    private static List<Signal> getInput() throws Exception {
        List<Signal> signals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\|");
                signals.add(new Signal(parts[0].trim().split(" "), parts[1].trim().split(" ")));
            }
        }

        return signals;
    }

    private static void part1(List<Signal> signals) {
        int count = 0;
        for (Signal s : signals) {
            for (String d : s.output) {
                if (d.length() == 2
                      || d.length() == 4
                      || d.length() == 3
                      || d.length() == 7) {
                    count++;
                }
            }
        }

        System.out.println("Part 1: " + count);
    }

    private static void part2(List<Signal> signals) {
        int sum = 0;
        for (Signal s : signals) {
            String d1 = deduce(s.patterns, "", 2);
            String d4 = deduce(s.patterns, "", 4);
            String d7 = deduce(s.patterns, "", 3);

            String a = deduce(s.patterns, d1, 1);
            String g = deduce(s.patterns, d4 + d7, 1);
            String e = deduce(s.patterns, d4 + d7 + g, 1);
            String d = deduce(s.patterns, d7 + g, 1);
            String c = deduce(s.patterns, a + g + d + e, 1);
            String f = deduce(s.patterns, c, 1);
            String b = deduce(s.patterns, a + c + d + e + f + g, 1);

            StringBuilder num = new StringBuilder();
            for (String out : s.output) {
                int mask = 0;
                for (int i = 0; i < out.length(); i++) {
                    String segment = out.charAt(i) + "";
                    if (segment.equals(a)) {
                        mask |= A_MASK;
                    } else if (segment.equals(b)) {
                        mask |= B_MASK;
                    } else if (segment.equals(c)) {
                        mask |= C_MASK;
                    } else if (segment.equals(d)) {
                        mask |= D_MASK;
                    } else if (segment.equals(e)) {
                        mask |= E_MASK;
                    } else if (segment.equals(f)) {
                        mask |= F_MASK;
                    } else if (segment.equals(g)) {
                        mask |= G_MASK;
                    } else {
                        throw new IllegalStateException("Unknown segment: " + segment);
                    }
                }

                int digit = DIGIT_MASKS.indexOf(mask);
                if (digit == -1) {
                    throw new IllegalStateException("Digit not found for '" + out + "' (mask: " + mask + ")");
                }

                num.append(digit);
            }

            sum += Integer.parseInt(num.toString());
        }

        System.out.println("Part 2: " + sum);
    }

    private static String deduce(String[] patterns, String exclude, int threshold) {
        List<String> matches = new ArrayList<>();
        for (String p : patterns) {
            StringBuilder sb = new StringBuilder(p);
            for (int i = sb.length() - 1; i >= 0; i--) {
                if (exclude.indexOf(sb.charAt(i)) != -1) {
                    sb.deleteCharAt(i);
                }
            }

            if (sb.length() == threshold && !matches.contains(sb.toString())) {
                matches.add(sb.toString());
            }
        }

        if (matches.size() == 1) {
            return matches.get(0);
        } else if (matches.isEmpty()) {
            throw new IllegalStateException("Can't deduce segments: no matches");
        } else {
            throw new IllegalStateException("Can't deduce segments: multiple matches " + matches);
        }
    }

    private static class Signal {

        final String[] patterns;
        final String[] output;

        Signal(String[] patterns, String[] output) {
            this.patterns = patterns;
            this.output = output;
        }
    }
}

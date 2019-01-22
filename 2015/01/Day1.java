import java.util.Scanner;

public class Day1 {

    private static final char UP = '(';
    private static final char DOWN = ')';

    public static void main(String[] args) {
        String steps = getSteps();
        part1(steps);
        part2(steps);
    }

    private static void part1(String steps) {
        int floor = 0;
        for (int i = 0; i < steps.length(); i++) {
            char step = steps.charAt(i);
            if (step == UP) {
                floor++;
            } else if (step == DOWN) {
                floor--;
            } else {
                throw new IllegalArgumentException("Unknown step: " + step);
            }
        }

        System.out.println(floor);
    }

    private static void part2(String steps) {
        int floor = 0;
        int i = 0;
        while (i < steps.length() && floor >= 0) {
            char step = steps.charAt(i);
            if (step == UP) {
                floor++;
            } else if (step == DOWN) {
                floor--;
            } else {
                throw new IllegalArgumentException("Unknown step: " + step);
            }

            i++;
        }

        System.out.println(i);
    }

    private static String getSteps() {
        return new Scanner(System.in).nextLine();
    }
}

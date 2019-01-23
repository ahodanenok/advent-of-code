import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2015/day/2
 */
public class Day2 {

    public static void main(String[] args) throws Exception {
        List<Box> boxes = getBoxes();
        part1(boxes);
        part2(boxes);
    }

    private static void part1(List<Box> boxes) {
        int paperNeeded = 0;
        for (Box box : boxes) {
            paperNeeded += box.getTotalSquare();
            paperNeeded += Math.min(
                    Math.min(box.getTopSquare(), box.getSideSquare()),
                    box.getFrontSquare());
        }

        System.out.println(paperNeeded);
    }

    private static void part2(List<Box> boxes) {
        int ribbonNeeded = 0;
        for (Box box : boxes) {
            ribbonNeeded += Math.min(
                    Math.min(box.getTopPerimeter(), box.getSidePerimeter()),
                    box.getFrontPerimeter());
            ribbonNeeded += box.getVolume();
        }

        System.out.println(ribbonNeeded);
    }

    private static List<Box> getBoxes() {
        List<Box> boxes = new ArrayList<Box>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("x");

            boxes.add(new Box(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])));
        }

        return boxes;
    }

    private static class Box {

        private int width;
        private int height;
        private int length;

        Box(int length, int width, int height) {
            this.length = length;
            this.width = width;
            this.height = height;
        }

        int getFrontSquare() {
            return height * width;
        }

        int getTopSquare() {
            return width * length;
        }

        int getSideSquare() {
            return height * length;
        }

        int getTotalSquare() {
            return 2 * getFrontSquare() + 2 * getTopSquare() + 2 * getSideSquare();
        }

        int getFrontPerimeter() {
            return 2 * (width + height);
        }

        int getTopPerimeter() {
            return 2 * (width + length);
        }

        int getSidePerimeter() {
            return 2 * (height + length);
        }

        int getVolume() {
            return width * height * length;
        }
    }
}

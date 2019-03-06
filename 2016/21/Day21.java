import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2016/day/21
 */
public class Day21 {

    public static void main(String[] args) {
        List<Operation> operations = getOperations();
        part1("abcdefgh", operations);
    }

    private static void part1(String msg, List<Operation> operations) {
        StringBuilder result = new StringBuilder(msg);
        for (Operation op : operations) {
            op.scramble(result);
        }

        System.out.println(result.toString());
    }

    private static List<Operation> getOperations() {
        List<Operation> operations = new ArrayList<Operation>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            if ("swap".equals(parts[0])) {
                if ("position".equals(parts[1])) {
                    operations.add(new SwapPositionOperation(Integer.parseInt(parts[2]), Integer.parseInt(parts[5])));
                } else if ("letter".equals(parts[1])) {
                    operations.add(new SwapLetterOperation(parts[2], parts[5]));
                } else {
                    throw new IllegalArgumentException(parts[1]);
                }
            } else if ("rotate".equals(parts[0])) {
                if ("left".equals(parts[1])) {
                    operations.add(new RotateLeftOperation(Integer.parseInt(parts[2])));
                } else if ("right".equals(parts[1])) {
                    operations.add(new RotateRightOperation(Integer.parseInt(parts[2])));
                } else if ("based".equals(parts[1])) {
                    operations.add(new RotatePositionalOperation(parts[6]));
                } else {
                    throw new IllegalArgumentException(parts[1]);
                }
            } else if ("reverse".equals(parts[0])) {
                operations.add(new ReverseOperation(Integer.parseInt(parts[2]), Integer.parseInt(parts[4])));
            } else if ("move".equals(parts[0])) {
                operations.add(new MoveOperation(Integer.parseInt(parts[2]), Integer.parseInt(parts[5])));
            } else {
                throw new IllegalArgumentException(parts[0]);
            }
        }

        return operations;
    }

    private interface Operation {
        void scramble(StringBuilder str);
    }

    private static class SwapPositionOperation implements Operation {

        private final int x;
        private final int y;

        SwapPositionOperation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void scramble(StringBuilder str) {
            char tmp = str.charAt(x);
            str.setCharAt(x, str.charAt(y));
            str.setCharAt(y, tmp);
        }
    }

    private static class SwapLetterOperation implements Operation {

        private final String x;
        private final String y;

        SwapLetterOperation(String x, String y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void scramble(StringBuilder str) {
            int xPos = str.indexOf(x);
            int yPos = str.indexOf(y);
            new SwapPositionOperation(xPos, yPos).scramble(str);
        }
    }

    private static class RotateLeftOperation implements Operation {

        private final int x;

        RotateLeftOperation(int x) {
            this.x = x;
        }

        @Override
        public void scramble(StringBuilder str) {
            int offset = x % str.length();
            if (offset == 0) {
                return;
            }

            String moved = str.substring(0, offset);
            str.delete(0, offset);
            str.append(moved);
        }
    }

    private static class RotateRightOperation implements Operation {

        private final int x;

        RotateRightOperation(int x) {
            this.x = x;
        }

        @Override
        public void scramble(StringBuilder str) {
            int offset = x % str.length();
            if (offset == 0) {
                return;
            }

            String moved = str.substring(str.length() - offset, str.length());
            str.delete(str.length() - offset, str.length());
            str.insert(0, moved);
        }
    }

    private static class RotatePositionalOperation implements Operation {

        private final String x;

        RotatePositionalOperation(String x) {
            this.x = x;
        }

        @Override
        public void scramble(StringBuilder str) {
            new RotateRightOperation(getOffset(str)).scramble(str);
        }

        private int getOffset(StringBuilder str) {
            int idx = str.indexOf(x);
            int offset = idx + 1;
            if (idx >= 4) {
                offset++;
            }

            return offset;
        }
    }

    private static class ReverseOperation implements Operation {

        private final int x;
        private final int y;

        ReverseOperation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void scramble(StringBuilder str) {
            StringBuilder substr = new StringBuilder(str.substring(x, y + 1)).reverse();
            str.delete(x, y + 1);
            str.insert(x, substr);
        }
    }

    private static class MoveOperation implements Operation {

        private final int x;
        private final int y;

        MoveOperation(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void scramble(StringBuilder str) {
            char ch = str.charAt(x);
            str.deleteCharAt(x);
            str.insert(y, ch);
        }
    }
}

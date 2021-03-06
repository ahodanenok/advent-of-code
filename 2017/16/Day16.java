import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2017/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        List<Move> moves = getMoves();
        System.out.println(dance("abcdefghijklmnop", moves, 1));
        System.out.println(dance("abcdefghijklmnop", moves, 1_000_000_000));
    }

    private static String dance(String initialLine, List<Move> moves, int size) {
        StringBuilder line = new StringBuilder(initialLine);
        for (int i = 0; i < size; i++) {
            for (Move move : moves) {
                if ("s".equals(move.cmd)) {
                    int x = Integer.parseInt(move.arg1);
                    String tmp = line.substring(line.length() - x);
                    line.delete(line.length() - x, line.length());
                    line.insert(0, tmp);
                } else if ("x".equals(move.cmd)) {
                    int a = Integer.parseInt(move.arg1);
                    int b = Integer.parseInt(move.arg2);
                    char tmp = line.charAt(a);
                    line.setCharAt(a, line.charAt(b));
                    line.setCharAt(b, tmp);
                } else if ("p".equals(move.cmd)) {
                    int a = line.indexOf(move.arg1);
                    int b = line.indexOf(move.arg2);
                    char tmp = line.charAt(a);
                    line.setCharAt(a, line.charAt(b));
                    line.setCharAt(b, tmp);
                } else {
                    throw new IllegalStateException("Unknown move: " + move.cmd);
                }
            }

            // skip repeating moves
            if (line.toString().equals(initialLine)) {
                i = size - (size % (i + 1)) - 1;
            }
        }

        return line.toString();
    }

    private static List<Move> getMoves() {
        List<Move> commands = new ArrayList<Move>();

        String[] strings = new Scanner(System.in).nextLine().split(",");
        for (String str : strings) {
            Move m = new Move();
            m.cmd = "" + str.charAt(0);
            String[] args = str.substring(1).split("/");
            m.arg1 = args[0];
            if (args.length == 2) {
                m.arg2 = args[1];
            }

            commands.add(m);
        }

        return commands;
    }

    private static class Move {

        private String cmd;
        private String arg1;
        private String arg2;
    }
}

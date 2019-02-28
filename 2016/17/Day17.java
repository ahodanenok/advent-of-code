import java.util.LinkedList;
import java.security.MessageDigest;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2016/day/17
 */
public class Day17 {

    private static final String UP = "U";
    private static final String DOWN = "D";
    private static final String LEFT = "L";
    private static final String RIGHT = "R";

    private static final MessageDigest MD5;
    static {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MD5 = md5;
    }

    public static void main(String[] args) {
        part1("yjjvjgan");
        part2("yjjvjgan");
    }

    private static void part1(String hash) {
        LinkedList<State> queue = new LinkedList<State>();
        queue.add(new State(0, 0, ""));

        String path = null;
        while (queue.size() > 0 && path == null) {
            State state = queue.poll();
            if (state.row == 3 && state.col == 3) {
                path = state.path;
            }

            String pathHash = getMD5(hash + state.path);

            // up
            if (state.row > 0 && isOpen(pathHash.charAt(0))) {
                queue.add(new State(state.row - 1, state.col, state.path + UP));
            }

            // down
            if (state.row < 3 && isOpen(pathHash.charAt(1))) {
                queue.add(new State(state.row + 1, state.col, state.path + DOWN));
            }

            // left
            if (state.col > 0 && isOpen(pathHash.charAt(2))) {
                queue.add(new State(state.row, state.col - 1, state.path + LEFT));
            }

            // right
            if (state.col < 3 && isOpen(pathHash.charAt(3))) {
                queue.add(new State(state.row, state.col + 1, state.path + RIGHT));
            }
        }

        System.out.println(path);
    }

    private static void part2(String hash) {
        System.out.println(findLongestPath(new State(0, 0, ""), hash));
    }

    private static int findLongestPath(State state, String hash) {
        if (state.row == 3 && state.col == 3) {
            return state.path.length();
        }

        int maxSteps = Integer.MIN_VALUE;
        String pathHash = getMD5(hash + state.path);

        // up
        if (state.row > 0 && isOpen(pathHash.charAt(0))) {
            maxSteps = Math.max(maxSteps, findLongestPath(new State(state.row - 1, state.col, state.path + UP), hash));
        }

        // down
        if (state.row < 3 && isOpen(pathHash.charAt(1))) {
            maxSteps = Math.max(maxSteps, findLongestPath(new State(state.row + 1, state.col, state.path + DOWN), hash));
        }

        // left
        if (state.col > 0 && isOpen(pathHash.charAt(2))) {
            maxSteps = Math.max(maxSteps, findLongestPath(new State(state.row, state.col - 1, state.path + LEFT), hash));
        }

        // right
        if (state.col < 3 && isOpen(pathHash.charAt(3))) {
            maxSteps = Math.max(maxSteps, findLongestPath(new State(state.row, state.col + 1, state.path + RIGHT), hash));
        }

        return maxSteps;
    }

    private static boolean isOpen(char doorState) {
        return doorState == 'b'
                || doorState == 'c'
                || doorState == 'd'
                || doorState == 'e'
                || doorState == 'f';
    }

    private static String getMD5(String str) {
        byte[] digest = null;
        try {
            digest = MD5.digest(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (digest == null) {
            return null;
        }

        String hash = "";
        for (int i = 0; i < digest.length; i++) {
            hash += Integer.toString((digest[i] & 0xF0) >> 4, 16); // hi
            hash += Integer.toString(digest[i] & 0x0F, 16);        // lo
        }

        while (hash.length() < 32) {
            hash = "0" + hash;
        }

        return hash;
    }

    private static class State {

        private int row;
        private int col;
        private String path;

        State(int row, int col, String path) {
            this.row = row;
            this.col = col;
            this.path = path;
        }
    }
}

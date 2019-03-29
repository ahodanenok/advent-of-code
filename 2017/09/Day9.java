import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2017/day/9
 */
public class Day9 {

    public static void main(String[] args) {
        State state = getState();
        traverseGroups(state, 0);
        System.out.println(state.totalScore);
        System.out.println(state.garbageCount);
    }

    private static State getState() {
        return new State(new Scanner(System.in).nextLine());
    }

    private static void traverseGroups(State state, int parentGroupScore) {
        int currentGroupScore = parentGroupScore + 1;
        state.totalScore += currentGroupScore;

        state.pos++; // skip '{'

        boolean garbage = false;
        while (true) {
            char ch = state.data.charAt(state.pos);
            if (ch == '!') {
                state.pos += 2;
            } else if (!garbage && ch == '<') {
                garbage = true;
                state.pos++;
            } else if (garbage && ch == '>') {
                garbage = false;
                state.pos++;
            } else if (!garbage && ch == '{') {
                traverseGroups(state, currentGroupScore);
            } else if (!garbage && ch == '}') {
                state.pos++;
                break;
            } else {
                state.pos++;
                if (garbage) {
                    state.garbageCount++;
                }
            }
        }
    }

    private static class State {

        private String data;
        private int pos;

        private int totalScore;
        private int garbageCount;

        State(String data) {
            this.data = data;
        }
    }
}

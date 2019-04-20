/**
 * Advent of Code - Day 25
 * https://adventofcode.com/2017/day/25
 */
public class Day25 {

    public static void main(String[] args) {
        Machine machine = new Machine(State.A, new Tape());
        for (int i = 0; i < 12261543; i++) machine.step();
        System.out.println(machine.checksum());
    }

    private static class Machine {

        private State currentState;
        private Tape tape;

        Machine(State initialState, Tape tape) {
            this.currentState = initialState;
            this.tape = tape;
        }

        void step() {
            if (currentState == State.A) {
                int value = tape.read();
                if (value == 0) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.B;
                } else if (value == 1) {
                    tape.write(0);
                    tape.moveLeft();
                    currentState = State.C;
                } else {
                    throw new IllegalStateException("Unknown value: " + value);
                }
            } else if (currentState == State.B) {
                int value = tape.read();
                if (value == 0) {
                    tape.write(1);
                    tape.moveLeft();
                    currentState = State.A;
                } else if (value == 1) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.C;
                } else {
                    throw new IllegalStateException("Unknown value: " + value);
                }
            } else if (currentState == State.C) {
                int value = tape.read();
                if (value == 0) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.A;
                } else if (value == 1) {
                    tape.write(0);
                    tape.moveLeft();
                    currentState = State.D;
                } else {
                    throw new IllegalStateException("Unknown value: " + value);
                }
            } else if (currentState == State.D) {
                int value = tape.read();
                if (value == 0) {
                    tape.write(1);
                    tape.moveLeft();
                    currentState = State.E;
                } else if (value == 1) {
                    tape.write(1);
                    tape.moveLeft();
                    currentState = State.C;
                } else {
                    throw new IllegalStateException("Unknown value: " + value);
                }
            } else if (currentState == State.E) {
                int value = tape.read();
                if (value == 0) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.F;
                } else if (value == 1) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.A;
                } else {
                    throw new IllegalStateException("Unknown value: " + value);
                }
            } else if (currentState == State.F) {
                int value = tape.read();
                if (value == 0) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.A;
                } else if (value == 1) {
                    tape.write(1);
                    tape.moveRight();
                    currentState = State.E;
                } else {
                    throw new IllegalStateException("Unknown value: " + value);
                }
            } else {
                throw new IllegalStateException("Unknown state: " + currentState);
            }
        }

        int checksum() {
            int count = 0;
            for (int cell : tape.tape) {
                if (cell == 1) {
                    count++;
                }
            }

            return count;
        }
    }

    private static class Tape {

        private int[] tape;
        private int pos;

        Tape() {
            this.tape = new int[1_000_000];
            pos = tape.length / 2;
        }

        void moveLeft() {
            pos--;
        }

        void moveRight() {
            pos++;
        }

        int read() {
            return tape[pos];
        }

        void write(int value) {
            tape[pos] = value;
        }
    }

    private enum State {

        A, B, C, D, E, F;
    }
}

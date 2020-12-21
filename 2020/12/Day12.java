import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class Day12 {

    public static void main(String[] args) throws Exception {
        List<Command> commands = getCommands();

        State state = new State();
        state.point = new Point(0, 0);
        state.direction = Direction.E;
        for (Command cmd : commands) {
            cmd.apply(state);
        }
System.out.printf("%s (%d, %d)%n", state.direction, state.point.x, state.point.y);
        int distance = (int) (Math.abs(state.point.x) + Math.abs(state.point.y));
        System.out.println("Part 1: " + distance);
    }

    private static List<Command> getCommands() throws Exception {
        List<Command> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                char action = line.charAt(0);
                int value = Integer.parseInt(line.substring(1));
                if (action == 'N') {
                    commands.add(s -> s.point = Direction.N.move(s.point, value));
                } else if (action == 'S') {
                    commands.add(s -> s.point = Direction.S.move(s.point, value));
                } else if (action == 'E') {
                    commands.add(s -> s.point = Direction.E.move(s.point, value));
                } else if (action == 'W') {
                    commands.add(s -> s.point = Direction.W.move(s.point, value));
                } else if (action == 'L') {
                    int times = value / 90;
                    commands.add(s -> IntStream.range(0, times).forEach(__ -> s.direction = s.direction.left()));
                } else if (action == 'R') {
                    int times = value / 90;
                    commands.add(s -> IntStream.range(0, times).forEach(__ -> s.direction = s.direction.right()));
                } else if (action == 'F') {
                    commands.add(s -> s.point = s.direction.move(s.point, value));
                } else {
                    throw new IllegalStateException(line);
                }
            }
        }

        return commands;
    }

    private interface Command {
        void apply(State state);
    }

    private static class State {
        Point point;
        Direction direction;
    }

    private enum Direction {
        N(0, -1) {
            @Override
            Direction left() {
                return W;
            }

            @Override
            Direction right() {
                return E;
            }
        },
        S(0, 1) {
            @Override
            Direction left() {
                return E;
            }

            @Override
            Direction right() {
                return W;
            }
        },
        E(1, 0) {
            @Override
            Direction left() {
                return N;
            }

            @Override
            Direction right() {
                return S;
            }
        },
        W(-1, 0) {
            @Override
            Direction left() {
                return S;
            }

            @Override
            Direction right() {
                return N;
            }
        };

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        abstract Direction left();
        abstract Direction right();

        Point move(Point p, int value) {
            return new Point(p.x + dx * value, p.y + dy * value);
        }
    }

    private static class Point {

        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}

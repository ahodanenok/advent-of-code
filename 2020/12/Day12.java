import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;


/**
 * Advent of Code - Day 12
 * https://adventofcode.com/2020/day/12
 */
public class Day12 {

    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    // ~ Part 1 =======================================================================================================
    private static void part1() throws Exception {
        List<MoveCommand> commands = getCommands_part1();

        MoveState state = new MoveState();
        state.point = new Point(0, 0);
        state.direction = Direction.E;
        for (MoveCommand cmd : commands) {
            cmd.apply(state);
        }

        //System.out.printf("%s (%d, %d)%n", state.direction, state.point.x, state.point.y);
        System.out.println("Part 1: " + state.point.distance());
    }

    private static List<MoveCommand> getCommands_part1() throws Exception {
        List<MoveCommand> commands = new ArrayList<>();
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

    private interface MoveCommand {
        void apply(MoveState state);
    }

    private static class MoveState {
        Point point;
        Direction direction;
    }
    // ================================================================================================================

    // ~ Part 2 =======================================================================================================
    private static void part2() throws Exception {
        List<WaypointCommand> commands = getCommands_part2();

        WaypointState state = new WaypointState();
        state.waypoint = new Point(10, -1);
        state.ship = new Point(0, 0);
        for (WaypointCommand cmd : commands) {
            //System.out.printf("ship=(%d, %d), wp=(%d, %d)%n", state.ship.x, state.ship.y, state.waypoint.x, state.waypoint.y);
            cmd.apply(state);
        }

        //System.out.printf("ship=(%d, %d), wp=(%d, %d)%n", state.ship.x, state.ship.y, state.waypoint.x, state.waypoint.y);
        System.out.println("Part 2: " + state.ship.distance());
    }

    private static List<WaypointCommand> getCommands_part2() throws Exception {
        List<WaypointCommand> commands = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                char action = line.charAt(0);
                int value = Integer.parseInt(line.substring(1));
                if (action == 'N') {
                    commands.add(s -> s.waypoint = Direction.N.move(s.waypoint, value));
                } else if (action == 'S') {
                    commands.add(s -> s.waypoint = Direction.S.move(s.waypoint, value));
                } else if (action == 'E') {
                    commands.add(s -> s.waypoint = Direction.E.move(s.waypoint, value));
                } else if (action == 'W') {
                    commands.add(s -> s.waypoint = Direction.W.move(s.waypoint, value));
                } else if (action == 'L') {
                    commands.add(s -> s.waypoint = s.waypoint.rotate(s.ship, -value));
                } else if (action == 'R') {
                    commands.add(s -> s.waypoint = s.waypoint.rotate(s.ship, value));
                } else if (action == 'F') {
                    commands.add(s -> {
                        int dx = s.waypoint.x - s.ship.x;
                        int dy = s.waypoint.y - s.ship.y;

                        s.ship = new Point(s.ship.x + dx * value, s.ship.y + dy * value);
                        s.waypoint = new Point(s.waypoint.x + dx * value, s.waypoint.y + dy * value);
                    });
                } else {
                    throw new IllegalStateException(line);
                }
            }
        }

        return commands;
    }

    private interface WaypointCommand {
        void apply(WaypointState state);
    }

    private static class WaypointState {
        Point waypoint;
        Point ship;
    }
    // ================================================================================================================

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

        int distance() {
            return (int) (Math.abs(x) + Math.abs(y));
        }

        Point rotate(Point around, int angle) {
            int cos = (int) Math.cos(angle * Math.PI / 180);
            int sin = (int) Math.sin(angle * Math.PI / 180);
            return new Point(
                cos * (x - around.x) - sin * (y - around.y) + around.x,
                sin * (x - around.x) + cos * (y - around.y) + around.y);
        }
    }
}

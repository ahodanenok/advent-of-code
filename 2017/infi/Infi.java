import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Advent of Code - Infi
 * https://aoc.infi.nl/2017/
 */
 public class Infi {

    public static void main(String[] args) throws Exception {
        System.out.println(countCollisions(loadLogFile()));
        printMessage(loadLogFile());
    }

    static LogFile loadLogFile() throws Exception {
        LogFile logFile = new LogFile();
        logFile.robots = new ArrayList<Robot>();
        logFile.moves = new ArrayList<Move>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();

            int pos = 0;
            while (pos < line.length()) {
                if (line.charAt(pos) == '[') {
                    pos++;
                    String str = "";
                    while (line.charAt(pos) != ']') str += line.charAt(pos++);
                    String[] parts = str.split(",");
                    logFile.robots.add(new Robot(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                    pos++;
                } else if (line.charAt(pos) == '(') {
                    pos++;
                    String str = "";
                    while (line.charAt(pos) != ')') str += line.charAt(pos++);
                    String[] parts = str.split(",");
                    logFile.moves.add(new Move(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                    pos++;
                } else {
                    throw new IllegalStateException("Illegal char at pos=" + pos + ", char=" + line.charAt(pos));
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return logFile;
    }

    private static int countCollisions(LogFile logFile) {
        Observer<Integer> obs = new Observer<Integer>() {
            int collisions = 0;

            public void collision(Location loc) {
                collisions++;
            }

            public Integer result() {
                return collisions;
            }
        };
        simulate(logFile, obs);
        return obs.result();
    }

    private static void printMessage(LogFile logFile) {
        Observer<Void> obs = new Observer<Void>() {
            int yMin = Integer.MAX_VALUE;
            int yMax = Integer.MIN_VALUE;
            int xMin = Integer.MAX_VALUE;
            int xMax = Integer.MIN_VALUE;
            Set<Location> collisions = new HashSet<Location>();

            public void collision(Location loc) {
                collisions.add(loc);
                yMin = Math.min(yMin, loc.y);
                yMax = Math.max(yMax, loc.y);
                xMin = Math.min(xMin, loc.x);
                xMax = Math.max(xMax, loc.x);
            }

            public Void result() {
                for (int y = yMin; y <= yMax; y++) {
                    for (int x = xMin; x <= xMax; x++) {
                        if (collisions.contains(new Location(x, y))) {
                            System.out.print("@");
                        } else {
                            System.out.print(" ");
                        }
                    }
                    System.out.println();
                }

                return null;
            }
        };
        simulate(logFile, obs);
        obs.result();
    }

    private static void simulate(LogFile logFile, Observer obs) {
        for (int moveIdx = 0; moveIdx < logFile.moves.size(); moveIdx += logFile.robots.size()) {
            for (int robotIdx = 0; robotIdx < logFile.robots.size(); robotIdx++) {
                Robot r = logFile.robots.get(robotIdx);
                Move m = logFile.moves.get(moveIdx + robotIdx);
                r.x += m.offsetX;
                r.y += m.offsetY;
            }

            Map<Location, List<Robot>> map = new HashMap<Location, List<Robot>>();
            for (Robot r : logFile.robots) {
                List<Robot> robotsAtLocation = map.get(new Location(r.x, r.y));
                if (robotsAtLocation == null) {
                    robotsAtLocation = new ArrayList<Robot>();
                    map.put(new Location(r.x, r.y), robotsAtLocation);
                }

                robotsAtLocation.add(r);
            }

            for (Map.Entry<Location, List<Robot>> entry : map.entrySet()) {
                if (entry.getValue().size() > 1) {
                    obs.collision(entry.getKey());
                }
            }
        }
    }

    private static interface Observer<T> {
        void collision(Location loc);
        T result();
    }

    static class LogFile {

        List<Robot> robots;
        List<Move> moves;

        @Override
        public String toString() {
            String str = "";
            
            str += "Robots:\n";
            for (Robot r : robots) {
                str += "  ";
                str += r;
                str += "\n";
            }

            str += "Moves:\n";
            for (Move m : moves) {
                str += "  ";
                str += m;
                str += "\n";
            }

            return str;
        }
    }

    static class Move {

        final int offsetX;
        final int offsetY;

        Move(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        @Override
        public String toString() {
            return "Move: offsetX=" + offsetX + ", offsetY=" + offsetY;
        }
    }

    static class Location {

        final int x;
        final int y;

        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public String toString() {
            return "Location: x=" + x + ", y=" + y;
        }
    }

    static class Robot {

        int x;
        int y;

        Robot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Robot: x=" + x + ", y=" + y;
        }
    }
}

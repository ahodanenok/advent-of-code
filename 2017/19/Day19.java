import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 19
 * https://adventofcode.com/2017/day/19
 */
public class Day19 {

    public static void main(String[] args) {
        Diagram diagram = getDiagram();
        Packet packet = new Packet();
        diagram.follow(packet);
        System.out.println(packet.letters);
    }

    private static Diagram getDiagram() {
        List<String> lines = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        return new Diagram(lines);
    }

    private static class Packet {

        private String letters = "";

        void passing(char pathElement) {
            if (Character.isLetter(pathElement)) {
                letters += String.valueOf(pathElement);
            }
        }
    }

    private static class Diagram {

        private static final char V_PATH = '|';
        private static final char H_PATH = '-';
        private static final char TURN = '+';
        private static final char EMPTY = ' ';

        private List<String> lines;
        private Dir dir;
        private int row;
        private int col;

        Diagram(List<String> lines) {
            this.lines = lines;
            this.dir = Dir.SOUTH;

            String firstLine = lines.get(0);
            for (int i = 0; i < firstLine.length(); i++) {
                if (firstLine.charAt(i) == V_PATH) {
                    this.col = i;
                    break;
                }
            }
            this.row = 0;
        }

        void follow(Packet packet) {
            char pathElement;
            while (true) {
                pathElement = pathElementAt(row, col);
                if (pathElement == EMPTY) {
                    break;
                }

                packet.passing(pathElement);

                if (pathElement == TURN) {
                    turn();
                }

                row = dir.nextRow(row);
                col = dir.nextCol(col);
            }
        }

        private void turn() {
            Dir nextDir = null;
            if (dir != Dir.NORTH && isNotEmptyAdjacent(Dir.SOUTH)) {
                nextDir = Dir.SOUTH;
            }

            if (dir != Dir.EAST && isNotEmptyAdjacent(Dir.WEST)) {
                nextDir = Dir.WEST;
            }

            if (dir != Dir.SOUTH && isNotEmptyAdjacent(Dir.NORTH)) {
                nextDir = Dir.NORTH;
            }

            if (dir != Dir.WEST && isNotEmptyAdjacent(Dir.EAST)) {
                nextDir = Dir.EAST;
            }

            if (nextDir == null) {
                throw new IllegalStateException("Can't determine next direction");
            }

            dir = nextDir;
        }

        private boolean isNotEmptyAdjacent(Dir adjDir) {
            int adjRow = adjDir.nextRow(row);
            int adjCol = adjDir.nextCol(col);
            return adjRow >= 0 && adjRow < lines.size() 
                    && adjCol >= 0 && adjCol < lines.get(0).length()
                    && pathElementAt(adjRow, adjCol) != EMPTY;
        }

        private char pathElementAt(int row, int col) {
            return lines.get(row).charAt(col);
        }
    }

    private enum Dir {

        NORTH(-1, 0),
        EAST(0, 1),
        SOUTH(1, 0),
        WEST(0, -1);

        private int rowOffset;
        private int colOffset;

        Dir(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        int nextCol(int col) {
            return col + colOffset;
        }

        int nextRow(int row) {
            return row + rowOffset;
        }
    }
}

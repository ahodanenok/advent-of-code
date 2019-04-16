import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2017/day/22
 */
public class Day22 {

    public static void main(String[] args) throws Exception {
        Grid grid = getGrid();
        part1(grid);
    }

    private static void part1(Grid grid) {
        Carrier carrier = new Carrier(grid);
        for (int i = 0; i < 10000; i++) carrier.burst();
        System.out.println(carrier.infectedCount);
    }

    private static Grid getGrid() {
        List<String> def = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            def.add(scanner.nextLine());
        }

        Grid grid = new Grid();
        for (int i = 0, row = -def.size() / 2; i < def.size(); row++, i++) {
            for (int j = 0, col = -def.get(i).length() / 2; j < def.get(i).length(); col++, j++) {
                char ch = def.get(i).charAt(j);
                if (ch == '.') {
                    grid.nodes.put(new Node(row, col), State.CLEAN);
                } else if (ch == '#') {
                    grid.nodes.put(new Node(row, col), State.INFECTED);
                } else {
                    throw new IllegalStateException("Unknown node state: " + ch);
                }
            }
        }

        return grid;
    }

    private static class Carrier {

        private Grid grid;
        private Node currentNode;
        private Dir dir;

        private int infectedCount;

        Carrier(Grid grid) {
            this.dir = Dir.UP;
            this.grid = grid;
            this.currentNode = new Node(0, 0);
        }

        void burst() {
            if (grid.isInfected(currentNode)) {
                dir = dir.right;
                grid.clean(currentNode);
            } else if (grid.isClean(currentNode)) {
                dir = dir.left;
                grid.infect(currentNode);
                infectedCount++;
            } else {
                throw new IllegalStateException("Illegal node state");
            }

            //currentNode.row = dir.nextRow(currentNode.row);
            //currentNode.col = dir.nextCol(currentNode.col);
            currentNode = new Node(dir.nextRow(currentNode.row), dir.nextCol(currentNode.col));
        }

        void print() {
            grid.print(currentNode);
        }
    }

    private static class Grid {

        private Map<Node, State> nodes = new LinkedHashMap<Node, State>();

        boolean isInfected(Node node) {
            return nodes.get(node) == State.INFECTED;
        }

        boolean isWeakened(Node node) {
            return nodes.get(node) == State.WEAKENED;
        }

        boolean isFlagged(Node node) {
            return nodes.get(node) == State.FLAGGED;
        }

        boolean isClean(Node node) {
            State s = nodes.get(node);
            return s == null || s == State.CLEAN;
        }

        void infect(Node node) {
            nodes.put(new Node(node.row, node.col), State.INFECTED);
        }

        void clean(Node node) {
            nodes.put(new Node(node.row, node.col), State.CLEAN);
        }

        void flag(Node node) {
            nodes.put(new Node(node.row, node.col), State.FLAGGED);
        }

        void weaken(Node node) {
            nodes.put(new Node(node.row, node.col), State.WEAKENED);
        }

        void print(Node currentNode) {
            int rowMin = Integer.MAX_VALUE, rowMax = Integer.MIN_VALUE;
            int colMin = Integer.MAX_VALUE, colMax = Integer.MIN_VALUE;
            for (Node node : nodes.keySet()) {
                rowMin = Math.min(rowMin, node.row);
                rowMax = Math.max(rowMax, node.row);
                colMin = Math.min(colMin, node.col);
                colMax = Math.max(colMax, node.col);
            }

            for (int row = rowMin; row <= rowMax; row++) {
                for (int col = colMin; col <= colMax; col++) {
                    boolean found = false;
                    for (Map.Entry<Node, State> entry : nodes.entrySet()) {
                        if (entry.getKey().row == row && entry.getKey().col == col) {
                            System.out.print(entry.getValue() == State.INFECTED ? " #" : " .");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.print(" .");
                    }
                }
                System.out.println();
            }
        }
    }

    private static class Node {

        private final int row;
        private final int col;

        Node(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Node other = (Node) obj;
            return row == other.row && col == other.col;
        }

        @Override
        public String toString() {
            return "row=" + row + ", col=" + col;
        }
    }

    private enum State {
        INFECTED, CLEAN, WEAKENED, FLAGGED;
    }

    private enum Dir {

        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        static {
            Dir.UP.left = Dir.LEFT;
            Dir.UP.right = Dir.RIGHT;
            Dir.UP.backwards = Dir.DOWN;

            Dir.DOWN.left = Dir.RIGHT;
            Dir.DOWN.right = Dir.LEFT;
            Dir.DOWN.backwards = Dir.UP;

            Dir.LEFT.left = Dir.DOWN;
            Dir.LEFT.right = Dir.UP;
            Dir.LEFT.backwards = Dir.RIGHT;

            Dir.RIGHT.left = Dir.UP;
            Dir.RIGHT.right = Dir.DOWN;
            Dir.RIGHT.backwards = Dir.LEFT;
        }

        private int rowOffset;
        private int colOffset;
        private Dir left;
        private Dir right;
        private Dir backwards;

        Dir(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        int nextRow(int row) {
            return row + rowOffset;
        }

        int nextCol(int col) {
            return col + colOffset;
        }
    }
}

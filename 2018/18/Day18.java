import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2018/day/18
 */
public class Day18 {

    public static void main(String[] args) {
        CollectionArea area = getArea();

        area = collect(area, 10);
        resourceValue(area);

        area = collect(area, 1000000000 - 10);
        resourceValue(area);
    }

    private static void resourceValue(CollectionArea area) {
        int treesCount = 0;
        int lumberCount = 0;
        for (int row = 0; row < area.height; row++) {
            for (int col = 0; col < area.width; col++) {
                if (area.get(row, col) == CollectionArea.TREES) {
                    treesCount++;
                } else if (area.get(row, col) == CollectionArea.LUMBER) {
                    lumberCount++;
                }
            }
        }

        System.out.println(treesCount * lumberCount);
    }

    private static CollectionArea collect(CollectionArea initialArea, int minutes) {
        List<CollectionArea> seen = new ArrayList<CollectionArea>();

        CollectionArea area = initialArea;
        int m = 0;
        while (m < minutes) {
            CollectionArea newArea = new CollectionArea(area.width, area.height);
            for (int row = 0; row < area.height; row++) {
                for (int col = 0; col < area.width; col++) {
                    int type = area.get(row, col);
                    if (type == CollectionArea.EMPTY && area.countAdjacent(row, col, CollectionArea.TREES) >= 3) {
                        newArea.set(row, col, CollectionArea.TREES);
                    } else if (type == CollectionArea.TREES && area.countAdjacent(row, col, CollectionArea.LUMBER) >= 3) {
                        newArea.set(row, col, CollectionArea.LUMBER);
                    } else if (type == CollectionArea.LUMBER) {
                        if (area.countAdjacent(row, col, CollectionArea.LUMBER) > 0 && area.countAdjacent(row, col, CollectionArea.TREES) > 0) {
                            newArea.set(row, col, CollectionArea.LUMBER);
                        } else {
                            newArea.set(row, col, CollectionArea.EMPTY);
                        }
                    } else {
                        newArea.set(row, col, type);
                    }
                }
            }

            // fast-forward
            if (seen.contains(newArea)) {
                int seenMinute = seen.indexOf(newArea);
                int repeatInterval = m - seenMinute;
                int minutesLeft = minutes - m;
                m = minutes - minutesLeft % repeatInterval;
            }

            area = newArea;
            seen.add(area);
            m++;
        }

        return area;
    }

    private static CollectionArea getArea() {
        List<String> rows = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            rows.add(scanner.nextLine().trim());
        }

        int height = rows.size();
        int width = rows.get(0).length();
        CollectionArea area = new CollectionArea(width, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                char ch = rows.get(row).charAt(col);
                if (ch == '.') {
                    area.set(row, col, CollectionArea.EMPTY);
                } else if (ch == '|') {
                    area.set(row, col, CollectionArea.TREES);
                } else if (ch == '#') {
                    area.set(row, col, CollectionArea.LUMBER);
                } else {
                    throw new IllegalStateException("Unknown char: " + ch);
                }
            }
        }

        return area;
    }

    private static class CollectionArea {

        static final int EMPTY = 0;
        static final int TREES = 1;
        static final int LUMBER = 2;

        private int width;
        private int height;
        private int[][] area;

        CollectionArea(int width, int height) {
            this.width = width;
            this.height = height;
            this.area = new int[height][width];
        }

        void set(int row, int col, int type) {
            area[row][col] = type;
        }

        int get(int row, int col) {
            return area[row][col];
        }

        int countAdjacent(int row, int col, int type) {
            int count = 0;

            if (inArea(row - 1, col) && get(row - 1, col) == type) {
                count++;
            }

            if (inArea(row - 1, col + 1) && get(row - 1, col + 1) == type) {
                count++;
            }

            if (inArea(row, col + 1) && get(row, col + 1) == type) {
                count++;
            }

            if (inArea(row + 1, col + 1) && get(row + 1, col + 1) == type) {
                count++;
            }

            if (inArea(row + 1, col) && get(row + 1, col) == type) {
                count++;
            }

            if (inArea(row + 1, col - 1) && get(row + 1, col - 1) == type) {
                count++;
            }

            if (inArea(row, col - 1) && get(row, col - 1) == type) {
                count++;
            }

            if (inArea(row - 1, col - 1) && get(row - 1, col - 1) == type) {
                count++;
            }

            return count;
        }

        private boolean inArea(int row, int col) {
            return row >= 0 && row < height && col >= 0 && col < width;
        }

        @Override
        public boolean equals(Object obj) {
            CollectionArea other = (CollectionArea) obj;

            if (other.width != width || other.height != height) {
                return false;
            }

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (other.get(row, col) != get(row, col)) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (get(row, col) == EMPTY) {
                        sb.append('.');
                    } else if (get(row, col) == TREES) {
                        sb.append('|');
                    } else if (get(row, col) == LUMBER) {
                        sb.append('#');
                    }
                }
                sb.append('\n');
            }

            return sb.toString();
        }
    }
}
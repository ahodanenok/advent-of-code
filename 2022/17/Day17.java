import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * https://adventofcode.com/2022/day/17
 */
public class Day17 {

    private static final int CHAMBER_WIDTH = 7;

    public static void main(String[] args) throws Exception {
        String jets = getJets();
        part1(jets);

        //boolean[][] chamber = new boolean[7][CHAMBER_WIDTH];
        //Rock.CROSS.moveLeft(chamber, 2, 3);
        //Rock.CROSS.moveLeft(chamber, 2, 3);
        //printChamber(chamber);

    }

    private static void printChamber(boolean[][] chamber) {
        for (int row = chamber.length - 1; row >= 0; row--) {
            for (int col = 0; col < CHAMBER_WIDTH; col++) {
                if (chamber[row][col]) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

    private static void part1(String jets) {
        // tallest rock is 4 tiles and there will be 2022 rocks at max
        boolean[][] chamber = new boolean[4 * 2022][CHAMBER_WIDTH];
        int height = 0;

        Rock[] rocks = Rock.values();
        int rockCount = 0;
        int jetPos = 0;
        while (rockCount < 2022) {
            Rock rock = rocks[rockCount % rocks.length];
            int x = 2;
            int y = height + 3;

            while (true) {
                char jet = jets.charAt(jetPos);
                jetPos = (jetPos + 1) % jets.length();

                if (jet == '<') {
                    if (rock.moveLeft(chamber, x, y)) {
                        x--;
                    }
                } else if (jet == '>') {
                    if (rock.moveRight(chamber, x, y)) {
                        x++;
                    }
                } else {
                    throw new IllegalStateException("jet: " + jet);
                }

                if (rock.moveDown(chamber, x, y)) {
                    y--;
                } else {
                    rock.draw(chamber, x, y);
                    height = Math.max(y + rock.height, height);
                    break;
                }
            }

            rockCount++;
        }

        System.out.println("Part 1: " + height);
    }

    private static String getJets() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            return reader.readLine();
        }
    }

    private enum Rock {

        HORIZONTAL(1) {
            boolean moveLeft(boolean[][] chamber, int x, int y) {
                if (x == 0 || chamber[y][x - 1]) {
                    return false;
                }

                return true;
            }

            boolean moveRight(boolean[][] chamber, int x, int y) {
                if (x + 3 == CHAMBER_WIDTH - 1 || chamber[y][x + 4]) {
                    return false;
                }

                return true;
            }

            boolean moveDown(boolean[][] chamber, int x, int y) {
                if (y == 0) {
                    return false;
                }

                for (int i = 0; i < 4; i++) {
                    if (chamber[y - 1][x + i]) {
                        return false;
                    }
                }

                return true;
            }

            void draw(boolean[][] chamber, int x, int y) {
                for (int i = 0; i < 4; i++) {
                    chamber[y][x + i] = true;
                }
            }
        },
        CROSS(3) {
            boolean moveLeft(boolean[][] chamber, int x, int y) {
                if (x == 0) {
                    return false;
                }

                if (chamber[y + 2][x] || chamber[y + 1][x - 1] || chamber[y][x]) {
                    return false;
                }

                return true;
            }

            boolean moveRight(boolean[][] chamber, int x, int y) {
                if ((x + 2) == CHAMBER_WIDTH - 1) {
                    return false;
                }

                if (chamber[y + 2][x + 2] || chamber[y + 1][x + 3] || chamber[y][x + 2]) {
                    return false;
                }

                return true;
            }

            boolean moveDown(boolean[][] chamber, int x, int y) {
                if (y == 0) {
                    return false;
                }

                if (chamber[y][x] || chamber[y - 1][x + 1] || chamber[y][x + 2]) {
                    return false;
                }

                return true;
            }

            void draw(boolean[][] chamber, int x, int y) {
                chamber[y + 2][x + 1] = true;
                for (int i = 0; i < 3; i++) {
                    chamber[y + 1][x + i] = true;
                }
                chamber[y][x + 1] = true;
            }
        },
        ANGLE(3) {
            boolean moveLeft(boolean[][] chamber, int x, int y) {
                if (x == 0) {
                    return false;
                }

                if (chamber[y][x - 1] || chamber[y + 1][x + 1] || chamber[y + 2][x + 1]) {
                    return false;
                }

                return true;
            }

            boolean moveRight(boolean[][] chamber, int x, int y) {
                if (x + 2 == CHAMBER_WIDTH - 1) {
                    return false;
                }

                if (chamber[y][x + 3] || chamber[y + 1][x + 3] || chamber[y + 2][x + 3]) {
                    return false;
                }

                return true;
            }

            boolean moveDown(boolean[][] chamber, int x, int y) {
                if (y == 0) {
                    return false;
                }

                if (chamber[y - 1][x] || chamber[y - 1][x + 1] || chamber[y - 1][x + 2]) {
                    return false;
                }

                return true;
            }

            void draw(boolean[][] chamber, int x, int y) {
                chamber[y][x] = true;
                chamber[y][x + 1] = true;
                chamber[y][x + 2] = true;
                chamber[y + 1][x + 2] = true;
                chamber[y + 2][x + 2] = true;
            }
        },
        VERTICAL(4) {
            boolean moveLeft(boolean[][] chamber, int x, int y) {
                if (x == 0) {
                    return false;
                }

                for (int i = 0; i < 4; i++) {
                    if (chamber[y + i][x - 1]) {
                        return false;
                    }
                }

                return true;
            }

            boolean moveRight(boolean[][] chamber, int x, int y) {
                if (x == CHAMBER_WIDTH - 1) {
                    return false;
                }

                for (int i = 0; i < 4; i++) {
                    if (chamber[y + i][x + 1]) {
                        return false;
                    }
                }

                return true;
            }

            boolean moveDown(boolean[][] chamber, int x, int y) {
                if (y == 0 || chamber[y - 1][x]) {
                    return false;
                }

                return true;
            }

            void draw(boolean[][] chamber, int x, int y) {
                for (int i = 0; i < 4; i++) {
                    chamber[y + i][x] = true;
                }
            }
        },
        SQUARE(2) {
            boolean moveLeft(boolean[][] chamber, int x, int y) {
                if (x == 0) {
                    return false;
                }

                if (chamber[y][x - 1] || chamber[y + 1][x - 1]) {
                    return false;
                }

                return true;
            }

            boolean moveRight(boolean[][] chamber, int x, int y) {
                if (x + 1 == CHAMBER_WIDTH - 1) {
                    return false;
                }

                if (chamber[y][x + 2] || chamber[y + 1][x + 2]) {
                    return false;
                }

                return true;
            }

            boolean moveDown(boolean[][] chamber, int x, int y) {
                if (y == 0) {
                    return false;
                }

                if (chamber[y - 1][x] || chamber[y - 1][x + 1]) {
                    return false;
                }

                return true;
            }

            void draw(boolean[][] chamber, int x, int y) {
                chamber[y][x] = true;
                chamber[y][x + 1] = true;
                chamber[y + 1][x + 1] = true;
                chamber[y + 1][x] = true;
            }
        };

        final int height;

        Rock(int height) {
            this.height = height;
        }

        abstract boolean moveLeft(boolean[][] chamber, int x, int y);
        abstract boolean moveRight(boolean[][] chamber, int x, int y);
        abstract boolean moveDown(boolean[][] chamber, int x, int y);
        abstract void draw(boolean[][] chamber, int x, int y);
    }
}

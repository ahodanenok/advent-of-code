import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

/**
 * Advent of Code - Day 17
 * https://adventofcode.com/2022/day/17
 */
public class Day17 {

    private static final int CHAMBER_WIDTH = 7;

    public static void main(String[] args) throws Exception {
        String jets = getJets();
        part1(jets);
        part2(jets);
    }

    private static void part1(String jets) {
        System.out.println("Part 1: " + simulate(jets, 2022, -1).height);
    }

    private static void part2(String jets) {
        // hope there will be a pattern :)
        SimulationResult result = simulate(jets, 5000, -1);

        int floorStart = 0;
        int floorHeight = 0;

        int f0 = 0;
        int f1 = 1;
        int h = 0;

        next:
        // searching for a pattern
        while (f0 < result.height) {
            for (int x = 0; x < CHAMBER_WIDTH; x++) {
                if (result.chamber[f0 + h][x] != result.chamber[f1 + h][x]) {
                    f1++;
                    if (f1 > result.height) {
                        f0++;
                        f1 = f0 + 1;
                    }
                    h = 0;

                    continue next;
                }
            }

            h++;
            if (f0 + h == f1) {
                if (h > floorHeight) {
                    floorStart = f0;
                    floorHeight = h;
                }

                f0++;
                f1 = f0 + 1;
                h = 0;
            }
        }

        BigInteger rockCount = new BigInteger("1000000000000");
        BigInteger height = BigInteger.ZERO;

        // calculate height for ground floor
        SimulationResult a = simulate(jets, -1, floorStart);
        height = height.add(BigInteger.valueOf(a.height));
        rockCount = rockCount.subtract(BigInteger.valueOf(a.rockCount));
        //System.out.printf("a.height=%d, a.rocks=%d, height=%d, rockCount=%d%n", a.height, a.rockCount, height, rockCount);

        // calculate height for repeating floors
        SimulationResult b = simulate(jets, -1, floorStart + floorHeight);
        height = height.add(rockCount.divide(BigInteger.valueOf(b.rockCount - a.rockCount)).multiply(BigInteger.valueOf(floorHeight)));
        rockCount = rockCount.mod(BigInteger.valueOf(b.rockCount - a.rockCount));
        //System.out.printf("b.height=%d, b.rocks=%d, height=%d, rockCount=%d%n", b.height, b.rockCount, height, rockCount);

        // calculate height for remaining rocks - roof!
        if (rockCount.compareTo(BigInteger.ZERO) > 0) {
            SimulationResult c = simulate(jets, a.rockCount + rockCount.intValue(), -1);
            height = height.add(BigInteger.valueOf(c.height - a.height));
            rockCount = rockCount.subtract(BigInteger.valueOf(c.rockCount - a.rockCount));
            //System.out.printf("c.height=%d, c.rocks=%d, height=%d, rockCount=%d%n", c.height, c.rockCount, height, rockCount);
        }

        if (rockCount.compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalStateException("Rock count: " + rockCount);
        }

        System.out.println("Part 2: " + height);
    }

    private static SimulationResult simulate(String jets, int maxRocks, int maxHeight) {
        boolean[][] chamber = new boolean[Math.max(4 * maxRocks, maxHeight) + 10][CHAMBER_WIDTH];

        if (maxRocks == -1) {
            maxRocks = Integer.MAX_VALUE;
        }
        if (maxHeight == -1) {
            maxHeight = Integer.MAX_VALUE;
        }

        int height = 0;
        Rock[] rocks = Rock.values();
        int rockCount = 0;
        int jetPos = 0;
        while (true) {
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
                    break;
                }
            }

            if (y + rock.height > maxHeight) {
                break;
            }

            rock.draw(chamber, x, y);
            height = Math.max(y + rock.height, height);
            rockCount++;

            if (rockCount == maxRocks) {
                break;
            }
        }

        return new SimulationResult(chamber, height, rockCount);
    }

    private static String getJets() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            return reader.readLine();
        }
    }

    private static class SimulationResult {

        final boolean chamber[][];
        final int height;
        final int rockCount;

        SimulationResult(boolean[][] chamber, int height, int rockCount) {
            this.chamber = chamber;
            this.height = height;
            this.rockCount = rockCount;
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

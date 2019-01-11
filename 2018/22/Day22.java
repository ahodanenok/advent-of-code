import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2018/day/21
 */
public class Day22 {

    public static void main(String[] args) {
        Cave cave = new Cave(7863, new Region(14, 760));
        //Cave cave = new Cave(510, new Region(10, 10));
        part1(cave);
    }

    private static void part1(Cave cave) {
        int riskLevel = 0;
        for (int y = 0; y <= cave.target.y; y++) {
            for (int x = 0; x <= cave.target.x; x++) {
                int type = cave.getRegionType(new Region(x, y));
                if (type == Cave.WET) {
                    riskLevel += 1;
                } else if (type == Cave.NARROW) {
                    riskLevel += 2;
                }
            }
        }

        System.out.println(riskLevel);
    }

    private static class Cave {

        static final int ROCKY = 1;
        static final int WET = 2;
        static final int NARROW = 3;

        private int depth;
        private Region target;

        private Map<Region, Integer> erosionLevels;

        Cave(int depth, Region target) {
            this.depth = depth;
            this.target = target;
            this.erosionLevels = new HashMap<Region, Integer>();
        }

        int getRegionType(Region region) {
            int el = getErosionLevel(region);
            if (el % 3 == 0) {
                return ROCKY;
            } else if (el % 3 == 1) {
                return WET;
            } else if (el % 3 == 2) {
                return NARROW;
            } else {
                throw new IllegalStateException("Unknown type for " + (el % 3));
            }
        }

        @Override
        public String toString() {
            return toString(new Region(0, 0), target);
        }

        public String toString(Region from, Region to) {
            StringBuilder sb = new StringBuilder();
            for (int y = from.y; y <= to.y; y++) {
                for (int x = from.x; x <= to.x; x++) {
                    int type = getRegionType(new Region(x, y));
                    if (type == ROCKY) {
                        sb.append('.');
                    } else if (type == WET) {
                        sb.append('=');
                    } else if (type == NARROW) {
                        sb.append('|');
                    } else {
                        throw new IllegalStateException("Unknown type: " + type);
                    }
                }
                sb.append('\n');
            }

            return sb.toString();
        }

        private int getErosionLevel(Region region) {
            if (erosionLevels.containsKey(region)) {
                return erosionLevels.get(region);
            }

            int el = (getGeologicIndex(region) + depth) % 20183;
            erosionLevels.put(region, el);
            return el;
        }

        private int getGeologicIndex(Region region) {
            if (region.x == 0 && region.y == 0) {
                return 0;
            } else if (region.equals(target)) {
                return 0;
            } else if (region.y == 0) {
                return region.x * 16807;
            } else if (region.x == 0) {
                return region.y * 48271;
            } else {
                return getErosionLevel(new Region(region.x - 1, region.y)) * getErosionLevel(new Region(region.x, region.y - 1));
            }
        }
    }

    private static class Region {

        private final int x;
        private final int y;

        Region(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        @Override
        public boolean equals(Object obj) {
            Region other = (Region) obj;
            return x == other.x && y == other.y;
        }
    }
}

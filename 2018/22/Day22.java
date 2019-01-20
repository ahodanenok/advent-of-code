import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2018/day/22
 */
public class Day22 {

    public static void main(String[] args) {
        Cave cave = new Cave(7863, new Region(14, 760));
        //Cave cave = new Cave(510, new Region(10, 10));
        part1(cave);
        part2(cave);
    }

    private static void part1(Cave cave) {
        int riskLevel = 0;
        for (int y = 0; y <= cave.target.y; y++) {
            for (int x = 0; x <= cave.target.x; x++) {
                RegionType type = cave.getRegionType(new Region(x, y));
                if (type == RegionType.WET) {
                    riskLevel += 1;
                } else if (type == RegionType.NARROW) {
                    riskLevel += 2;
                }
            }
        }

        System.out.println(riskLevel);
    }

    private enum Equipment {
        TORCH(RegionType.ROCKY, RegionType.NARROW),
        GEAR(RegionType.ROCKY, RegionType.WET),
        NOTHINHG(RegionType.WET, RegionType.NARROW);

        List<RegionType> regionTypes = new ArrayList<RegionType>();

        Equipment(RegionType... regionTypes) {
            for (RegionType type : regionTypes) {
                this.regionTypes.add(type);
            }
        }
    }

    private static class Step {

        private final Region region;
        private final Equipment equipment;
        private int minutes;

        Step(Region region, Equipment equipment, int minutes) {
            this.region = region;
            this.equipment = equipment;
            this.minutes = minutes;
        }

        @Override
        public int hashCode() {
            return 31 * region.hashCode() + equipment.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Step other = (Step) obj;
            return other.region.equals(region) && other.equipment == equipment;
        }
    }

    private static void part2(Cave cave) {
        Step initial = new Step(new Region(0, 0), Equipment.TORCH, 0);
        Step target = new Step(cave.target, Equipment.TORCH, 0);

        Map<Step, Integer> minutes = new HashMap<Step, Integer>();

        PriorityQueue<Step> queue = new PriorityQueue<Step>(new java.util.Comparator<Step>() {
            @Override
            public int compare(Step a, Step b) {
                return Integer.compare(a.minutes, b.minutes);
            }
        });
        queue.add(initial);

        while (!queue.isEmpty()) {
            Step current = queue.poll();
            if (minutes.containsKey(current) && minutes.get(current) <= current.minutes) {
                continue;
            }
            minutes.put(current, current.minutes);

            if (current.equals(target)) {
                break;
            }

            RegionType type = cave.getRegionType(current.region);
            for (Equipment equipment : Equipment.values()) {
                if (equipment.regionTypes.contains(type) && current.equipment != equipment) {
                    queue.add(new Step(current.region, equipment, current.minutes + 7));
                }
            }

            for (Region adjacent : cave.getAdjacentRegions(current.region)) {
                if (adjacent.x > cave.target.x + 100 || adjacent.y > cave.target.y + 100) {
                    continue;
                }

                type = cave.getRegionType(adjacent);
                for (Equipment equipment : Equipment.values()) {
                    if (equipment.regionTypes.contains(type) && current.equipment == equipment) {
                        queue.add(new Step(adjacent, current.equipment, current.minutes + 1));
                    }
                }
            }
        }

        System.out.println(minutes.get(target));
    }

    private enum RegionType {
        ROCKY, WET, NARROW;
    }

    private static class Cave {

        private int depth;
        private Region target;

        private Map<Region, Integer> erosionLevels;

        Cave(int depth, Region target) {
            this.depth = depth;
            this.target = target;
            this.erosionLevels = new HashMap<Region, Integer>();
        }

        RegionType getRegionType(Region region) {
            int el = getErosionLevel(region);
            if (el % 3 == 0) {
                return RegionType.ROCKY;
            } else if (el % 3 == 1) {
                return RegionType.WET;
            } else if (el % 3 == 2) {
                return RegionType.NARROW;
            } else {
                throw new IllegalStateException("Unknown type for " + (el % 3));
            }
        }

        List<Region> getAdjacentRegions(Region region) {
            List<Region> result = new ArrayList<Region>();

            if (region.x - 1 >= 0) {
                result.add(new Region(region.x - 1, region.y));
            }
            result.add(new Region(region.x + 1, region.y));

            if (region.y - 1 >= 0) {
                result.add(new Region(region.x, region.y - 1));
            }
            result.add(new Region(region.x, region.y + 1));

            return result;
        }

        @Override
        public String toString() {
            return toString(new Region(0, 0), target);
        }

        public String toString(Region from, Region to) {
            StringBuilder sb = new StringBuilder();
            for (int y = from.y; y <= to.y; y++) {
                for (int x = from.x; x <= to.x; x++) {
                    RegionType type = getRegionType(new Region(x, y));
                    if (type == RegionType.ROCKY) {
                        sb.append('.');
                    } else if (type == RegionType.WET) {
                        sb.append('=');
                    } else if (type == RegionType.NARROW) {
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
        public String toString() {
            return x + "," + y;
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

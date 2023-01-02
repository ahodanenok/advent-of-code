import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2022/day/23
 */
public class Day23 {

    public static void main(String[] args) throws Exception {
        List<Location> elves = getElves();
        part1(elves);
        part2(elves);
    }

    private static void part1(List<Location> elves) {
        Simulation s = new Simulation(elves);
        for (int n = 0; n < 10; n++) {
            s.spread();
        }

        int rowMin = Integer.MAX_VALUE;
        int rowMax = Integer.MIN_VALUE;
        int colMin = Integer.MAX_VALUE;
        int colMax = Integer.MIN_VALUE;
        for (Location elf : s.elves) {
            rowMin = Math.min(elf.row, rowMin);
            rowMax = Math.max(elf.row, rowMax);
            colMin = Math.min(elf.col, colMin);
            colMax = Math.max(elf.col, colMax);
        }

        int area = (rowMax - rowMin + 1) * (colMax - colMin + 1);
        System.out.println("Part 1: " + (area - s.elves.size()));
    }

    private static void part2(List<Location> elves) {
        Simulation s = new Simulation(elves);
        int n = 0;
        while (s.changedCount != 0) {
            s.spread();
            n++;
            //System.out.printf("%d -> %d%n", n, s.changedCount);
        }

        System.out.println("Part 2: " + n);
    }

    private static List<Location> getElves() throws Exception {
        List<Location> elves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == '#') {
                        elves.add(new Location(row, col));
                    }
                }

                row++;
            }
        }

        return elves;
    }

    private static class Simulation {

        private final Proposal noneProposal;
        private final LinkedList<Proposal> proposals;

        List<Location> elves;
        int changedCount = -1;

        Simulation(List<Location> elves) {
            this.elves = elves;
            this.noneProposal = new Proposal(Arrays.asList(Direction.values()), null);

            this.proposals = new LinkedList<>();
            this.proposals.add(new Proposal(List.of(Direction.N, Direction.NE, Direction.NW), Direction.N));
            this.proposals.add(new Proposal(List.of(Direction.S, Direction.SE, Direction.SW), Direction.S));
            this.proposals.add(new Proposal(List.of(Direction.W, Direction.NW, Direction.SW), Direction.W));
            this.proposals.add(new Proposal(List.of(Direction.E, Direction.NE, Direction.SE), Direction.E));
        }

        void spread() {
            /*for (int row = -2; row < 10; row++) {
                for (int col = -3; col < 10; col++) {
                    if (elves.contains(new Location(row, col))) {
                        System.out.print('#');
                    } else {
                        System.out.print('.');
                    }
                }
                System.out.println();
            }
            System.out.println();*/

            int changedCount = 0;
            Map<Location, List<Location>> proposed = new HashMap<>();
            for (Location elf : elves) {
                Location next = noneProposal.propose(elf, elves);
                if (next != null) {
                    //System.out.printf("(%d, %d) -> none%n", elf.row, elf.col);
                    proposed.computeIfAbsent(elf, __ -> new ArrayList<>()).add(elf);
                    continue;
                }

                for (int i = 0; i < proposals.size() && next == null; i++) {
                    next = proposals.get(i).propose(elf, elves);
                }

                if (next == null) {
                    next = elf;
                }

                //System.out.printf("(%d, %d) -> (%d, %d)%n", elf.row, elf.col, next.row, next.col);
                proposed.computeIfAbsent(next, __ -> new ArrayList<>()).add(elf);
            }

            List<Location> nextElves = new ArrayList<>();
            for (Map.Entry<Location, List<Location>> entry : proposed.entrySet()) {
                if (entry.getValue().size() == 1) {
                    nextElves.add(entry.getKey());
                    if (!entry.getKey().equals(entry.getValue().get(0))) {
                        changedCount++;
                    }
                } else {
                    nextElves.addAll(entry.getValue());
                }
            }

            elves = nextElves;
            proposals.add(proposals.removeFirst());
            this.changedCount = changedCount;
        }
    }

    private static class Proposal {

        private final List<Direction> empty;
        private final Direction toDir;

        Proposal(List<Direction> empty, Direction toDir) {
            this.empty = empty;
            this.toDir = toDir;
        }

        Location propose(Location elf, List<Location> elves) {
            int emptyCount = 0;
            for (Direction dir : empty) {
                if (!elves.contains(dir.next(elf))) {
                    emptyCount++;
                }
            }

            if (emptyCount == empty.size()) {
                return toDir != null ? toDir.next(elf) : elf;
            } else {
                return null;
            }
        }
    }

    private static class Location {

        final int row;
        final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return row == other.row && col == other.col;
        }
    }

    private enum Direction {

        N {
            @Override
            Location next(Location location) {
                return new Location(location.row - 1, location.col);
            }
        },
        NE {
            @Override
            Location next(Location location) {
                return new Location(location.row - 1, location.col + 1);
            }
        },
        NW {
            @Override
            Location next(Location location) {
                return new Location(location.row - 1, location.col - 1);
            }
        },
        S {
            @Override
            Location next(Location location) {
                return new Location(location.row + 1, location.col);
            }
        },
        SE {
            @Override
            Location next(Location location) {
                return new Location(location.row + 1, location.col + 1);
            }
        },
        SW {
            @Override
            Location next(Location location) {
                return new Location(location.row + 1, location.col - 1);
            }
        },
        E {
            @Override
            Location next(Location location) {
                return new Location(location.row, location.col + 1);
            }
        },
        W {
            @Override
            Location next(Location location) {
                return new Location(location.row, location.col - 1);
            }
        };

        abstract Location next(Location location);
    }
}

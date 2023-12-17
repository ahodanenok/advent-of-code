import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2023/day/16
 */
public class Day16 {

    public static void main(String[] args) throws Exception {
        Grid grid = getInput();
        part1(grid);
    }

    private static void part1(Grid grid) {
        Location startingLocation = new Location(0, 0);
        Beam startingBeam = new Beam(startingLocation, Direction.RIGHT);

        Set<Location> energizedTiles = new HashSet<>();
        energizedTiles.add(startingLocation);

        Set<Beam> history = new HashSet<>();
        history.add(startingBeam);

        LinkedList<Beam> queue = new LinkedList<>();
        queue.addLast(startingBeam);
        while (!queue.isEmpty()) {
            Beam beam = queue.removeFirst();
            List<Beam> outgoingBeams = grid.tiles.get(beam.location).encounter(beam);
            for (Beam b : outgoingBeams) {
                if (b.location.row < 0 || b.location.row >= grid.height
                        || b.location.col < 0 || b.location.col >= grid.width) {
                    continue;
                }

                if (history.contains(b)) {
                    continue;
                }

                energizedTiles.add(b.location);
                history.add(b);
                queue.addLast(b);
            }
        }

        System.out.println("Part 1: " + energizedTiles.size());
    }

    private static Grid getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Map<Location, ObjectType> tiles = new HashMap<>();

            int row = 0;
            int width = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                width = line.length();
                for (int col = 0; col < line.length(); col++) {
                    char obj = line.charAt(col);
                    if (obj == '/') {
                        tiles.put(new Location(row, col), ObjectType.R_MIRROR);
                    } else if (obj == '\\') {
                        tiles.put(new Location(row, col), ObjectType.L_MIRROR);
                    } else if (obj == '-') {
                        tiles.put(new Location(row, col), ObjectType.H_SPLITTER);
                    } else if (obj == '|') {
                        tiles.put(new Location(row, col), ObjectType.V_SPLITTER);
                    } else if (obj == '.') {
                        tiles.put(new Location(row, col), ObjectType.EMPTY);
                    } else {
                        throw new IllegalStateException("Unknown object: " + obj);
                    }
                }

                row++;
            }

            return new Grid(width, row, tiles);
        }
    }

    private static class Grid {

        final int width;
        final int height;
        final Map<Location, ObjectType> tiles;

        Grid(int width, int height, Map<Location, ObjectType> tiles) {
            this.width = width;
            this.height = height;
            this.tiles = tiles;
        }
    }

    private static class Beam {

        final Location location;
        final Direction direction;

        Beam(Location location, Direction direction) {
            this.location = location;
            this.direction = direction;
        }

        Beam move() {
            return move(direction);
        }

        Beam move(Direction direction) {
            return new Beam(direction.next(location), direction);
        }

        @Override
        public int hashCode() {
            return 31 * location.hashCode() + direction.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Beam other = (Beam) obj;
            return location.equals(other.location) && direction.equals(other.direction);
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

        UP {
            @Override
            Location next(Location location) {
                return new Location(location.row - 1, location.col);
            }
        },
        DOWN {
            @Override
            Location next(Location location) {
                return new Location(location.row + 1, location.col);
            }
        },
        LEFT {
            @Override
            Location next(Location location) {
                return new Location(location.row, location.col - 1);
            }
        },
        RIGHT {
            @Override
            Location next(Location location) {
                return new Location(location.row, location.col + 1);
            }
        };

        abstract Location next(Location location);
     }

     private enum ObjectType {

        EMPTY {
            @Override
            public List<Beam> encounter(Beam beam) {
                return List.of(beam.move());
            }
        },
        L_MIRROR {
            @Override
            public List<Beam> encounter(Beam beam) {
                if (beam.direction == Direction.UP) {
                    return List.of(beam.move(Direction.LEFT));
                } else if (beam.direction == Direction.DOWN) {
                    return List.of(beam.move(Direction.RIGHT));
                } else if (beam.direction == Direction.RIGHT) {
                    return List.of(beam.move(Direction.DOWN));
                } else if (beam.direction == Direction.LEFT) {
                    return List.of(beam.move(Direction.UP));
                } else {
                    throw new IllegalStateException(beam.direction.name());
                }
            }
        },
        R_MIRROR {
            @Override
            public List<Beam> encounter(Beam beam) {
                if (beam.direction == Direction.UP) {
                    return List.of(beam.move(Direction.RIGHT));
                } else if (beam.direction == Direction.DOWN) {
                    return List.of(beam.move(Direction.LEFT));
                } else if (beam.direction == Direction.RIGHT) {
                    return List.of(beam.move(Direction.UP));
                } else if (beam.direction == Direction.LEFT) {
                    return List.of(beam.move(Direction.DOWN));
                } else {
                    throw new IllegalStateException(beam.direction.name());
                }
            }
        },
        H_SPLITTER {
            @Override
            public List<Beam> encounter(Beam beam) {
                if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
                    return List.of(beam.move(Direction.LEFT), beam.move(Direction.RIGHT));
                } else if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
                    return List.of(beam.move());
                } else {
                    throw new IllegalStateException(beam.direction.name());
                }
            }
        },
        V_SPLITTER {
            @Override
            public List<Beam> encounter(Beam beam) {
                if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
                    return List.of(beam.move());
                } else if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
                    return List.of(beam.move(Direction.UP), beam.move(Direction.DOWN));
                } else {
                    throw new IllegalStateException(beam.direction.name());
                }
            }
        };

        abstract List<Beam> encounter(Beam beam);
     }
}

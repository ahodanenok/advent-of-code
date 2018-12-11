import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2018/day/6
 */
public class Day6 {

    public static void main(String[] args) {
        List<Location> locations = getLocations();
        part1(locations);
    }

    private static void part1(List<Location> locations) {
        int rowStart = Integer.MAX_VALUE;
        int rowEnd = Integer.MIN_VALUE;
        int colStart = Integer.MAX_VALUE;
        int colEnd = Integer.MIN_VALUE;

        // determine grid size
        for (Location loc : locations) {
            rowStart = Math.min(loc.row, rowStart);
            rowEnd = Math.max(loc.row, rowEnd);
            colStart = Math.min(loc.col, colStart);
            colEnd = Math.max(loc.col, colEnd);
        }

        int gridHeight = rowEnd - rowStart + 1;
        int gridWidth = colEnd - colStart + 1;
        int[][] grid = new int[gridHeight][gridWidth];

        // determine area around each destination location
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int col = colStart; col <= colEnd; col++) {
                Location gridLocation = new Location(row, col);

                int tiedCount = 1;
                int closestLocationIdx = 0;
                for (int i = 1; i < locations.size(); i++) {
                    int distanceToClosestLocation = gridLocation.distanceTo(locations.get(closestLocationIdx));
                    int distanceToDestinationLocation = gridLocation.distanceTo(locations.get(i));

                    if (distanceToDestinationLocation < distanceToClosestLocation) {
                        closestLocationIdx = i;
                        tiedCount = 1;
                    } else if (distanceToDestinationLocation == distanceToClosestLocation) {
                        tiedCount++;
                    }
                }

                grid[row - rowStart][col - colStart] = tiedCount == 1 ? closestLocationIdx : -1;
            }
        }

        Set<Integer> infiniteAreas = new HashSet<Integer>();
        // determine infinite areas, the ones on the edges
        for (int row = 0; row < gridHeight; row++) {
            infiniteAreas.add(grid[row][0]);
            infiniteAreas.add(grid[row][gridWidth - 1]);
        }
        for (int col = 0; col < gridWidth; col++) {
            infiniteAreas.add(grid[0][col]);
            infiniteAreas.add(grid[gridHeight - 1][col]);
        }

        Map<Integer, Integer> areaSizes = new HashMap<Integer, Integer>();
        // calculate area for each destination location
        for (int row = 0; row < gridHeight; row++) {
            for (int col = 0; col < gridWidth; col++) {
                int destLocation = grid[row][col];
                if (infiniteAreas.contains(destLocation)) {
                    continue;
                }

                if (areaSizes.containsKey(destLocation)) {
                    areaSizes.put(destLocation, areaSizes.get(destLocation) + 1);
                } else {
                    areaSizes.put(destLocation, 1);
                }
            }
        }

        int maxArea = Integer.MIN_VALUE;
        for (Map.Entry<Integer, Integer> entry : areaSizes.entrySet()) {
            maxArea = Math.max(entry.getValue(), maxArea);
        }

        System.out.println(maxArea);
    }

    private static List<Location> getLocations() {
        List<Location> locations = new ArrayList<Location>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().trim().split(",");
            locations.add(new Location(
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[0].trim())));
        }

        return locations;
    }

    private static class Location {

        private final int row;
        private final int col;

        Location(int row, int col) {
            this.row = row;
            this.col = col;
        }

        int distanceTo(Location loc) {
            return (int) (Math.abs(row - loc.row) + Math.abs(col - loc.col));
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return other.row == row && other.col == col;
        }
    }
}

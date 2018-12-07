import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2018/day/3
 */
public class Day3 {

    public static void main(String[] args) {
        List<Claim> claims = getClaims();
        part1(claims);
        part2(claims);
    }

    private static void part1(List<Claim> claims) {
        int[][] fabric = new int[1000][1000];
        for (Claim claim : claims) {
            for (int row = 0; row < claim.height; row++) {
                for (int col = 0; col < claim.width; col++) {
                    fabric[row + claim.top][col + claim.left]++;
                }
            }
        }

        int overlapCount = 0;
        for (int row = 0; row < fabric.length; row++) {
            for (int col = 0; col < fabric[row].length; col++) {
                if (fabric[row][col] > 1) {
                    overlapCount++;
                }
            }
        }

        System.out.println(overlapCount);
    }

    private static void part2(List<Claim> claims) {
        Map<Integer, Boolean> overlaps = new HashMap<Integer, Boolean>();

        int[][] fabric = new int[1000][1000];
        for (Claim claim : claims) {
            overlaps.put(claim.id, false);

            for (int row = 0; row < claim.height; row++) {
                for (int col = 0; col < claim.width; col++) {
                    if (fabric[row + claim.top][col + claim.left] != 0) {
                        overlaps.put(fabric[row + claim.top][col + claim.left], true);
                        overlaps.put(claim.id, true);
                    }

                    fabric[row + claim.top][col + claim.left] = claim.id;
                }
            }
        }

        for (Map.Entry<Integer, Boolean> entry : overlaps.entrySet()) {
            if (!entry.getValue()) {
                System.out.println(entry.getKey());
            }
        }
    }

    private static List<Claim> getClaims() {
        List<Claim> claims = new ArrayList<Claim>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            String[] offsetParts = parts[2].split(",");
            String[] dimensionParts = parts[3].split("x");

            int id = Integer.parseInt(parts[0].substring(1));
            int left = Integer.parseInt(offsetParts[0]);
            int top = Integer.parseInt(offsetParts[1].substring(0, offsetParts[1].length() - 1));
            int width = Integer.parseInt(dimensionParts[0]);
            int height = Integer.parseInt(dimensionParts[1]);

            claims.add(new Claim(id, left, top, width, height));
        }

        return claims;
    }

    private static class Claim {

        private final int id;
        private final int left;
        private final int top;
        private final int width;
        private final int height;

        Claim(int id, int left, int top, int width, int height) {
            this.id = id;
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("#%d @ %d,%d: %dx%d", id, left, top, width, height);
        }
    }
}

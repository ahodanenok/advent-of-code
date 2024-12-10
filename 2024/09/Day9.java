import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2024/day/9
 */
public class Day9 {

    public static void main(String... args) throws Exception {
        String diskMap = getInput();
        part1(diskMap);
    }

    private static void part1(String diskMap) {
        int blockCount = 0;
        for (int i = 0; i < diskMap.length(); i++) {
            blockCount += diskMap.charAt(i) - '0';
        }

        int[] blocks = new int[blockCount];
        int blockIdx = 0;
        boolean used = true;
        int fileId = 0;
        for (int i = 0; i < diskMap.length(); i++) {
            int n = diskMap.charAt(i) - '0';
            if (used) {
                for (int j = 0; j < n; j++) {
                    blocks[blockIdx++] = fileId;
                }

                fileId++;
            } else if (!used) {
                for (int j = 0; j < n; j++) {
                    blocks[blockIdx++] = -1;
                }
            }

            used = !used;
        }

        int freeIdx = 0;
        int fileIdx = blocks.length - 1;
        while (true) {
            while (freeIdx < blocks.length && blocks[freeIdx] != -1) {
                freeIdx++;
            }
            while (fileIdx >= 0 && blocks[fileIdx] == -1) {
                fileIdx--;
            }
            if (freeIdx >= fileIdx) {
                break;
            }

            blocks[freeIdx] = blocks[fileIdx];
            blocks[fileIdx] = -1;
        }

        long checksum = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != -1) {
                checksum += blocks[i] * i;
            }
        }

        System.out.println("Part 1: " + checksum);
    }

    private static String getInput() throws Exception {
        return new String(Files.readAllBytes(Paths.get("input.txt")));
    }
}

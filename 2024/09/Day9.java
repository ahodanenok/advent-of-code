import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2024/day/9
 */
public class Day9 {

    public static void main(String... args) throws Exception {
        String diskMap = getInput();
        part1(diskMap);
        part2(diskMap);
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

    private static void part2(String diskMap) {
        List<Blocks> files = new ArrayList<>();
        List<Blocks> freeSpace = new ArrayList<>();
        int fileId = 0;
        int offset = 0;
        boolean used = true;
        for (int i = 0; i < diskMap.length(); i++) {
            int n = diskMap.charAt(i) - '0';
            if (used) {
                files.add(new Blocks(fileId, offset, n));
                fileId++;
            } else {
                freeSpace.add(new Blocks(Blocks.EMPTY, offset, n));
            }

            used = !used;
            offset += n;
        }

        List<Blocks> movedFiles = new ArrayList<>();
        while (!files.isEmpty()) {
            Blocks file = files.remove(files.size() - 1);

            int freeIdx = -1;
            for (int i = 0; i < freeSpace.size(); i++) {
                Blocks free = freeSpace.get(i);
                if (free.offset> file.offset) {
                    break;
                }
                if (free.count >= file.count) {
                    freeIdx = i;
                    break;
                }
            }

            if (freeIdx != -1) {
                Blocks free = freeSpace.get(freeIdx);
                if (free.count == file.count) {
                    freeSpace.remove(freeIdx);
                } else {
                    freeSpace.set(freeIdx, new Blocks(
                        Blocks.EMPTY, free.offset + file.count, free.count - file.count));
                }

                movedFiles.add(new Blocks(file.fileId, free.offset, file.count));
            } else {
                movedFiles.add(file);
            }
        }

        long checksum = 0;
        for (Blocks file : movedFiles) {
            for (int i = 0; i < file.count; i++) {
                checksum += file.fileId * (file.offset + i);
            }
        }

        System.out.println("Part 2: " + checksum);
    }

    private static String getInput() throws Exception {
        return new String(Files.readAllBytes(Paths.get("input.txt")));
    }

    private static class Blocks {

        static final int EMPTY = -1;
    
        final int fileId;
        final int offset;
        final int count;

        Blocks(int fileId, int offset, int count) {
            this.fileId = fileId;
            this.offset = offset;
            this.count = count;
        }
    }
}

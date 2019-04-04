/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2017/day/14
 */
public class Day14 {

    public static void main(String[] args) {
        part1("jzgqcdpd");
    }

    private static void part1(String key) {
        int used = 0;
        for (int i = 0; i < 128; i++) {
            String row = hexToBinary(knotHash(key + "-" + i));
            for (int j = 0; j < row.length(); j++) {
                if (row.charAt(j) == '1') {
                    used++;
                }
            }
        }

        System.out.println(used);
    }

    private static String hexToBinary(String hex) {
        String binary = "";
        for (int i = 0; i < hex.length(); i++) {
            String b = Integer.toString(Integer.parseInt("" + hex.charAt(i), 16), 2);
            while (b.length() < 4) {
                b = "0" + b;
            }

            binary += b;
        }

        return binary;
    }

    private static String knotHash(String str) {
        int[] list = new int[256];
        for (int i = 0; i < list.length; i++) {
            list[i] = i;
        }
    
        int[] lengths = new int[str.length() + 5];
        for (int i = 0; i < str.length(); i++) {
            lengths[i] = (int) str.charAt(i);
        }

        // standard length suffix
        lengths[lengths.length - 5] = 17;
        lengths[lengths.length - 4] = 31;
        lengths[lengths.length - 3] = 73;
        lengths[lengths.length - 2] = 47;
        lengths[lengths.length - 1] = 23;

        int pos = 0;
        int skip = 0;
        // calculating sparse hash
        for (int round = 0; round < 64; round++) {
            for (int i = 0; i < lengths.length; i++) {
                int length = lengths[i];
                //System.out.println(Arrays.toString(list) + ", pos=" + pos + ", skip=" + skip + ", length=" + length);

                // reverse
                int left = pos;
                int right = (pos + length - 1) % list.length;
                int toMove = length / 2;
                while (toMove > 0) {
                    int tmp = list[left];
                    list[left] = list[right];
                    list[right] = tmp;

                    left = (left + 1) % list.length;
                    right = (list.length + right - 1) % list.length;
                    toMove--;
                }

                pos = (pos + length + skip) % list.length;
                skip++;
            }
        }

        // calculating dense hash
        int blockLength = 16;
        int blockCount = list.length / blockLength;
        int[] denseHash = new int[blockCount];
        for (int block = 0; block < list.length / blockLength; block++) {
            int n = list[block * blockLength];
            for (int i = block * blockLength + 1; i < (block + 1) * blockLength; i++) {
                n = n ^ list[i];
            }

            denseHash[block] = n;
        }

        // hexadecimal hash
        String hash = "";
        for (int i = 0; i < denseHash.length; i++) {
            String hex = Integer.toString(denseHash[i], 16);
            if (hex.length() == 1) {
                hash += "0";
            }
            hash += hex;
        }

        return hash;
    }
}

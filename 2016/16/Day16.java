/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2016/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        //System.out.println(generateData(3, "1"));
        //System.out.println(generateData(3, "0"));
        //System.out.println(generateData(11, "11111"));
        //System.out.println(generateData(25, "111100001010"));

        //System.out.println(generateChecksum("10000011110010000111"));
        
        System.out.println(generateChecksum(generateData(272, "01000100010010111")));
        System.out.println(generateChecksum(generateData(35651584, "01000100010010111")));
    }

    public static String generateData(int size, String initialData) {
        String data = initialData;
        while (data.length() < size) {
            StringBuilder sb = new StringBuilder(data);
            sb.reverse();
            for (int i = 0; i < sb.length(); i++) {
                if (sb.charAt(i) == '0') {
                    sb.setCharAt(i, '1');
                } else if (sb.charAt(i) == '1') {
                    sb.setCharAt(i, '0');
                } else {
                    throw new IllegalArgumentException(initialData);
                }
            }

            data = data + "0" + sb.toString();
        }

        return data.substring(0, size);
    }

    public static String generateChecksum(String data) {
        StringBuilder checksum = new StringBuilder();
        for (int i = 1; i < data.length(); i += 2) {
            char a = data.charAt(i - 1);
            char b = data.charAt(i);
            if (a == '0' && b == '0' || a == '1' && b == '1') {
                checksum.append('1');
            } else {
                checksum.append('0');
            }
        }

        //System.out.println(checksum);
        if (checksum.length() % 2 == 0) {
            return generateChecksum(checksum.toString());
        } else {
            return checksum.toString();
        }
    }
}

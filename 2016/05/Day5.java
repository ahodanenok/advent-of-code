import java.security.MessageDigest;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2016/day/5
 */
public class Day5 {

    private static final MessageDigest MD5;
    static {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MD5 = md5;
    }

    public static void main(String[] args) {
        part1("uqwqemis", 8);
    }

    private static void part1(String doorId, int digits) {
        String password = "";
        int idx = 0;
        for (int i = 0; i < digits; i++) {
            String hash;
            while (true) {
                hash = getMD5(doorId + idx);
                idx++;
                if (hash.substring(0, 5).equals("00000")) {
                    break;
                }
            }

            //System.out.println("hash=" + hash + ", idx=" + idx);
            password += hash.charAt(5);
        }

        System.out.println(password);
    }

    private static String getMD5(String str) {
        byte[] digest = null;
        try {
            digest = MD5.digest(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        String hash = "";
        for (int i = 0; i < digest.length; i++) {
            hash += Integer.toString((digest[i] & 0xF0) >> 4, 16); // hi
            hash += Integer.toString(digest[i] & 0x0F, 16);        // lo
        }

        while (hash.length() < 32) {
            hash = "0" + hash;
        }

        return hash;
    }
}

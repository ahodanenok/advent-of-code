import java.security.MessageDigest;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 14
 * https://adventofcode.com/2016/day/14
 */
public class Day14 {

    private static final MessageDigest MD5;
    static {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }

        MD5 = md;
    }

    public static void main(String[] args) {
        part1(64, "ihaygndm");
    }

    private static void part1(int count, String salt) {
        int idx = -1;
        int generatedCount = 0;
        List<String> cache = new ArrayList<String>();
        while (generatedCount < count) {
            idx++;
            String hash;
            if (cache.size() > idx) {
                hash = cache.get(idx);
            } else {
                hash = getMD5(salt + idx);
                cache.add(hash);
            }

            if (isKey(hash, idx, salt, cache)) {
                generatedCount++;
                //System.out.println("Generated=" + generatedCount + ", idx=" + idx + ", hash=" + hash);
            }
        }

        System.out.println(idx);
    }

    private static boolean isKey(String hash, int idx, String salt, List<String> cache) {
        char lookupChar = '\0';
        for (int i = 2; i < hash.length(); i++) {
            char a = hash.charAt(i - 2);
            char b = hash.charAt(i - 1);
            char c = hash.charAt(i);
            if (a == b && a == c) {
                lookupChar = a;
                break;
            }
        }

        if (lookupChar == '\0') {
            return false;
        }

        for (int i = 1; i <= 1000; i++) {
            String nextHash;
            if (cache.size() > idx + i) {
                nextHash = cache.get(idx + i);
            } else {
                nextHash = getMD5(salt + (idx + i));
                cache.add(nextHash);
            }

            for (int j = 4; j < nextHash.length(); j++) {
                char a = nextHash.charAt(j - 4);
                char b = nextHash.charAt(j - 3);
                char c = nextHash.charAt(j - 2);
                char d = nextHash.charAt(j - 1);
                char e = nextHash.charAt(j);
                if (a == lookupChar 
                        && b == lookupChar
                        && c == lookupChar 
                        && d == lookupChar
                        && e == lookupChar) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String getMD5(String str) {
        byte[] digest = null;
        try {
            digest = MD5.digest(str.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (digest == null) {
            return null;
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

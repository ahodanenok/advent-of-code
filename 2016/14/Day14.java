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
        System.out.println(generateKeys(64, "ihaygndm", false));
        System.out.println(generateKeys(64, "ihaygndm", true));
    }

    private static int generateKeys(int count, String salt, boolean useStretching) {
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
                if (useStretching) {
                    hash = stretch(hash);
                }

                cache.add(hash);
            }

            if (isKey(hash, idx, salt, useStretching, cache)) {
                generatedCount++;
                //System.out.println("Generated=" + generatedCount + ", idx=" + idx + ", hash=" + hash);
            }
        }

        return idx;
    }

    private static String stretch(String hash) {
        String stretched = hash;
        for (int i = 0; i < 2016; i++) {
            stretched = getMD5(stretched);
        }

        if (stretched.equals(hash)) {
            return getMD5(hash);
        } else {
            return stretched;
        }
    }

    private static boolean isKey(String hash, int idx, String salt, boolean useStretching, List<String> cache) {
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
                if (useStretching) {
                    nextHash = stretch(nextHash);
                }

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

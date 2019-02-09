import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2016/day/4
 */
public class Day4 {

    public static void main(String[] args) {
        List<Room> rooms = getRooms();
        part1(rooms);
        part2(rooms);
    }

    private static void part1(List<Room> rooms) {
        int sectorIdSum = 0;
        for (Room r : rooms) {
            if (r.isValid()) {
                sectorIdSum += r.sectorId;
            }
        }

        System.out.println(sectorIdSum);
    }

    private static void part2(List<Room> rooms) {
        for (Room r : rooms) {
            if (r.isValid() && r.getDecryptedName().equals("northpole object storage")) {
                System.out.println(r.sectorId);
                break;
            }
        }
    }

    private static List<Room> getRooms() {
        List<Room> rooms = new ArrayList<Room>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            rooms.add(new Room(line));
        }

        return rooms;
    }

    private static class Room {

        private final String encryptedName;
        private final int sectorId;
        private final String checksum;

        Room(String name) {
            int checksumStart = name.indexOf('[');
            this.checksum = name.substring(checksumStart + 1, name.length() - 1);

            int sectorSeparatorPos = name.lastIndexOf('-');
            this.encryptedName = name.substring(0, sectorSeparatorPos);
            this.sectorId = Integer.parseInt(name.substring(sectorSeparatorPos + 1, checksumStart));
        }

        String getDecryptedName() {
            String decryptedName = "";
            for (int i = 0; i < encryptedName.length(); i++) {
                char ch = encryptedName.charAt(i);
                if (!Character.isAlphabetic(ch)) {
                    decryptedName += " ";
                } else {
                    int num = 97 + ((ch - 97 + sectorId) % 26);
                    decryptedName += (char) num;
                }
            }
            //System.out.println(decryptedName);
            return decryptedName;
        }

        boolean isValid() {
            Map<Character, Integer> chars = new HashMap<Character, Integer>();
            for (int i = 0; i < encryptedName.length(); i++) {
                char ch = encryptedName.charAt(i);
                if (!Character.isAlphabetic(ch)) {
                    continue;
                }

                if (chars.containsKey(ch)) {
                    chars.put(ch, chars.get(ch) + 1);
                } else {
                    chars.put(ch, 1);
                }
            }

            List<Map.Entry<Character, Integer>> entries =
                    new ArrayList<Map.Entry<Character, Integer>>(chars.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<Character, Integer>>() {
                public int compare(Map.Entry<Character, Integer> a, Map.Entry<Character, Integer> b) {
                    if (a.getValue() > b.getValue()) {
                        return -1;
                    } else if (a.getValue() < b.getValue()) {
                        return 1;
                    } else {
                        return Character.compare(a.getKey(), b.getKey());
                    }
                }
            });

            String generatedChecksum = "";
            for (int i = 0; i < 5; i++) {
                Map.Entry<Character, Integer> entry = entries.get(i);
                generatedChecksum += entry.getKey();
            }
            //System.out.println("checksum=" + checksum + ", generatedChecksum=" + generatedChecksum);
            return checksum.equals(generatedChecksum);
        }
    }
}

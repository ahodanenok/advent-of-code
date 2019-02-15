import java.util.Scanner;

/**
 * Advent of Code - Day 9
 * https://adventofcode.com/2016/day/9
 */
public class Day9 {

    public static void main(String[] args) throws Exception {
        String compressed = new Scanner(System.in).nextLine();
        part1(compressed);
    }

    private static void part1(String compressed) {
        int length = 0;
        int pos = 0;
        while (pos < compressed.length()) {
            Marker marker = findMarker(compressed, pos);
            if (marker != null) {
                length += (marker.startPos - pos);
                length += (marker.sequenceLength * marker.repeat);
                pos = marker.endPos + marker.sequenceLength + 1;
            } else {
                length += (compressed.length() - pos);
                pos = compressed.length();
            }
        }

        System.out.println(length);
    }

    private static Marker findMarker(String compressed, int startFrom) {
        int startPos = -1;
        for (int i = startFrom; i < compressed.length(); i++) {
            if (compressed.charAt(i) == '(') {
                startPos = i;
                break;
            }
        }

        if (startPos == -1) {
            return null;
        }

        int endPos = -1;
        for (int i = startPos + 1; i < compressed.length(); i++) {
            if (compressed.charAt(i) == ')') {
                endPos = i;
                break;
            }
        }

        if (endPos == -1) {
            return null;
        }

        String[] markerInfo = compressed.substring(startPos + 1, endPos).split("x");

        Marker marker = new Marker();
        marker.startPos = startPos;
        marker.endPos = endPos;
        marker.sequenceLength = Integer.parseInt(markerInfo[0]);
        marker.repeat = Integer.parseInt(markerInfo[1]);
        return marker;
    }

    private static class Marker {

        private int startPos;
        private int endPos;
        private int sequenceLength;
        private int repeat;
    }
}

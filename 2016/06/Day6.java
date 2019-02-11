import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2016/day/6
 */
public class Day6 {

    public static void main(String[] args) throws Exception {
        List<String> messages = getMessages();
        part1(messages);
        part2(messages);
    }

    private static List<String> getMessages() {
        List<String> messages = new ArrayList<String>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            messages.add(scanner.nextLine().trim());
        }

        return messages;
    }

    private static void part1(List<String> messages) {
        List<Map<Character, Integer>> freq = getCharFrequences(messages);

        String msg = "";
        for (int i = 0; i < freq.size(); i++) {
            int maxFreq = Integer.MIN_VALUE;
            char maxFreqChar = '\0';
            for (Map.Entry<Character, Integer> entry : freq.get(i).entrySet()) {
                if (entry.getValue() > maxFreq) {
                    maxFreq = entry.getValue();
                    maxFreqChar = entry.getKey();
                }
            }

            msg += maxFreqChar;
        }

        System.out.println(msg);
    }

    private static void part2(List<String> messages) {
        List<Map<Character, Integer>> freq = getCharFrequences(messages);

        String msg = "";
        for (int i = 0; i < freq.size(); i++) {
            int minFreq = Integer.MAX_VALUE;
            char minFreqChar = '\0';
            for (Map.Entry<Character, Integer> entry : freq.get(i).entrySet()) {
                if (entry.getValue() < minFreq) {
                    minFreq = entry.getValue();
                    minFreqChar = entry.getKey();
                }
            }

            msg += minFreqChar;
        }

        System.out.println(msg);
    }

    private static List<Map<Character, Integer>> getCharFrequences(List<String> messages) {
        List<Map<Character, Integer>> freq = new ArrayList<Map<Character, Integer>>();

        int messageLength = messages.get(0).length();
        for (int i = 0; i < messageLength; i++) {
            freq.add(new HashMap<Character, Integer>());
        }

        for (String msg : messages) {
            for (int i = 0; i < messageLength; i++) {
                char ch = msg.charAt(i);
                if (freq.get(i).containsKey(ch)) {
                    freq.get(i).put(ch, freq.get(i).get(ch) + 1);
                } else {
                    freq.get(i).put(ch, 1);
                }
            }
        }

        return freq;
    }
}

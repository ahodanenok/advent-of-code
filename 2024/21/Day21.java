import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2024/day/21
 */
public class Day21 {

	public static void main(String... args) throws Exception {
        List<String> codes = getInput();
        System.out.println("Part 1: " + length(codes, 2));
        System.out.println("Part 2: " + length(codes, 25));
    }

    private static long length(List<String> codes, int level) {
        List<String> numpad = List.of("789", "456", "123", " 0A");
        Map<Move, List<String>> numseq = sequence(numpad);

        List<String> dirpad = List.of(" ^A", "<v>");
        Map<Move, List<String>> dirseq = sequence(dirpad);

        Map<String, Integer> lengths = new HashMap<>();
        for (Map.Entry<Move, List<String>> entry : numseq.entrySet()) {
            for (String sequence : entry.getValue()) {
                lengths.put(sequence, length(sequence, dirseq));
            }
        }
        for (Map.Entry<Move, List<String>> entry : dirseq.entrySet()) {
            for (String sequence : entry.getValue()) {
                lengths.put(sequence, length(sequence, dirseq));
            }
        }

        Map<Move, String> numseqMin = new HashMap<>();
        for (Map.Entry<Move, List<String>> entry : numseq.entrySet()) {
            numseqMin.put(
                entry.getKey(),
                entry.getValue().stream()
                    .min((s1, s2) -> Integer.compare(lengths.get(s1), lengths.get(s2)))
                    .get());
        }

        Map<Move, String> dirseqMin = new HashMap<>();
        for (Map.Entry<Move, List<String>> entry : dirseq.entrySet()) {
            dirseqMin.put(
                entry.getKey(),
                entry.getValue().stream()
                    .min((s1, s2) -> Integer.compare(lengths.get(s1), lengths.get(s2)))
                    .get());
        }
        Map<String, Long> cache = new HashMap<>();
        long sum = 0;
        for (String code : codes) {
            long seqLength = 0;
            char prevKey = 'A';
            for (char key : code.toCharArray()) {
                seqLength += length(numseqMin.get(new Move(prevKey, key)), dirseqMin, level, cache);
                prevKey = key;
            }

            sum += Integer.parseInt(code.substring(0, code.length() - 1)) * seqLength;
        }

        return sum;
    }

    private static long length(String seq, Map<Move, String> keyseq, int level, Map<String, Long> cache) {
        if (level == 0) {
            return seq.length() + 1;
        }

        String cacheKey = seq + "_" + level;
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        long length = 0;
        char prevKey = 'A';
        for (char key : (seq + 'A').toCharArray()) {
            length += length(keyseq.get(new Move(prevKey, key)), keyseq, level - 1, cache);
            prevKey = key;
        }
        cache.put(cacheKey, length);

        return length;
    }

    private static int length(String seq, Map<Move, List<String>> keyseq) {
        int length = 0;

        char prevA = 'A';
        for (char a : (seq + 'A').toCharArray()) {
            int minLengthB = Integer.MAX_VALUE;
            for (String sa : keyseq.get(new Move(prevA, a))) {
                int lengthB = 0;
                char prevB = 'A';
                for (char b : (sa + 'A').toCharArray()) {
                    lengthB += keyseq.get(new Move(prevB, b)).get(0).length();
                    prevB = b;
                }
                
                if (lengthB < minLengthB) {
                    minLengthB = lengthB;
                }
            }

            length += minLengthB;
            prevA = a;
        }

        return length;
    }

    private static Map<Move, List<String>> sequence(List<String> keypad) {
        Map<Move, List<String>> sequences = new HashMap<>();

        int height = keypad.size();
        int width = keypad.get(0).length();
        for (int fromRow = 0; fromRow < height; fromRow++) {
            for (int fromCol = 0; fromCol < width; fromCol++) {
                char fromKey = keypad.get(fromRow).charAt(fromCol);
                if (fromKey == ' ') {
                    continue;
                }

                for (int toRow = 0; toRow < height; toRow++) {
                    for (int toCol = 0; toCol < width; toCol++) {
                        char toKey = keypad.get(toRow).charAt(toCol);
                        if (toKey == ' ') {
                            continue;
                        }

                        sequences.put(
                            new Move(fromKey, toKey),
                            sequencesTo(new Position(fromRow, fromCol), new Position(toRow, toCol), keypad));
                    }
                }
            }
        }

        return sequences;
    }

    private static List<String> sequencesTo(Position fromPosition, Position toPosition, List<String> keypad) {
        if (fromPosition.equals(toPosition)) {
            return List.of("");
        }

        int height = keypad.size();
        int width = keypad.get(0).length();
        int distance = Math.abs(fromPosition.row - toPosition.row) + Math.abs(fromPosition.col - toPosition.col);

        List<String> sequences = new LinkedList<>();
        LinkedList<SequenceState> queue = new LinkedList<>();
        queue.add(new SequenceState(fromPosition, "", Set.of()));
        while (!queue.isEmpty()) {
            SequenceState state = queue.poll();
            if (state.sequence.length() > distance) {
                continue;
            }

            if (state.position.equals(toPosition)) {
                sequences.add(state.sequence);
                continue;
            }

            for (Direction direction : Direction.values()) {
                Position next = new Position(state.position.row + direction.dy, state.position.col + direction.dx);
                if (next.row < 0 || next.col < 0 || next.row >= height || next.col >= width) {
                    continue;
                }

                char nextKey = keypad.get(next.row).charAt(next.col);
                if (nextKey == ' ') {
                    continue;
                }

                if (state.seen.contains(nextKey)) {
                    continue;
                }

                Set<Character> nextSeen = new HashSet<>(state.seen);
                nextSeen.add(nextKey);
                queue.add(new SequenceState(next, state.sequence + direction.key, nextSeen));
            }
        }

        return sequences;
    }

    private static List<String> getInput() throws Exception {
        return Files.readAllLines(Paths.get("input.txt"));
    }

    private static class Move {

        final char fromKey;
        final char toKey;

        Move(char fromKey, char toKey) {
            this.fromKey = fromKey;
            this.toKey = toKey;
        }

        @Override
        public boolean equals(Object obj) {
            Move other = (Move) obj;
            return fromKey == other.fromKey && toKey == other.toKey;
        }

        @Override
        public int hashCode() {
            return 31 * fromKey + toKey;
        }
    }

    private static class Position {
    
        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            Position other = (Position) obj;
            return row == other.row && col == other.col;
        }
    }

    private enum Direction {

        TOP('^', -1, 0),
        BOTTOM('v', 1, 0),
        LEFT('<', 0, -1),
        RIGHT('>', 0, 1);

        final char key;
        final int dy;
        final int dx;

        Direction(char key, int dy, int dx) {
            this.key = key;
            this.dy = dy;
            this.dx = dx;
        }
    }

    private static class SequenceState {

        final Position position;
        final String sequence;
        final Set<Character> seen;

        SequenceState(Position position, String sequence, Set<Character> seen) {
            this.position = position;
            this.sequence = sequence;
            this.seen = seen;
        }
    }
}

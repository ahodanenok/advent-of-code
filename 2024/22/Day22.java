import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2024/day/22
 */
public class Day22 {

	private static final int SEQUENCE_LENGTH = 2000;

	public static void main(String... args) throws Exception {
		List<Long> numbers = getInput();
		part1(numbers);
		part2(numbers);
	}

	private static void part1(List<Long> numbers) {
		long sum = 0;
		for (long initialNumber : numbers) {
			sum += generateSequence(initialNumber, SEQUENCE_LENGTH).get(SEQUENCE_LENGTH);
		}

		System.out.println("Part 1: " + sum);
	}

	private static void part2(List<Long> numbers) {
		List<List<Long>> sequences = numbers.stream()
			.map(initialNumber -> generateSequence(initialNumber, SEQUENCE_LENGTH))
			.collect(Collectors.toList());

		List<Index> indices = sequences.stream()
			.map(Day22::createIndex)
			.collect(Collectors.toList());

		long maxEarned = 0;
		for (int a = -9; a < 10; a++) {
			for (int b = -9; b < 10; b++) {
				for (int c = -9; c < 10; c++) {
					for (int d = -9; d < 10; d++) {
						long earned = 0;
						for (int i = 0; i < sequences.size(); i++) {
							int pos = indices.get(i).lookup(List.of(a, b, c, d));
							if (pos != -1) {
								earned += sequences.get(i).get(pos) % 10;
							}
						}

						if (earned > maxEarned) {
							maxEarned = earned;
						}
					}
				}
			}
		}

		System.out.println("Part 2: " + maxEarned);
	}

	private static List<Long> generateSequence(long initialNumber, int length) {
		List<Long> sequence = new ArrayList<>(length + 1);
		sequence.add(initialNumber);

		long number = initialNumber;
		for (int i = 0; i < length; i++) {
			number = generateNumber(number);
			sequence.add(number);
		}

		return sequence;
	}

	private static long generateNumber(long number) {
		long result;

		result = ((number * 64) ^ number) % 16777216;
		result = ((result / 32) ^ result) % 16777216;
		result = ((result * 2048) ^ result) % 16777216;

		return result;
	}

	private static Index createIndex(List<Long> sequence) {
		Index index = new Index();
		LinkedList<Integer> changes = new LinkedList<>();
		for (int pos = 1; pos < sequence.size(); pos++) {
			changes.addLast((int) (sequence.get(pos) % 10 - sequence.get(pos - 1) % 10));
			if (changes.size() > 4) {
				changes.removeFirst();
			}

			index.add(changes, pos);
		}

		return index;
	}

	private static List<Long> getInput() throws Exception {
		return Files.readAllLines(Paths.get("input.txt")).stream()
			.map(Long::valueOf)
			.collect(Collectors.toList());
	}

	private static class Index {

		final SortedSet<Integer> positions = new TreeSet<>();
		final Map<Integer, Index> children = new HashMap<>();

		void add(List<Integer> changes, int pos) {
			if (changes.isEmpty()) {
				positions.add(pos);
				return;
			}

			Integer digit = changes.get(0);

			Index child;
			if (!children.containsKey(digit)) {
				child = new Index();
				children.put(digit, child);
			} else {
				child = children.get(digit);
			}

			child.add(changes.subList(1, changes.size()), pos);
		}

		int lookup(List<Integer> changes) {
			if (changes.isEmpty()) {
				return !positions.isEmpty() ? positions.first() : -1;
			}

			Index child = children.get(changes.get(0));
			if (child == null) {
				return -1;
			}

			return child.lookup(changes.subList(1, changes.size()));
		}
	}
}

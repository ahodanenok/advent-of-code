import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2024/day/22
 */
public class Day22 {

	public static void main(String... args) throws Exception {
		List<Long> numbers = getInput();
		part1(numbers);
	}

	private static void part1(List<Long> numbers) {
		long sum = 0;
		for (long initialNumber : numbers) {
			long number = initialNumber;
			for (int i = 0; i < 2000; i++) {
				number = generateNumber(number);
			}

			sum += number;
		}

		System.out.println("Part 1: " + sum);
	}

	private static long generateNumber(long number) {
		long result;

		result = ((number * 64) ^ number) % 16777216;
		result = ((result / 32) ^ result) % 16777216;
		result = ((result * 2048) ^ result) % 16777216;

		return result;
	}

	private static List<Long> getInput() throws Exception {
		return Files.readAllLines(Paths.get("input.txt")).stream()
			.map(Long::valueOf)
			.collect(Collectors.toList());
	}
}

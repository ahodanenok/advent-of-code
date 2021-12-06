import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Advent of Code - Day 6
 * https://adventofcode.com/2021/day/6
 */
public class Day6 {
	
	public static void main(String[] args) throws Exception {
		List<Integer> fish = getInput();
		part1(fish);
	}

	private static List<Integer> getInput() throws Exception {
		List<Integer> fish = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
			for (String part : reader.readLine().trim().split(",")) {
				fish.add(Integer.parseInt(part.trim()));
			}
		}

		return fish;
	}

	private static void part1(List<Integer> input) {
		LinkedList<Integer> fish = new LinkedList<>(input);
		for (int d = 0; d < 80; d++) {
			int checkCount = fish.size();
			while (checkCount > 0) {
				int f = fish.poll();

				f--;
				if (f < 0) {
					fish.offer(6);
					fish.offer(8);
				} else {
					fish.offer(f);
				}

				checkCount--;
			}
		}

		System.out.println("Part 1: " + fish.size());
	}
}

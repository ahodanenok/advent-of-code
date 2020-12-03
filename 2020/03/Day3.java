import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 3
 * https://adventofcode.com/2020/day/3
 */
public class Day3 {

	public static void main(String[] args) throws Exception {
		List<String> pattern = getPattern();
		
		int treeCount = 0;
		int row = 0;
		int col = 0;
		int height = pattern.size();
		int width = pattern.get(0).length();
		while (row < height) {
			if (pattern.get(row).charAt(col) == '#') {
				treeCount++;
			}

			col = (col + 3) % width;
			row++;
		}
		
		System.out.println("Part 1: " + treeCount);
	}

	private static List<String> getPattern() throws Exception {
		List<String> pattern = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				pattern.add(line);
			}
		}
		
		return pattern;
	}
}

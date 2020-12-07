import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2020/day/5
 */
public class Day5 {

	public static void main(String[] args) throws Exception {
		List<String> passes = getPasses();
		
		int maxSeatId = 0;
		for (String pass : passes) {
			//System.out.printf("row=%d, col=%d%n", getRow(pass), getCol(pass));
			maxSeatId = Math.max(getRow(pass) * 8 + getCol(pass), maxSeatId);
		}

		System.out.println("Part 1: " + maxSeatId);
	}
	
	private static int getRow(String pass) {
		int from = 0;
		int to = 128;
		for (int i = 0; i < 7; i++) {
			char ch = pass.charAt(i);
			if (ch == 'B') {
				from = from + (to - from) / 2;
			} else if (ch == 'F') {
				to = from + (to - from) / 2;
			} else {
				throw new IllegalStateException(pass);
			}
		}
		
		return from;
	}
	
	private static int getCol(String pass) {
		int from = 0;
		int to = 8;
		for (int i = pass.length() - 3; i < pass.length(); i++) {
			char ch = pass.charAt(i);
			if (ch == 'R') {
				from = from + (to - from) / 2;
			} else if (ch == 'L') {
				to = from + (to - from) / 2;
			} else {
				throw new IllegalStateException(pass);
			}
		}

		return from;
	}

	private static List<String> getPasses() throws Exception {
		List<String> passes = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				passes.add(line);
			}
		}
		
		return passes;
	}
}

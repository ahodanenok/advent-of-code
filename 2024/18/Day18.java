import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code - Day 18
 * https://adventofcode.com/2024/day/18
 */
public class Day18 {

	private static final int GRID_SIZE = 71;

	public static void main(String... args) throws Exception {
		List<Position> bytes = getInput();
		part1(bytes);
		part2(bytes);
	}

	private static void part1(List<Position> bytes) {
		System.out.println("Part 1: " + simulate(bytes.subList(0, 1024)));
	}

	private static void part2(List<Position> bytes) {
		for (int i = 1025; i < bytes.size(); i++) {
			if (simulate(bytes.subList(0, i)) == -1) {
				Position b = bytes.get(i - 1);
				System.out.println(String.format("Part 2: %s,%s", b.col, b.row));
				break;
			}
		}
	}

	private static int simulate(List<Position> bytes) {
		Position start = new Position(0, 0);
		Position end = new Position(GRID_SIZE - 1, GRID_SIZE - 1);
		Set<Position> corrupted = new HashSet<>(bytes);

		Map<Position, Integer> distances = new HashMap<>();
		distances.put(start, 0);

		LinkedList<Position> queue = new LinkedList<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Position position = queue.poll();
			if (position.equals(end)) {
				break;
			}

			for (Position next : List.of(
					new Position(position.row - 1, position.col),
					new Position(position.row + 1, position.col),
					new Position(position.row, position.col - 1),
					new Position(position.row, position.col + 1))) {
				if (next.row < 0 || next.col < 0 || next.row >= GRID_SIZE || next.col >= GRID_SIZE) {
					continue;
				}

				if (corrupted.contains(next)) {
					continue;
				}

				if (distances.containsKey(next)) {
					continue;
				}

				distances.put(next, distances.get(position) + 1);
				queue.add(next);
			}
		}

		return distances.getOrDefault(end, -1);
	}

	private static List<Position> getInput() throws Exception {
		List<Position> bytes = new ArrayList<>();
		for (String line : Files.readAllLines(Paths.get("input.txt"))) {
			String[] parts = line.split(",");
			bytes.add(new Position(Integer.parseInt(parts[1]), Integer.parseInt(parts[0])));
		}

		return bytes;
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

		@Override
		public int hashCode() {
			return 31 * row + col;
		}
	}
}

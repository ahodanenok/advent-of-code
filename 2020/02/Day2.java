import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 2
 * https://adventofcode.com/2020/day/2
 */
public class Day2 {

	public static void main(String[] args) throws Exception {
		List<Password> passwords = getPasswords();
		
		int validCount = 0;
		for (Password p : passwords) {
			int times = 0;
			for (int i = 0; i < p.value.length(); i++) {
				if (p.value.charAt(i) == p.requiredChar) {
					times++;
				}
			}
			
			if (times >= p.minTimes && times <= p.maxTimes) {
				validCount++;
			}
		}
		
		System.out.println("Part 1: " + validCount);
	}
	
	private static List<Password> getPasswords() throws Exception {
		List<Password> passwords = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(":");
				String[] policyParts = parts[0].split(" ");
				String[] policyTimesParts = policyParts[0].split("-");
				
				Password p = new Password();
				p.value = parts[1];
				p.requiredChar = policyParts[1].charAt(0);
				p.minTimes = Integer.parseInt(policyTimesParts[0]);
				p.maxTimes = Integer.parseInt(policyTimesParts[1]);
				
				passwords.add(p);
			}
		}

		return passwords;
	}
	
	private static class Password {
	
		String value;
		char requiredChar;
		int minTimes;
		int maxTimes;
	}
}

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 4
 * https://adventofcode.com/2020/day/4
 */
public class Day4 {

	public static void main(String[] args) throws Exception {
		List<Passport> passports = getPassports();
		
		int validCount = 0;
		for (Passport p : passports) {
			if (p.byr == null
				|| p.iyr == null
				|| p.eyr == null
				|| p.hgt == null
				|| p.hcl == null
				|| p.ecl == null
				|| p.pid == null) {
				
				continue;
			}
			
			validCount++;
		}
		
		System.out.println("Part 1: " + validCount);
	}
	
	private static List<Passport> getPassports() throws Exception {
		List<Passport> passports = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
			String line;
			Passport current = new Passport();
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					passports.add(current);
					current = new Passport();
					continue;
				}

				String[] parts = line.split(" ");
				for (String part : parts) {
					if (part.startsWith("byr:")) {
						current.byr = part.substring(4);
					} else if (part.startsWith("iyr:")) {
						current.iyr = part.substring(4);
					} else if (part.startsWith("eyr:")) {
						current.eyr = part.substring(4);
					} else if (part.startsWith("hgt:")) {
						current.hgt = part.substring(4);
					} else if (part.startsWith("hcl:")) {
						current.hcl = part.substring(4);
					} else if (part.startsWith("ecl:")) {
						current.ecl = part.substring(4);
					} else if (part.startsWith("pid:")) {
						current.pid = part.substring(4);
					} else if (part.startsWith("cid:")) {
						current.cid = part.substring(4);
					} else {
						throw new IllegalStateException(part);
					}
				}
			}
			
			passports.add(current);
		}
		
		return passports;
	}
	
	private static class Passport {
		String byr; // (Birth Year)
		String iyr; // (Issue Year)
		String eyr; // (Expiration Year)
		String hgt; // (Height)
		String hcl; // (Hair Color)
		String ecl; // (Eye Color)
		String pid; // (Passport ID)
		String cid; // (Country ID)
	}
}

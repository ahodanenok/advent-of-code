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
		part1(passports);
		part2(passports);
	}
	
	private static void part1(List<Passport> passports) {
		int validCount = 0;
		for (Passport p : passports) {
			if (hasRequiredFields(p)) {
				validCount++;				
			}
		}
		
		System.out.println("Part 1: " + validCount);
	}
	
	private static void part2(List<Passport> passports) {
		int validCount = 0;
		for (Passport p : passports) {
			if (hasRequiredFields(p) && isValid(p)) {
				validCount++;				
			}
		}
		
		System.out.println("Part 2: " + validCount);
	}

	private static boolean hasRequiredFields(Passport p) {
		if (p.byr == null
			|| p.iyr == null
			|| p.eyr == null
			|| p.hgt == null
			|| p.hcl == null
			|| p.ecl == null
			|| p.pid == null) {
			
			return false;
		}
		
		return true;
	}
	
	private static boolean isValid(Passport p) {
		int byr = Integer.parseInt(p.byr);
		if (byr < 1920 || byr > 2002) {
			return false;
		}

		int iyr = Integer.parseInt(p.iyr);
		if (iyr < 2010 || iyr > 2020) {
			return false;
		}

		int eyr = Integer.parseInt(p.eyr);
		if (eyr < 2020 || eyr > 2030) {
			return false;
		}

		if (p.hgt.length() < 4) {
			return false;
		}

		String hgtMst = p.hgt.substring(p.hgt.length() - 2);
		if (!"cm".equals(hgtMst) && !"in".equals(hgtMst)) {
			return false;
		}
		
		int hgt = Integer.parseInt(p.hgt.substring(0, p.hgt.length() - 2));
		if ("cm".equals(hgtMst) && (hgt < 150 || hgt > 193)) {
			return false;
		}
		if ("in".equals(hgtMst) && (hgt < 59 || hgt > 76)) {
			return false;
		}
		
		String hcl = p.hcl;
		if (hcl.length() != 7) {
			return false;
		}
		if (hcl.charAt(0) != '#') {
			return false;
		}
		for (int i = 1; i < hcl.length(); i++) {
			char ch = hcl.charAt(i);
			if ((ch < '0' || ch > '9') && (ch < 'a' || ch > 'f')) {
				return false;
			}
		}
		
		if (!"amb".equals(p.ecl)
			&& !"blu".equals(p.ecl)
			&& !"brn".equals(p.ecl)
			&& !"gry".equals(p.ecl)
			&& !"grn".equals(p.ecl)
			&& !"hzl".equals(p.ecl)
			&& !"oth".equals(p.ecl)) {

			return false;
		}
		
		if (p.pid.length() != 9) {
			return false;
		}
		
		try {
			Integer.parseInt(p.pid);
		} catch (Exception e) {
			return false;
		}
		
		return true;
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
						current.byr = part.substring(4).trim();
					} else if (part.startsWith("iyr:")) {
						current.iyr = part.substring(4).trim();
					} else if (part.startsWith("eyr:")) {
						current.eyr = part.substring(4).trim();
					} else if (part.startsWith("hgt:")) {
						current.hgt = part.substring(4).trim();
					} else if (part.startsWith("hcl:")) {
						current.hcl = part.substring(4).trim();
					} else if (part.startsWith("ecl:")) {
						current.ecl = part.substring(4).trim();
					} else if (part.startsWith("pid:")) {
						current.pid = part.substring(4).trim();
					} else if (part.startsWith("cid:")) {
						current.cid = part.substring(4).trim();
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

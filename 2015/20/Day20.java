import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2015/day/20
 */
public class Day20 {

    public static void main(String[] args) {
        int presentsCount = 29_000_000;
        part1(presentsCount);
        part2(presentsCount);

        //System.out.println(visitedByElves(1));
        //System.out.println(visitedByElves(2));
        //System.out.println(visitedByElves(3));
        //System.out.println(visitedByElves(4));
        //System.out.println(visitedByElves(5));
        //System.out.println(visitedByElves(6));
        //System.out.println(visitedByElves(7));
        //System.out.println(visitedByElves(8));
        //System.out.println(visitedByElves(9));
    }

    private static void part1(int presentsCount) {
        int houseNum = 1;
        while (presentsByHouse(houseNum, 10, Integer.MAX_VALUE) < presentsCount) {
            houseNum++;
        }

        System.out.println(houseNum);
    }

    private static void part2(int presentsCount) {
        int houseNum = 1;
        while (presentsByHouse(houseNum, 11, 50) < presentsCount) {
            houseNum++;
        }

        System.out.println(houseNum);
    }

    private static List<Integer> visitedByElves(int houseNum) {
        List<Integer> elves = new ArrayList<Integer>();
        elves.add(1);

        int elf = 2;
        int stopAt = houseNum / 2;
        while (elf <= stopAt) {
            if (houseNum % elf == 0) {
                elves.add(elf);
            }

            elf++;
        }

        elves.add(houseNum);
        return elves;
    }

    private static int presentsByHouse(int houseNum, int presentsPerElf, int limit) {
        List<Integer> elves = visitedByElves(houseNum);
        int presents = 0;
        for (Integer elf : elves) {
            if (houseNum / elf > limit) {
                continue;
            }

            presents += elf * presentsPerElf;
        }

        return presents;
    }
}

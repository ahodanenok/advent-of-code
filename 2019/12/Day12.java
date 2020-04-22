import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.math.BigInteger;

/** 
 * Advent of Code - Day 12
 * https://adventofcode.com/2019/day/12
 */
public class Day12 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        List<Moon> moons = getMoons();
        move(moons, 1000);

        int et = 0;
        for (Moon m : moons) {
            et += e(m);
        }

        System.out.println("Part 1: " + et);
    }

    // Thanks to:
    // https://www.reddit.com/r/adventofcode/comments/e9j0ve/2019_day_12_solutions/#t1_fampmi3
    private static void part2() {
        List<Moon> origMoons = getMoons();
        List<Moon> moons = getMoons();

        long[] stepsForOrbit = new long[3];
        Arrays.fill(stepsForOrbit, -1);

        long steps = 0;

        simulate:
        while (true) { //hashes.add(moons.hashCode())) {
            move(moons, 1);
            steps++;

            boolean xAtStart = true;
            boolean yAtStart = true;
            boolean zAtStart = true;
            for (int i = 0; i < moons.size(); i++) {
                if (moons.get(i).x != origMoons.get(i).x) xAtStart = false;  
                if (moons.get(i).y != origMoons.get(i).y) yAtStart = false;  
                if (moons.get(i).z != origMoons.get(i).z) zAtStart = false;  
            }

            if (xAtStart && stepsForOrbit[0] == -1) stepsForOrbit[0] = steps + 1;
            if (yAtStart && stepsForOrbit[1] == -1) stepsForOrbit[1] = steps + 1;
            if (zAtStart && stepsForOrbit[2] == -1) stepsForOrbit[2] = steps + 1;

            for (int i = 0; i < stepsForOrbit.length; i++) {
                if (stepsForOrbit[i] == -1) {
                    continue simulate;
                }
            } 

            break;
        }

        BigInteger result = BigInteger.valueOf(stepsForOrbit[0]);
        for (int i = 1; i < stepsForOrbit.length; i++) {
            BigInteger s = BigInteger.valueOf(stepsForOrbit[i]);
            result = result.multiply(s).divide(result.gcd(s));
        }
       
        System.out.println("Part 2: " + result); 
    }

    private static void move(List<Moon> moons, int times) {
        for (int step = 0; step < times; step++) {
            for (Moon a : moons) {
                for (Moon b : moons) {
                    if (a.x < b.x) a.vx++;
                    if (a.x > b.x) a.vx--;
                    if (a.y < b.y) a.vy++;
                    if (a.y > b.y) a.vy--;
                    if (a.z < b.z) a.vz++; 
                    if (a.z > b.z) a.vz--; 
                }
            }

            for (Moon m : moons) {
                m.x += m.vx;
                m.y += m.vy;
                m.z += m.vz;
            }
        }
    }

    private static int e(Moon m) {
        return pe(m) * ke(m);
    }

    private static int pe(Moon m) {
        return (int) (Math.abs(m.x) + Math.abs(m.y) + Math.abs(m.z));
    }

    private static int ke(Moon m) {
        return (int) (Math.abs(m.vx) + Math.abs(m.vy) + Math.abs(m.vz));
    }

    private static List<Moon> getMoons() {
        return Arrays.asList(
            new Moon(-16, -1, -12),
            new Moon(0, -4, -17),
            new Moon(-11, 11, 0),
            new Moon(2, 2, -6)); 
    }

    private static class Moon {

        private int x;
        private int y;
        private int z;

        private int vx;
        private int vy;
        private int vz;

        Moon(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String toString() {
            return String.format("(%4d, %4d, %4d | %4d, %4d, %4d)", x, y, z, vx, vy, vz);
        }

        @Override
        public int hashCode() {
            int h = x;
            h = h * 31 + y;
            h = h * 31 + z;
            //h = h * 31 + vx;
            //h = h * 31 + vx;
            //h = h * 31 + vx;
            return h;
        } 

        @Override
        public boolean equals(Object obj) {
            Moon other = (Moon) obj;
            return x == other.x && y == other.y && z == other.z;
                //&& vx == other.vx && vy == other.vy && vz == other.vz;
        }
    }
}


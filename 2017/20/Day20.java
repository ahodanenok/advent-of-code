import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2017/day/20
 */
public class Day20 {

    public static void main(String[] args) {
        List<Particle> particles1 = getParticles();
        List<Particle> particles2 = new ArrayList<Particle>(particles1.size());
        for (Particle p : particles1) {
            particles2.add(p.copy());
        }

        part1(particles1);
        part2(particles2);
    }

    private static void part1(List<Particle> particles) {
        Particle closest = null;
        int count = 0;
        while (true) {
            Particle minDistParticle = null;
            long minDist = Long.MAX_VALUE;
            for (Particle p : particles) {
                p.step();
                if (p.distance() < minDist) {
                    minDist = p.distance();
                    minDistParticle = p;
                }
            }

            if (closest != null && closest.num != minDistParticle.num) {
                count = 0;
            }

            closest = minDistParticle;

            count++;
            // wait till nothing changes for long enough
            if (count == 1000) {
                break;
            }
        }

        System.out.println(closest.num);
    }

    private static void part2(List<Particle> particles) {
        int particlesLeft = particles.size();
        int count = 0;
        while (true) {
            for (Particle p : particles) {
                p.step();
            }

            Map<Location, List<Particle>> collisions = new HashMap<Location, List<Particle>>();
            for (Particle p : particles) {
                Location loc = new Location(p.x, p.y, p.z);
                if (!collisions.containsKey(loc)) {
                    List<Particle> list = new ArrayList<Particle>();
                    list.add(p);
                    collisions.put(loc, list);
                } else {
                    collisions.get(loc).add(p);
                }
            }

            for (List<Particle> particlesAtLocation : collisions.values()) {
                if (particlesAtLocation.size() > 1) {
                    for (Particle p : particlesAtLocation) {
                        particles.remove(p);
                    }
                }
            }

            if (particlesLeft != particles.size()) {
                count = 0;
            }

            particlesLeft = particles.size();
            count++;
            // wait till nothing changes for long enough
            if (count == 1000) {
                break;
            }
        }

        System.out.println(particlesLeft);
    }

    private static List<Particle> getParticles() {
        List<Particle> particles = new ArrayList<Particle>();

        int num = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(", ");
            String[] posParts = parts[0].substring(3, parts[0].length() - 1).split(",");
            String[] velParts = parts[1].substring(3, parts[1].length() - 1).split(",");
            String[] accParts = parts[2].substring(3, parts[2].length() - 1).split(",");

            Particle p = new Particle();
            p.num = num++;
            p.x = Long.parseLong(posParts[0]);
            p.y = Long.parseLong(posParts[1]);
            p.z = Long.parseLong(posParts[2]);
            p.vX = Long.parseLong(velParts[0]);
            p.vY = Long.parseLong(velParts[1]);
            p.vZ = Long.parseLong(velParts[2]);
            p.aX = Long.parseLong(accParts[0]);
            p.aY = Long.parseLong(accParts[1]);
            p.aZ = Long.parseLong(accParts[2]);

            particles.add(p);
        }

        return particles;
    }

    private static class Location {

        private long x;
        private long y;
        private long z;

        Location(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            return (int) (31 * 31 * x + 31 * y + z);
        }

        @Override
        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return x == other.x && y == other.y && z == other.z;
        }
    }

    private static class Particle {

        private int num;

        private long x;
        private long y;
        private long z;

        private long vX;
        private long vY;
        private long vZ;

        private long aX;
        private long aY;
        private long aZ;

        void step() {
            vX += aX;
            vY += aY;
            vZ += aZ;
            x += vX;
            y += vY;
            z += vZ;
        }

        long distance() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }

        Particle copy() {
            Particle copy = new Particle();
            copy.x = x;
            copy.y = y;
            copy.z = z;
            copy.vX = vX;
            copy.vY = vY;
            copy.vZ = vZ;
            copy.aX = aX;
            copy.aY = aY;
            copy.aZ = aZ;

            return copy;
        }
    }
}

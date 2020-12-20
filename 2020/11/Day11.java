import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2020/day/11
 */
public class Day11 {

    public static void main(String[] args) throws Exception {
        Set<Seat> seats = getSeats();
        part1(seats);
        part2(seats);
    }

    private static void part1(Set<Seat> seats) {
        Set<Seat> occupied = new HashSet<>();
        occupied.addAll(seats);
        while (true) {
            Set<Seat> next = new HashSet<>();
            for (Seat seat : seats) {
                int nc = 0;
                if (occupied.contains(seat.top())) nc++;
                if (occupied.contains(seat.topRight())) nc++;
                if (occupied.contains(seat.right())) nc++;
                if (occupied.contains(seat.bottomRight())) nc++;
                if (occupied.contains(seat.bottom())) nc++;
                if (occupied.contains(seat.bottomLeft())) nc++;
                if (occupied.contains(seat.left())) nc++;
                if (occupied.contains(seat.topLeft())) nc++;

                if (!occupied.contains(seat) && nc == 0) {
                    next.add(seat);
                } else if (occupied.contains(seat) && nc > 3) {
                    continue;
                } else if (occupied.contains(seat)) {
                    next.add(seat);
                }
            }

            if (occupied.equals(next)) {
                break;
            }

            occupied = next;
        }

        System.out.println("Part 1: " + occupied.size());
    }

    private static void part2(Set<Seat> seats) {
        int width = Integer.MIN_VALUE;
        int height = Integer.MIN_VALUE;
        for (Seat seat : seats) {
            width = Math.max(seat.col, width);
            height = Math.max(seat.row, height);
        }
        width++;
        height++;

        Set<Seat> occupied = new HashSet<>();
        occupied.addAll(seats);
        while (true) {
            /*System.out.println();
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (occupied.contains(new Seat(row, col))) {
                        System.out.print('#');
                    } else if (seats.contains(new Seat(row, col))) {
                        System.out.print('L');
                    } else {
                        System.out.print('.');
                    }
                }

                System.out.println();
            }*/

            Set<Seat> next = new HashSet<>();
            for (Seat seat : seats) {
                int nc = 0;
                if (isVisibleOccupied(seat, s -> s.top(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.topRight(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.right(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.bottomRight(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.bottom(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.bottomLeft(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.left(), width, height, seats, occupied)) nc++;
                if (isVisibleOccupied(seat, s -> s.topLeft(), width, height, seats, occupied)) nc++;

                //System.out.printf("(%d, %d) -> %d%n", seat.row, seat.col, nc);

                if (!occupied.contains(seat) && nc == 0) {
                    next.add(seat);
                } else if (occupied.contains(seat) && nc > 4) {
                    continue;
                } else if (occupied.contains(seat)) {
                    next.add(seat);
                }
            }

            if (occupied.equals(next)) {
                break;
            }

            occupied = next;
        }

        System.out.println("Part 2: " + occupied.size());
    }

    private static boolean isVisibleOccupied(Seat from, 
                                             Function<Seat, Seat> next,
                                             int width,
                                             int height,
                                             Set<Seat> seats,
                                             Set<Seat> occupied) {
        Seat current = from;
        while (true) {
            current = next.apply(current);

            if (occupied.contains(current)) {
                return true;
            }

            if (seats.contains(current)) {
                return false;
            }

            if (current.row < 0 
                    || current.row >= height
                    || current.col < 0
                    || current.col >= width) {
                return false;
            }
        }
    }

    private static Set<Seat> getSeats() throws Exception {
        Set<Seat> seats = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            int row = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int col = 0; col < line.length(); col++) {
                    if (line.charAt(col) == 'L') {
                        seats.add(new Seat(row, col));
                    }
                }

                row++;
            }
        }

        return seats;
    }

    private static class Seat {

        final int row;
        final int col;

        Seat(int row, int col) {
            this.row = row;
            this.col = col;
        }

        Seat top() {
            return new Seat(row - 1, col);
        }

        Seat topRight() {
            return new Seat(row - 1, col + 1);
        }

        Seat right() {
            return new Seat(row, col + 1);
        }

        Seat bottomRight() {
            return new Seat(row + 1, col + 1);
        }

        Seat bottom() {
            return new Seat(row + 1, col);
        }

        Seat bottomLeft() {
            return new Seat(row + 1, col - 1);
        }

        Seat left() {
            return new Seat(row, col - 1);
        }

        Seat topLeft() {
            return new Seat(row - 1, col - 1);
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            Seat s = (Seat) obj;
            return s.row == row && s.col == col;
        }
    }
}

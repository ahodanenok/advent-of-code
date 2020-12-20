import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Set;
import java.util.HashSet;

/**
 * Advent of Code - Day 11
 * https://adventofcode.com/2020/day/11
 */
public class Day11 {

    public static void main(String[] args) throws Exception {
        Set<Seat> seats = getSeats();

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

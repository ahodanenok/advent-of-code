import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class Day9 {

    public static void main(String[] args) throws Exception {
        List<Long> numbers = getNumbers();

        int preambleSize = 25;
        for (int i = preambleSize; i < numbers.size(); i++) {
            long n = numbers.get(i);

            boolean valid = false;

            check:
            for (int j = i - preambleSize; j < i; j++) {
                for (int k = j + 1; k < i; k++) {
                    if (numbers.get(j) + numbers.get(k) == n) {
                        valid = true;
                        break check;
                    }
                }
            }

            if (!valid) {
                System.out.println("Part 1: " + n);
                break;
            }
        }
    }

    private static List<Long> getNumbers() throws Exception {
        List<Long> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Long.parseLong(line));
            }
        }

        return numbers;
    }
}

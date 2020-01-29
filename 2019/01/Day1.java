import java.io.FileReader;
import java.io.BufferedReader;
import java.util.List;
import java.util.ArrayList;

public class Day1 {
         
    public static void main(String[] args) throws Exception {
        int fuel_1 = 0;
        int fuel_2 = 0;
        for (int mass : getMasses()) {
            int fuel = calcFuel(mass);
            int fuelMass = fuel;

            fuel_1 += fuel;
	    do { fuel_2 += fuelMass; } while ((fuelMass = calcFuel(fuelMass)) > 0);
        }

        System.out.println("Part 1: " + fuel_1);
        System.out.println("Part 2: " + fuel_2);
    } 

    private static int calcFuel(int mass) {
        return mass / 3 - 2;
    }

    private static List<Integer> getMasses() throws Exception {
        List<Integer> masses = new ArrayList<>(); 
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line; 
            while ((line = reader.readLine()) != null) { 
                masses.add(Integer.parseInt(line.trim())); 
            } 
        }  

        return masses;
    }
}


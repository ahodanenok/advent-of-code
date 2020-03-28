import java.util.Arrays;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2019/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        byte[] signal = getSignal(1);
        byte[] output = phases(signal, 0, 100);
        System.out.println("Part 1: " + toString(Arrays.copyOfRange(output, 0, 8)));
    }

    private static void part2() {
        byte[] signal = getSignal(10000);
        int offset = Integer.parseInt(toString(Arrays.copyOfRange(signal, 0, 7)));
        byte[] output = phases(signal, offset, 100);
        System.out.println("Part 2: " + toString(Arrays.copyOfRange(output, offset, offset + 8)));
    }

    private static String toString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte i : arr) {
            sb.append(i);
        }

        return sb.toString();
    }

    private static byte[] getSignal(int repeats) {
        String input = "59717238168580010599012527510943149347930742822899638247083005855483867484356055489419913512721095561655265107745972739464268846374728393507509840854109803718802780543298141398644955506149914796775885246602123746866223528356493012136152974218720542297275145465188153752865061822191530129420866198952553101979463026278788735726652297857883278524565751999458902550203666358043355816162788135488915722989560163456057551268306318085020948544474108340969874943659788076333934419729831896081431886621996610143785624166789772013707177940150230042563041915624525900826097730790562543352690091653041839771125119162154625459654861922989186784414455453132011498";
 
        byte[] signal = new byte[input.length() * repeats];
        for (int i = 0; i < signal.length; i++) {
            signal[i] = Byte.parseByte(input.charAt(i % input.length()) + "");
        }

        return signal;
    }

    private static byte[] phases(byte[] signal, int offset, int count) {
        byte[] pattern = new byte[] { 0, 1, 0, -1 };

        byte[] input = signal;
        byte[] output = null;
        for (int phase = 0; phase < count; phase++) {
            output = new byte[input.length];

            int i = offset;
            while (i < input.length / 2) {
                int element = 0;

                for (int j = i; j < input.length; j++) {
                    int p = (j + 1) / (i + 1) % pattern.length;
                    element += input[j] * pattern[p];
                }

                output[i] = (byte) Math.abs(element % 10);
                i++;
            }
           
            // After the first half pattern will be zeroes following by ones: 
            //  1
            //  11
            //  111
            //  1111
            //  11111
            //  etc
            if (i < input.length) {
                int sum = 0;
                for (int j = i; j < input.length; j++) {
                    sum += input[j];
                }

                while (i < input.length) {
                    output[i] = (byte) (sum % 10);
                    sum -= input[i];
                    i++;
                }
            }

            input = output;
        }

        return output;
    }
}


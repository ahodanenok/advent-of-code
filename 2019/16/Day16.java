import java.util.Arrays;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2019/day/16
 */
public class Day16 {

    public static void main(String[] args) {
        int[] result = phases(getSignal(), 100); 
        System.out.println("Part 1: " + toString(java.util.Arrays.copyOfRange(result, 0, 8)));
    }

    private static String toString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i : arr) {
            sb.append(i);
        }

        return sb.toString();
    }

    private static int[] getSignal() {
        String input = "59717238168580010599012527510943149347930742822899638247083005855483867484356055489419913512721095561655265107745972739464268846374728393507509840854109803718802780543298141398644955506149914796775885246602123746866223528356493012136152974218720542297275145465188153752865061822191530129420866198952553101979463026278788735726652297857883278524565751999458902550203666358043355816162788135488915722989560163456057551268306318085020948544474108340969874943659788076333934419729831896081431886621996610143785624166789772013707177940150230042563041915624525900826097730790562543352690091653041839771125119162154625459654861922989186784414455453132011498";

        int[] signal = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            signal[i] = Integer.parseInt(input.substring(i, i + 1));
        }

        return signal;
    }

    private static int[] phases(int[] signal, int count) {
        int[] pattern = new int[] { 0, 1, 0, -1 };

        int[] input = signal;
        int[] output = null;
        for (int phase = 0; phase < count; phase++) {
            output = new int[input.length];

            for (int i = 0; i < input.length; i++) {
                int element = 0;
                for (int j = 0; j < input.length; j++) {
                    int p = (j + 1) / (i + 1) % pattern.length;
                    element += input[j] * pattern[p];

                }

                output[i] = Math.abs(element % 10);
            }

            input = output;
        }

        return output;
    }
}


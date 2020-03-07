import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 8
 * https://adventofcode.com/2019/day/8
 */
public class Day8 {

    public static void main(String[] args) throws Exception {
        List<Integer> data = getInput();
        Image img = new Image(25, 6, data);
        part1(img); 
    }

    private static void part1(Image img) {
        int layerWithFewestZeroes = -1;
        int layerWithFewestZeroesCount = Integer.MAX_VALUE;
        for (int n = 0; n < img.layersCount(); n++) {
            int zeroCount = 0;
            int[] layer = img.layer(n);
            for (int i = 0; i < layer.length; i++) {
                if (layer[i] == 0) {
                    zeroCount++;
                }
            }

            if (zeroCount < layerWithFewestZeroesCount) {
                layerWithFewestZeroes = n;
                layerWithFewestZeroesCount = zeroCount;
            } 
        }

        int oneDigitsCount = 0;
        int twoDigitsCount = 0;
        int[] layer = img.layer(layerWithFewestZeroes);
        for (int i = 0; i < layer.length; i++) {
            if (layer[i] == 1) {
                oneDigitsCount++;
            }

            if (layer[i] == 2) {
                twoDigitsCount++;
            }
        }

        System.out.println("Part 1: " + (oneDigitsCount * twoDigitsCount));
    }

    private static List<Integer> getInput() throws Exception {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String str = reader.readLine();
            for (int i = 0; i < str.length(); i++) {
                data.add(Integer.parseInt(str.substring(i, i + 1)));
            }
        }

        return data;
    }

    private static class Image {

        private int width;
        private int height;
        private int[][] layers;

        Image(int width, int height, List<Integer> data) {
            this.width = width;
            this.height = height;
            this.layers = new int[data.size() / (width * height)][width * height];
            for (int i = 0; i < data.size(); i++) {
                int layer = i / (width * height);
                int d = data.get(i);
                this.layers[layer][i % (width * height)] = d;
            }
        }

        int layersCount() {
            return layers.length;
        }

        int[] layer(int n) {
            return layers[n];
        }
    }
}


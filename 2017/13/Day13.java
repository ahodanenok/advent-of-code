import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 13
 * https://adventofcode.com/2017/day/13
 */
public class Day13 {

    public static void main(String[] args) {
        List<Layer> layers = getLayers();
        part1(layers);
        part2(layers);
    }

    private static List<Layer> getLayers() {
        List<Layer> layers = new ArrayList<Layer>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(":");
            Layer def = new Layer();
            def.depth = Integer.parseInt(parts[0].trim());
            def.range = Integer.parseInt(parts[1].trim());
            layers.add(def);
        }

        return layers;
    }

    private static void part1(List<Layer> layers) {
        Firewall fw = new Firewall(layers);

        int severity = 0;
        for (Layer layer : sendPacket(fw)) {
            severity += layer.depth * layer.range;
        }

        System.out.println(severity);
    }

    private static void part2(List<Layer> layers) {
        Firewall fw = new Firewall(layers);

        int delay = 0;
        while (true) {
            if (sendPacket(fw.copy()).size() == 0) {
                break;
            }

            fw.scan();
            delay++;
        }

        System.out.println(delay);
    }

    private static List<Layer> sendPacket(Firewall fw) {
        List<Layer> caught = new ArrayList<Layer>();
        for (int layerIdx = 0; layerIdx < fw.length(); layerIdx++) {
            if (fw.hasScannerAt(layerIdx) && fw.scannerPos(layerIdx) == 0) {
                caught.add(fw.layer(layerIdx));
            }

            fw.scan();
        }

        return caught;
    }

    private static class Firewall {

        private int length;
        private Map<Integer, LayerState> layers;

        private Firewall() { }

        Firewall(List<Layer> layers) {
            this.layers = new HashMap<Integer, LayerState>();
            for (Layer layer : layers) {
                LayerState state = new LayerState();
                state.layer = layer;
                this.layers.put(layer.depth, state);
            }

            int maxDepth = 0;
            for (Layer layer : layers) {
                maxDepth = Math.max(maxDepth, layer.depth);
            }
            this.length = maxDepth + 1;
        }

        int length() {
            return length;
        }

        boolean hasScannerAt(int layerIdx) {
            return layers.containsKey(layerIdx);
        }

        int scannerPos(int layerIdx) {
            return layers.get(layerIdx).scannerPos;
        }

        Layer layer(int layerIdx) {
            return layers.get(layerIdx).layer;
        }

        void scan() {
            for (LayerState state : layers.values()) {
                if (!state.moveTop && state.scannerPos + 1 >= state.layer.range) {
                    state.moveTop = true;
                } else if (state.moveTop && state.scannerPos == 0) {
                    state.moveTop = false;
                }

                if (state.moveTop) {
                    state.scannerPos--;
                } else {
                    state.scannerPos++;
                }
            }
        }

        Firewall copy() {
            Firewall copy = new Firewall();
            copy.length = length;
            copy.layers = new HashMap<Integer, LayerState>();
            for (LayerState state : layers.values()) {
                LayerState stateCopy = new LayerState();
                stateCopy.moveTop = state.moveTop;
                stateCopy.scannerPos = state.scannerPos;
                stateCopy.layer = state.layer;
                copy.layers.put(state.layer.depth, stateCopy);
            }

            return copy;
        }
    }

    private static class LayerState {

        private boolean moveTop;
        private int scannerPos;
        private Layer layer;
    }

    private static class Layer {

        private int depth;
        private int range;
    }
}

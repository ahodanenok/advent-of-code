import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Advent of Code - Day 20
 * https://adventofcode.com/2023/day/20
 */
public class Day20 {

    public static void main(String[] args) throws Exception {
        part1();
    }

    private static void part1() throws Exception {
        Map<String, Module> modules = getInput();
        int lowCount = 0;
        int highCount = 0;
        for (int n = 0; n < 1000; n++) {
            LinkedList<Signal> queue = new LinkedList<>();
            queue.addLast(new Signal("button", "broadcaster", Pulse.LO));
            while (!queue.isEmpty()) {
                Signal currentSignal = queue.removeFirst();
                if (currentSignal.pulse == Pulse.LO) {
                    lowCount++;
                } else if (currentSignal.pulse == Pulse.HI) {
                    highCount++;
                }

                for (Signal outputSignal : modules.get(currentSignal.to).receiveSignal(currentSignal)) {
                    queue.addLast(outputSignal);
                }
            }
        }

        System.out.println("Part 1: " + (lowCount * highCount));
    }

    private static Map<String, Module> getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Map<String, Module> modules = new HashMap<>();
            Map<String, String[]> connections = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+->\\s+");
                String name = parts[0];
                String[] outputs = parts[1].split(",\\s+");
                if (name.equals("broadcaster")) {
                    modules.put(name, new BroadcastModule());
                    connections.put(name, outputs);
                } else if (name.charAt(0) == '%') {
                    modules.put(name.substring(1), new FlipFlopModule());
                    connections.put(name.substring(1), outputs);
                } else if (name.charAt(0) == '&') {
                    modules.put(name.substring(1), new ConjunctionModule());
                    connections.put(name.substring(1), outputs);
                } else {
                    throw new IllegalStateException("Unknown module type: '" + name + "'");
                }
            }

            modules.get("broadcaster").connectInput("button");
            for (Map.Entry<String, String[]> entry : connections.entrySet()) {
                Module module = modules.get(entry.getKey());
                for (String output : entry.getValue()) {
                    if (!modules.containsKey(output)) {
                        modules.put(output, new DummyModule());
                    }

                    module.connectOutput(output);
                    modules.get(output).connectInput(entry.getKey());
                }
            }

            return modules;
        }
    }

    abstract static class Module {

        protected final List<String> inputs = new ArrayList<>();
        protected final List<String> outputs = new ArrayList<>();

        void connectInput(String name) {
            inputs.add(name);
        }

        void connectOutput(String name) {
            outputs.add(name);
        }

        final List<Signal> receiveSignal(Signal signal) {
            if (!inputs.contains(signal.from)) {
                throw new IllegalStateException("Received signal from non-connected module: " + signal.from);
            }

            return doReceiveSignal(signal);
        }

        abstract List<Signal> doReceiveSignal(Signal signal);
    }

    static class BroadcastModule extends Module {

        @Override
        List<Signal> doReceiveSignal(Signal signal) {
            return outputs.stream()
                .map(output -> new Signal(signal.to, output, signal.pulse))
                .collect(java.util.stream.Collectors.toList());
        }
    }

    static class FlipFlopModule extends Module {

        private boolean on = false;

        @Override
        List<Signal> doReceiveSignal(Signal signal) {
            if (signal.pulse == Pulse.HI) {
                return List.of();
            }

            Pulse pulse = on ? Pulse.LO : Pulse.HI;
            on = !on;

            return outputs.stream()
                .map(output -> new Signal(signal.to, output, pulse))
                .collect(java.util.stream.Collectors.toList());
        }
    }

    static class ConjunctionModule extends Module {

        private final Map<String, Pulse> received = new HashMap<>();

        @Override
        List<Signal> doReceiveSignal(Signal signal) {
            received.put(signal.from, signal.pulse);

            Pulse pulse;
            if (inputs.stream().allMatch(in -> received.getOrDefault(in, Pulse.LO) == Pulse.HI)) {
                pulse = Pulse.LO;
            } else {
                pulse = Pulse.HI;
            }

            return outputs.stream()
                .map(output -> new Signal(signal.to, output, pulse))
                .collect(java.util.stream.Collectors.toList());
        }
    }

    static class DummyModule extends Module {

        @Override
        void connectOutput(String name) {
            throw new IllegalStateException("Dummy module doesn't have outputs");
        }

        @Override
        List<Signal> doReceiveSignal(Signal signal) {
             return List.of();
        }
    }

    static class Signal {

        final String from;
        final String to;
        final Pulse pulse;

        Signal(String from, String to, Pulse pulse) {
            this.from = from;
            this.to = to;
            this.pulse = pulse;
        }
    }

    enum Pulse {

        HI,
        LO;
    }
}

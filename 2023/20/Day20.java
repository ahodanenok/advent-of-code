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
        part2();
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

    private static void part2() throws Exception {
        Map<String, Module> modules = getInput();

        // The circuit consists of multiple counters connected to a conjunction module,
        // which in turn is connected to the rx module
        // Each counter moves at its own increment and for the conjunction module to send a low pulse to the rx module
        // the number of button presses must be divisable by each counter's increment
        // So the answer will be the lowest common multiplier of increments
        Module rx = modules.get("rx");
        if (rx.inputs.size() != 1) {
            throw new IllegalStateException("Different circuit: multiple inputs to rx");
        }

        Module rxInput = modules.get(rx.inputs.get(0));
        if (!(rxInput instanceof ConjunctionModule)) {
            throw new IllegalStateException("Different circuit: input to rx is not a conjunction module");
        }

        int counter = 0;
        Map<String, Integer> increments = new HashMap<>();

        btnLoop:
        while (true) {
            counter++;

            LinkedList<Signal> queue = new LinkedList<>();
            queue.addLast(new Signal("button", "broadcaster", Pulse.LO));
            while (!queue.isEmpty()) {
                Signal currentSignal = queue.removeFirst();
                // collect increment for each input to the conjunction module
                if (rxInput.inputs.contains(currentSignal.from)
                        && currentSignal.to.equals(rx.inputs.get(0))
                        && currentSignal.pulse == Pulse.HI
                        && !increments.containsKey(currentSignal.from)) {
                    increments.put(currentSignal.from, counter);
                }

                // all increments have been determined
                if (increments.size() == rxInput.inputs.size()) {
                    break btnLoop;
                }

                for (Signal outputSignal : modules.get(currentSignal.to).receiveSignal(currentSignal)) {
                    queue.addLast(outputSignal);
                }
            }
        }

        List<Integer> commonFactors = new ArrayList<>();
        for (Integer p : increments.values()) {
            List<Integer> factors = factorize(p);
            for (Integer f : factors) {
                long neededCount = factors.stream().filter(n -> n.equals(f)).count();
                long currentCount = commonFactors.stream().filter(n -> n.equals(f)).count();
                while (currentCount < neededCount) {
                    commonFactors.add(f);
                    currentCount++;
                }
            }
        }

        System.out.println("Part 2: " + commonFactors.stream()
            .mapToLong(n -> n.longValue()).reduce(1, (n1, n2) -> n1 * n2));
    }

    private static List<Integer> factorize(int n) {
        List<Integer> factors = new ArrayList<>();
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(n);
        while (!queue.isEmpty()) {
            int f = queue.removeLast();
            boolean divisable = false;
            for (int i = 2; i < f / 2; i++) {
                if (f % i == 0) {
                    queue.addLast(i);
                    queue.addLast(f / i);
                    divisable = true;
                    break;
                }
            }

            if (!divisable) {
                factors.add(f);
            }
        }

        return factors;
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

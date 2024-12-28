import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2024/day/24
 */
public class Day24 {

	public static void main(String... args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        Circuit circuit = getInput();
        for (Map.Entry<String, Boolean> entry : new HashMap<>(circuit.wires).entrySet()) {
            circuit.connections.get(entry.getKey()).forEach(gate -> gate.accept(entry.getValue()));
        }

        int b = 0;
        long n = 0;
        while (true) {
            String wire = String.format("z%02d", b);
            if (!circuit.wires.containsKey(wire)) {
                break;
            }

            if (circuit.wires.get(wire)) {
                n |= 1L << b;
            }

            b++;
        }

        System.out.println("Part 1: " + n);
    }

    private static void part2() throws Exception {
        Set<String> wires = new java.util.HashSet<>();
        Set<String> gates = new java.util.HashSet<>();
        StringBuilder connections = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while (!(line = reader.readLine()).isEmpty());

            int gateIdx = 0;
            Pattern gatePattern = Pattern.compile("^([a-z0-9]+) (XOR|OR|AND) ([a-z0-9]+) -> ([a-z0-9]+)$");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = gatePattern.matcher(line);
                if (!matcher.find()) {
                    throw new IllegalStateException(line);
                }

                String outWire = matcher.group(4);
                String gate = matcher.group(2) + "_" + (gateIdx++);
                String wireA = matcher.group(1);
                String wireB = matcher.group(3);

                connections.append(wireA + "->" + gate + ";");
                connections.append(wireB + "->" + gate + ";");
                connections.append(gate + "->" + outWire + ";");
                wires.add(wireA);
                wires.add(wireB);
                wires.add(outWire);
                gates.add(gate);
            }
        }

        String graphTemplate = """
            digraph Circuit {
                fontname="Helvetica,Arial,sans-serif"
                node [fontname="Helvetica,Arial,sans-serif"]
                edge [fontname="Helvetica,Arial,sans-serif"]
                rankdir=LR;
                node [shape=box]; %s;
                node [shape=circle,fixedsize=true,width=0.9]; %s;
                %s

                overlap=false
                fontsize=12;
            }
        """;

        Files.write(
            Paths.get("circuit.dot"),
            String.format(
                graphTemplate,
                gates.stream().collect(Collectors.joining(";")),
                wires.stream().collect(Collectors.joining(";")),
                connections).getBytes());

        // dot.exe -Tsvg circuit.dot -o circuit.svg
        // examine carefully...

        System.out.println("Part 2: fcd,fhp,hmk,rvf,tpc,z16,z20,z33");
    }

    private static Circuit getInput() throws Exception {
        Circuit circuit = new Circuit();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                String[] parts = line.split(":");
                circuit.wires.put(parts[0].trim(), parts[1].trim().equals("1"));
            }
            Pattern gatePattern = Pattern.compile("^([a-z0-9]+) (XOR|OR|AND) ([a-z0-9]+) -> ([a-z0-9]+)$");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = gatePattern.matcher(line);
                if (!matcher.find()) {
                    throw new IllegalStateException(line);
                }

                String outWire = matcher.group(4);
                Gate gate = switch (matcher.group(2)) {
                    case "XOR" -> new XorGate(circuit, outWire);
                    case "OR" -> new OrGate(circuit, outWire);
                    case "AND" -> new AndGate(circuit, outWire);
                    default -> throw new IllegalStateException(matcher.group(2));
                };

                circuit.connections.computeIfAbsent(matcher.group(1), __ -> new ArrayList<>()).add(gate::setA);
                circuit.connections.computeIfAbsent(matcher.group(3), __ -> new ArrayList<>()).add(gate::setB);
            }
        }

        return circuit;
    }

    private static class Circuit {

        final Map<String, Boolean> wires = new HashMap<>();
        final Map<String, List<Consumer<Boolean>>> connections = new HashMap<>();
    }

    private static abstract class Gate {

        final Circuit circuit;
        final String outWire;

        Boolean a;
        Boolean b;

        Gate(Circuit circuit, String outWire) {
            this.circuit = circuit;
            this.outWire = outWire;
        }

        void setA(boolean a) {
            if (this.a == null) {
                this.a = a;
            }

            if (this.a != null && this.b != null) {
                boolean output = operate();
                circuit.wires.put(outWire, output);
                if (circuit.connections.containsKey(outWire)) {
                    circuit.connections.get(outWire).forEach(gate -> gate.accept(output));
                }
            }
        }

        void setB(boolean b) {
            if (this.b == null) {
                this.b = b;
            }

            if (this.a != null && this.b != null) {
                boolean output = operate();
                circuit.wires.put(outWire, output);
                if (circuit.connections.containsKey(outWire)) {
                    circuit.connections.get(outWire).forEach(gate -> gate.accept(output));
                }
            }
        }

        abstract boolean operate();
    }

    private static class AndGate extends Gate {

        AndGate(Circuit circuit, String outWire) {
            super(circuit, outWire);
        }

        boolean operate() {
            return a.booleanValue() && b.booleanValue();
        }
    }

    private static class OrGate extends Gate {

        OrGate(Circuit circuit, String outWire) {
            super(circuit, outWire);
        }

        boolean operate() {
            return a.booleanValue() || b.booleanValue();
        }
    }

    private static class XorGate extends Gate {

        XorGate(Circuit circuit, String outWire) {
            super(circuit, outWire);
        }

        boolean operate() {
            return a.booleanValue() != b.booleanValue();
        }
    }
}

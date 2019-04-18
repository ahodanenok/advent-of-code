import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2017/day/24
 */
public class Day24 {

    public static void main(String[] args) {
        List<Component> components = getComponents();
        part1(components);
    }

    private static void part1(List<Component> components) {
        List<Bridge> bridges = getBridges(components);
        int strength = Integer.MIN_VALUE;
        for (Bridge b : bridges) {
            if (b.strength() > strength) {
                strength = b.strength();
            }
        }

        System.out.println(strength);
    }

    private static List<Bridge> getBridges(List<Component> components) {
        List<Bridge> bridges = new ArrayList<Bridge>();
        collectBridges(new Bridge(), components, bridges);
        return bridges;
    }

    private static void collectBridges(Bridge currentBridge, List<Component> components, List<Bridge> bridges) {
        boolean found = false;
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (currentBridge.canConnect(c)) {
                Bridge copy = currentBridge.copy();
                copy.connect(c);
                components.remove(i);
                collectBridges(copy, components, bridges);
                components.add(i, c);
                found = true;
            }
        }

        if (!found) {
            bridges.add(currentBridge);
        }
    }

    private static List<Component> getComponents() {
        List<Component> components = new ArrayList<Component>();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split("/");
            components.add(new Component(
                Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())));
        }

        return components;
    }

    private static class Bridge {

        private List<Component> components = new ArrayList<Component>();
        private int connectionPort;

        boolean canConnect(Component c) {
            return c.portA == connectionPort || c.portB == connectionPort;
        }

        void connect(Component c) {
            components.add(c);
            if (connectionPort == c.portA) {
                connectionPort = c.portB;
            } else if (connectionPort == c.portB) {
                connectionPort = c.portA;
            } else {
                throw new IllegalStateException("Connection port doesn't match");
            }
        }

        int length() {
            return components.size();
        }

        int strength() {
            int value = 0;
            for (Component c : components) {
                value += c.portA;
                value += c.portB;
            }

            return value;
        }

        Bridge copy() {
            Bridge copy = new Bridge();
            copy.components = new ArrayList<Component>(components);
            copy.connectionPort = connectionPort;

            return copy;
        }

        @Override
        public String toString() {
            String str = "";
            for (int i = 0; i < components.size(); i++) {
                str += components.get(i);
                if (i < components.size() - 1) {
                    str += " -> ";
                }
            }

            return str;
        }
    }

    private static class Component {

        final int portA;
        final int portB;

        Component(int portA, int portB) {
            this.portA = portA;
            this.portB = portB;
        }

        @Override
        public String toString() {
            return portA + "/" + portB;
        }
    }
}

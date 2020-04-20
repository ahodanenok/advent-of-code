import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.In;
import ahodanenok.aoc.intcode.Out;

import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;

/**
 * Advent of Code - Day 23
 * https://adventofcode.com/2019/day/23
 */
public class Day23 {

    public static void main(String[] args) throws Exception {
        long[] program = IntcodeComputer.load("input.txt");

        Network network = new Network();
        for (int address = 0; address < 50; address++) {
            network.add(new PC(address, program));
        }

        network.activate();
    }

    private static class Network {

        private Map<Long, PC> computers = new TreeMap<>();
        private boolean enabled;

        void add(PC pc) {
            pc.connect(this);
            computers.put(pc.address, pc);
        }

        void send(Packet packet) {
            if (packet.to == 255) {
                System.out.println("Part 1: " + packet.y);
                enabled = false;
            }

            if (computers.containsKey(packet.to)) {
                computers.get(packet.to).receive(packet);
            }
        }

        void activate() {
            enabled = true;
            while (true) {
                for (PC pc : computers.values()) {
                    if (!enabled) {
                        return;
                    }

                    pc.activate();
                } 
            }
        }
    }

    private static class PC implements In, Out {

        private IntcodeComputer nic;
        private long address;
        private Network network;

        private LinkedList<Long> outbound = new LinkedList<>();
        private LinkedList<Long> incoming = new LinkedList<>();

        PC(long address, long[] program) {
            this.address = address;
            this.nic = new IntcodeComputer(program);
        }

        void connect(Network network) {
            this.network = network;
            //this.nic.setStopOnOut(true);
            this.nic.setIn(this);
            this.nic.setOut(this);
            this.incoming.add(address);
        }

        void receive(Packet packet) {
            this.incoming.addLast(packet.x);
            this.incoming.addLast(packet.y);
        }

        void activate() {
            this.nic.run();
        }

        @Override
        public void write(long n) {
            outbound.addLast(n);
            if (outbound.size() == 3) {
                long to = outbound.removeFirst();
                long x = outbound.removeFirst();
                long y = outbound.removeFirst();
                //System.out.println("sending from " + address + " to " + to + ", x=" + x + ", y= " + y);
                nic.interrupt();
                network.send(new Packet(to, x, y)); 
            }
        }

        @Override
        public long read() {
           //System.out.println("read");
           if (incoming.isEmpty()) {
               nic.interrupt();
               return -1;
           } 

           return incoming.removeFirst(); 
        }

        @Override
        public void close() { }

    }

    private static class Packet {

        private long to;
        private long x;
        private long y;

        Packet(long to, long x, long y) {
            this.to = to;
            this.x = x;
            this.y = y;
        }
    }
}


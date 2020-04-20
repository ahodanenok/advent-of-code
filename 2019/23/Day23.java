import ahodanenok.aoc.intcode.IntcodeComputer;
import ahodanenok.aoc.intcode.In;
import ahodanenok.aoc.intcode.Out;

import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

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

        Supervisor supervisor = new Supervisor();
        network.activate(supervisor);

        System.out.println("Part 1: " + supervisor.part1);
        System.out.println("Part 2: " + supervisor.part2);
    }

    private static class Supervisor {

        long part1 = -1;
        long part2 = -1;

        Set<Long> natDeliveredY = new HashSet<>();

        void onPacketSent(Packet packet) {
            if (packet.to == 255 && part1 == -1) {
                part1 = packet.y;
            }
        }

        void onNatPacketSent(Packet natPacket) {
            if (natDeliveredY.contains(natPacket.y) && part2 == -1) {
                part2 = natPacket.y; 
            }

            natDeliveredY.add(natPacket.y);
        }

        boolean resume() {
           return part1 == -1 || part2 == -1; 
        }
    }

    private static class Network {

        private Supervisor supervisor;
        private Map<Long, PC> computers = new TreeMap<>();

        private Packet natPacket;
        
        void add(PC pc) {
            pc.connect(this);
            computers.put(pc.address, pc);
        }

        void send(Packet packet) {
            if (packet.to == 255) {
                natPacket = new Packet(0, packet.x, packet.y);
            }

            if (computers.containsKey(packet.to)) {
                computers.get(packet.to).receive(packet);
            }

            supervisor.onPacketSent(packet);
        }

        void activate(Supervisor supervisor) {
            this.supervisor = supervisor;
            while (supervisor.resume()) {
                for (PC pc : computers.values()) {
                    pc.activate();
                } 

                int idleCount = 0;
                for (PC pc : computers.values()) {
                    if (pc.idle()) {
                        idleCount++;
                    }
                }

                if (idleCount == computers.size() && natPacket != null) {
                    send(natPacket);
                    supervisor.onNatPacketSent(natPacket); 
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

        boolean idle() {
            return this.incoming.isEmpty() && this.outbound.isEmpty();
        }

        @Override
        public void write(long n) {
            outbound.addLast(n);
            if (outbound.size() == 3) {
                long to = outbound.removeFirst();
                long x = outbound.removeFirst();
                long y = outbound.removeFirst();
                nic.interrupt();
                network.send(new Packet(to, x, y)); 
            }
        }

        @Override
        public long read() {
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


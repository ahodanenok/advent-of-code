import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Advent of Code - Day 16
 * https://adventofcode.com/2021/day/16
 */
public class Day16 {

    public static void main(String[] args) throws Exception {
        Packet packet = readPacket(getInput());
        part1(packet);
        part2(packet);
    }

    private static Message getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            return new Message(reader.readLine().trim());
        }
    }

    private static void part1(Packet packet) {
        int versionSum = 0;

        LinkedList<Packet> queue = new LinkedList<>();
        queue.offer(packet);

        // don't want recursion
        while (!queue.isEmpty()) {
            Packet current = queue.poll();
            versionSum += current.version;
            if (current.type != Packet.TYPE_LITERAL) {
                for (Packet sub : current.subPackets) {
                    queue.offer(sub);
                }
            }
        }

        System.out.println("Part 1: " + versionSum);
    }

    private static void part2(Packet packet) {
        System.out.println("Part 2: " + evaluatePacket(packet));
    }

    // recursion finds me :(
    private static long evaluatePacket(Packet packet) {
        if (packet.type == Packet.TYPE_LITERAL) {
            return packet.value;
        } else if (packet.type == Packet.TYPE_SUM) {
            long sum = 0;
            for (Packet sub : packet.subPackets) {
                sum += evaluatePacket(sub);
            }

            return sum;
        } else if (packet.type == Packet.TYPE_PRODUCT) {
            long product = 1;
            for (Packet sub : packet.subPackets) {
                product *= evaluatePacket(sub);
            }

            return product;
        } else if (packet.type == Packet.TYPE_MIN) {
            long min = Long.MAX_VALUE;
            for (Packet sub : packet.subPackets) {
                min = Math.min(evaluatePacket(sub), min);
            }

            return min;
        } else if (packet.type == Packet.TYPE_MAX) {
            long max = Long.MIN_VALUE;
            for (Packet sub : packet.subPackets) {
                max = Math.max(evaluatePacket(sub), max);
            }

            return max;
        } else if (packet.type == Packet.TYPE_GT) {
            long a = evaluatePacket(packet.subPackets.get(0));
            long b = evaluatePacket(packet.subPackets.get(1));
            return a > b ? 1 : 0;
        } else if (packet.type == Packet.TYPE_LT) {
            long a = evaluatePacket(packet.subPackets.get(0));
            long b = evaluatePacket(packet.subPackets.get(1));
            return a < b ? 1 : 0;
        } else if (packet.type == Packet.TYPE_EQ) {
            long a = evaluatePacket(packet.subPackets.get(0));
            long b = evaluatePacket(packet.subPackets.get(1));
            return a == b ? 1 : 0;
        } else {
            throw new IllegalStateException("Unknown packet type: " + packet.type);
        }
    }

    private static Packet readPacket(Message message) {
        int length = 0;

        int version = message.read(3);
        length += 3;

        int type = message.read(3);
        length += 3;

        if (type == Packet.TYPE_LITERAL) {
            long value = 0;
            while (true) {
                int group = message.read(5);
                length += 5;

                value = (value << 4) | (group & 0xF);
                if ((group & 0x10) == 0) {
                    break;
                }
            }

            return new Packet(version, type, length, value);
        } else {
            List<Packet> subPackets = new ArrayList<>();

            int subLengthType = message.read(1);
            length += 1;

            if (subLengthType == 0) {
                int subLength = message.read(15);
                length += 15;

                while (subLength > 0) {
                    Packet sub = readPacket(message);
                    subPackets.add(sub);
                    subLength -= sub.length;
                    length += sub.length;
                }
            } else if (subLengthType == 1) {
                int subCount = message.read(11);
                length += 11;

                for (int i = 0; i <subCount; i++) {
                    Packet sub = readPacket(message);
                    subPackets.add(sub);
                    length += sub.length;
                }
            } else {
                throw new IllegalStateException("Unknown length type: " + subLengthType);
            }

            return new Packet(version, type, length, subPackets);
        }
    }

    private static class Message {

        private final String bits;
        private int pos;

        Message(String hex) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hex.length(); i++) {
                sb.append(decode(hex.charAt(i)));
            }

            this.bits = sb.toString();
        }

        int read(int bitCount) {
            if (pos + bitCount > bits.length()) {
                throw new IllegalStateException();
            }

            String str = "";
            while (str.length() < bitCount) {
                str += bits.charAt(pos);
                pos++;
            }

            return Integer.parseInt(str, 2);
        }

        private String decode(char ch) {
            switch (ch) {
                case '0': return "0000";
                case '1': return "0001";
                case '2': return "0010";
                case '3': return "0011";
                case '4': return "0100";
                case '5': return "0101";
                case '6': return "0110";
                case '7': return "0111";
                case '8': return "1000";
                case '9': return "1001";
                case 'A': return "1010";
                case 'B': return "1011";
                case 'C': return "1100";
                case 'D': return "1101";
                case 'E': return "1110";
                case 'F': return "1111";
                default:
                    throw new IllegalStateException("Unknown character: " + ch);
            }
        }
    }

    private static class Packet {

        static final int TYPE_SUM = 0;
        static final int TYPE_PRODUCT = 1;
        static final int TYPE_MIN = 2;
        static final int TYPE_MAX = 3;
        static final int TYPE_LITERAL = 4;
        static final int TYPE_GT = 5;
        static final int TYPE_LT = 6;
        static final int TYPE_EQ = 7;

        final int version;
        final int type;
        final int length;
        final long value;
        final List<Packet> subPackets;

        Packet(int version, int type, int length, long value) {
            this.version = version;
            this.type = type;
            this.length = length;
            this.value = value;
            this.subPackets = null;
        }

        Packet(int version, int type, int length, List<Packet> subPackets) {
            this.version = version;
            this.type = type;
            this.length = length;
            this.value = Long.MIN_VALUE;
            this.subPackets = subPackets;
        }
    }
}

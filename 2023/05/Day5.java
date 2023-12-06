import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Advent of Code - Day 5
 * https://adventofcode.com/2023/day/5
 */
public class Day5 {

    private static final Comparator<Mapping> MAPPING_COMPARATOR =
        Comparator.comparingLong(m -> m.sourceStart);

    public static void main(String[] args) throws Exception {
        Almanac almanac = getInput();
        almanac.seedSoil.sort(MAPPING_COMPARATOR);
        almanac.soilFertilizer.sort(MAPPING_COMPARATOR);
        almanac.fertilizerWater.sort(MAPPING_COMPARATOR);
        almanac.waterLight.sort(MAPPING_COMPARATOR);
        almanac.lightTemperature.sort(MAPPING_COMPARATOR);
        almanac.temperatureHumidity.sort(MAPPING_COMPARATOR);
        almanac.humidityLocation.sort(MAPPING_COMPARATOR);

        part1(almanac);
        part2(almanac);
    }

    private static void part1(Almanac almanac) {
        long locationMin = almanac.seeds.stream()
            .mapToLong(n -> n.longValue())
            .map(n -> translate(n, almanac.seedSoil))
            .map(n -> translate(n, almanac.soilFertilizer))
            .map(n -> translate(n, almanac.fertilizerWater))
            .map(n -> translate(n, almanac.waterLight))
            .map(n -> translate(n, almanac.lightTemperature))
            .map(n -> translate(n, almanac.temperatureHumidity))
            .map(n -> translate(n, almanac.humidityLocation))
            .min()
            .orElseThrow(() -> new IllegalStateException("No value!"));

        System.out.println("Part 1: " + locationMin);
    }

    private static void part2(Almanac almanac) {
        List<Range> seeds = new ArrayList<>();
        for (int i = 0; i < almanac.seeds.size(); i += 2) {
            seeds.add(new Range(almanac.seeds.get(i), almanac.seeds.get(i + 1)));
        }

        long locationMin = seeds.stream()
            .flatMap(r -> translate(r, almanac.seedSoil).stream())
            .flatMap(r -> translate(r, almanac.soilFertilizer).stream())
            .flatMap(r -> translate(r, almanac.fertilizerWater).stream())
            .flatMap(r -> translate(r, almanac.waterLight).stream())
            .flatMap(r -> translate(r, almanac.lightTemperature).stream())
            .flatMap(r -> translate(r, almanac.temperatureHumidity).stream())
            .flatMap(r -> translate(r, almanac.humidityLocation).stream())
            .min((a, b) -> Long.compare(a.from, b.from))
            .map(r -> r.from)
            .orElseThrow(() -> new IllegalStateException("No value!"));

        System.out.println("Part 2: " + locationMin);
    }

    private static long translate(long source, List<Mapping> mappings) {
        for (Mapping m : mappings) {
            if (m.sourceStart > source) {
                break;
            }

            if (source >= m.sourceStart && source < m.sourceStart + m.length) {
                return m.targetStart + (source - m.sourceStart);
            }
        }

        return source;
    }

    private static List<Range> translate(Range source, List<Mapping> mappings) {
        List<Range> result = new ArrayList<>();

        long rangeStart = source.from;
        for (Mapping m : mappings) {
            if (m.sourceStart >= source.to) {
                break;
            }

            if (rangeStart < m.sourceStart + m.length && source.to > m.sourceStart) {
                Range range;
                if (rangeStart < m.sourceStart) {
                    range = new Range(
                        rangeStart,
                        m.sourceStart - rangeStart);
                    rangeStart += range.length;
                    result.add(range);
                }
                if (m.sourceStart + m.length < source.to) {
                    range = new Range(
                        m.targetStart + (rangeStart - m.sourceStart),
                        (m.sourceStart + m.length) - rangeStart);
                    rangeStart += range.length;
                    result.add(range);
                } else {
                    range = new Range(
                        m.targetStart + (rangeStart - m.sourceStart),
                        source.to - rangeStart);
                    rangeStart += range.length;
                    result.add(range);
                }
            }
        }

        long remainingLength = source.to - rangeStart;
        if (remainingLength > 0) {
            result.add(new Range(rangeStart, remainingLength));
        }

        return result;
    }

    private static Almanac getInput() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            Almanac almanac = new Almanac();

            String line;
            line = reader.readLine().trim();
            for (String n : line.split(":")[1].trim().split("\\s+")) {
                almanac.seeds.add(Long.parseLong(n.trim()));
            }
            reader.readLine();
            reader.readLine();
            parseMappings(reader, almanac.seedSoil);
            reader.readLine();
            parseMappings(reader, almanac.soilFertilizer);
            reader.readLine();
            parseMappings(reader, almanac.fertilizerWater);
            reader.readLine();
            parseMappings(reader, almanac.waterLight);
            reader.readLine();
            parseMappings(reader, almanac.lightTemperature);
            reader.readLine();
            parseMappings(reader, almanac.temperatureHumidity);
            reader.readLine();
            parseMappings(reader, almanac.humidityLocation);

            return almanac;
        }
    }

    private static void parseMappings(BufferedReader reader, List<Mapping> out) throws Exception {
        String line;
        while (true) {
            line = reader.readLine();
            if (line == null || line.isBlank()) {
                break;
            }

            String[] parts = line.split("\\s+");
            out.add(new Mapping(
                Long.parseLong(parts[1].trim()),
                Long.parseLong(parts[0].trim()),
                Long.parseLong(parts[2].trim())));
        }
    }

    private static class Almanac {

        final List<Long> seeds = new ArrayList<>();
        final List<Mapping> seedSoil = new ArrayList<>();
        final List<Mapping> soilFertilizer = new ArrayList<>();
        final List<Mapping> fertilizerWater = new ArrayList<>();
        final List<Mapping> waterLight = new ArrayList<>();
        final List<Mapping> lightTemperature = new ArrayList<>();
        final List<Mapping> temperatureHumidity = new ArrayList<>();
        final List<Mapping> humidityLocation = new ArrayList<>();
    }

    private static class Mapping {

        final long sourceStart;
        final long targetStart;
        final long length;

        Mapping(long sourceStart, long targetStart, long length) {
            this.sourceStart = sourceStart;
            this.targetStart = targetStart;
            this.length = length;
        }
    }

    private static class Range {

        final long from;
        final long to;
        final long length;

        Range(long from, long length) {
            this.from = from;
            this.to = from + length;
            this.length = length;
        }
    }
}

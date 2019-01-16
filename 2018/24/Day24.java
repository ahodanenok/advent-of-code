import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Advent of Code - Day 24
 * https://adventofcode.com/2018/day/24
 */
public class Day24 {

    private static final boolean DEBUG = false;
    private static final Pattern UNITS_GROUP_PATTERN = Pattern.compile("^(\\d+) units each with (\\d+) hit points (\\(([a-z]+ to ([ ,a-z]+))?(; )?([a-z]+ to ([ ,a-z]+))\\) )?with an attack that does (\\d+) ([a-z]+) damage at initiative (\\d+)$");

    public static void main(String[] args) {
        Condition condition = getCondition();
        part1(condition.copy());
        part2(condition.copy());
    }

    private static void part1(Condition condition) {
        fight(condition);

        List<UnitsGroup> winningArmy;
        if (!condition.infection.isEmpty()) {
            winningArmy = condition.infection;
        } else if (!condition.immuneSystem.isEmpty()) {
            winningArmy = condition.immuneSystem;
        } else {
            throw new IllegalStateException("No winner");
        }

        int unitsCount = 0;
        for (UnitsGroup group : winningArmy) {
            unitsCount += group.unitsCount;
        }

        System.out.println(unitsCount);
    }

    private static void part2(Condition condition) {
        int boost = 1;

        boostSearch:
        while (true) {
            for (int i = 0; i < condition.immuneSystem.size(); i++) {
                Condition boostedCondition = condition.copy();
                for (UnitsGroup group : boostedCondition.immuneSystem) {
                    group.unitAttackDamage += boost;
                }

                fight(boostedCondition);
                if (!boostedCondition.immuneSystem.isEmpty() && boostedCondition.infection.isEmpty()) {
                    int unitsCount = 0;
                    for (UnitsGroup group : boostedCondition.immuneSystem) {
                        unitsCount += group.unitsCount;
                    }

                    System.out.println(unitsCount);
                    break boostSearch;
                }
            }

            boost++;
        }
    }

    private static void fight(Condition condition) {
        boolean anyUnitsKilled;

        do {
            if (DEBUG) {
                System.out.println("-- Groups alive --");
                for (UnitsGroup group : condition.immuneSystem) {
                    System.out.println("  " + group.id);
                }
                for (UnitsGroup group : condition.infection) {
                    System.out.println("  " + group.id);
                }
            }

            if (DEBUG) System.out.println("-- Target selection phase --");
            Map<UnitsGroup, UnitsGroup> selectedTargets = new TreeMap<UnitsGroup, UnitsGroup>(UnitsGroup.ATTACK_ORDER);
            selectTargets(condition.infection, condition.immuneSystem, selectedTargets);
            selectTargets(condition.immuneSystem, condition.infection, selectedTargets);

            if (DEBUG) System.out.println("-- Attacking phase --");
            anyUnitsKilled = false;
            for (UnitsGroup attacker : selectedTargets.keySet()) {
                if (attacker.isAlive()) {
                    boolean killedAny = attacker.attack(selectedTargets.get(attacker));
                    if (killedAny) {
                        anyUnitsKilled = true;
                    }
                }
            }

            if (DEBUG) System.out.println("-- Cleanup --");
            for (int i = condition.immuneSystem.size() - 1; i >= 0; i--) {
                if (!condition.immuneSystem.get(i).isAlive()) {
                    if (DEBUG) System.out.println("  removing dead group " + condition.immuneSystem.get(i).id);
                    condition.immuneSystem.remove(i);
                }
            }
            for (int i = condition.infection.size() - 1; i >= 0; i--) {
                if (!condition.infection.get(i).isAlive()) {
                    if (DEBUG) System.out.println("  removing dead group " + condition.infection.get(i).id);
                    condition.infection.remove(i);
                }
            }
        } while (!condition.immuneSystem.isEmpty() && !condition.infection.isEmpty() && anyUnitsKilled);
    }

    private static void selectTargets(List<UnitsGroup> attackers, List<UnitsGroup> targets, Map<UnitsGroup, UnitsGroup> selectedTargets) {
        attackers = new ArrayList<UnitsGroup>(attackers);
        Collections.sort(attackers, UnitsGroup.TARGET_SELECTION_ORDER);

        for (UnitsGroup attacker : attackers) {
            List<UnitsGroup> availableTargets = new ArrayList<UnitsGroup>();
            for (UnitsGroup target : targets) {
                if (!selectedTargets.containsValue(target)) {
                    availableTargets.add(target);
                }
            }

            if (DEBUG) {
                System.out.println("  " + attacker.id + " (effective power " + attacker.getEffectivePower() + ", initiative " + attacker.initiative + ") selecting target from:");
                for (UnitsGroup target : availableTargets) {
                    System.out.println("    " + target.id + " (" + attacker.getPossibleDamage(target) + ")");
                }
                System.out.println();
            }

            int maxDamage = Integer.MIN_VALUE;
            for (UnitsGroup target : availableTargets) {
                maxDamage = Math.max(attacker.getPossibleDamage(target), maxDamage);
            }

            List<UnitsGroup> possibleTargets = new ArrayList<UnitsGroup>();
            if (maxDamage > 0) {
                for (UnitsGroup target : availableTargets) {
                    if (attacker.getPossibleDamage(target) == maxDamage) {
                        possibleTargets.add(target);
                    }
                }
            }

            if (DEBUG) {
                System.out.println("    " + attacker.id + " consideres attacking groups:");
                if (!possibleTargets.isEmpty()) {
                    for (UnitsGroup target : possibleTargets) {
                        System.out.println("    " + target.id + " (attack effective power " + attacker.getPossibleDamage(target) + ")");
                    }
                } else {
                    System.out.println("    -- none --");
                }
                System.out.println();
            }

            if (!possibleTargets.isEmpty()) {
                Collections.sort(possibleTargets, UnitsGroup.TARGET_SELECTION_ORDER);
                selectedTargets.put(attacker, possibleTargets.get(0));
                if (DEBUG) System.out.println("    selected targets is " + possibleTargets.get(0).id);
                if (DEBUG) System.out.println();
            }
        }
    }

    private static Condition getCondition() {
        Condition condition = new Condition();

        Scanner scanner = new Scanner(System.in);
        readGroups(scanner, condition.immuneSystem, "Immune System Group ");
        readGroups(scanner, condition.infection, "Infection Group ");

        return condition;
    }

    private static void readGroups(Scanner scanner, List<UnitsGroup> groups, String idPrefix) {
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }

            Matcher m = UNITS_GROUP_PATTERN.matcher(line);
            m.find();

            String immunitiesGroup = null;
            String weaknessesGroup = null;

            if (m.group(4) != null && m.group(4).startsWith("immune")) {
                immunitiesGroup = m.group(5);
            } else if (m.group(7) != null && m.group(7).startsWith("immune")) {
                immunitiesGroup = m.group(8);
            }

            if (m.group(4) != null && m.group(4).startsWith("weak")) {
                weaknessesGroup = m.group(5);
            } else if (m.group(7) != null && m.group(7).startsWith("weak")) {
                weaknessesGroup = m.group(8);
            }

            List<String> weaknesses = new ArrayList<String>();
            if (weaknessesGroup != null) {
                for (String weakness : weaknessesGroup.split(", ")) {
                    weaknesses.add(weakness.trim());
                }
            }

            List<String> immunities = new ArrayList<String>();
            if (immunitiesGroup != null) {
                for (String immune : immunitiesGroup.split(", ")) {
                    immunities.add(immune.trim());
                }
            }

            groups.add(new UnitsGroup(
                idPrefix + (groups.size() + 1),
                Integer.parseInt(m.group(1)),
                Integer.parseInt(m.group(2)),
                Integer.parseInt(m.group(9)),
                m.group(10),
                weaknesses,
                immunities,
                Integer.parseInt(m.group(11))));
        }
    }

    private static class Condition {

        final List<UnitsGroup> immuneSystem = new ArrayList<UnitsGroup>();
        final List<UnitsGroup> infection = new ArrayList<UnitsGroup>();

        Condition copy() {
            Condition copy = new Condition();
            for (UnitsGroup group : immuneSystem) {
                copy.immuneSystem.add(group.copy());
            }
            for (UnitsGroup group : infection) {
                copy.infection.add(group.copy());
            }

            return copy;
        }
    }

    private static class UnitsGroup {

        static final Comparator<UnitsGroup> TARGET_SELECTION_ORDER = new Comparator<UnitsGroup>() {
            @Override
            public int compare(UnitsGroup a, UnitsGroup b) {
                int powerCmp = Integer.compare(b.getEffectivePower(), a.getEffectivePower());
                if (powerCmp == 0) {
                    return Integer.compare(b.initiative, a.initiative);
                } else {
                    return powerCmp;
                }
            }
        };

        static final Comparator<UnitsGroup> ATTACK_ORDER = new Comparator<UnitsGroup>() {
            @Override
            public int compare(UnitsGroup a, UnitsGroup b) {
                return Integer.compare(b.initiative, a.initiative);
            }
        };

        private final String id;
        private int unitsCount;

        private final int unitHp;
        private int unitAttackDamage;

        private final String attackType;
        private final List<String> weaknesses = new ArrayList<String>();
        private final List<String> immunities = new ArrayList<String>();

        private final int initiative;

        UnitsGroup(String id,
                   int unitsCount,
                   int unitHp,
                   int unitAttackDamage,
                   String attackType,
                   List<String> weaknesses,
                   List<String> immunities,
                   int initiative) {
            this.id = id;
            this.unitsCount = unitsCount;
            this.unitHp = unitHp;
            this.unitAttackDamage = unitAttackDamage;
            this.attackType = attackType;
            this.weaknesses.addAll(weaknesses);
            this.immunities.addAll(immunities);
            this.initiative = initiative;
        }

        boolean isAlive() {
            return unitsCount > 0;
        }

        int getEffectivePower() {
            return unitsCount * unitAttackDamage;
        }

        boolean attack(UnitsGroup target) {
            int damage = getPossibleDamage(target);
            int unitsKilled = damage / target.unitHp;
            target.unitsCount -= unitsKilled;
            if (DEBUG) System.out.println("  " + id + " (initiative " + initiative + ") attacks " + target.id + " and kills " + unitsKilled + ", units remained " + target.unitsCount);
            return unitsKilled > 0;
        }

        int getPossibleDamage(UnitsGroup target) {
            if (target.immunities.contains(attackType)) {
                return 0;
            }

            int damage = getEffectivePower();
            if (target.weaknesses.contains(attackType)) {
                damage *= 2;
            }

            return damage;
        }

        UnitsGroup copy() {
            return new UnitsGroup(
                id,
                unitsCount,
                unitHp,
                unitAttackDamage,
                attackType,
                weaknesses,
                immunities,
                initiative);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            UnitsGroup other = (UnitsGroup) obj;
            return other.id.equals(id);
        }

        @Override
        public String toString() {
            return String.format("id=%s, unitsCount=%d, unitHp=%d, unitAttackDamage=%d, attackType=%s, weaknesses=%s, immunities=%s, initiative=%d",
                id, unitsCount, unitHp, unitAttackDamage, attackType, weaknesses, immunities, initiative);
        }
    }
}
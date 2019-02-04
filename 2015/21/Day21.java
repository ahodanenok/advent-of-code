import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Advent of Code - Day 21
 * https://adventofcode.com/2015/day/21
 */
public class Day21 {

    private static final Item[] WEAPONS = new Item[] {
        new Item("Dagger", 8, 4, 0),
        new Item("Shortsword", 10, 5, 0),
        new Item("Warhammer", 25, 6, 0),
        new Item("Longsword", 40, 7, 0),
        new Item("Greataxe", 74, 8, 0)
    };

    private static final Item[] ARMORS = new Item[] {
        new Item("Leather", 13, 0, 1),
        new Item("Chainmail", 31, 0, 2),
        new Item("Splintmail", 53, 0, 3),
        new Item("Bandedmail", 75, 0, 4),
        new Item("Platemail", 102, 0, 5)
    };

    private static final Item[] RINGS = new Item[] {
        new Item("Damage +1", 25, 1, 0),
        new Item("Damage +2", 50, 2, 0),
        new Item("Damage +3", 100, 3, 0),
        new Item("Defense +1", 20, 0, 1),
        new Item("Defense +2", 40, 0, 2),
        new Item("Defense +3", 80, 0, 3)
    };

    public static void main(String[] args) {
        List<Equipment> equipments = getEquipments(WEAPONS, ARMORS, RINGS);
        part1(equipments);
        part2(equipments);
    }

    private static void part1(List<Equipment> equipments) {
        int minCost = Integer.MAX_VALUE;
        for (Equipment equipment : equipments) {
            Player player = new Player("player", 100, equipment.attack(), equipment.defence());
            Player boss = new Player("boss", 104, 8, 1);

            Player winner = fight(player, boss);
            if (winner == player) {
                minCost = Math.min(equipment.cost(), minCost);
            }
        }

        System.out.println(minCost);
    }

    private static void part2(List<Equipment> equipments) {
        int maxCost = Integer.MIN_VALUE;
        for (Equipment equipment : equipments) {
            Player player = new Player("player", 100, equipment.attack(), equipment.defence());
            Player boss = new Player("boss", 104, 8, 1);

            Player winner = fight(player, boss);
            if (winner == boss) {
                maxCost = Math.max(equipment.cost(), maxCost);
            }
        }

        System.out.println(maxCost);
    }

    private static Player fight(Player player, Player boss) {
        boolean playerTurn = true;
        while (player.hp > 0 && boss.hp > 0) {
            if (playerTurn) {
                boss.hp -= Math.max(1, player.attack - boss.defence);
                playerTurn = false;
            } else {
                player.hp -= Math.max(1, boss.attack - player.defence);
                playerTurn = true;
            }
        }

        if (player.hp > 0 && boss.hp <= 0) {
            return player;
        } else if (player.hp <= 0 && boss.hp > 0) {
            return boss;
        } else {
            throw new IllegalStateException("No winner");
        }
    }

    private static List<Equipment> getEquipments(Item[] weapons, Item[] armors, Item[] rings) {
        List<Equipment> equipments = new ArrayList<Equipment>();

        for (Item w : weapons) {
            equipments.add(new Equipment(w));
        }

        for (Item w : weapons) {
            for (Item a : armors) {
                equipments.add(new Equipment(w, a));
            }
        }

        for (Item w : weapons) {
            for (Item a : armors) {
                for (Item r : rings) {
                    equipments.add(new Equipment(w, a, r));
                }
            }
        }

        for (Item w : weapons) {
            for (int i = 0; i < rings.length; i++) {
                for (int j = i + 1; j < rings.length; j++) {
                    equipments.add(new Equipment(w, rings[i], rings[j]));
                }
            }
        }

        for (Item w : weapons) {
            for (Item a : armors) {
                for (int i = 0; i < rings.length; i++) {
                    for (int j = i + 1; j < rings.length; j++) {
                        equipments.add(new Equipment(w, a, rings[i], rings[j]));
                    }
                }
            }
        }

        return equipments;
    }

    private static class Player {

        private final String name;
        private int hp;
        private final int attack;
        private final int defence;

        Player(String name, int hp, int attack, int defence) {
            this.name = name;
            this.hp = hp;
            this.attack = attack;
            this.defence = defence;
        }

        @Override
        public String toString() {
            return name + ": hp=" + hp + ", attack=" + attack + ", defence=" + defence;
        }
    }

    private static class Equipment {

        private final List<Item> items;

        Equipment(Item... items) {
            this.items = new ArrayList<Item>(items.length);
            for (Item item : items) {
                this.items.add(item);
            }
        }

        int attack() {
            int value = 0;
            for (Item item : items) {
                value += item.attack;
            }

            return value;
        }

        int defence() {
            int value = 0;
            for (Item item : items) {
                value += item.defence;
            }

            return value;
        }

        int cost() {
            int value = 0;
            for (Item item : items) {
                value += item.cost;
            }

            return value;
        }
    }

    private static class Item {

        private final String name;
        private final int cost;
        private final int attack;
        private final int defence;

        Item(String name, int cost, int attack, int defence) {
            this.name = name;
            this.cost = cost;
            this.attack = attack;
            this.defence = defence;
        }

        @Override
        public String toString() {
            return name + ": cost=" + cost + ", attack=" + attack + ", defence=" + defence;
        }
    }
}

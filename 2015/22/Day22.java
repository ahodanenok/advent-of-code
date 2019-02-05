import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Advent of Code - Day 22
 * https://adventofcode.com/2015/day/22
 */
public class Day22 {

    private static final boolean LOGGING_ENABLED = false;

    private static final List<Spell> SPELLS = Arrays.asList(
        new MagicMissleSpell(),
        new DrainSpell(),
        new ShieldSpell(),
        new PoisonSpell(),
        new RechargeSpell());

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        Wizard player = new Wizard(50, 500, SPELLS);
        Boss boss = new Boss(71, 10);
        System.out.println(fight(player, boss, true, false, 0));
    }

    private static void part2() {
        Wizard player = new Wizard(50, 500, SPELLS);
        Boss boss = new Boss(71, 10);
        System.out.println(fight(player, boss, true, true, 0));
    }

    private static int fight(Wizard player, Boss boss, boolean playerTurn, boolean hardMode, int manaUsed) {

        logTurnStart(player, boss, playerTurn);

        if (hardMode && playerTurn) {
            player.hp--;
        }

        if (boss.hp <= 0 && player.hp > 0) {
            return manaUsed;
        } else if (boss.hp > 0 && player.hp <= 0) {
            return Integer.MAX_VALUE;
        }

        applyEffects(player, boss);

        if (boss.hp <= 0 && player.hp > 0) {
            return manaUsed;
        } else if (boss.hp > 0 && player.hp <= 0) {
            return Integer.MAX_VALUE;
        }

        if (playerTurn) {
            int minManaUsed = Integer.MAX_VALUE;
            boolean spellCasted = false;
            for (Spell spell : player.spells()) {
                if (spell.canCast(player, boss)) {
                    Wizard playerCopy = player.copy();
                    Boss bossCopy = boss.copy();

                    if (LOGGING_ENABLED) System.out.println("Player casts " + spell.name());
                    spell.cast(playerCopy, bossCopy);
                    spellCasted = true;

                    minManaUsed = Math.min(fight(playerCopy, bossCopy, false, hardMode, manaUsed + spell.manaCost), minManaUsed);
                }
            }

            if (!spellCasted) {
                return Integer.MAX_VALUE;
            }

            return minManaUsed;
        } else {
            Wizard playerCopy = player.copy();
            int attack = Math.max(1, boss.attack - playerCopy.armor);
            playerCopy.hp -= attack;
            if (LOGGING_ENABLED) System.out.println("Boss attacks for " + attack + " damage");

            return fight(playerCopy, boss, true, hardMode, manaUsed);
        }
    }

    private static void applyEffects(Wizard player, Boss boss) {
        List<EffectTimer> playerTimers = player.effectTimers();
        for (EffectTimer timer : playerTimers) {
            timer.apply(player, boss);
        }

        for (int i = playerTimers.size() - 1; i >= 0; i--) {
            if (playerTimers.get(i).isOver()) {
                playerTimers.remove(i);
            }
        }

        List<EffectTimer> bossTimers = boss.effectTimers();
        for (EffectTimer timer : bossTimers) {
            timer.apply(player, boss);
        }

        for (int i = bossTimers.size() - 1; i >= 0; i--) {
            if (bossTimers.get(i).isOver()) {
                bossTimers.remove(i);
            }
        }
    }

    private static void logTurnStart(Wizard player, Boss boss, boolean playerTurn) {
        if (!LOGGING_ENABLED) {
            return;
        }
    
        System.out.println();
        if (playerTurn) {
            System.out.println("-- Player turn --");
        } else {
            System.out.println("-- Boss turn --");
        }

        System.out.println("- Player has " + player.hp + " hp, " + player.armor + " armor, " + player.mana + " mana");
        System.out.println("- Boss has " + boss.hp + " hp");
    }

    private static class Wizard {

        private int hp;
        private int mana;
        private int armor;
        private List<Spell> spells;
        private List<EffectTimer> effectTimers;

        Wizard(int hp, int mana, List<Spell> spells) {
            this.hp = hp;
            this.mana = mana;
            this.spells = spells;
            this.effectTimers = new ArrayList<EffectTimer>();
        }

        List<Spell> spells() {
            return spells;
        }

        List<EffectTimer> effectTimers() {
            return effectTimers;
        }

        Wizard copy() {
            Wizard wizard = new Wizard(hp, mana, spells);
            wizard.armor = armor;
            for (EffectTimer timer : effectTimers) {
                wizard.effectTimers.add(timer.copy());
            }

            return wizard;
        }
    }

    private static class Boss {

        private int hp;
        private int attack;
        private List<EffectTimer> effectTimers;

        Boss(int hp, int attack) {
            this.hp = hp;
            this.attack = attack;
            this.effectTimers = new ArrayList<EffectTimer>();
        }

        List<EffectTimer> effectTimers() {
            return effectTimers;
        }

        Boss copy() {
            Boss boss = new Boss(hp, attack);
            for (EffectTimer timer : effectTimers) {
                boss.effectTimers.add(timer.copy());
            }

            return boss;
        }
    }

    private static abstract class Spell {

        private String name;
        private int manaCost;

        Spell(String name, int manaCost) {
            this.name = name;
            this.manaCost = manaCost;
        }

        String name() {
            return name;
        }

        Effect effect() {
            return null;
        }

        boolean canCast(Wizard caster, Boss target) {
            if (caster.mana < manaCost) {
                return false;
            }

            for (EffectTimer timer : caster.effectTimers()) {
                if (effect() != null && effect().name().equals(timer.effectName())) {
                    return false;
                }
            }

            for (EffectTimer timer : target.effectTimers()) {
                if (effect() != null && effect().name().equals(timer.effectName())) {
                    return false;
                }
            }

            return true;
        }

        void cast(Wizard caster, Boss target) {
            doCast(caster, target);
            caster.mana -= manaCost;
        }

        void addEffect(Wizard player, Boss boss, int timer, Effect effect) {
            player.effectTimers.add(new EffectTimer(timer, effect));
            effect.onCast(player, boss);
        }

        void addEffect(Boss boss, Wizard player, int timer, Effect effect) {
            boss.effectTimers.add(new EffectTimer(timer, effect));
            effect.onCast(player, boss);
        }

        abstract void doCast(Wizard caster, Boss target);
    }

    private static class MagicMissleSpell extends Spell {

        MagicMissleSpell() {
            super("Magic Missle", 53);
        }

        @Override
        void doCast(Wizard caster, Boss target) {
            target.hp -= 4;
            if (LOGGING_ENABLED) System.out.println(name() + " deals 4 damage");
        }
    }

    private static class DrainSpell extends Spell {

        DrainSpell() {
            super("Drain", 73);
        }

        @Override
        void doCast(Wizard caster, Boss target) {
            target.hp -= 2;
            caster.hp += 2;
            if (LOGGING_ENABLED) System.out.println("Drain deals 2 damage, and heals 2 hit points");
        }
    }

    private static class ShieldSpell extends Spell {

        private Effect effect = new ShieldEffect();

        ShieldSpell() {
            super("Shield", 113);
        }

        Effect effect() {
            return effect;
        }

        @Override
        void doCast(Wizard caster, Boss target) {
            addEffect(caster, target, 6, effect);
        }
    }

    private static class PoisonSpell extends Spell {

        private Effect effect = new PoisonEffect();

        PoisonSpell() {
            super("Poison", 173);
        }

        Effect effect() {
            return effect;
        }

        @Override
        void doCast(Wizard caster, Boss target) {
            addEffect(target, caster, 6, effect);
        }
    }

    private static class RechargeSpell extends Spell {

        private Effect effect = new RechargeEffect();

        RechargeSpell() {
            super("Recharge", 229);
        }

        Effect effect() {
            return effect;
        }

        @Override
        void doCast(Wizard caster, Boss target) {
            addEffect(caster, target, 5, effect);
        }
    }

    private static class EffectTimer {

        private int timer;
        private Effect effect;

        EffectTimer(int timer, Effect effect) {
            this.timer = timer;
            this.effect = effect;
        }

        String effectName() {
            return effect.name();
        }

        boolean isOver() {
            return timer == 0;
        }

        void apply(Wizard wizard, Boss boss) {
            if (timer == 0) {
                throw new IllegalStateException("Timer has expired");
            }

            effect.apply(wizard, boss);
            timer--;
            if (LOGGING_ENABLED) System.out.println(effect.name() + " timer is " + timer);
            if (isOver()) {
                effect.onExpire(wizard, boss);
            }
        }

        EffectTimer copy() {
            return new EffectTimer(timer, effect);
        };
    }

    private static abstract class Effect {

        private String name;

        Effect(String name) {
            this.name = name;
        }

        String name() {
            return name;
        }

        abstract void onCast(Wizard player, Boss boss);
        abstract void apply(Wizard player, Boss boss);
        abstract void onExpire(Wizard player, Boss boss);
    }

    private static class ShieldEffect extends Effect {

        ShieldEffect() {
            super("Shield");
        }

        void onCast(Wizard player, Boss boss) {
            player.armor += 7;
            if (LOGGING_ENABLED) System.out.println("Player armor increased by 7");
        }

        void apply(Wizard player, Boss boss) {
            // no-op
        }

        void onExpire(Wizard player, Boss boss) {
            player.armor -= 7;
            if (LOGGING_ENABLED) System.out.println("Shield wears off, player armor decreased by 7");
        }
    }

    private static class PoisonEffect extends Effect {

        PoisonEffect() {
            super("Poison");
        }

        void onCast(Wizard player, Boss boss) { 
            // no-op
        }

        void apply(Wizard player, Boss boss) {
            boss.hp -= 3;
            if (LOGGING_ENABLED) System.out.println("Poison deals 3 damage");
        }

        void onExpire(Wizard player, Boss boss) {
            if (LOGGING_ENABLED)System.out.println("Poison wears off");
        }
    }

    private static class RechargeEffect extends Effect {

        RechargeEffect() {
            super("Recharge");
        }

        void onCast(Wizard player, Boss boss) { 
            // no-op
        }

        void apply(Wizard player, Boss boss) {
            player.mana += 101;
            if (LOGGING_ENABLED) System.out.println("Recharge provides 101 mana");
        }

        void onExpire(Wizard player, Boss boss) {
            if (LOGGING_ENABLED) System.out.println("Recharge wears off");
        }
    }
}

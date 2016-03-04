package com.skscd91.advent;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sk-scd91 on 12/22/15.
 */
public class AdventDay22Part1 implements Advent {

    private static final Pattern BOSS_STATS = Pattern.compile("(?<label>[^:]+): (?<count>\\d+)");

    /**
     * A class representing the state of a turn.
     */
    protected static class Turn {
        private int heroHP;
        private int heroMana;
        private int heroArmor;

        private int bossHP;
        private int bossDamage;

        private EnumMap<Magic, Integer> effects;

        public Turn(int heroHP, int heroMana, int bossHP, int bossDamage) {
            this.heroHP = heroHP;
            this.heroMana = heroMana;
            this.heroArmor = 0;
            this.bossHP = bossHP;
            this.bossDamage = bossDamage;
            this.effects = new EnumMap<>(Magic.class);
        }

        // Copy Constructor for clones and child classes.
        protected Turn(Turn other) {
            heroHP = other.heroHP;
            heroMana = other.heroMana;
            heroArmor = other.heroArmor;
            bossHP = other.bossHP;
            bossDamage = other.bossDamage;
            effects = new EnumMap<>(other.effects);
        }

        public void attackHero() {
            heroHP -= Math.max(bossDamage - heroArmor, 1);
        }

        public void decreaseHeroHP(int amount) {
            heroHP -= amount;
        }

        public void increaseHeroHP(int amount) {
            heroHP += amount;
        }

        public void decreaseBossHP(int amount) {
            bossHP -= amount;
        }

        public void decreaseHeroArmor(int amount) {
            heroArmor = Math.max(heroArmor - amount, 0);
        }

        public void increaseHeroArmor(int amount) {
            heroArmor += amount;
        }

        public boolean canDecreaseHeroMana(int amount) {
            return amount <= heroMana;
        }

        public void decreaseHeroMana(int amount) {
            heroMana -= amount;
        }

        public void increaseHeroMana(int amount) {
            heroMana += amount;
        }

        public boolean hasWon() {
            return (bossHP <= 0) && (heroHP > 0);
        }

        public boolean battleIsOver() {
            return (bossHP <= 0) || (heroHP <= 0);
        }

        /**
         *
         * @param playerMove The next move the magic player uses.
         * @return The state after the next turn for both player and boss, or null if the move is'nt allowed.
         */
        public Turn nextTurn(Magic playerMove) {
            if(battleIsOver() || !canAddEffect(playerMove) || !canDecreaseHeroMana(playerMove.getManaCost()))
                return null;
            Turn next = new Turn(this);
            next.applyEffects();
            if (next.battleIsOver())
                return next;
            next.decreaseHeroMana(playerMove.getManaCost());
            playerMove.applyOnStart(next);
            next.addEffect(playerMove);
            next.applyEffects();
            if (!next.battleIsOver())
                next.attackHero();
            return next;
        }

        // The move can be made if the magic has no lasting effect, the move isn't in effect, or it is about to wear off.
        private boolean canAddEffect(Magic effect) {
            return (effect.getTimes() <= 0) || (!effects.containsKey(effect))
                    || (effects.get(effect) <= 1);
        }

        // Put the magic into effect.
        private void addEffect(Magic effect) {
            if (effect.getTimes() > 0) {
                effects.put(effect, effect.getTimes());
            }
        }

        // Apply each of the effects to the player or boss.
        private void applyEffects() {
            effects.forEach((magic, times) -> {
                if (times > 0)
                    magic.applyPerTurn(this);
                if (times == 1)
                    magic.applyOnEnd(this);
            });
            effects.replaceAll((magic, times) -> Math.max(times - 1, 0));
        }
    }

    protected enum Magic {
        MAGIC_MISSILE(53, 0, t -> t.decreaseBossHP(4), null, null),
        DRAIN(73, 0, t -> {t.decreaseBossHP(2); t.increaseHeroHP(2);}, null, null),
        SHIELD(113, 6, t -> t.increaseHeroArmor(7), null, t -> t.decreaseHeroArmor(7)),
        POISON(173, 6, null, t -> t.decreaseBossHP(3), null),
        RECHARGE(229, 5, null, t -> t.increaseHeroMana(101), null);

        private final int manaCost;
        private final int times;
        private final Consumer<Turn> onStart;
        private final Consumer<Turn> perTurn;
        private final Consumer<Turn> onEnd;

        /**
         * Initialize the stats for a magic spell.
         * @param manaCost The cost in mana of the magic spell.
         * @param times The length, in turns, that the spell has effect.
         * @param onStart The action to make as the spell is cast.
         * @param perTurn The action to make each turn.
         * @param onEnd The action to make after the spell wears off.
         */
        Magic(int manaCost, int times, Consumer<Turn> onStart, Consumer<Turn> perTurn, Consumer<Turn> onEnd) {
            this.manaCost = manaCost;
            this.times = times;
            this.onStart = onStart;
            this.perTurn = perTurn;
            this.onEnd = onEnd;
        }

        public int getManaCost() {
            return manaCost;
        }

        public int getTimes() {
            return times;
        }

        public void applyOnStart(Turn t) {
            if (onStart != null)
                onStart.accept(t);
        }

        public void applyPerTurn(Turn t) {
            if (perTurn != null)
                perTurn.accept(t);
        }

        public void applyOnEnd(Turn t) {
            if (onEnd != null)
                onEnd.accept(t);
        }
    }

    @Override
    public int getDay() {
        return 22;
    }

    @Override
    public String compute(BufferedReader input) {
        Turn initialTurn = initStats(input);

        int cost = minManaCost(initialTurn);

        return "The total mana needed is " + cost + ".";
    }

    // Initializes the hero's stats with the boss stats from a file.
    protected Turn initStats(BufferedReader input) {
        Map<String, Integer> bossStats = input.lines()
                .map(BOSS_STATS::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.toMap(m -> m.group("label"),
                        m -> Integer.parseInt(m.group("count"))));
        int heroHP = 50;
        int heroMana = 500;

        int bossHP = bossStats.get("Hit Points");
        int bossDamage = bossStats.get("Damage");
        return new Turn(heroHP, heroMana, bossHP, bossDamage);
    }

    // Find the minimum mana to win.
    private int minManaCost(Turn initialTurn) {
        return EntryStream.of(
                StreamEx.iterate( // Stream as a breadth-first search.
                        EntryStream.of(initialTurn, 0).toList(), // Stream turns with the mana sum.
                        ts -> Arrays.stream(Magic.values())
                                .flatMap(magic -> EntryStream.of(ts.stream())
                                        .mapKeys(t -> t.nextTurn(magic))
                                        .filterKeys(Objects::nonNull)
                                        .mapValues(mana -> mana + magic.getManaCost()))
                                .collect(Collectors.toList()))
                        .takeWhile(c -> !c.isEmpty())
                        .flatMap(Collection::stream))
                .filterKeys(Turn::hasWon) // Seek winning turns.
                .values()
                .limit(100L) // Limit to first 100 to prevent long searches.
                .min(Integer::compareTo).orElse(-1);
    }
}

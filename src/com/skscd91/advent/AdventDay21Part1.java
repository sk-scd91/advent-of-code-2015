package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/21/15.
 */
public class AdventDay21Part1 implements Advent {

    private static final Pattern BOSS_STATS = Pattern.compile("(?<label>[^:]+): (?<count>\\d+)");

    protected static int   DAMAGE_START = 4;
    protected static int[] WEAPON_COSTS = new int[]{ 8, 10, 25, 40,  74};

    protected static int[] ARMOR_COSTS  = new int[]{13, 31, 53, 75, 102};

    protected static int[] RING_DAMAGE_COSTS = new int[]{25, 50, 100};
    protected static int[] RING_ARMOR_COSTS  = new int[]{20, 40,  80};

    private int heroHP;

    private int bossHP;
    private int bossDamage;
    private int bossArmor;

    @Override
    public int getDay() {
        return 21;
    }

    @Override
    public String compute(BufferedReader input) {
        initStats(input);

        int cost = minimumCost();

        return "The minimum cost to defeat the boss is " + cost + " gold.";
    }

    /**
     * Parse the stats for the boss, then initialize the stats for the hero and boss.
     * @param input The input stream containing the boss stats.
     */
    protected void initStats(BufferedReader input) {
        Map<String, Integer> bossStats = input.lines()
                .map(BOSS_STATS::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.toMap(m -> m.group("label"),
                        m -> Integer.parseInt(m.group("count"))));
        heroHP = 100;

        bossHP = bossStats.get("Hit Points");
        bossDamage = bossStats.get("Damage");
        bossArmor = bossStats.get("Armor");
    }

    /**
     * @return The minimum cost, in gold, to win the fight.
     */
    private int minimumCost() {
        return IntStream.rangeClosed(DAMAGE_START, WEAPON_COSTS.length + DAMAGE_START
                + RING_DAMAGE_COSTS.length + RING_DAMAGE_COSTS.length - 1)
                .flatMap(damage -> costsForStats(damage, armorForDamage(damage)))
                .min().orElse(-1);
    }

    /**
     * @return The minimum armor needed to fight the boss with the given damage.
     */
    protected int armorForDamage(int damage) {
        int playerTurns = (bossHP - 1) / (damage - bossArmor) + 1;
        int i = 0;

        for (; playerTurns > (heroHP - 1) / Math.max(0, bossDamage - i) + 1; i++)
            ;
        return i;
    }

    /**
     * @param damage The hero's damage.
     * @param armor The hero's armor.
     * @return A stream of prices for the hero to have the given armor and damage.
     */
    protected IntStream costsForStats(int damage, int armor) {
        IntStream.Builder sums = IntStream.builder();
        int damageIndex = damage - DAMAGE_START;
        int armorIndex = armor - 1;

        // Zero rings.
        if (damageIndex < WEAPON_COSTS.length && armorIndex < ARMOR_COSTS.length)
            sums.accept(WEAPON_COSTS[damageIndex] + ((armor > 0) ? ARMOR_COSTS[armorIndex] : 0));

        // One ring each.
        for (int i = 0; i < RING_DAMAGE_COSTS.length; i++) {
            boolean canBuyDamageRing = damage - i > DAMAGE_START && damageIndex - i - 1 < WEAPON_COSTS.length;

            for (int j = 0; j < RING_ARMOR_COSTS.length; j++) {
                boolean canBuyArmorRing = armor > 0 && armorIndex >= j && armorIndex - j - 1 < ARMOR_COSTS.length;

                if (canBuyDamageRing) {
                    if (armorIndex < ARMOR_COSTS.length)
                        sums.accept(WEAPON_COSTS[damageIndex - i - 1] + RING_DAMAGE_COSTS[i]
                            + ((armor > 0) ? ARMOR_COSTS[armorIndex] : 0));

                    if(canBuyArmorRing) // if both are true.
                        sums.accept(WEAPON_COSTS[damageIndex - i - 1] + RING_DAMAGE_COSTS[i]
                                + ((armorIndex > j) ? ARMOR_COSTS[armorIndex - j - 1] : 0) + RING_ARMOR_COSTS[j]);
                }
                if (canBuyArmorRing && damageIndex < WEAPON_COSTS.length)
                    sums.accept(WEAPON_COSTS[damageIndex]
                            + ((armorIndex > j) ? ARMOR_COSTS[armorIndex - j - 1] : 0) + RING_ARMOR_COSTS[j]);
            }
        }

        //Two rings.
        if (damageIndex >= 3 && armorIndex < ARMOR_COSTS.length)
            for (int i = 1; i < RING_DAMAGE_COSTS.length; i++) {
                for (int j = 0; j < i; j++) {
                    int di = damageIndex - i - j - 2;
                    if (di >= 0 && di < WEAPON_COSTS.length)
                        sums.accept(WEAPON_COSTS[di] + RING_DAMAGE_COSTS[i] + RING_DAMAGE_COSTS[j]
                                + ((armor > 0) ? ARMOR_COSTS[armorIndex] : 0));
                }
            }

        if (armor >= 3 && damageIndex < WEAPON_COSTS.length)
            for (int i = 1; i < RING_ARMOR_COSTS.length; i++) {
                for (int j = 0; j < i; j++) {
                    int ai = armorIndex - i - j - 2;
                    if (ai >= 0 && ai < ARMOR_COSTS.length)
                        sums.accept(WEAPON_COSTS[damageIndex]
                                + ARMOR_COSTS[ai] + RING_ARMOR_COSTS[i] + RING_ARMOR_COSTS[j]);
                    else if (i + j + 2 == armor)
                        sums.accept(WEAPON_COSTS[damageIndex] + RING_ARMOR_COSTS[i] + RING_ARMOR_COSTS[j]);
                }
            }

        return sums.build();
    }
}

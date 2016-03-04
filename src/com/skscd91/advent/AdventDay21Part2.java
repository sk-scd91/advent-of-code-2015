package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/21/15.
 */
public class AdventDay21Part2 extends AdventDay21Part1 {

    @Override
    public String compute(BufferedReader input) {
        initStats(input);

        int cost = maxLosingCost();

        return "The maximum cost to lose is " + cost + " gold.";
    }

    /**
     * @return The maximum cost of a losing battle.
     */
    private int maxLosingCost() {
        int maxWeapon = DAMAGE_START + WEAPON_COSTS.length + RING_DAMAGE_COSTS.length * 2 - 1;
        int minWinningDamage = IntStream.range(DAMAGE_START, maxWeapon)
                .filter(d -> armorForDamage(d) == 0)
                .findFirst().orElse(maxWeapon);

        return IntStream.range(DAMAGE_START, minWinningDamage)
                .flatMap(damage -> IntStream.range(0, armorForDamage(damage))
                        .flatMap(armor -> costsForStats(damage, armor)))
                .max().orElse(-1);

    }

}

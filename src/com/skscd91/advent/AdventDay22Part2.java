package com.skscd91.advent;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/22/15.
 */
public class AdventDay22Part2 extends AdventDay22Part1 {

    // Similar to Turn, but depletes HP before player strikes.
    protected class HardTurn extends Turn {

        public HardTurn(Turn turn) {
            super(turn);
        }

        @Override
        public HardTurn nextTurn(Magic playerMove) {
            decreaseHeroHP(1); // Remove a HP before turns.
            Turn next = super.nextTurn(playerMove);
            increaseHeroHP(1); // Retain old state for other clones.
            return (next == null) ? null : new HardTurn(next);
        }

    }

    @Override
    protected Turn initStats(BufferedReader input) {
        return new HardTurn(super.initStats(input));
    }

}

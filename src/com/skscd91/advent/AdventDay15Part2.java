package com.skscd91.advent;

/**
 * Created by sk-scd91 on 12/15/15.
 */
public class AdventDay15Part2 extends AdventDay15Part1 {

    @Override
    // Same as original, except it scores only 500 calorie recipes.
    protected int calculateScore(int[][] stats, int[] weights) {
        int product = 1;

        for(int i = 0; i < 4; i++) {
            int dotProduct = 0;
            for(int j = 0; j < weights.length; j++) {
                dotProduct += stats[j][i] * weights[j];
            }
            product *= Math.max(dotProduct, 0);
        }

        int calories = 0;

        for(int j = 0; j < weights.length; j++) {
            calories += stats[j][4] * weights[j];
        }

        return (calories == 500) ? product : 0;
    }

}

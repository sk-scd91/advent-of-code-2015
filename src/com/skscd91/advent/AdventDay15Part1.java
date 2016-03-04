package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/15/15.
 */
public class AdventDay15Part1 implements Advent {

    private static final Pattern INGREDIENT_PATTERN =
            Pattern.compile("([A-Za-z]+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), "
                    + "texture (-?\\d+), calories (-?\\d+)");

    @Override
    public int getDay() {
        return 15;
    }

    @Override
    public String compute(BufferedReader input) {

        int[][] stats = input.lines()
                .map(INGREDIENT_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(this::parseIngredientStats)
                .toArray(int[][]::new);

        int max = generateWeights(100, stats.length, stats.length)
                .mapToInt(ws -> calculateScore(stats, ws))
                .max().orElse(0);

        return "The score for the maximum recipe is " + max + " points.";
    }

    /**
     * @param ingredient The matched ingredient.
     * @return The capacity, durability, flavor, texture, and calories in an array.
     */
    private int[] parseIngredientStats(Matcher ingredient) {
        int[] stats = new int[5];
        for (int i = 0; i < stats.length; i++) {
            stats[i] = Integer.parseInt(ingredient.group(2 + i)); // Matching stats starts at group 2.
        }
        return stats;
    }

    /**
     * Recursively stream arrays that add up to the initial limit.
     * @param limit The limit to count up to from 0 (inclusive).
     * @param step Recursive steps, stops at 1.
     * @param size The size of the arrays in the stream.
     * @return A stream of arrays
     */
    private Stream<int[]> generateWeights(int limit, int step, int size) {
        if (step <= 1) { // Base case: a new array with the first element the remaining limit.
            int[] result = new int[size];
            result[0] = limit;
            return Stream.of(result);
        }
        return IntStream.rangeClosed(0, limit) // Other cases: for n -> [0, limit],
                .boxed()
                // recurse with limit -= n and step -= 1, then set the step-th element to n.
                .flatMap(n -> generateWeights(limit - n, step - 1, size).peek(a -> a[step - 1] = n));
    }

    /**
     * @param stats Ingredient stats
     * @param weights Weights
     * @return The product of the weighted sum for each statistic.
     */
    protected int calculateScore(int[][] stats, int[] weights) {
        int product = 1;

        for(int i = 0; i < 4; i++) {
            int dotProduct = 0;
            for(int j = 0; j < weights.length; j++) {
                dotProduct += stats[j][i] * weights[j];
            }
            product *= Math.max(dotProduct, 0);
        }

        return product;
    }
}

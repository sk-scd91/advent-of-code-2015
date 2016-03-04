package com.skscd91.advent;

import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

/**
 * Created by sk-scd91 on 12/13/15.
 */
public class AdventDay13Part1 implements Advent {

    private static final Pattern SEATING =
            Pattern.compile("([A-Za-z]+) would (gain|lose) ([0-9]+) happiness units by sitting next to ([A-Za-z]+)\\.");

    protected Map<String, Map<String, Integer>> pairs;

    @Override
    public int getDay() {
        return 13;
    }

    @Override
    public String compute(BufferedReader input) {
        initPairs(input);

        int happiest = getMaxHappiness();

        pairs = null;
        return "The total change in happiness is " + happiest + " units.";
    }

    /**
     * Initialize the 2D pairs map from the input.
     */
    protected void initPairs(BufferedReader input) {
        pairs = input.lines()
                .map(SEATING::matcher)
                .filter(Matcher::matches)
                .collect(groupingBy(m -> m.group(1),
                        toMap(m -> m.group(4), m -> toSign(m.group(2), Integer.valueOf(m.group(3))))));
    }

    /**
     * @return The maximum happiness of the optimal seating arrangement.
     */
    protected int getMaxHappiness() {
        ArrayList<String> people = new ArrayList<>(pairs.keySet());
        int happiest;

        happiest = StreamEx.ofPermutations(pairs.size())
                .mapToInt(indexes -> IntStreamEx.of(indexes)
                        .append(indexes[0])
                        .elements(people)
                        .pairMap(this::totalHappiness)
                        .mapToInt(Integer::intValue)
                        .sum())
                .max().orElse(0);
        return happiest;
    }

    /**
     * @return The total happiness among person x and person y sitting next to each other.
     */
    private Integer totalHappiness(String x, String y) {
        return pairs.get(x).getOrDefault(y, 0) + pairs.get(y).getOrDefault(x, 0);
    }

    /**
     * @param word "gain" or "lose".
     * @param amount The absolute value of the change in happiness.
     * @return The signed value, corresponding to whether it is a gain or loss.
     */
    private static int toSign(String word, int amount) {
        return ("gain".equals(word)) ? amount : -amount;
    }

}

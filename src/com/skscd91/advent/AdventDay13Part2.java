package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by sk-scd91 on 12/13/15.
 */
public class AdventDay13Part2 extends AdventDay13Part1 {

    @Override
    public String compute(BufferedReader input) {
        initPairs(input);

        pairs.forEach((k, v) -> v.put("Me", 0)); // Set person -> "Me" relations.

        pairs.put("Me", pairs.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), x -> 0))); // Set "Me" -> person.

        int happiest = getMaxHappiness();

        return "The total change in happiness with me is " + happiest + " units.";
    }

}

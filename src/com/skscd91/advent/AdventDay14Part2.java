package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/14/15.
 */
public class AdventDay14Part2 extends AdventDay14Part1 {

    @Override
    public String compute(BufferedReader input) {

        Reindeer[] reindeer = parseReindeer(input.lines()).toArray(Reindeer[]::new);

        long maxPoints = IntStream.rangeClosed(1, TIME)
                // Find leading reindeer for given time.
                .mapToObj(time -> Arrays.stream(reindeer)
                        .max((r1, r2) -> r1.distanceForTime(time) - r2.distanceForTime(time)))
                .filter(Optional::isPresent) // Ignore if no reindeer.
                .map(Optional::get)
                // Count leading seconds for reindeer.
                .collect(Collectors.groupingBy(Reindeer::getName, Collectors.counting()))
                .entrySet().stream()
                .mapToLong(e -> e.getValue()) // Find reindeer in the lead the longest.
                .max().orElse(-1L);

        return "The winning reindeer has " + maxPoints + " points.";
    }

}

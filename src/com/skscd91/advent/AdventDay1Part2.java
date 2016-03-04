package com.skscd91.advent;

import one.util.streamex.StreamEx;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/1/15.
 */
public class AdventDay1Part2 implements Advent {
    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public String compute(BufferedReader input) {
        // A reference to the floor Santa is on.
        // Used to accumulate a prefix-sum for ordered, sequential streams.
        final int[] floor = new int[]{ 0 };

        long position = StreamEx.ofLines(input)
                .flatMapToInt(String::chars)
                .map(AdventDay1Part2::directionForInstruction)
                .map(d -> (floor[0] += d)) // Move up or down a floor.
                .indexOf(-1).orElse(-1L) + 1L; // Get the index where the basement is entered.

        if (position == 0)
            return "Santa did not go into the basement";

        return "Santa entered the basement at position " + position + ".";
    }

    private static int directionForInstruction(int instruction) {
        return    (instruction == '(') ?  1
                : (instruction == ')') ? -1
                : 0;
    }
}

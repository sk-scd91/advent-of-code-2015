package com.skscd91.advent;

import one.util.streamex.StreamEx;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/10/15.
 */
public class AdventDay10Part1 implements Advent {
    @Override
    public int getDay() {
        return 10;
    }

    @Override
    public String compute(BufferedReader input) {
        String lsNum = input.lines().findFirst().orElse("");

        int lsNumLength = StreamEx.iterate(lsNum, num
                -> StreamEx.split(num, "")
                .runLengths() // Convenient digit counter.
                .mapKeyValue((digit, count) -> count + digit)
                .joining())
                .skip(repeatCount())
                .findFirst().orElse("").length();

        return lsNumLength + " is the length of the result.";
    }

    /**
     * Number of times to iterate the number string. Can be overridden by subclasses.
     */
    protected int repeatCount() {
        return 40;
    }
}

package com.skscd91.advent;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/8/15.
 */
public class AdventDay8Part2 implements Advent {

    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public String compute(BufferedReader input) {
        int length = input.lines().mapToInt(this::storageDifference).sum();

        return "Santa gained " + length + " chars of memory.";
    }

    /**
     * Wrap the given string in quotes, and escape the backslashes and quotation marks.
     */
    private String encodeString(String code) {
        return "\"" + code.replaceAll("\\\\|\"","\\\\$0") + "\"";
    }

    /**
     * Compute between the converted and original code string.
     */
    private int storageDifference(String code) {
        return encodeString(code).length() - code.length();
    }
}

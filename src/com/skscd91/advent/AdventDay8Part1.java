package com.skscd91.advent;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/8/15.
 */
public class AdventDay8Part1 implements Advent {
    @Override
    public int getDay() {
        return 8;
    }

    @Override
    public String compute(BufferedReader input) {
        int length = input.lines().mapToInt(this::storageDifference).sum();

        return "Santa saved " + length + " chars of memory.";
    }

    /**
     * Compute the difference between the escaped code and unescaped content.
     */
    private int storageDifference(String code) {
        int valueLength = code.replaceAll("\\\\(?:\"|\\\\|x\\p{XDigit}{2})", ".") // Make escape sequences one character.
                .replaceAll("\"", "") // Remove unescaped quotes.
                .length();

        return code.length() - valueLength;
    }
}

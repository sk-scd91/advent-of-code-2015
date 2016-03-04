package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.regex.Pattern;

/**
 * Created by sk-scd91 on 12/12/15.
 */
public class AdventDay12Part1 implements Advent {

    private static Pattern NON_NUMBERS = Pattern.compile("[^-\\d]+");

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public String compute(BufferedReader input) {

        int sum = input.lines()
                .flatMap(NON_NUMBERS::splitAsStream)
                .filter(num -> !num.isEmpty())
                .mapToInt(Integer::parseInt)
                .sum();

        return "The sum of all numbers in the document is " + sum + ".";
    }
}

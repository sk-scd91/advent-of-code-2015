package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.regex.Pattern;

/**
 * Created by sk-scd91 on 12/5/15.
 */
public class AdventDay5Part1 implements Advent {

    private static String VOWELS = "aeiou";

    @Override
    public int getDay() {
        return 5;
    }

    @Override
    public String compute(BufferedReader input) {
        long count = input.lines()
                .filter(this::isNice)
                .count();
        return "There are " + count + " nice strings.";
    }

    /**
     * Determine if the given String is nice, based on the given criteria.
     */
    protected boolean isNice(String string) {
        // Nice strings have 3+ vowels.
        if(string.chars().filter(l -> VOWELS.contains(String.valueOf((char)l))).limit(3).count() < 3)
            return false;

        // They also have at least one sequence with 2 letters in a row.
        if(!Pattern.compile("([a-z])\\1").matcher(string).find())
            return false;

        // And they do not have the following sequences.
        return (!Pattern.compile("ab|cd|pq|xy").matcher(string).find());
    }
}

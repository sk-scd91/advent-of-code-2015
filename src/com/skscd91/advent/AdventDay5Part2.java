package com.skscd91.advent;

import java.util.regex.Pattern;

/**
 * Created by sk-scd91 on 12/5/15.
 */
public class AdventDay5Part2 extends AdventDay5Part1 {

    @Override
    protected boolean isNice(String string) {
        // Nice strings have at least two (non-overlapping) pairs of strings.
        if (!Pattern.compile("([a-z][a-z]).*\\1").matcher(string).find())
            return false;

        // They also have at least one pair with a single letter between them.
        return (Pattern.compile("([a-z])[a-z]\\1").matcher(string).find());
    }

}

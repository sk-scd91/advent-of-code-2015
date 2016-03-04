package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sk-scd91 on 12/12/15.
 */
public class AdventDay12Part2 implements Advent {

    private static Pattern NON_NUMBERS = Pattern.compile("[^-\\d]+");
    private static Pattern RED_VALUES = Pattern.compile(":\"red\"");

    @Override
    public int getDay() {
        return 12;
    }

    @Override
    public String compute(BufferedReader input) {

        int sum = input.lines()
                .map(this::docWithoutRedProps)
                .flatMap(NON_NUMBERS::splitAsStream)
                .filter(num -> !num.isEmpty())
                .mapToInt(Integer::parseInt)
                .sum();

        return "The sum of all numbers in the document is " + sum + ".";
    }

    /**
     * Remove all the objects with red values.
     * @param doc The original document.
     * @return The document, with the objects specified removed, ready to be filtered out further using regex.
     */
    private CharSequence docWithoutRedProps(String doc) {
        BitSet removals = new BitSet(doc.length()); // BitSet to remove in place.
        int lastRightBound = 0;
        Matcher redMatcher = RED_VALUES.matcher(doc);
        while (redMatcher.find(lastRightBound)) { // While there are red values to be found.
            int braceCount = 1;
            int i;
            for (i = redMatcher.start() - 1; i >= 0 && braceCount > 0; i--) { // Find left brace of containing object.
                if (doc.charAt(i) == '}')
                    braceCount++;
                else if (doc.charAt(i) == '{')
                    braceCount--;
            }
            removals.set(i + 1, redMatcher.end()); // Remove left part of object and red value.
            braceCount = 1;
            for (i = redMatcher.end(); i < doc.length() && braceCount > 0; i++) { // Find matching right brace.
                if (doc.charAt(i) == '}')
                    braceCount--;
                else if (doc.charAt(i) == '{')
                    braceCount++;
            }
            removals.set(redMatcher.end(), i); // Remove the rest of the object.
            lastRightBound = i;
        }
        StringBuilder result = new StringBuilder(doc.length() - removals.cardinality() / 8); // Approx. capacity.
        for(int from = 0, to = removals.nextSetBit(0); to > 0;
            from = removals.nextClearBit(to), to = removals.nextSetBit(from)) {
            result.append(doc, from, to).append("{}"); // Copy over objects that have not been removed.
        }
        result.append(doc, removals.previousSetBit(doc.length() - 1) + 1, doc.length()); // Append the rest.
        return result;
    }
}

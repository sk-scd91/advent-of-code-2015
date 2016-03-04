package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by sk-scd91 on 12/7/15.
 */
public class AdventDay7Part2 extends AdventDay7Part1 {

    @Override
    public String compute(BufferedReader input) {
        wires = new TreeMap<>();
        String[] lines = input.lines().toArray(String[]::new); // Cache lines to array.

        // First compute the first part.
        computeSignals(Arrays.stream(lines));

        // Then wire a to b, then reset the other wires.
        Integer signal = wires.get("a");
        wires = new TreeMap<>();
        wires.put("b", signal);

        // Compute again, ignoring setting to b.
        computeSignals(Arrays.stream(lines).filter(line -> !line.endsWith(" -> b")));

        // Get the final result.
        signal = wires.get("a");
        wires = null;
        return "Signal " + signal + " is provided to wire a.";
    }

}

package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;

/**
 * Created by sk-scd91 on 12/2/15.
 */
public class AdventDay2Part2 implements Advent {
    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public String compute(BufferedReader input) {
        int length = input.lines()
                .filter(prism -> prism.matches("^\\d+x\\d+x\\d+$")) // Safety regex.
                .mapToInt(AdventDay2Part2::computeRibbonLength)
                .sum();
        return "The elves need " + length + "ft of ribbon.";
    }

    private static int computeRibbonLength(String prism) {
        // Convert dimensions to sorted integer array
        int[] dims = Arrays.stream(prism.split("x")).mapToInt(Integer::parseInt).toArray();
        Arrays.sort(dims);

        int volume = dims[0] * dims[1] * dims[2];
        int perimeter = (dims[0] + dims[1]) * 2; // Perimeter of smallest surface.
        return volume + perimeter;
    }
}

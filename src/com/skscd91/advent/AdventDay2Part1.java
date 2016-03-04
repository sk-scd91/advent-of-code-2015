package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;

/**
 * Created by sk-scd91 on 12/2/15.
 */
public class AdventDay2Part1 implements Advent {
    @Override
    public int getDay() {
        return 2;
    }

    @Override
    public String compute(BufferedReader input) {
        int totalArea = input.lines()
                .filter(prism -> prism.matches("^\\d+x\\d+x\\d+$")) // Safety regex.
                .mapToInt(AdventDay2Part1::computeSurfaceArea)
                .sum();
        return "The elves need " + totalArea + "ft\u00b2 of wrapping paper.";
    }

    /**
     * @param prism The dimensions of a prism in format NUMxNUMxNUM
     * @return The area of paper needed to wrap the box.
     */
    private static int computeSurfaceArea(String prism) {
        // Convert dimensions to integer array
        int[] dims = Arrays.stream(prism.split("x")).mapToInt(Integer::parseInt).toArray();

        // Surface areas of each unique side.
        int x = dims[0] * dims[1];
        int y = dims[1] * dims[2];
        int z = dims[2] * dims[0];

        int extra = Math.min(Math.min(x, y), z);

        return (x + y + z) * 2 + extra;
    }
}

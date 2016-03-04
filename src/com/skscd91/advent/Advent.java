package com.skscd91.advent;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/1/15.
 */
public interface Advent {

    /**
     * @return The day to execute the advent puzzle.
     */
    int getDay();

    /**
     * Solve an advent puzzle given a file.
     *
     * @param input The puzzle input.
     * @return The solution to the puzzle
     */
    String compute(BufferedReader input);
}

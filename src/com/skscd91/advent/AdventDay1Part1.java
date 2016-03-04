package com.skscd91.advent;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/1/15.
 */
public class AdventDay1Part1 implements Advent {
    @Override
    public int getDay() {
        return 1;
    }

    @Override
    public String compute(BufferedReader input) {
        int floor = input.lines()
                         .flatMapToInt(String::chars)
                         .map(AdventDay1Part1::directionForInstruction)
                         .sum();
        return "Santa will go to floor " + floor + ".";
    }

    private static int directionForInstruction(int instruction) {
        return    (instruction == '(') ?  1
                : (instruction == ')') ? -1
                : 0;
    }
}

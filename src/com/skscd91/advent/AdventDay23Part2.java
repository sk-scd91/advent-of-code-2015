package com.skscd91.advent;

/**
 * Created by sk-scd91 on 12/23/15.
 */
public class AdventDay23Part2 extends AdventDay23Part1 {

    @Override
    protected void resetRegisters() {
        regB = regPC = 0;
        regA = 1;
    }

}

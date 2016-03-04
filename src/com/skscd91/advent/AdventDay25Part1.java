package com.skscd91.advent;

import java.io.BufferedReader;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sk-scd91 on 12/25/15.
 */
public class AdventDay25Part1 implements Advent {

    private static final Pattern COORDINATE_PATTERN =
            Pattern.compile("Enter the code at row (?<row>\\d+), column (?<column>\\d+).");

    private static final BigInteger CODE = BigInteger.valueOf(20151125L);
    private static final BigInteger MULTIPLIER = BigInteger.valueOf(252533L);
    private static final BigInteger MOD = BigInteger.valueOf(33554393L);

    @Override
    public int getDay() {
        return 25;
    }

    @Override
    public String compute(BufferedReader input) {
        long position = input.lines()
                .map(COORDINATE_PATTERN::matcher)
                .filter(Matcher::find)
                .mapToLong(matcher -> positionForCoordinate(
                        Integer.parseInt(matcher.group("row")),
                        Integer.parseInt(matcher.group("column"))))
                .findFirst().orElse(0L);

        BigInteger power = BigInteger.valueOf(position);

        BigInteger code = CODE.multiply(MULTIPLIER.modPow(power, MOD)).mod(MOD);

        return "The code to unlock the machine is: " + code;
    }

    /**
     * @return The diagonal position for the given matrix coordinate.
     */
    private long positionForCoordinate(int row, int column) {
        long diagStart = (row - 1) + (column - 1);

        return (diagStart * (diagStart + 1L)) / 2L + (column - 1);
    }
}

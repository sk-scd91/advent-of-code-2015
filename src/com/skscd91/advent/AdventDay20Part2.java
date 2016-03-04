package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/20/15.
 */
public class AdventDay20Part2 extends AdventDay20Part1 {

    @Override
    public String compute(BufferedReader input) {
        int presentCount = input.lines().mapToInt(Integer::parseInt).findFirst().orElse(0);

        int houseNumber = IntStream.range(10, Integer.MAX_VALUE)
                .filter(x -> sumOfDivisors(x) * 11 >= presentCount)
                .findFirst().orElse(0);

        return "House " + houseNumber + " has at least " + presentCount + " presents.";
    }

    /**
     * @return The divisors of n, where n is one of the first 50 multiples of the divisor.
     */
    @Override
    protected IntStream divisors(int n) {
        return IntStream.rangeClosed(1, (int) Math.sqrt(n))
                .filter(x -> n % x == 0)
                .flatMap(x -> IntStream.concat(IntStream.of(x),
                        (x * x == n) ? IntStream.empty() : IntStream.of(n / x)))
                .filter(x -> (n - 1) / 50 < x);
    }

}

package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/20/15.
 */
public class AdventDay20Part1 implements Advent {
    @Override
    public int getDay() {
        return 20;
    }

    @Override
    public String compute(BufferedReader input) {
        int presentCount = input.lines().mapToInt(Integer::parseInt).findFirst().orElse(0);
        int elfCount = presentCount / 10;

        int houseNumber = IntStream.range(10, Integer.MAX_VALUE)
                .filter(x -> sumOfDivisors(x) >= elfCount)
                .findFirst().orElse(0);

        return "House " + houseNumber + " has at least " + presentCount + " presents.";
    }

    /**
     * @return The sum of divisors of n.
     */
    protected int sumOfDivisors(int n) {
        return divisors(n)
                .sum();
    }

    /**
     * @return A stream of the sums of matching divisors.
     */
    protected IntStream divisors(int n) {
        return IntStream.rangeClosed(1, (int) Math.sqrt(n))
                .filter(x -> n % x == 0) // divisible by n.
                .map(x -> (x * x == n) ? x : x + n / x);
    }

}

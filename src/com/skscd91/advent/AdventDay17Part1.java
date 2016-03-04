package com.skscd91.advent;

import one.util.streamex.IntStreamEx;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/17/15.
 */
public class AdventDay17Part1 implements Advent {

    protected static final int TOTAL = 150; // in liters.

    @Override
    public int getDay() {
        return 17;
    }

    @Override
    public String compute(BufferedReader input) {

        int[] containers = input.lines()
                .mapToInt(Integer::parseInt)
                .toArray();
        Arrays.sort(containers);

        long combinations = containerCombinations(containers);

        return combinations + " combinations of containers can fill " + TOTAL + " liters of eggnog.";
    }

    /**
     * @param containers The volumes of each container.
     * @return The count of all the container combinations that match the total volume.
     */
    protected long containerCombinations(int[] containers) {
        return IntStream.range(0, containers.length)
                .mapToLong(i -> containerCombinationsForIndex(containers, i, TOTAL))
                .sum();
    }

    /**
     * Recursively count all the combinations that fit the given volume.
     * @param containers The volumes of each container.
     * @param index The index of the next container to try.
     * @param volume The volume to fit.
     * @return The sum of all the container combinations that fit the volume.
     */
    private long containerCombinationsForIndex(int[] containers, int index, int volume) {
        final int remainingVolume = volume - containers[index];
        long count = Arrays.stream(containers, index + 1, containers.length)
                .filter(c -> c == remainingVolume)
                .count();

        count += IntStreamEx.range(index + 1, containers.length)
                .takeWhile(i -> containers[i] < remainingVolume)
                .mapToLong(i -> containerCombinationsForIndex(containers, i, remainingVolume))
                .sum();

        return count;
    }
}

package com.skscd91.advent;

import one.util.streamex.IntStreamEx;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/17/15.
 */
public class AdventDay17Part2 extends AdventDay17Part1 {

    /**
     * @param containers The volumes of each container.
     * @return The count of the minimum contaner combinations that match the total volume.
     */
    @Override
    protected long containerCombinations(int[] containers) {
        return IntStream.range(0, containers.length)
                .mapToLong(step -> combinationsForStep(containers, 0, TOTAL, step))
                .filter(count -> count > 0)
                .findFirst().orElse(0L);
    }

    /**
     * @param containers The volumes of each container.
     * @param index The index of the next container to try.
     * @param volume The volume to fit.
     * @param step The amount of steps to recurse.
     * @return The count of combinations that fit the given volume in "step" steps.
     */
    private long combinationsForStep(int[] containers, int index, int volume, int step) {
        if (step == 0)
            return Arrays.stream(containers, index, containers.length)
                    .filter(c -> c == volume)
                    .count();

        return IntStream.range(index, containers.length - step)
                .mapToLong(i -> combinationsForStep(containers, i + 1, volume - containers[i], step - 1))
                .sum();
    }

}

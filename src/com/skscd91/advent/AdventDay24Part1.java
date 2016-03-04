package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.BitSet;
import java.util.OptionalLong;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/24/15.
 */
public class AdventDay24Part1 implements Advent {

    @Override
    public int getDay() {
        return 24;
    }

    @Override
    public String compute(BufferedReader input) {

        BitSet presents = input.lines().mapToInt(Integer::parseInt)
                .collect(BitSet::new, BitSet::set, BitSet::or);

        int balancedWeights = getBalancedWeight(presents.stream().sum());

        // Minimum combination to take from set that is >= sum.
        int start = 0;
        for (int bit = presents.length() - 1, sum = 0;
             bit >= 0 && sum < balancedWeights;
             start++, bit = presents.previousSetBit(bit - 1)) {
            sum += bit;
        }

        // Maximum combination
        int end = 0;
        for (int bit = presents.nextSetBit(0), sum = 0;
             bit >= 0 && sum < balancedWeights;
             end++, bit = presents.nextSetBit(bit + 1)) {
            sum += bit;
        }

        // Assumes configuration with lowest quantum of entanglement can be separated further.
        long entanglement = IntStream.rangeClosed(start, end)
                .mapToObj(step -> presentCombinations(presents, balancedWeights, step)
                        // Convert to long and compute total product. Long should be enough for the input.
                        .mapToLong(ps -> ps.stream().mapToLong(i -> (long) i).reduce(1L, (x, y) -> x * y))
                        .min())
                .filter(OptionalLong::isPresent)
                .mapToLong(OptionalLong::getAsLong)
                .findFirst().orElse(0);

        return "The optimal quantum of entanglement is " + entanglement + ".";
    }

    // Get the weight to balance the presents.
    protected int getBalancedWeight(int sum) {
        return sum / 3;
    }

    /**
     *
     * @param presents A set of present weights.
     * @param total The total weight of the presents to balance.
     * @param step The amount of recursive steps to match the weight.
     * @return A stream of sets of presents that match the total weight.
     */
    protected Stream<BitSet> presentCombinations(BitSet presents, int total, int step) {
        if (step == 1) {
            if (!presents.get(total))
                return Stream.empty();
            BitSet result = new BitSet();
            result.set(total);
            return Stream.of(result);
        }
        return presents.stream()
                .filter(bit -> bit < total)
                .mapToObj(bit -> presentCombinations(presents.get(0, bit), total - bit, step - 1)
                        .peek(ps -> ps.set(bit)))
                .flatMap(Function.identity());
    }
}

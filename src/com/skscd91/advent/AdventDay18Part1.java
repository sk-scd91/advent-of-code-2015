package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/18/15.
 */
public class AdventDay18Part1 implements Advent {

    @Override
    public int getDay() {
        return 18;
    }

    @Override
    public String compute(BufferedReader input) {
        BitSet[] grid = createRows(input);

        grid = Stream.iterate(grid, g -> IntStream.range(0, g.length)
                    .mapToObj(i -> generateForRow(g, i))
                    .toArray(BitSet[]::new))
                .skip(100L)
                .findFirst().orElseGet(() -> new BitSet[0]);

        int count = Arrays.stream(grid)
                .mapToInt(BitSet::cardinality)
                .sum();

        return count + " lights are set after 100 iterations.";
    }

    /**
     * Create rows of cells from the input.
     */
    protected BitSet[] createRows(BufferedReader input) {
        return input.lines()
                .map(this::createRow)
                .toArray(BitSet[]::new);
    }

    /**
     * Create a row of cells from the given input, matching "#" to "true" and "." to false.
     */
    private BitSet createRow(String line) {
        return IntStream.range(0, line.length())
                .filter(i -> line.charAt(i) == '#')
                .collect(BitSet::new, BitSet::set, BitSet::or);
    }

    /**
     * Generate a row of cells, based on the previous state.
     * @param grid The cell grid from the previous iteration.
     * @param row The row index to generate.
     * @return A row of cells for the next state.
     */
    protected BitSet generateForRow(BitSet[] grid, int row) {
        return IntStream.range(0, grid.length)  // Assume square matrix.
                .filter(i -> {
                    int count = IntStream.rangeClosed(Math.max(0, row - 1), Math.min(grid.length - 1, row + 1))
                            .map(j -> grid[j].get(Math.max(0, i - 1), i + 2).cardinality())
                            .sum();
                    return (grid[row].get(i) && count == 4) // 3 neighbors and cell are set.
                            || count == 3; // 2 (when set) or 3 neighbors are set.
                })
                .collect(BitSet::new, BitSet::set, BitSet::or);

    }
}

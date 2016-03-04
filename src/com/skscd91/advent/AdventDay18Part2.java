package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.BitSet;

/**
 * Created by sk-scd91 on 12/18/15.
 */
public class AdventDay18Part2 extends AdventDay18Part1 {

    @Override
    protected BitSet[] createRows(BufferedReader input) {
        BitSet[] grid = super.createRows(input);
        keepOnCornerLights(grid[0], grid.length);
        keepOnCornerLights(grid[grid.length - 1], grid.length);
        return grid;
    }

    @Override
    protected BitSet generateForRow(BitSet[] grid, int row) {
        BitSet result = super.generateForRow(grid, row);
        if (row == 0 || row == grid.length - 1)
            keepOnCornerLights(result, grid.length);
        return result;
    }

    /**
     * Make sure the corners are always set.
     */
    private static void keepOnCornerLights(BitSet row, int length) {
        row.set(0);
        row.set(length - 1);
    }
}

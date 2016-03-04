package com.skscd91.advent;

import java.util.Comparator;

/**
 * Created by sk-scd91 on 12/9/15.
 */
public class AdventDay9Part2 extends AdventDay9Part1 {
    @Override
    protected Comparator<Edge> getEdgeComparator() {
        return super.getEdgeComparator().reversed();
    }

    @Override
    protected int reduce(int x, int y) {
        return (y > x) ? y : x;
    }

    @Override
    protected String getResult(int distance) {
        return "The longest distance is " + distance + ".";
    }

}

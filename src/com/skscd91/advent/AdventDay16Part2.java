package com.skscd91.advent;

/**
 * Created by sk-scd91 on 12/16/15.
 */
public class AdventDay16Part2 extends AdventDay16Part1 {

    /**
     * Like the parent class, except Sue has more than "count" trees or cats
     * and less than "count" Pomeranians and goldfish.
     */
    @Override
    protected boolean hasProp(String propName, Integer count) {
        Integer other = sues.get(propName);
        switch(propName) {
            case "cats":case "trees":
                return count > other;
            case "pomeranians":case "goldfish":
                return count < other;
        }
        return other.equals(count);
    }
}

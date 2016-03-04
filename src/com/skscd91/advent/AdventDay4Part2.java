package com.skscd91.advent;

/**
 * Created by sk-scd91 on 12/4/15.
 */
public class AdventDay4Part2 extends AdventDay4Part1 {

    @Override
    protected boolean minedCoin(String key) {
        byte[] coinHash = getCoinHash(key.getBytes());
        // Now get 6 leading 0's.
        return ((coinHash[0] | coinHash[1] | coinHash[2]) == 0);
    }

}

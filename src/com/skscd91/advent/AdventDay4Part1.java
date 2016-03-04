package com.skscd91.advent;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

/**
 * Created by sk-scd91 on 12/4/15.
 */
public class AdventDay4Part1 implements Advent {

    private MessageDigest md5;

    @Override
    public int getDay() {
        return 4;
    }

    @Override
    public String compute(BufferedReader input) {
        String key = keyFromInput(input);

        try { // Initialize MD5 generator.
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String coin = IntStream.rangeClosed(0, Integer.MAX_VALUE)
                .mapToObj(Integer::toString)
                .map(key::concat)
                .filter(this::minedCoin)
                .findFirst().orElse("None");

        return "The coin was mined with " + coin + ".";
    }

    /**
     * Generate a hash of a key that could mine a coin.
     * @param keyBytes The key, in bytes, encoded using the default encoding (assumed to be ASCII).
     * @return The key's MD5 hash value.
     */
    protected byte[] getCoinHash(byte[] keyBytes) {
        md5.reset();
        return md5.digest(keyBytes);
    }

    /**
     * Attempt to mine a coin from the given key.
     * @param key The key, as a String.
     * @return If a coin has been mined.
     */
    protected boolean minedCoin(String key) {
        byte[] coinHash = getCoinHash(key.getBytes());
        // A coin is mined when the first 5 hex digits are 0.
        return (coinHash[0] == 0 &&
                coinHash[1] == 0 &&
                (coinHash[2] & 0xf0) == 0);
    }

    /**
     * Extract one line from the BufferedReader.
     */
    private static String keyFromInput(BufferedReader input) {
        String key = "";
        try {
            key = input.readLine();
        } catch (IOException e) {}
        return key;
    }

}

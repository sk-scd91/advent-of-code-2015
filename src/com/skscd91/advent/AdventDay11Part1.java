package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/11/15.
 */
public class AdventDay11Part1 implements Advent {
    @Override
    public int getDay() {
        return 11;
    }

    @Override
    public String compute(BufferedReader input) {
        String password = input.lines().findFirst().orElse("aaaaaaaa");
        password = nextValidPassword(password).orElse("No password");
        return password + " is Santa's next password.";
    }

    /**
     * Repeatedly increment the password until it is a valid password.
     */
    protected Optional<String> nextValidPassword(String oldPassword) {
        return Stream.iterate(nextPassword(oldPassword), this::nextPassword)
                .filter(this::isValidPassword)
                .findFirst();
    }

    /**
     * Increment the given password by one.
     */
    private String nextPassword(String oldPassword) {
        StringBuilder newPassword = new StringBuilder(oldPassword.length());
        newPassword.append(oldPassword);
        int i;
        for (i = oldPassword.length() - 1; i >= 0 && 'z' == oldPassword.charAt(i); i--)
            newPassword.setCharAt(i, 'a');
        if (i >= 0) // Increment first non-z letter.
            newPassword.setCharAt(i, (char)(oldPassword.charAt(i) + 1));
        return newPassword.toString();
    }

    /**
     * Validate the password with the criteria below.
     */
    private boolean isValidPassword(String password) {
        // The password must have 8 lowercase letters.
        if (!password.matches("[a-z]{8}"))
            return false;
        // It must also have three increasing letters.
        if (!Pattern.matches(".*?(.)\\1\\1.*",
                IntStream.range(0, password.length())
                        // Decrease each letter by its index.
                        .map(i -> password.charAt(i) - i)
                                // Then collect into a CharSequence.
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint,
                                StringBuilder::append)))
            return false;
        // It should not have the letters 'i', 'l', or 'o'.
        if (password.matches(".*?[ilo].*"))
            return false;
        // Finally, it must have two non-overlapping pairs of the same letters.
        return password.matches(".*?(.)\\1.*?(.)\\2.*");
    }

}

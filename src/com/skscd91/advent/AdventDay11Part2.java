package com.skscd91.advent;

import java.io.BufferedReader;

/**
 * Created by sk-scd91 on 12/11/15.
 */
public class AdventDay11Part2 extends AdventDay11Part1 {

    @Override
    public String compute(BufferedReader input) {
        String password = input.lines().findFirst().orElse("aaaaaaaa");
        password = nextValidPassword(password).flatMap(this::nextValidPassword).orElse("No password");
        return password + " is Santa's next password.";
    }

}

package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/3/15.
 */
public class AdventDay3Part2 implements Advent {
    @Override
    public int getDay() {
        return 3;
    }

    @Override
    public String compute(BufferedReader input) {
        // Get the directions from the Reader into a string.
        String directions = input.lines().collect(Collectors.joining());

        // Split the interleaved directions for Santa and RoboSanta.
        String santaRoute = IntStream.range(0, (directions.length() + 1) / 2)
                .collect(StringBuilder::new,
                        (route, i) -> route.append(directions.charAt(i * 2)),
                        StringBuilder::append).toString();
        String robotRoute = IntStream.range(0, directions.length() / 2)
                .collect(StringBuilder::new,
                        (route, i) -> route.append(directions.charAt(i * 2 + 1)),
                        StringBuilder::append).toString();

        // Get the coordinates for where they go, then count the houses they went to.
        long count = Stream.concat(new AdventDay3Part1().streamOfCoordinates(Stream.of(santaRoute)),
                                   new AdventDay3Part1().streamOfCoordinates(Stream.of(robotRoute)))
                .distinct()
                .count();

        return count + " houses receive at least one present.";
    }
}

package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/3/15.
 */
public class AdventDay3Part1 implements Advent {

    private int x;
    private int y;

    @Override
    public int getDay() {
        return 3;
    }

    /**
     * @param directions The directions to travel.
     * @return The coordinates travelled to, as String objects.
     */
    Stream<String> streamOfCoordinates(Stream<String> directions) {
        resetLocation();

        return Stream.concat(Stream.of("0,0"),
                directions
                        .flatMapToInt(String::chars)
                        .mapToObj(this::nextLocation));
    }

    @Override
    public String compute(BufferedReader input) {
        long count = streamOfCoordinates(input.lines())
                .distinct()
                .count();
        return count + " houses receive at least one present.";
    }

    /**
     * Restart the coordinates.
     */
    private void resetLocation() {
        x = y = 0;
    }

    /**
     * Move in the direction specified, then display the coordinates in a string.
     * @param direction An arrow character specifying the direction.
     * @return The coordinates travelled to, as a String.
     */
    private String nextLocation(int direction) {
        switch (direction) {
            case '^': // up
                y++;
                break;
            case '>': // right
                x++;
                break;
            case 'v': // down
                y--;
                break;
            case '<': // left
                x--;
                break;
            default:
                break;
        }
        return x + "," + y;
    }
}

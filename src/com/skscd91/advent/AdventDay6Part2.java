package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/6/15.
 */
public class AdventDay6Part2 implements Advent {

    private static final Pattern DIRECTION_PATTERN =
            Pattern.compile("(turn on|turn off|toggle) (\\d{1,3}),(\\d{1,3}) through (\\d{1,3}),(\\d{1,3})");

    // Indexes for the regex groupings.
    private static final int INSTRUCTION = 1;
    private static final int START_X = 2;
    private static final int START_Y = 3;
    private static final int END_X = 4;
    private static final int END_Y = 5;

    private int[][] lights;

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public String compute(BufferedReader input) {
        lights = Stream.generate(() -> new int[1000])
                .peek(arr -> Arrays.fill(arr, 0))
                .limit(1000).toArray(size -> new int[size][]);

        input.lines().forEach(this::setLights);

        int count = Arrays.stream(lights).flatMapToInt(Arrays::stream).sum();

        lights = null;
        return "The brightness is " + count + ".";
    }

    protected void setLights(String direction) {
        Matcher matcher = DIRECTION_PATTERN.matcher(direction);
        if (!matcher.matches())
            return;
        int startX = Integer.parseInt(matcher.group(START_X)),
                endX = Integer.parseInt(matcher.group(END_X));
        Arrays.stream(lights).skip(startX).limit(endX - startX + 1)
                .forEach(horizontalSetter(matcher.group(INSTRUCTION),
                        Integer.parseInt(matcher.group(START_Y)),
                        Integer.parseInt(matcher.group(END_Y))));
    }

    /**
     * Similar to horizontalSetter in part 1, except it controls the brightness levels instead of
     * the on/off state of the lights.
     */
    protected Consumer<int[]> horizontalSetter(String instruction, int start, int end) {
        switch (instruction) {
            case "turn on": // Brighten by 1.
                return lightRow -> IntStream.rangeClosed(start, end).forEach(i -> lightRow[i]++);
            case "turn off": // Dim by 1.
                return lightRow -> IntStream.rangeClosed(start, end)
                        .forEach(i -> lightRow[i] = Math.max(lightRow[i] - 1, 0));
            case "toggle": // Brighten by 2.
                return lightRow -> IntStream.rangeClosed(start, end).forEach(i -> lightRow[i] += 2);
            default:
                throw new IllegalArgumentException("Instruction " + instruction + " not supported.");
        }
    }
}

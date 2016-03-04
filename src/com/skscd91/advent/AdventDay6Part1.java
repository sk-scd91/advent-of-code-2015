package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/6/15.
 */
public class AdventDay6Part1 implements Advent {

    private static final Pattern DIRECTION_PATTERN =
            Pattern.compile("(turn on|turn off|toggle) (\\d{1,3}),(\\d{1,3}) through (\\d{1,3}),(\\d{1,3})");

    // Indexes for the regex groupings.
    private static final int INSTRUCTION = 1;
    private static final int START_X = 2;
    private static final int START_Y = 3;
    private static final int END_X = 4;
    private static final int END_Y = 5;

    protected List<BitSet> lights;

    @Override
    public int getDay() {
        return 6;
    }

    @Override
    public String compute(BufferedReader input) {
        lights = Stream.generate(()->new BitSet(1000)).limit(1000).collect(Collectors.toList());

        input.lines().forEach(this::setLights);

        int count = lights.stream().mapToInt(BitSet::cardinality).sum();

        lights = null;
        return count + " lights are lit.";
    }

    /**
     * Validate the direction specified in the String.
     * If it is valid, activate the lights of the given boundaries.
     * Otherwise, ignore the String.
     */
    protected void setLights(String direction) {
        Matcher matcher = DIRECTION_PATTERN.matcher(direction);
        if (!matcher.matches())
            return;
        int startX = Integer.parseInt(matcher.group(START_X)),
                endX = Integer.parseInt(matcher.group(END_X));
        lights.stream().skip(startX).limit(endX - startX + 1) // Do from range [startX, endX).
                .forEach(horizontalSetter(matcher.group(INSTRUCTION),
                        Integer.parseInt(matcher.group(START_Y)),
                        Integer.parseInt(matcher.group(END_Y)) + 1));
    }

    /**
     * @param instruction Whether the lights should turn on, turn off, or toggle.
     * @param start The inclusive start-point of the lights to be activated.
     * @param end The exclusive endpoint of the lights.
     * @return The setter that activates the lights with the given instruction from range [start, end).
     */
    protected Consumer<BitSet> horizontalSetter(String instruction, int start, int end) {
        switch (instruction) {
            case "turn on":
                return lightSet -> lightSet.set(start, end);
            case "turn off":
                return lightSet -> lightSet.clear(start, end);
            case "toggle":
                return lightSet -> lightSet.flip(start, end);
            default:
                throw new IllegalArgumentException("Instruction " + instruction + " not supported.");
        }
    }
}

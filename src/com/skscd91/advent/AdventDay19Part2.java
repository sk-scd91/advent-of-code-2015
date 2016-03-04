package com.skscd91.advent;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.io.BufferedReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sk-scd91 on 12/19/15.
 */
public class AdventDay19Part2 extends AdventDay19Part1 {

    private static final Pattern MOLECULE_REPLACEMENT =
            Pattern.compile("(?<particle>[A-Za-z]+) => (?<replacement>[A-Za-z]+)");

    private Map<String, String> transforms;

    @Override
    public String compute(BufferedReader input) {
        String[] lines = input.lines().toArray(String[]::new);
        String compound = lines[lines.length - 1];

        // Extract particles with their replacements.
        transforms = Arrays.stream(lines, 0, lines.length - 2)
                .map(MOLECULE_REPLACEMENT::matcher)
                .filter(Matcher::matches)
                .collect(Collectors.toMap(m -> m.group("replacement"), m -> m.group("particle"),
                        (a, b) -> b, LinkedHashMap::new));

        if (transforms.size() != lines.length - 2)
            return "Try Again.";

        long steps = stepsToCreate(compound);

        transforms = null;
        return "It takes " + steps + " number of steps to make the compound.";
    }

    /**
     * @return The steps to create the given compound using the transform rules.
     */
    private long stepsToCreate(String compound) {
        StringBuilder currentCompound = new StringBuilder(compound.length());
        currentCompound.append(compound);

        return EntryStream.of(StreamEx.constant(transforms, Integer.MAX_VALUE)
                    .takeWhile(ignore -> !"e".contentEquals(currentCompound))
                .flatMap(map -> map.entrySet().stream()))
                .mapKeys(Pattern::compile)
                .mapKeys(pattern -> pattern.matcher(currentCompound))
                .filterKeys(Matcher::find)
                .mapKeyValue((match, p) -> currentCompound.replace(match.start(), match.end(), p))
                .count();
    }

}

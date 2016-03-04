package com.skscd91.advent;

import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;

import java.io.BufferedReader;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sk-scd91 on 12/16/15.
 */
public class AdventDay16Part1 implements Advent {

    private static final Pattern SUE_PATTERN = Pattern.compile("Sue (?<number>\\d+): (?<rest>.+)");
    private static final Pattern COMMAS = Pattern.compile(", ");
    private static final Pattern COLONS = Pattern.compile(": ");

    // Sue's possessions.
    protected Map<String, Integer> sues = EntryStream
            .of(    "children",    3, "cats",   7, "samoyeds", 2)
            .append("pomeranians", 3, "akitas", 0, "vizslas", 0)
            .append("goldfish",    5, "trees",  3, "cars",     2)
            .append("perfumes",    1)
            .toMap();

    @Override
    public int getDay() {
        return 16;
    }

    @Override
    public String compute(BufferedReader input) {

        String sue = input.lines()
                .map(SUE_PATTERN::matcher)
                .filter(Matcher::matches)
                .flatMap(this::matchingProperties)
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse("None");

        return "Sue #" + sue + " sent the gift.";
    }

    /**
     * @param sue Information on the current Sue, already parsed.
     * @return A Stream containing the current Sue's number, paired with the count of properties that match the Sue
     * being searched for, if all known properties match.
     */
    private EntryStream<String, Long> matchingProperties(Matcher sue) {

        Map<Boolean, Long> matchingPropCount = StreamEx.of(COMMAS.splitAsStream(sue.group("rest")))
                .map(COLONS::split)
                .mapToEntry(a -> a[0], a -> a[1])
                .filterKeys(sues::containsKey)
                .mapValues(Integer::new)
                .collect(Collectors.partitioningBy(e -> hasProp(e.getKey(), e.getValue()),
                        Collectors.counting()));

        Long count = 0L;

        if (matchingPropCount.get(false).equals(0L))
            count = matchingPropCount.get(true);

        return EntryStream.of(sue.group("number"), count);
    }

    /**
     * @return If the Sue being searched for has the exact number of this property.
     */
    protected boolean hasProp(String propName, Integer count) {
        return sues.get(propName).equals(count);
    }
}

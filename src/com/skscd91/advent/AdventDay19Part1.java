package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/19/15.
 */
public class AdventDay19Part1 implements Advent {

    private static final Pattern MOLECULE_REPLACEMENT =
            Pattern.compile("(?<atom>[A-Z][a-z]*) => (?<replacement>[A-Za-z]+)");

    @Override
    public int getDay() {
        return 19;
    }

    @Override
    public String compute(BufferedReader input) {
        String[] lines = input.lines().toArray(String[]::new);
        String compound = lines[lines.length - 1];

        long count = Arrays.stream(lines, 0, lines.length - 2)
                .map(MOLECULE_REPLACEMENT::matcher)
                .filter(Matcher::matches)
                .flatMap(m -> moleculesForPattern(m.group("atom"), m.group("replacement"), compound))
                .distinct()
                .count();

        return count + " distinct molecules can be created.";
    }

    /**
     * Generate all possible molecules where one atom of the given element is replaced
     * with the given replacement molecule.
     * @param atom The element atom to replace.
     * @param replacement The replacement molecule.
     * @param compound The compound to transform.
     * @return A stream of possible molecules that can be created.
     */
    protected static Stream<String> moleculesForPattern(String atom, String replacement, String compound) {
        Matcher moleculeMatcher = Pattern.compile(atom).matcher(compound);
        StringBuilder moleculeBuilder = new StringBuilder(compound.length() - atom.length() + replacement.length());
        Stream.Builder<String> molecules = Stream.builder();

        while(moleculeMatcher.find()) {
            moleculeBuilder.setLength(0);
            molecules.accept(moleculeBuilder.append(compound, 0, moleculeMatcher.start())
                    .append(replacement)
                    .append(compound, moleculeMatcher.end(), compound.length())
                    .toString());
        }

        return molecules.build();
    }
}

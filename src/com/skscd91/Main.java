package com.skscd91;

import com.skscd91.advent.*;

import java.io.*;
import java.util.Arrays;

public class Main {

    private static final Advent[] adventPuzzles = new Advent[] {
        new AdventDay1Part1(), new AdventDay1Part2(),
        new AdventDay2Part1(), new AdventDay2Part2(),
        new AdventDay3Part1(), new AdventDay3Part2(),
        new AdventDay4Part1(), new AdventDay4Part2(),
        new AdventDay5Part1(), new AdventDay5Part2(),
        new AdventDay6Part1(), new AdventDay6Part2(),
        new AdventDay7Part1(), new AdventDay7Part2(),
        new AdventDay8Part1(), new AdventDay8Part2(),
        new AdventDay9Part1(), new AdventDay9Part2(),
        new AdventDay10Part1(), new AdventDay10Part2(),
        new AdventDay11Part1(), new AdventDay11Part2(),
        new AdventDay12Part1(), new AdventDay12Part2(),
        new AdventDay13Part1(), new AdventDay13Part2(),
        new AdventDay14Part1(), new AdventDay14Part2(),
        new AdventDay15Part1(), new AdventDay15Part2(),
        new AdventDay16Part1(), new AdventDay16Part2(),
        new AdventDay17Part1(), new AdventDay17Part2(),
        new AdventDay18Part1(), new AdventDay18Part2(),
        new AdventDay19Part1(), new AdventDay19Part2(),
        new AdventDay20Part1(), new AdventDay20Part2(),
        new AdventDay21Part1(), new AdventDay21Part2(),
        new AdventDay22Part1(), new AdventDay22Part2(),
        new AdventDay23Part1(), new AdventDay23Part2(),
        new AdventDay24Part1(), new AdventDay24Part2(),
        new AdventDay25Part1()
    };

    private static final String FILE_FORMAT = "Day%d";

    private static BufferedReader readerForDay(int day) {
        BufferedReader reader = null;
        try {
            final FileReader fileReader = new FileReader(String.format(FILE_FORMAT, day));
            reader = new BufferedReader(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private static String computeForAdvent(Advent puzzle) {
        BufferedReader reader = readerForDay(puzzle.getDay());
        String result = puzzle.compute(reader);
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
	// write your code here
        final int day = (args.length > 0) ? Integer.parseInt(args[0]) : 0;
        Arrays.stream(adventPuzzles)
                .filter(puzzle -> puzzle.getDay() >= day)
                .map(Main::computeForAdvent)
                .forEachOrdered(System.out::println);
    }
}

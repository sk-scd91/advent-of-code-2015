package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/14/15.
 */
public class AdventDay14Part1 implements Advent {

    private static final Pattern RSPEED_PATTERN =
            Pattern.compile("([A-Za-z]+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds\\.");

    protected static final int TIME = 2503;

    protected static class Reindeer {
        private final String name;
        private final int speed; // in km/s
        private final int runTime; // in s
        private final int restTime; // in s

        public Reindeer(String name, int speed, int runTime, int restTime) {
            this.name = name;
            this.speed = speed;
            this.runTime = runTime;
            this.restTime = restTime;
        }

        // Get the name of the reindeer.
        public String getName() {
            return name;
        }

        // Calculate the distance the reindeer travels for the given time.
        public int distanceForTime(int seconds) {
            int intervals = seconds / (runTime + restTime);
            return speed * runTime * intervals + Math.min(seconds % (runTime + restTime), runTime) * speed;
        }

        // Calculate the time traveled for the given distance.
        public int timeForDistance(int km) {
            return (km / speed) * (runTime + restTime) / runTime;
        }
    }

    @Override
    public int getDay() {
        return 14;
    }

    @Override
    public String compute(BufferedReader input) {

        int mostTraveled = parseReindeer(input.lines())
                .mapToInt(r -> r.distanceForTime(TIME))
                .max().orElse(-1);

        return "The winning reindeer traveled " + mostTraveled + "km";
    }

    /**
     * Parse the lines into Reindeer.
     */
    protected Stream<Reindeer> parseReindeer(Stream<String> lines) {
        return lines
                .map(RSPEED_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(m -> new Reindeer(m.group(1),
                        Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3)),
                        Integer.parseInt(m.group(4))));
    }
}

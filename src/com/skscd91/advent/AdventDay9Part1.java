package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;

/**
 * Created by sk-scd91 on 12/9/15.
 */
public class AdventDay9Part1 implements Advent {

    protected static final class Edge implements Comparable<Edge> {

        public List<String> path;
        public int distance;

        public Edge(String src, String dst, int distance) {
            path = Arrays.asList(src, dst);
            this.distance = distance;
        }

        public Edge(List<String> prevPath, String dst, int distance) {
            path = new ArrayList<>(prevPath);
            path.add(dst);
            this.distance = distance;
        }

        public String nextLocation() {
            return path.get(path.size() - 1);
        }

        public boolean hasVisited(String location) {
            return !path.contains(location);
        }

        @Override
        public int compareTo(Edge o) {
            return distance - o.distance;
        }
    }

    private static final Pattern DISTANCE_PATTERN = Pattern.compile("([A-Za-z]*) to ([A-Za-z]*) = (\\d+)");

    protected Map<String, Map<String, Integer>> routes;

    @Override
    public int getDay() {
        return 9;
    }

    @Override
    public String compute(BufferedReader input) {
        // first parse the directions.
        Matcher[] matches = input.lines()
                .map(DISTANCE_PATTERN::matcher)
                .filter(Matcher::matches)
                .toArray(Matcher[]::new);

        // Collect the matches into a 2D map.
        routes = Arrays.stream(matches)
                .collect(groupingBy(m -> m.group(1),
                        toMap(m -> m.group(2), m -> Integer.valueOf(m.group(3)))));

        Arrays.stream(matches) // Merge with transposed entries to make bidirectional.
                .collect(groupingBy(m -> m.group(2),
                        toMap(m -> m.group(1), m -> Integer.valueOf(m.group(3)))))
                .forEach((outerKey, map) -> {
                    routes.computeIfAbsent(outerKey, x -> new HashMap<>())
                    .putAll(map);
                });

        // Find the optimal path of each start location.
        int optimalDistance = routes.keySet().stream()
                .mapToInt(this::shortestDistanceFromLocation)
                .reduce(0, this::reduce);

        routes = null;
        return getResult(optimalDistance);
    }

    /**
     * Using Dijkstra's algorithm, search for the optimal distance.
     * @param location Start location of the search.
     * @return The shortest/longest distance through all locations.
     */
    private int shortestDistanceFromLocation(String location) {
        PriorityQueue<Edge> remaining = new PriorityQueue<>(getEdgeComparator());
        remaining.addAll(routes.get(location).entrySet().stream()
                .map(e -> new Edge(location, e.getKey(), e.getValue()))
                .collect(toList()));


        for(Edge next = remaining.poll(); next != null; next = remaining.poll()) {
            if (next.path.size() == routes.size()) {
                return next.distance;
            }

            String nextLocation = next.nextLocation();

            final Edge finalEdge = next;

            if (routes.containsKey(nextLocation))
                routes.get(nextLocation).entrySet().stream()
                    .filter(e -> finalEdge.hasVisited(e.getKey()))
                    .map(e -> new Edge(finalEdge.path, e.getKey(), finalEdge.distance + e.getValue()))
                    .forEach(remaining::add);
        }

        return 0;
    }

    /**
     * @return A comparator for Edge, that can be overridden by subclasses of this class.
     */
    protected Comparator<Edge> getEdgeComparator() {
        return Edge::compareTo;
    }

    /**
     * Reducing function that returns y when x is 0, or the optimal argument otherwise.
     * Used for allowing zero as the default value.
     */
    protected int reduce(int x, int y) {
        return (x == 0 || (y < x && y != 0)) ? y : x;
    }

    /**
     * @param distance The optimal distance traveled.
     * @return The message
     */
    protected String getResult(int distance) {
        return "The shortest distance is " + distance + ".";
    }

}

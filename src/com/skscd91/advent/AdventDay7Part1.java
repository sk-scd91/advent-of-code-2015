package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sk-scd91 on 12/7/15.
 */
public class AdventDay7Part1 implements Advent {

    private final class Gate {

        private static final int MASK = 0xffff; // Enforce 16-bit unsigned ints.

        private final String instruction;
        private final String lhs;
        private final String rhs;
        private final String destination;

        public Gate(String instruction, String lhs, String rhs, String destination) {
            this.instruction = instruction;
            this.lhs = lhs;
            this.rhs = rhs;
            this.destination = destination;
        }

        /**
         * @return If the operands are constants or wires already evaluated.
         */
        public boolean canEvaluate() {
            return (isNumber(lhs) || wires.containsKey(lhs))
                    && (rhs == null || isNumber(rhs) || wires.containsKey(rhs));
        }

        /**
         * Evaluate the gate.
         */
        public void evaluate() {
            if (instruction == null || "".equals(instruction)) {
                wires.put(destination, isNumber(lhs) ? Integer.valueOf(lhs) : wires.get(lhs));
            } else if ("NOT".equals(instruction)) {
                wires.put(destination, (isNumber(lhs) ? Integer.valueOf(lhs) : wires.get(lhs)) ^ MASK);
            } else {
                int leftInt = isNumber(lhs) ? Integer.valueOf(lhs) : wires.get(lhs);
                int rightInt = isNumber(rhs) ? Integer.valueOf(rhs) : wires.get(rhs);
                int result;
                switch (instruction) {
                    case "AND":
                        result = leftInt & rightInt;
                        break;
                    case "OR":
                        result = leftInt | rightInt;
                        break;
                    case "LSHIFT":
                        result = (leftInt << rightInt) & MASK;
                        break;
                    case "RSHIFT":
                        result = (leftInt >>> rightInt) & MASK;
                        break;
                    default:
                        throw new IllegalArgumentException(instruction + " not supported.");
                }
                wires.put(destination, result);
            }
        }

        /**
         * Test if the string is all decimal digits.
         */
        private boolean isNumber(String s) {
            return s.matches("\\d+");
        }
    }

    protected SortedMap<String, Integer> wires;

    @Override
    public int getDay() {
        return 7;
    }

    @Override
    public String compute(BufferedReader input) {
        wires = new TreeMap<>();

        computeSignals(input.lines());

        Integer signal = wires.get("a");
        wires = null;
        return "Signal " + signal + " is provided to wire a.";
    }

    /**
     * Compute the signal levels of the gates from the given circuit.
     * @param input A sequence of instructions to connect the gates.
     */
    protected void computeSignals(Stream<String> input) {
        Predicate<Gate> tryEvaluate = this::tryEvaluate; // Captured predicate to be negated.

        List<Gate> unevaluated = input
                .map(line -> line.split(" -> ")) // Split by operator.
                .map(this::createGate) // Parse the rest into a gate.
                .filter(tryEvaluate.negate()) // Try to evaluate the gate.
                .collect(Collectors.toList()); // Store unevaluated gate instructions for later.

        // Keep evaluating until there are no more gate instructions.
        while(!unevaluated.isEmpty())
            unevaluated.removeIf(tryEvaluate);
    }

    /**
     * Create a gate object, based on the expression separated by the "->" operator.
     * @param parts Left hand and right hand parts of an expression.
     * @return A gate representing the expression.
     */
    private Gate createGate(String[] parts) {
        Scanner sourceScanner = new Scanner(parts[0]); // Get operators and operands from the scanner.
        String destination = parts[1];
        String lhs = sourceScanner.next();
        if("NOT".equals(lhs)) { // Unary not.
            lhs = sourceScanner.next();
            return new Gate("NOT", lhs, null, destination);
        } else if (!sourceScanner.hasNext()) { // Set expression.
            return new Gate("", lhs, null, destination);
        } // other expressions.
        String instruction = sourceScanner.next();
        String rhs = sourceScanner.next();
        return new Gate(instruction, lhs, rhs, destination);
    }

    /**
     * Test if the gate can be evaluated, then evaluate if it can.
     * @param g The Gate to evaluate.
     * @return If the gate has been evaluated.
     */
    private boolean tryEvaluate(Gate g) {
        if (g.canEvaluate()) {
            g.evaluate();
            return true;
        }
        return false;
    }
}

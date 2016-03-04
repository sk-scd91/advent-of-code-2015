package com.skscd91.advent;

import java.io.BufferedReader;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.regex.Pattern;

/**
 * Created by sk-scd91 on 12/23/15.
 */
public class AdventDay23Part1 implements Advent {

    protected int regA;
    protected int regB;
    protected int regPC;

    @Override
    public int getDay() {
        return 23;
    }

    @Override
    public String compute(BufferedReader input) {
        resetRegisters();

        String[][] instructions = input.lines()
                .map(Pattern.compile(" ")::split)
                .toArray(String[][]::new);

        while (regPC >= 0 && regPC < instructions.length)
            executeInstruction(instructions[regPC]);

        return "Register B is " + regB + ".";
    }

    // Reset the registers to their default values.
    protected void resetRegisters() {
        regA = regB = regPC = 0;
    }

    // Execute the given instruction.
    private void executeInstruction(String[] instruction) {
        switch (instruction[0]) {
            case "hlf": // Divide by 2.
                executeModifier(instruction[1], r -> r >>> 1);
                break;
            case "tpl": // Multiply by 3.
                executeModifier(instruction[1], r -> (r * 3) & Integer.MAX_VALUE);
                break;
            case "inc": // Add 1.
                executeModifier(instruction[1], r -> (r + 1) & Integer.MAX_VALUE);
                break;
            case "jmp": { // Jump relative to start of instruction.
                int offset = Integer.parseInt(instruction[1]);
                regPC += offset;
                return;
            }
            case "jie": // Jump if even.
                if (executeConditional(instruction[1], r -> (r & 1) == 0)) {
                    int offset = Integer.parseInt(instruction[2]);
                    regPC += offset;
                    return;
                }
                break;
            case "jio": // Jump if one.
                if (executeConditional(instruction[1], r -> r == 1)) {
                    int offset = Integer.parseInt(instruction[2]);
                    regPC += offset;
                    return;
                }
                break;
            default:
                throw new IllegalArgumentException(instruction[0] + " is not supported.");
        }
        regPC++;
    }

    // Execute an operator that modifies the register.
    private void executeModifier(String argument, IntUnaryOperator operator) {
        if ("a".equals(argument))
            regA = operator.applyAsInt(regA);
        else if ("b".equals(argument))
            regB = operator.applyAsInt(regB);
        else
            throw new IllegalArgumentException(argument + " is not a register.");
    }

    // Execute a condition for the register, to branch when true.
    private boolean executeConditional(String argument, IntPredicate condition) {
        int register;
        if ("a,".equals(argument))
            register = regA;
        else if ("b,".equals(argument))
            register = regB;
        else
            throw new IllegalArgumentException(argument.substring(0, Math.min(argument.length(), 1))
                    + " is not a register.");
        return condition.test(register);
    }

}

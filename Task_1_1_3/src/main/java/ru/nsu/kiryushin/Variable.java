package ru.nsu.kiryushin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable reference in the expression tree.
 */
public class Variable extends Expression {
    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile("\\b([a-zA-Z]+)\\s*=\\s*(-?\\d+)\\b");

    private final String variable;

    /**
     * Creates a new variable node.
     *
     * @param variable variable name
     */
    public Variable(String variable) {
        this.variable = variable;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.variable;
    }

    /** {@inheritDoc} */
    @Override
    public int eval(String variables) {
        Matcher matcher = ASSIGNMENT_PATTERN.matcher(variables);
        while (matcher.find()) {
            if (matcher.group(1).equals(variable)) {
                return Integer.parseInt(matcher.group(2));
            }
        }
        throw new IllegalArgumentException("Variable is not assigned: " + variable);
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        if (variable.equals(this.variable)) {
            return new Number(1);
        }
        return new Number(0);
    }
}


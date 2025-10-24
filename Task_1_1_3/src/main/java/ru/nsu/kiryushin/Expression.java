package ru.nsu.kiryushin;

/**
 * Base class for all expression tree nodes.
 */
public abstract class Expression {

    /**
     * Converts the expression to the canonical string representation.
     *
     * @return expression text
     */
    @Override
    public abstract String toString();

    /**
     * Evaluates the expression using the provided variable assignments.
     *
     * @param variables assignments in the form {@code name = value}
     * @return computed integer value
     */
    public abstract int eval(String variables);

    /**
     * Calculates the symbolic derivative for the specified variable.
     *
     * @param variable name of the variable to differentiate by
     * @return expression describing the derivative
     */
    public abstract Expression derivative(String variable);
}


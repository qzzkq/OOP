package ru.nsu.kiryushin;

/**
 * Contract for arithmetic expression nodes.
 *
 * Implementers:
 * - Keep nodes immutable.
 * - Make toString() canonical (works with Parser).
 * - Throw IllegalArgumentException if a variable is missing; ArithmeticException for bad ops.
 *
 * Usage:
 *   Expression e = new Div(new Mul(new Add(new Variable("x"), new Number(3)), new Variable("y")),
 *                          new Number(2));
 *   e.eval("x=5 y=4"); // 16
 */
public interface Expression {

    /**
     * Evaluates the expression using the provided variable assignments.
     *
     * @param variables assignments in the form {@code name = value}
     * @return computed integer value
     */
    int eval(String variables);

    /**
     * Calculates the symbolic derivative for the specified variable.
     *
     * @param variable name of the variable to differentiate by
     * @return expression describing the derivative
     */
    Expression derivative(String variable);

    /**
     * Creates a simplified copy of the expression according to algebraic rules.
     *
     * @return simplified expression tree
     */
    Expression simplification();
}


package ru.nsu.kiryushin;

/**
 * Represents a constant numeric value in the expression tree.
 */
public class Number extends Expression {

    private final int value;

    /**
     * Creates a constant expression with the specified value.
     *
     * @param value integer constant stored in the node
     */
    public Number(int value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /** {@inheritDoc} */
    @Override
    public int eval(String variables) {
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }
}

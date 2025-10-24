package ru.nsu.kiryushin;

/**
 * Binary addition node for the expression tree.
 */
public class Add extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates a new addition expression.
     *
     * @param left  left operand
     * @param right right operand
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + this.left.toString() + "+" + this.right.toString() + ")";
    }

    /** {@inheritDoc} */
    @Override
    public int eval(String variables) {
        return this.left.eval(variables) + this.right.eval(variables);
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        return new Add(this.left.derivative(variable), this.right.derivative(variable));
    }
}

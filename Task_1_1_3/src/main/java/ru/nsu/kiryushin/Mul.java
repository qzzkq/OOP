package ru.nsu.kiryushin;

/**
 * Binary multiplication node for the expression tree.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates a new multiplication expression.
     *
     * @param left  left operand
     * @param right right operand
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + this.left.toString() + "*" + this.right.toString() + ")";
    }

    /** {@inheritDoc} */
    @Override
    public int eval(String variables) {
        return this.left.eval(variables) * this.right.eval(variables);
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        Expression leftDerivative = new Mul(this.left.derivative(variable), this.right);
        Expression rightDerivative = new Mul(this.left, this.right.derivative(variable));
        return new Add(leftDerivative, rightDerivative);
    }
}

package ru.nsu.kiryushin;

/**
 * Binary multiplication node for the expression tree.
 */
public class Mul implements Expression {
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
    /** {@inheritDoc} */
    @Override
    public Expression simplification() {
        Expression simplifiedLeft = this.left.simplification();
        Expression simplifiedRight = this.right.simplification();

        Integer leftValue = simplifiedLeft instanceof Number ? simplifiedLeft.eval("") : null;
        Integer rightValue = simplifiedRight instanceof Number ? simplifiedRight.eval("") : null;

        if (leftValue != null && leftValue == 0 || rightValue != null && rightValue == 0) {
            return new Number(0);
        }

        if (leftValue != null && leftValue == 1) {
            return simplifiedRight;
        }

        if (rightValue != null && rightValue == 1) {
            return simplifiedLeft;
        }

        if (leftValue != null && rightValue != null) {
            int value = leftValue * rightValue;
            return new Number(value);
        }

        return new Mul(simplifiedLeft, simplifiedRight);
    }
}

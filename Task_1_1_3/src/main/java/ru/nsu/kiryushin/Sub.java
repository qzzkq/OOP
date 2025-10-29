package ru.nsu.kiryushin;

/**
 * Binary subtraction node for the expression tree.
 */
public class Sub implements Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates a new subtraction expression.
     *
     * @param left  left operand
     * @param right right operand
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + this.left.toString() + "-" + this.right.toString() + ")";
    }

    /** {@inheritDoc} */
    @Override
    public int eval(String variables) {
        return this.left.eval(variables) - this.right.eval(variables);
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        return new Sub(this.left.derivative(variable), this.right.derivative(variable));
    }

    /** {@inheritDoc} */
    @Override
    public Expression simplification() {
        Expression simplifiedLeft = this.left.simplification();
        Expression simplifiedRight = this.right.simplification();

        if (simplifiedLeft.toString().equals(simplifiedRight.toString())) {
            return new Number(0);
        }

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int value = simplifiedLeft.eval("") - simplifiedRight.eval("");
            return new Number(value);
        }

        return new Sub(simplifiedLeft, simplifiedRight);
    }
}


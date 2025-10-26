package ru.nsu.kiryushin;

/**
 * Binary division node for the expression tree.
 */
public class Div implements Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates a new division expression.
     *
     * @param left  numerator expression
     * @param right denominator expression
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + this.left.toString() + "/" + this.right.toString() + ")";
    }

    /** {@inheritDoc} */
    @Override
    public int eval(String variables) {
        int leftValue = this.left.eval(variables);
        int rightValue = this.right.eval(variables);
        if (rightValue == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return leftValue / rightValue;
    }

    /** {@inheritDoc} */
    @Override
    public Expression derivative(String variable) {
        Expression numerator = new Sub(
                new Mul(this.left.derivative(variable), this.right),
                new Mul(this.left, this.right.derivative(variable))
        );
        Expression denominator = new Mul(this.right, this.right);
        return new Div(numerator, denominator);
    }

    /** {@inheritDoc} */
    @Override
    public Expression simplification() {
        Expression simplifiedLeft = this.left.simplification();
        Expression simplifiedRight = this.right.simplification();

        if (simplifiedLeft instanceof Number && simplifiedRight instanceof Number) {
            int denominator = simplifiedRight.eval("");
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero");
            }
            int value = simplifiedLeft.eval("") / denominator;
            return new Number(value);
        }

        return new Div(simplifiedLeft, simplifiedRight);
    }
}

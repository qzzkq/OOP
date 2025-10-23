package ru.nsu.kiryushin;

public class Div extends Expression{
    final private Expression left;
    final private Expression right;

    Div(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(){
        return "(" + this.left.toString() + "/" + this.right.toString() + ")";
    }

    @Override
    public int eval(String variables){
        int leftValue = this.left.eval(variables);
        int rightValue = this.right.eval(variables);
        if (rightValue == 0){
            throw new ArithmeticException("Division by zero");
        }
        return leftValue / rightValue;
    }

    @Override
    public Expression derivative(String variable){
        Expression leftV = new Mul(this.left.derivative(variable), this.right);
        Expression rightV = new Mul(this.left, this.right.derivative(variable));
        return new Div(new Sub(leftV, rightV), new Mul(rightV, rightV));
    }

}
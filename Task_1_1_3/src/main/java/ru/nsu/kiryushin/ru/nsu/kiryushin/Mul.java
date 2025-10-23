package ru.nsu.kiryushin;

public class Mul extends Expression{
    final private Expression left;
    final private Expression right;

    Mul(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(){
        return "(" + this.left.toString() + "*" + this.right.toString() + ")";
    }

    @Override
    public int eval(String variables){
        return this.left.eval(variables) * this.right.eval(variables);
    }

    @Override
    public Expression derivative(String variable){
        Expression leftV = new Mul(this.left.derivative(variable), this.right);
        Expression rightV = new Mul(this.left, this.right.derivative(variable));
        return new Add(leftV, rightV);
    }
}
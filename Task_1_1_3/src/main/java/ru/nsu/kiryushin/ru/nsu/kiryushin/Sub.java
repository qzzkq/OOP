package ru.nsu.kiryushin;

public class Sub extends Expression{
    final private Expression left;
    final private Expression right;

    Sub(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(){
        return "(" + this.left.toString() + "-" + this.right.toString() + ")";
    }

    @Override
    public int eval(String variables){
        return this.left.eval(variables) - this.right.eval(variables);
    }

    @Override
    public Expression derivative(String variable){
        return new Sub(this.left.derivative(variable), this.right.derivative(variable));
    }
}

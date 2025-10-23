package ru.nsu.kiryushin;

public class Number extends Expression{

    final private int value;

    Number(int value){
        this.value = value;
    }

    @Override
    public String toString(){
        return String.valueOf(this.value);
    }

    @Override
    public int eval(String variables){
        return this.value;
    }

    @Override
    public Expression derivative(String variable){
        return new Number(0);
    }
}
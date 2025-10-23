package ru.nsu.kiryushin;

public abstract class Expression{

    public abstract String toString();

    public abstract int eval(String variables);

    public abstract Expression derivative(String variable);
}

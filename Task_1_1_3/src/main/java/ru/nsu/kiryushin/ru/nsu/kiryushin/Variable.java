package ru.nsu.kiryushin;

public class Variable extends Expression{
    private final String variable;

    Variable(String variable){
        this.variable = variable;
    }

    @Override
    public String toString(){
        return this.variable;
    }

    @Override
    public int eval(String variables){
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("\\b" + java.util.regex.Pattern.quote(variable) + "\\s*=\\s*(-?\\d+)\\b")
                .matcher(variables);
        if (!m.find()) throw new RuntimeException("Не задана переменная: " + variable);
        return Integer.parseInt(m.group(1));
    }

    @Override
    public Expression derivative(String variable){
        if (variable.equals(this.variable)){
            return new Number(1);
        }
        else{
            return new Number(0);
        }
    }
}

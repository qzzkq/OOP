package ru.nsu.kiryushin;

import java.util.List;
public class Parser{
    private int pos;
    final private String text;
    final private int len;

    private String readToken(){
       while ((pos < len) && Character.isWhitespace(text.charAt(pos))){
           pos++;
       }

       if (pos == len){
           return "";
       }

       if (List.of('+', '-', '*', '/', ')','(').contains(text.charAt(pos))){
           String token = Character.toString(text.charAt(pos));
           pos++;
           return token;
       }


       int left = pos;

       while ((pos < len) && Character.isLetterOrDigit(text.charAt(pos))){
           pos++;
       }

       if (left == pos){
           throw new RuntimeException("Unexpected character: '" + text.charAt(pos) + "' at " + pos);
       }
       return text.substring(left, pos);
    }

    private String peekToken(){
        int oldPos = pos;
        String token = readToken();
        pos = oldPos;
        return token;
    }

    private Expression parseExpr(){
        Expression res = parseMonome();

        while (peekToken().equals("+") || peekToken().equals("-")){
            String operand = readToken();
            Expression nextMonome = parseMonome();
            res = operand.equals("+") ? new Add(res, nextMonome) : new Sub(res, nextMonome);
        }

        return res;
    }

    private Expression parseMonome(){
        Expression res = parseAtom();
        while (peekToken().equals("*") || peekToken().equals("/")){
            String oper = readToken();
            Expression nextAtom = parseAtom();
            if (oper.equals("*")){
                res = new Mul(res, nextAtom);
            }
            else{
                res = new Div(res, nextAtom);
            }
        }

        return res;
    }

    private Expression parseAtom(){
        Expression res;
        if (peekToken().equals("-")) { // унарный минус
            readToken();
            return new Mul(new Number(-1), parseAtom());
        }
        if (peekToken().equals("(")) {
            readToken();
            res = parseExpr();
            readToken();
            return res;
        }
        String stringRes = readToken();
        if (stringRes.matches("[a-zA-Z]+")){
            res = new Variable(stringRes);
        }
        else{
            res = new Number(Integer.parseInt(stringRes));
        }
        return res;
    }

    public Parser(String inputText){
        text = inputText;
        len = text.length();
        pos = 0;
    }

    public Expression parse(){
        Expression expr = parseExpr();
        if (!peekToken().isEmpty()) {
            throw new RuntimeException("Extra tokens after the expression");
        }
        return expr;
    }
}
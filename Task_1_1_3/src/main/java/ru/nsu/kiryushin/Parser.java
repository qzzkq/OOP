package ru.nsu.kiryushin;

import java.util.Set;

/**
 * Recursive descent parser for arithmetic expressions.
 */
public class Parser {
    private static final Set<Character> SIMPLE_TOKENS = Set.of('+', '-', '*', '/', ')', '(');
    private static final Set<String> ADD_SUB_OPERATORS = Set.of("+", "-");
    private static final Set<String> MUL_DIV_OPERATORS = Set.of("*", "/");

    private int pos;
    private final String text;
    private final int len;

    /**
     * Creates a parser for the provided text.
     *
     * @param inputText expression text to parse
     */
    public Parser(String inputText) {
        text = inputText;
        len = text.length();
        pos = 0;
    }

    /**
     * Reads the next token and advances the position.
     *
     * @return token string or an empty string if the end was reached
     */
    private String readToken() {
        while ((pos < len) && Character.isWhitespace(text.charAt(pos))) {
            pos++;
        }

        if (pos == len) {
            return "";
        }

        char current = text.charAt(pos);
        if (SIMPLE_TOKENS.contains(current)) {
            pos++;
            return Character.toString(current);
        }

        int left = pos;

        while ((pos < len) && Character.isLetterOrDigit(text.charAt(pos))) {
            pos++;
        }

        if (left == pos) {
            throw new IllegalArgumentException("Unexpected character: '" + text.charAt(pos) + "' at " + pos);
        }
        return text.substring(left, pos);
    }

    /**
     * Reads a token without advancing the current position.
     *
     * @return upcoming token
     */
    private String peekToken() {
        int oldPos = pos;
        String token = readToken();
        pos = oldPos;
        return token;
    }

    /**
     * Parses an expression made of terms separated by addition or subtraction.
     *
     * @return parsed expression node
     */
    private Expression parseExpr() {
        Expression res = parseMonome();

        while (ADD_SUB_OPERATORS.contains(peekToken())) {
            String operand = readToken();
            Expression nextMonome = parseMonome();
            res = operand.equals("+") ? new Add(res, nextMonome) : new Sub(res, nextMonome);
        }

        return res;
    }

    /**
     * Parses a term made of factors separated by multiplication or division.
     *
     * @return parsed expression node
     */
    private Expression parseMonome() {
        Expression res = parseAtom();
        while (MUL_DIV_OPERATORS.contains(peekToken())) {
            String oper = readToken();
            Expression nextAtom = parseAtom();
            if (oper.equals("*")) {
                res = new Mul(res, nextAtom);
            } else {
                res = new Div(res, nextAtom);
            }
        }

        return res;
    }

    /**
     * Parses a single atom: number, variable, parenthesized expression or unary minus.
     *
     * @return parsed expression node
     */
    private Expression parseAtom() {
        if (peekToken().equals("-")) {
            readToken();
            return new Mul(new Number(-1), parseAtom());
        }
        if (peekToken().equals("(")) {
            readToken();
            Expression res = parseExpr();
            String closing = readToken();
            if (!")".equals(closing)) {
                throw new IllegalArgumentException("Missing closing parenthesis");
            }
            return res;
        }
        String stringRes = readToken();
        if (stringRes.isEmpty()) {
            throw new IllegalArgumentException("Unexpected end of input");
        }
        if (stringRes.matches("[a-zA-Z]+")) {
            return new Variable(stringRes);
        }
        try {
            return new Number(Integer.parseInt(stringRes));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid token: " + stringRes, ex);
        }
    }

    /**
     * Parses the whole expression and ensures that no extra tokens remain.
     *
     * @return root expression node
     */
    public Expression parse() {
        Expression expr = parseExpr();
        if (!peekToken().isEmpty()) {
            throw new IllegalArgumentException("Extra tokens after the expression");
        }
        return expr;
    }
}

package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test Parser.
 */
public class ParserTest{

    /**
     * Test parse Expression with parentheses.
     */
    @Test
    void parseTestWithParentheses() {
        Parser parser = new Parser("(x+(2*(y-3)))");
        Expression expression = parser.parse();

        assertEquals("(x+(2*(y-3)))", expression.toString());
        assertEquals(18, expression.eval("x = 4 y = 10"));

        Expression derivative = expression.derivative("y");
        assertEquals("(0+((0*(y-3))+(2*(1-0))))", derivative.toString());
        assertEquals(2, derivative.eval("x = 0; y = 0"));
    }

    /**
     * Test parse Expression without parentheses.
     */
    @Test
    void parseTestWithoutParentheses() {
        Parser parser = new Parser("x+2*(y-3)");
        Expression expression = parser.parse();

        assertEquals("(x+(2*(y-3)))", expression.toString());
        assertEquals(18, expression.eval("x = 4 y = 10"));

        Expression derivative = expression.derivative("y");
        assertEquals("(0+((0*(y-3))+(2*(1-0))))", derivative.toString());
        assertEquals(2, derivative.eval("y = 10"));
    }

    /**
     * Test parse Expression with unary minus.
     */
    @Test
    void parseTestUnaryMinus() {
        Expression expression = new Parser("-x").parse();
        assertEquals("(-1*x)", expression.toString());
    }

    /**
     * Test parse with invalid character
     */
    @Test
    void parseTestInvalidCharacter() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Parser("x & y").parse());
        assertTrue(exception.getMessage().contains("Unexpected character"));
    }
}
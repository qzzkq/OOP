package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test Expressions.
 */
public class ExpressionsTest {

    /**
     * Test Number.
     */
    @Test
    void testNumber() {
        Number number = new Number(42);

        assertEquals("42", number.toString());
        assertEquals(42, number.eval(""));
        assertEquals("0", number.derivative("x").toString());
    }

    /**
     * Test Mul.
     */
    @Test
    void testMul() {
        Expression exp = new Mul(new Variable("x"), new Number(4));

        assertEquals("(x*4)", exp.toString());
        assertEquals(40, exp.eval("x = 10; y = 4"));
        assertEquals("((1*4)+(x*0))", exp.derivative("x").toString());
    }

    /**
     * Test Div.
     */
    @Test
    void testDiv() {
        Expression exp = new Div(new Number(4), new Variable("x"));

        assertEquals("(4/x)", exp.toString());
        assertEquals(2, exp.eval("x = 2; y = 400"));
        assertThrows(ArithmeticException.class, () -> exp.eval("x = 0; y = 400"));
        assertEquals("(((0*x)-(4*1))/(x*x))", exp.derivative("x").toString());
    }

    /**
     * Test Sub.
     */
    @Test
    void testSub() {
        Expression exp = new Sub(new Mul(new Variable("x"), new Variable("y")), new Number(4));

        assertEquals("((x*y)-4)", exp.toString());
        assertEquals(0, exp.eval("x = 2; y = 2"));
        assertEquals("(((1*y)+(x*0))-0)", exp.derivative("x").toString());
    }

    /**
     * Test Variable.
     */
    @Test
    void testVariable() {
        Expression exp = new Variable("x");

        assertEquals("x", exp.toString());
        assertEquals(2, exp.eval("x = 2"));
        assertThrows(IllegalArgumentException.class, () -> exp.eval("y = 2"));
        assertEquals("1", exp.derivative("x").toString());
    }

    /**
     * Test Add.
     */
    @Test
    void testAdd(){
        Expression exp = new Add(new Variable("x"), new Variable("y"));

        assertEquals("(x+y)", exp.toString());
        assertEquals(1200, exp.eval("x = 1000; y = 200"));
        assertEquals("(1+0)", exp.derivative("x").toString());
    }

}

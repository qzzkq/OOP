package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test Hand
 */
public class HandTest {

    /**
     * Test init Hand
     */
    @Test
    void Test0() {
        Card c1 = new Card(0, 0);
        Card c2 = new Card(1, 1);
        Hand hand = new Hand(c1, c2);

        assertEquals(2, hand.getHand().size());
        assertEquals(c1, hand.getHand().get(0));
        assertEquals(c2, hand.getHand().get(1));
    }

    /**
     * Test getSumHand
     */
    @Test
    void Test1() {
        Hand hand = new Hand(new Card(6, 0), new Card(7, 1));
        assertEquals(17, hand.getSumHand());
    }

    /**
     * Test getSumHand
     */
    @Test
    void Test2() {
        Hand hand = new Hand(new Card(9, 0), new Card(7, 1));
        hand.addCard(new Card(3, 2));
        assertEquals(15, hand.getSumHand());
    }

    /**
     * Test getSoftAces
     */
    @Test
    void Test3() {
        Hand hand = new Hand(new Card(9, 0), new Card(5, 1));
        hand.getSumHand();
        assertEquals(1, hand.getSoftAces());

        hand.addCard(new Card(7, 2));
        hand.getSumHand();
        assertEquals(0, hand.getSoftAces());
    }

    /**
     * Test addCard
     */
    @Test
    void Test4() {
        Hand hand = new Hand(new Card(8, 0), new Card(12, 1));
        assertEquals(20, hand.getSumHand());

        hand.addCard(new Card(0, 2));
        assertEquals(3, hand.getHand().size());
        assertEquals(22, hand.getSumHand());
    }

    /**
     * Test getHand
     */
    @Test
    void Test5() {
        Card c1 = new Card(2, 0);
        Card c2 = new Card(11, 3);
        Hand hand = new Hand(c1, c2);

        assertEquals(2, hand.getHand().size());
        assertSame(c1, hand.getHand().get(0));
        assertSame(c2, hand.getHand().get(1));

        Card c3 = new Card(9, 2);
        hand.addCard(c3);
        assertEquals(3, hand.getHand().size());
        assertSame(c3, hand.getHand().get(2));
    }
}

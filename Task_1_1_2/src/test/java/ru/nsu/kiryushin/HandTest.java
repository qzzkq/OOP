package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Hand} class.
 */
public class HandTest {

    /**
     * Verifies that the hand is initialized with the provided cards.
     */
    @Test
    void initHandCreatesHandWithCards() {
        Card c1 = new Card(0, 0);
        Card c2 = new Card(1, 1);
        Hand hand = new Hand(c1, c2);

        assertEquals(2, hand.getHand().size());
        assertEquals(c1, hand.getHand().get(0));
        assertEquals(c2, hand.getHand().get(1));
    }

    /**
     * Checks that the hand sum is computed correctly without aces.
     */
    @Test
    void getSumHandReturnsSumForNonAceCards() {
        Hand hand = new Hand(new Card(6, 0), new Card(7, 1));
        assertEquals(17, hand.getSumHand());
    }

    /**
     * Checks that the hand sum considers multiple cards.
     */
    @Test
    void getSumHandReturnsSumForThreeCards() {
        Hand hand = new Hand(new Card(9, 0), new Card(7, 1));
        hand.addCard(new Card(3, 2));
        assertEquals(15, hand.getSumHand());
    }

    /**
     * Ensures the count of soft aces is updated when the hand value changes.
     */
    @Test
    void getSoftAcesUpdatesWhenHandChanges() {
        Hand hand = new Hand(new Card(9, 0), new Card(5, 1));
        hand.getSumHand();
        assertEquals(1, hand.getSoftAces());

        hand.addCard(new Card(7, 2));
        hand.getSumHand();
        assertEquals(0, hand.getSoftAces());
    }

    /**
     * Validates that adding a card updates the hand value and size.
     */
    @Test
    void addCardIncreasesHandSizeAndSum() {
        Hand hand = new Hand(new Card(8, 0), new Card(12, 1));
        assertEquals(20, hand.getSumHand());

        hand.addCard(new Card(0, 2));
        assertEquals(3, hand.getHand().size());
        assertEquals(22, hand.getSumHand());
    }

    /**
     * Ensures that the original cards remain accessible through getters.
     */
    @Test
    void getHandReturnsCardsInOrder() {
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

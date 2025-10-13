package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Dealer} class.
 */
public class DealerTest {

    /**
     * Verifies that the dealer hand is initialized with the provided cards.
     */
    @Test
    void initDealerCreatesHandWithCards() {
        Card c1 = new Card(0, 0);
        Card c2 = new Card(1, 1);
        Dealer dealer = new Dealer(c1, c2);

        assertEquals(2, dealer.getHand().size());
        assertEquals(c1, dealer.getHand().get(0));
        assertEquals(c2, dealer.getHand().get(1));
    }

    /**
     * Checks that the dealer sum is computed correctly for two cards.
     */
    @Test
    void getSumHandReturnsSumForTwoCards() {
        Dealer dealer = new Dealer(new Card(6, 0), new Card(7, 1));
        assertEquals(17, dealer.getSumHand());
    }

    /**
     * Checks that the dealer sum accounts for an additional card.
     */
    @Test
    void getSumHandReturnsSumForThreeCards() {
        Dealer dealer = new Dealer(new Card(9, 0), new Card(7, 1));
        dealer.addCard(new Card(3, 2));
        assertEquals(15, dealer.getSumHand());
    }

    /**
     * Ensures that soft aces are recalculated after the hand changes.
     */
    @Test
    void getSoftAcesRecalculatesAfterUpdates() {
        Dealer dealer = new Dealer(new Card(9, 0), new Card(5, 1));
        dealer.getSumHand();
        assertEquals(1, dealer.getSoftAces());

        dealer.addCard(new Card(7, 2));
        dealer.getSumHand();
        assertEquals(0, dealer.getSoftAces());
    }

    /**
     * Validates that adding a card updates the dealer hand size and sum.
     */
    @Test
    void addCardUpdatesHandSumAndSize() {
        Dealer dealer = new Dealer(new Card(8, 0), new Card(12, 1));
        assertEquals(20, dealer.getSumHand());

        dealer.addCard(new Card(0, 2));
        assertEquals(3, dealer.getHand().size());
        assertEquals(22, dealer.getSumHand());
    }

    /**
     * Ensures that cards remain accessible through the dealer hand getter.
     */
    @Test
    void getHandReturnsCardsInOrder() {
        Card c1 = new Card(2, 0);
        Card c2 = new Card(11, 3);
        Dealer dealer = new Dealer(c1, c2);

        assertEquals(2, dealer.getHand().size());
        assertSame(c1, dealer.getHand().get(0));
        assertSame(c2, dealer.getHand().get(1));

        Card c3 = new Card(9, 2);
        dealer.addCard(c3);
        assertEquals(3, dealer.getHand().size());
        assertSame(c3, dealer.getHand().get(2));
    }

    /**
     * Verifies that the dealer hand hides the second card before revealing.
     */
    @Test
    void getHandStringHidesCardBeforeReveal() {
        Card c1 = new Card(4, 3);
        Card c2 = new Card(5, 2);
        Dealer dealer = new Dealer(c1, c2);
        String expected = "[6 Крести (6), <закрытая карта>]";
        assertEquals(expected, dealer.getHandString());
    }

    /**
     * Ensures that the dealer hand reveals all cards after changing state.
     */
    @Test
    void getHandStringRevealsCardsAfterStateChange() {
        Card c1 = new Card(4, 3);
        Card c2 = new Card(5, 2);
        Dealer dealer = new Dealer(c1, c2);
        dealer.playerFinishedTurn();
        String expected = "[6 Крести (6), 7 Бубны (7)] ==> 13";
        assertEquals(expected, dealer.getHandString());
    }

    /**
     * Checks that the dealer exposes the correct close card.
     */
    @Test
    void getClosedCardReturnsSecondCard() {
        Card c1 = new Card(4, 0);
        Card c2 = new Card(7, 1);
        Dealer dealer = new Dealer(c1, c2);
        assertSame(c2, dealer.getClosedCard());
    }


    /**
     * Ensures that the dealer output includes sums after revealing.
     */
    @Test
    void getStringHandDealerShowsSumAfterReveal() {
        Dealer dealer = new Dealer(new Card(9, 0), new Card(9, 1));
        dealer.addCard(new Card(7, 2));
        dealer.playerFinishedTurn();
        String expected = "[Туз Черви (11), Туз Пики (1), 9 Бубны (9)] ==> 21";
        assertEquals(expected, dealer.getHandString());
    }

    /**
     * Verifies that blackjack hands are displayed correctly after revealing.
     */
    @Test
    void getHandStringDisplaysBlackjack() {
        Dealer dealer = new Dealer(new Card(12, 1), new Card(9, 2));
        dealer.playerFinishedTurn();
        String expected = "[Король Пики (10), Туз Бубны (11)] ==> 21";
        assertEquals(expected, dealer.getHandString());
    }
}

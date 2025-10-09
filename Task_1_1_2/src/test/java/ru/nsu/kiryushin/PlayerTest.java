package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Player} class.
 */
public class PlayerTest {

    /**
     * Verifies that the player hand is initialized with the provided cards.
     */
    @Test
    void initPlayerCreatesHandWithCards() {
        Card c1 = new Card(0, 0);
        Card c2 = new Card(1, 1);
        Player player = new Player(c1, c2);

        assertEquals(2, player.getHand().size());
        assertEquals(c1, player.getHand().get(0));
        assertEquals(c2, player.getHand().get(1));
    }

    /**
     * Checks that the hand sum is computed correctly for two cards.
     */
    @Test
    void getSumHandReturnsSumForTwoCards() {
        Player player = new Player(new Card(6, 0), new Card(7, 1));
        assertEquals(17, player.getSumHand());
    }

    /**
     * Checks that the hand sum includes an additional card.
     */
    @Test
    void getSumHandReturnsSumForThreeCards() {
        Player player = new Player(new Card(9, 0), new Card(7, 1));
        player.addCard(new Card(3, 2));
        assertEquals(15, player.getSumHand());
    }

    /**
     * Ensures the number of soft aces is recalculated after hand updates.
     */
    @Test
    void getSoftAcesRecalculatesAfterUpdates() {
        Player player = new Player(new Card(9, 0), new Card(5, 1));
        player.getSumHand();
        assertEquals(1, player.getSoftAces());

        player.addCard(new Card(7, 2));
        player.getSumHand();
        assertEquals(0, player.getSoftAces());
    }

    /**
     * Validates that adding a card updates the hand sum and size.
     */
    @Test
    void addCardUpdatesHandSumAndSize() {
        Player player = new Player(new Card(8, 0), new Card(12, 1));
        assertEquals(20, player.getSumHand());

        player.addCard(new Card(0, 2));
        assertEquals(3, player.getHand().size());
        assertEquals(22, player.getSumHand());
    }

    /**
     * Ensures that cards are returned in the order they were dealt.
     */
    @Test
    void getHandReturnsCardsInOrder() {
        Card c1 = new Card(2, 0);
        Card c2 = new Card(11, 3);
        Player player = new Player(c1, c2);

        assertEquals(2, player.getHand().size());
        assertSame(c1, player.getHand().get(0));
        assertSame(c2, player.getHand().get(1));

        Card c3 = new Card(9, 2);
        player.addCard(c3);
        assertEquals(3, player.getHand().size());
        assertSame(c3, player.getHand().get(2));
    }

    /**
     * Confirms that the hand string is formatted correctly for simple hands.
     */
    @Test
    void getStringHandPlayerFormatsSimpleHands() {
        Player player = new Player(new Card(4, 3), new Card(5, 2));
        String expected = "[6 Крести (6), 7 Бубны (7)] ==> 13";
        assertEquals(expected, player.getStringHandPlayer());
    }

    /**
     * Ensures that the string output reflects blackjack hands.
     */
    @Test
    void getStringHandPlayerDisplaysBlackjack() {
        Player player = new Player(new Card(9, 0), new Card(12, 1));
        assertEquals(21, player.getSumHand());
        String expected = "[Туз Черви (11), Король Пики (10)] ==> 21";
        assertEquals(expected, player.getStringHandPlayer());
    }

    /**
     * Ensures that soft aces are represented correctly in the string output.
     */
    @Test
    void getStringHandPlayerHandlesSoftAces() {
        Player player = new Player(new Card(9, 0), new Card(9, 1));
        player.addCard(new Card(7, 2));
        assertEquals(21, player.getSumHand());
        String expected = "[Туз Черви (11), Туз Пики (1), 9 Бубны (9)] ==> 21";
        assertEquals(expected, player.getStringHandPlayer());
    }

    /**
     * Checks that multiple aces are adjusted to avoid busting.
     */
    @Test
    void getStringHandPlayerAdjustsMultipleAces() {
        Player player = new Player(new Card(9, 0), new Card(8, 3));
        player.addCard(new Card(9, 1));
        player.addCard(new Card(9, 2));
        assertEquals(13, player.getSumHand());
        String expected = "[Туз Черви (1), 10 Крести (10), Туз Пики (1), Туз Бубны (1)] ==> 13";
        assertEquals(expected, player.getStringHandPlayer());
    }

    /**
     * Verifies that high card hands are printed correctly.
     */
    @Test
    void getStringHandPlayerPrintsHighCards() {
        Player player = new Player(new Card(10, 0), new Card(11, 1));
        assertEquals(20, player.getSumHand());
        String expected = "[Валет Черви (10), Дама Пики (10)] ==> 20";
        assertEquals(expected, player.getStringHandPlayer());
    }

    /**
     * Ensures that bust hands are represented with the correct sum.
     */
    @Test
    void getStringHandPlayerShowsBustHands() {
        Player player = new Player(new Card(8, 0), new Card(12, 1));
        player.addCard(new Card(0, 2));
        assertEquals(22, player.getSumHand());
        String expected = "[10 Черви (10), Король Пики (10), 2 Бубны (2)] ==> 22";
        assertEquals(expected, player.getStringHandPlayer());
    }

}

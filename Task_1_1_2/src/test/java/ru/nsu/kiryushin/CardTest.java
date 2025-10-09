package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Card} class.
 */
public class CardTest {

    /**
     * Checks that the rank name is returned correctly.
     */
    @Test
    void getRankNameReturnsCorrectName() {
        String rank = "2";
        Card card = new Card(0, 0);
        assertEquals(rank, card.getRankName());
    }

    /**
     * Checks that the suit name is returned correctly.
     */
    @Test
    void getSuitNameReturnsCorrectName() {
        String suit = "Пики";
        Card card = new Card(5, 1);
        assertEquals(suit, card.getSuitName());
    }

    /**
     * Checks that the card value is returned correctly.
     */
    @Test
    void getValueReturnsCardValue() {
        int value = 4;
        Card card = new Card(2, 2);
        assertEquals(value, card.getValue());
    }

    /**
     * Checks that the human-readable card name is formatted correctly.
     */
    @Test
    void getCardNameReturnsFormattedName() {
        String expected = "Дама Пики (10)";
        Card card = new Card(11, 1);
        assertEquals(expected, card.getCardName());
    }
}

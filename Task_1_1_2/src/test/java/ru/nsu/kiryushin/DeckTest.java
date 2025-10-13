package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Deck} class.
 */
public class DeckTest {
    /**
     * Verifies that a deck can be created with a valid number of decks.
     */
    @Test
    void deckInitializesWithValidDeckCount() {
        Deck deck = Deck.create(1);
    }

    /**
     * Ensures an exception is thrown for invalid deck counts.
     */
    @Test
    void deckThrowsWhenDeckCountInvalid() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> Deck.create(0)
        );
        assertEquals("Количество колод должно быть от 1 до 8 включительно!", ex.getMessage());
    }

    /**
     * Checks that the deck size matches the number of decks provided.
     */
    @Test
    void deckSizeMatchesNumberOfDecks() {
        for (int i = 1; i < 9; ++i) {
            Deck deck = Deck.create(i);
            assertEquals(i * 52, deck.size());
        }
    }

    @Test
    void deckDecreasesSizeWhenCardsAreDrawn() {
        int decks = 3;
        int size = 52;
        Deck deck = Deck.create(decks);
        for (int i = 0; i < decks * size; ++i) {
            assertEquals(deck.size(), decks * size - i);
            deck.getCard();
        }
    }

    /**
     * Ensures drawing from an empty deck throws an exception.
     */
    @Test
    void deckThrowsWhenEmpty() {
        Deck deck = Deck.create(1);
        for (int i = 0; i < 52; ++i) {
            deck.getCard();
        }
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, deck::getCard);
        assertEquals("Колода пустая", ex.getMessage());
    }
}

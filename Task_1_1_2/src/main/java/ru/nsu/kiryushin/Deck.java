package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Deck of playing cards composed of one to eight standard 52-card packs.
 */
public class Deck {
    private final List<Card> cards;

    private Deck(int numDecks) {
        cards = new ArrayList<>(numDecks * 52);
    }

    /**
     * Constructs a deck.
     *
     * @param numDecks number of 52-card packs
     * @throws IllegalArgumentException if {@code numDecks} is outside 1..8
     */
    public static Deck create(int numDecks) {
        if (numDecks < 1 || numDecks > 8) {
            throw new IllegalArgumentException(
                    "Количество колод должно быть от 1 до 8 включительно!");
        }
        Deck deck = new Deck(numDecks);
        for (int i = 0; i < numDecks; i++) {
            for (int suit = 0; suit < 4; suit++) {
                for (int rank = 0; rank < 13; rank++) {
                    deck.cards.add(new Card(rank, suit));
                }
            }
        }
        Collections.shuffle(deck.cards);
        return deck;
    }

    /**
     * Gets one card from the top of the deck.
     *
     * @return card from the top of the deck
     */
    public Card getCard() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Колода пустая");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Returns the number of remaining cards in the deck.
     *
     * @return remaining cards count
     */
    public int size() {
        return cards.size();
    }
}

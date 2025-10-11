package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.List;

/**
 * Blackjack hand stores cards and computes the hand total.
 */
public class Hand {
    private final List<Card> hand;
    private int softAces;

    /**
     * Creates a hand with two initial cards.
     *
     * @param card1 first card
     * @param card2 second card
     */
    public Hand(Card card1, Card card2) {
        this.hand = new ArrayList<>();
        this.hand.add(card1);
        this.hand.add(card2);
    }

    /**
     * Computes the Blackjack total.
     *
     * @return the final hand total
     */
    public int getSumHand() {
        int sum = 0;
        int aces = 0;
        for (Card c : this.hand) {
            sum += c.getValue();
            if ("Туз".equals(c.getRankName())) {
                aces++;
            }
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        this.softAces = aces;
        return sum;
    }

    /**
     * Returns the number of soft Aces.
     *
     * @return soft Ace count
     */
    public int getSoftAces() {
        return this.softAces;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        this.hand.add(card);
    }

    /**
     * Returns the internal list of cards.
     *
     * @return list of cards
     */
    public List<Card> getHand() {
        return hand;
    }
}

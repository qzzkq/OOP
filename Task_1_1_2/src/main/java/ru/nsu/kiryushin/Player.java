package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's hand in Blackjack.
 */
public class Player extends Hand {

    /**
     * Creates a player's hand with two initial cards.
     *
     * @param card1 first card
     * @param card2 second card
     */
    public Player(Card card1, Card card2) {
        super(card1, card2);
    }

    /**
     * Formats player's hand for display.
     *
     * @return string like "[...] ==> sum"
     */
    public String getStringHandPlayer() {
        List<String> handPlayer = new ArrayList<>();
        int sum = this.getSumHand();
        int softAces = this.getSoftAces();
        for (Card card : this.getHand()) {
            handPlayer.add(cardString(card, softAces > 0));
            if ("Туз".equals(card.getRankName()) && softAces > 0) {
                softAces--;
            }
        }
        return handPlayer.toString() + " ==> " + sum;
    }

    /**
     * Formats a single card for player output, taking into account soft Aces.
     *
     * @param card card to format
     * @param softAce {@code true} if the Ace should count as 11
     * @return formatted representation of the card
     */
    private String cardString(Card card, boolean softAce) {
        if ("Туз".equals(card.getRankName())) {
            String aceValue = softAce ? "11" : "1";
            return card.getRankName() + " " + card.getSuitName() + " (" + aceValue + ")";
        }
        return card.getRankName() + " " + card.getSuitName() + " (" + card.getValue() + ")";
    }
}

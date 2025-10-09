package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.List;

/**
 * Dealer hand for Blackjack.
 */
public class Dealer extends Hand {

    private boolean playerFinished = false;

    /**
     * Marks that the player's turn is over.
     */
    public void changeState() {
        this.playerFinished = true;
    }

    /**
     * Creates a dealer's hand with two initial cards.
     *
     * @param card1 first card
     * @param card2 second card
     */
    public Dealer(Card card1, Card card2) {
        super(card1, card2);
    }

    /**
     * Returns the dealer's hidden card.
     *
     * @return hidden card
     */
    public Card getCloseCard() {
        return this.getHand().get(1);
    }

    /**
     * Formats dealer's hand for display.
     *
     * @return string representation of dealer's hand
     */
    public String getStringHandDealer() {
        List<String> handDealer = new ArrayList<>();
        if (!playerFinished) {
            Card card = this.getHand().get(0);
            handDealer.add(openCardString(card, false));
            handDealer.add("<закрытая карта>");
            return handDealer.toString();
        }

        int sum = this.getSumHand();
        int softAces = this.getSoftAces();
        for (Card card : this.getHand()) {
            handDealer.add(openCardString(card, softAces > 0));
            if ("Туз".equals(card.getRankName()) && softAces > 0) {
                softAces--;
            }
        }
        return handDealer.toString() + " ==> " + sum;
    }

    /**
     * Formats a single card for dealer output, taking into account soft Aces.
     *
     * @param card card to format
     * @param softAce {@code true} if the Ace should count as 11
     * @return formatted representation of the card
     */
    private String openCardString(Card card, boolean softAce) {
        if ("Туз".equals(card.getRankName())) {
            String aceValue = softAce ? "11" : "1";
            return card.getRankName() + " " + card.getSuitName() + " (" + aceValue + ")";
        }
        return card.getRankName() + " " + card.getSuitName() + " (" + card.getValue() + ")";
    }
}

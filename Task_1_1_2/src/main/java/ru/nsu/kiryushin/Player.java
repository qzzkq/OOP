package ru.nsu.kiryushin;

import java.util.ArrayList;

/**
 * Represents the player's hand in Blackjack.
 */
public class Player extends Hand{

    /**
     * Returns a string a player's hand with two initial cards.
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
    public String getStringHandPlayer(){
        ArrayList<String> handPlayer = new ArrayList<String>();
        String valueAce, stringCard, valueCard;
        int sum = this.getSumHand();
        int softAces = this.getSoftAces();
        for (Card card : this.getHand()){
            if (card.getRankName().equals("Туз")){
                if (softAces > 0){
                    valueAce = "11";
                    --softAces;
                }
                else{
                    valueAce = "1";
                }
                stringCard = card.getRankName() + " " + card.getSuitName() + " " + "(" + valueAce + ")";
                handPlayer.add(stringCard);
            }
            else{
                valueCard = String.valueOf(card.getValue());
                stringCard = card.getRankName() + " " + card.getSuitName() + " " + "(" + valueCard + ")";
                handPlayer.add(stringCard);
            }
        }
        return handPlayer.toString() + " ==> " + String.valueOf(sum);
    }

}

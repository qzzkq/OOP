package ru.nsu.kiryushin;

import java.util.ArrayList;

/**
 * Dealer hand for Blackjack.
 */
public class Dealer extends Hand{

    private boolean playerFinished = false;

    /**
     * Marks that the player's turn is over
     */
    public void changeState(){
        this.playerFinished = true;
    }

    /**
     * Creates a dealer's hand with two initial cards
     *
     * @param card1 first card
     * @param card2 second card
     */
    public Dealer(Card c1, Card c2) {
        super(c1, c2);
    }

    /**
     * Returns the dealer's hidden card
     *
     * @return hidden card
     */
    public Card getCloseCard(){
        return this.getHand().get(1);
    }

    /**
     * Formats dealer's hand for display
     *
     * @return string like "[...] ==> sum" or "[first, <закрытая карта>]"
     */
    public String getStringHandDealer(){
        ArrayList<String> handDealer = new ArrayList<String>();
        String valueAce, stringCard, valueCard;
        if (playerFinished == false){
            Card card = this.getHand().get(0);
            if (card.getRankName().equals("Туз")){
                stringCard = card.getRankName() + " " + card.getSuitName();
            }
            else{
                valueCard = String.valueOf(card.getValue());
                stringCard = card.getRankName() + " " + card.getSuitName() + " " + "(" + valueCard + ")";
            }
            handDealer.add(stringCard);
            handDealer.add("<закрытая карта>");
            return handDealer.toString();
        }
        else{
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
                    handDealer.add(stringCard);
                }
                else{
                    valueCard = String.valueOf(card.getValue());
                    stringCard = card.getRankName() + " " + card.getSuitName() + " " + "(" + valueCard + ")";
                    handDealer.add(stringCard);
                }
            }
            return handDealer.toString() + " ==> " + String.valueOf(sum);
        }
    }
}

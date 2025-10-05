package ru.nsu.kiryushin;

/**
 * Single playing card (rank 0..12, suit 0..3).
 */
public class Card {
    private final int rank;
    private final int suit;

    private static final String[] SUITS = {"Черви", "Пики", "Бубны", "Крести"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Туз", "Валет", "Дама", "Король"};

    /**
     * @param rank rank index (0..12)
     * @param suit suit index (0..3)
     */
    Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * @return rank name
     */
    public String getRankName() {
        return RANKS[this.rank];
    }

    /**
     * @return suit name
     */
    public String getSuitName() {
        return SUITS[this.suit];
    }

    /**
     * Blackjack value: 2–10 by face, J/Q/K=10, Ace=11
     *
     * @return numeric value (2..11)
     */
    public int getValue() {
        if (rank == 9) return 11;
        if (rank >= 10) return 10;
        return rank + 2;
    }

    /**
     * @return human-readable name
     */
    public String getCardName(){
        return RANKS[this.rank] + " " + SUITS[this.suit] + " " + "(" + this.getValue() + ")";
    }
}

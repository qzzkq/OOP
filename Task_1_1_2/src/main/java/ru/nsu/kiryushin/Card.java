package ru.nsu.kiryushin;

/**
 * Single playing card with rank and suit indices.
 */
public class Card {
    private static final String[] SUITS = {"Черви", "Пики", "Бубны", "Крести"};
    private static final String[] RANKS = {
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "Туз",
        "Валет",
        "Дама",
        "Король"
    };

    private final int rank;
    private final int suit;

    /**
     * Creates a card.
     *
     * @param rank rank index (0..12)
     * @param suit suit index (0..3)
     */
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Returns the rank name.
     *
     * @return rank name
     */
    public String getRankName() {
        return RANKS[this.rank];
    }

    /**
     * Returns the suit name.
     *
     * @return suit name
     */
    public String getSuitName() {
        return SUITS[this.suit];
    }

    /**
     * Blackjack value: 2–10 by face, face cards equal 10, Ace equals 11.
     *
     * @return numeric value (2..11)
     */
    public int getValue() {
        if (rank == 9) {
            return 11;
        }
        if (rank >= 10) {
            return 10;
        }
        return rank + 2;
    }

    /**
     * Returns a human-readable name of the card.
     *
     * @return card name
     */
    public String getCardName() {
        return RANKS[this.rank] + " " + SUITS[this.suit] + " (" + getValue() + ")";
    }
}

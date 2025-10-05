package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Test Card
 */
public class CardTest{

    /**
     * Check getRankName
     */
    @Test
    void Test0(){
        String rank = "2";
        Card card = new Card(0, 0);
        assertEquals(card.getRankName(), rank);
    }

    /**
     * Check getSuitName
     */
    @Test
    void Test1(){
        String suit = "Пики";
        Card card = new Card(5,1);
        assertEquals(card.getSuitName(),suit);
    }

    /**
     * Check card value
     */
    @Test
    void Test2(){
        int value = 4;
        Card card = new Card(2, 2);
        assertEquals(value, card.getValue());
    }

    /**
     * Check human-readable name of card
     */
    @Test
    void Test3(){
        String expected = "Дама Пики (10)";
        Card card = new Card(11,1);
        assertEquals(card.getCardName(), expected);
    }
}

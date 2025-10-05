package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

/**
 * Test Deck
 */
public class DeckTest{
    /**
     * Test init Deck
     */
    @Test
    void Test0(){
        Deck deck = new Deck(1);
    }

    /**
     * Test wrong init Deck
     */
    @Test
    void Test1(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Deck(0));
        assertEquals("Количество колод должно быть от 1 до 8 включительно!", ex.getMessage());
    }

    /**
     * Tests for getting size
     */
    @Test
    void Test2(){
        for (int i=1; i < 9; ++i){
            Deck deck = new Deck(i);
            assertEquals(i*52, deck.size());
        }
    }

    @Test
    void Test3(){
        int decks = 3;
        int size = 52;
        Deck deck = new Deck(decks);
        for (int i = 0; i < decks*size; ++i){
            assertEquals(deck.size(), decks*size-i);
            deck.getCard();
        }
    }

    /**
     * Test for getting card from empty deck
     */
    @Test
    void Test4(){
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; ++i){
            deck.getCard();
        }
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, deck::getCard);
        assertEquals("Колода пустая", ex.getMessage());
    }
}

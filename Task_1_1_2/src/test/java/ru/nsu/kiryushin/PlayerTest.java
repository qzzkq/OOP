package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test Player
 */
public class PlayerTest {

    /**
     * Test init Player
     */
    @Test
    void Test0() {
        Card c1 = new Card(0, 0);
        Card c2 = new Card(1, 1);
        Player player = new Player(c1, c2);

        assertEquals(2, player.getHand().size());
        assertEquals(c1, player.getHand().get(0));
        assertEquals(c2, player.getHand().get(1));
    }

    /**
     * Test getSumHand
     */
    @Test
    void Test1() {
        Player player = new Player(new Card(6, 0), new Card(7, 1));
        assertEquals(17, player.getSumHand());
    }

    /**
     * Test getSumHand
     */
    @Test
    void Test2() {
        Player player = new Player(new Card(9, 0), new Card(7, 1));
        player.addCard(new Card(3, 2));
        assertEquals(15, player.getSumHand());
    }

    /**
     * Test getSoftAces
     */
    @Test
    void Test3() {
        Player player = new Player(new Card(9, 0), new Card(5, 1));
        player.getSumHand();
        assertEquals(1, player.getSoftAces());

        player.addCard(new Card(7, 2));
        player.getSumHand();
        assertEquals(0, player.getSoftAces());
    }

    /**
     * Test addCard
     */
    @Test
    void Test4() {
        Player player = new Player(new Card(8, 0), new Card(12, 1));
        assertEquals(20, player.getSumHand());

        player.addCard(new Card(0, 2));
        assertEquals(3, player.getHand().size());
        assertEquals(22, player.getSumHand());
    }

    /**
     * Test getHand
     */
    @Test
    void Test5() {
        Card c1 = new Card(2, 0);
        Card c2 = new Card(11, 3);
        Player player = new Player(c1, c2);

        assertEquals(2, player.getHand().size());
        assertSame(c1, player.getHand().get(0));
        assertSame(c2, player.getHand().get(1));

        Card c3 = new Card(9, 2);
        player.addCard(c3);
        assertEquals(3, player.getHand().size());
        assertSame(c3, player.getHand().get(2));
    }

    /**
     * Test getStringHandPlayer
     */
    @Test
    void Test6() {
        Player player = new Player(new Card(4, 3), new Card(5, 2));
        String expected = "[6 Крести (6), 7 Бубны (7)] ==> 13";
        assertEquals(expected, player.getStringHandPlayer());
    }
}

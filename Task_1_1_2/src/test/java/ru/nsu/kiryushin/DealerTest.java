package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test Dealer
 */
public class DealerTest {

    /**
     * Test init Dealer
     */
    @Test
    void Test0() {
        Card c1 = new Card(0, 0);
        Card c2 = new Card(1, 1);
        Dealer dealer = new Dealer(c1, c2);

        assertEquals(2, dealer.getHand().size());
        assertEquals(c1, dealer.getHand().get(0));
        assertEquals(c2, dealer.getHand().get(1));
    }

    /**
     * Test getSumHand
     */
    @Test
    void Test1() {
        Dealer dealer = new Dealer(new Card(6, 0), new Card(7, 1));
        assertEquals(17, dealer.getSumHand());
    }

    /**
     * Test getSumHand with ace downgrade
     */
    @Test
    void Test2() {
        Dealer dealer = new Dealer(new Card(9, 0), new Card(7, 1));
        dealer.addCard(new Card(3, 2));
        assertEquals(15, dealer.getSumHand());
    }

    /**
     * Test getSoftAces
     */
    @Test
    void Test3() {
        Dealer dealer = new Dealer(new Card(9, 0), new Card(5, 1));
        dealer.getSumHand();
        assertEquals(1, dealer.getSoftAces());

        dealer.addCard(new Card(7, 2));
        dealer.getSumHand();
        assertEquals(0, dealer.getSoftAces());
    }

    /**
     * Test addCard
     */
    @Test
    void Test4() {
        Dealer dealer = new Dealer(new Card(8, 0), new Card(12, 1));
        assertEquals(20, dealer.getSumHand());

        dealer.addCard(new Card(0, 2));
        assertEquals(3, dealer.getHand().size());
        assertEquals(22, dealer.getSumHand());
    }

    /**
     * Test getHand
     */
    @Test
    void Test5() {
        Card c1 = new Card(2, 0);
        Card c2 = new Card(11, 3);
        Dealer dealer = new Dealer(c1, c2);

        assertEquals(2, dealer.getHand().size());
        assertSame(c1, dealer.getHand().get(0));
        assertSame(c2, dealer.getHand().get(1));

        Card c3 = new Card(9, 2);
        dealer.addCard(c3);
        assertEquals(3, dealer.getHand().size());
        assertSame(c3, dealer.getHand().get(2));
    }

    /**
     * Test showing hand before changeState
     *
     */
    @Test
    void Test6() {
        Card c1 = new Card(4, 3);
        Card c2 = new Card(5, 2);
        Dealer dealer = new Dealer(c1, c2);
        String expected = "[6 Крести (6), <закрытая карта>]";
        assertEquals(expected, dealer.getStringHandDealer());
    }

    /**
     *  Test showing hand after changeState
     */
    @Test
    void Test7() {
        Card c1 = new Card(4, 3);
        Card c2 = new Card(5, 2);
        Dealer dealer = new Dealer(c1, c2);
        dealer.changeState();
        String expected = "[6 Крести (6), 7 Бубны (7)] ==> 13";
        assertEquals(expected, dealer.getStringHandDealer());
    }

    @Test
    void Test8() {
        Card c1 = new Card(4, 0);
        Card c2 = new Card(7, 1);
        Dealer dealer = new Dealer(c1, c2);
        assertSame(c2, dealer.getCloseCard());
    }

    @Test
    void Test9() {
        Card c1 = new Card(9, 1);
        Card c2 = new Card(4, 2);
        Dealer dealer = new Dealer(c1, c2);
        String expected = "[Туз Пики, <закрытая карта>]";
        assertEquals(expected, dealer.getStringHandDealer());
    }

    @Test
    void Test10() {
        Dealer dealer = new Dealer(new Card(9, 0), new Card(9, 1));
        dealer.addCard(new Card(7, 2));
        dealer.changeState();
        String expected = "[Туз Черви (11), Туз Пики (1), 9 Бубны (9)] ==> 21";
        assertEquals(expected, dealer.getStringHandDealer());
    }

    @Test
    void Test11() {
        Dealer dealer = new Dealer(new Card(12, 1), new Card(9, 2));
        dealer.changeState();
        String expected = "[Король Пики (10), Туз Бубны (11)] ==> 21";
        assertEquals(expected, dealer.getStringHandDealer());
    }
}

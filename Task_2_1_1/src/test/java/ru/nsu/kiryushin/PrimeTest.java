package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PrimeTest{
    @Test
    public void testAllPrimes() throws InterruptedException {
        int[] numbers = {2, 3, 5, 7, 11, 13, 17};
        assertFalse(PrimeCheckerThread.containsAnyNonPrime(numbers, 2));
    }

    @Test
    public void testWithComposite() throws InterruptedException {
        int[] numbers = {2, 3, 4, 5, 7};
        assertTrue(PrimeCheckerThread.containsAnyNonPrime(numbers, 3));
    }

    @Test
    public void testWithOne() throws InterruptedException {
        int[] numbers = {1, 2, 3, 5};
        assertTrue(PrimeCheckerThread.containsAnyNonPrime(numbers, 5));
    }

    @Test
    public void testEmptyArray() throws InterruptedException {
        int[] numbers = {};
        assertFalse(PrimeCheckerThread.containsAnyNonPrime(numbers, 1));
    }

    @Test
    public void testLargePrimes() throws InterruptedException {
        int[] numbers = {100003, 100019, 100043};
        assertFalse(PrimeCheckerThread.containsAnyNonPrime(numbers, 10));
    }
}

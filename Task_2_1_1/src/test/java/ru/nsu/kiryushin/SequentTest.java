package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SequentTest {
    @Test
    public void testAllPrimes() {
        int[] numbers = {2, 3, 5, 7, 11, 13, 17};
        assertFalse(PrimeCheckerSequent.containsAnyNonPrime(numbers));
    }

    @Test
    public void testWithComposite() {
        int[] numbers = {2, 3, 4, 5, 7};
        assertTrue(PrimeCheckerSequent.containsAnyNonPrime(numbers));
    }

    @Test
    public void testWithOne() {
        int[] numbers = {1, 2, 3, 5};
        assertTrue(PrimeCheckerSequent.containsAnyNonPrime(numbers));
    }

    @Test
    public void testEmptyArray() {
        int[] numbers = {};
        assertFalse(PrimeCheckerSequent.containsAnyNonPrime(numbers));
    }

    @Test
    public void testLargePrimes() {
        int[] numbers = {100003, 100019, 100043};
        assertFalse(PrimeCheckerSequent.containsAnyNonPrime(numbers));
    }
}

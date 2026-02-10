package ru.nsu.kiryushin;

import java.util.Arrays;

/**
 * Class for checking an array of numbers for non-prime numbers sequentially.
 */
public class PrimeCheckerSequent {

    /**
     * Checks if the number is prime
     *
     * @param number Number
     * @return True if the number is prime, false otherwise
     */
    private static boolean isPrime(int number) {
        if (number == 1) return false;
        if (number == 2) return true;
        if ((number % 2) == 0) return false;
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks an array of numbers for non-prime numbers sequentially.
     *
     * @param numbers Array of numbers to check.
     * @return Result of check.
     */
    public static boolean containsAnyNonPrime(int[] numbers) {
        return !(Arrays.stream(numbers).allMatch(PrimeCheckerSequent::isPrime));
    }
}

package ru.nsu.kiryushin;

/**
 * Class for checking if an array of integers contains any non-prime number using parallel threads.
 */
public class PrimeCheckerThread {
    private static volatile boolean isFound = false;

    /**
     * Checks if the number is prime
     *
     * @param number Number
     * @return True if the number is prime, false otherwise
     */
    private static boolean isPrime(int number) {
        if (number == 1) {
            return false;
        }
        if (number == 2){
            return true;
        }
        if ((number % 2) == 0){
            return false;
        }
        for (int i = 3; i * i <= number; i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Worker class that represents a single thread for prime checking.
     */
    public static class Worker extends Thread {
        private final int start;
        private final int end;
        private final int[] numbers;
        public Worker(int start, int end, int[] numbers) {
            this.start = start;
            this.end = end;
            this.numbers = numbers;
        }

        @Override
        public void run() {
            for (int i = start; i < end; i++) {
                if (isFound) break;
                if (!isPrime(numbers[i])) {
                    isFound = true;
                    break;
                }
            }
        }
    }

    /**
     * Checks an array of numbers for non-prime numbers using multithreading.
     *
     * @param numbers Array of numbers to check.
     * @param numThreads Number of threads.
     * @return Result of check (true if a non-prime number is found).
     * @throws InterruptedException If the thread execution was interrupted.
     */
    public static boolean containsAnyNonPrime(int[] numbers, int numThreads) throws InterruptedException {
        isFound = false;
        int len = numbers.length;
        if (numThreads > len) {
            numThreads = len;
        }

        if (numThreads == 0 || len == 0) {
            return false;
        }
        int chunkSize = len / numThreads;
        Worker[] threads = new Worker[numThreads];

        for (int i = 0; i < numThreads; i++){
            int start = chunkSize * i;
            int end = (i == numThreads - 1) ? len : (start + chunkSize);
            threads[i] = new Worker(start, end, numbers);
            threads[i].start();
        }

        for (Worker t : threads) {
            t.join();
        }

        return isFound;
    }
}

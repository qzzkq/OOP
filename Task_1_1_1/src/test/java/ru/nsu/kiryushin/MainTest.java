package ru.nsu.kiryushin;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MainTest {
    @Test
    void testSimpleUnsortedArray() {
        int[] arr = {2, 3, 1, 4, 53, 3};
        int[] expected = {1, 2, 3, 3, 4, 53};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testEmptyArray() {
        int[] arr = {};
        int[] expected = {};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testSingleElementArray() {
        int[] arr = {42};
        int[] expected = {42};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testArrayWithNegatives() {
        int[] arr = {-3, 2, -3, 9, 10};
        int[] expected = {-3, -3, 2, 9, 10};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testAlreadySortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testArrayWithDuplicates() {
        int[] arr = {5, 1, 3, 5, 2, 1};
        int[] expected = {1, 1, 2, 3, 5, 5};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testArrayWithExtremeValues() {
        int[] arr = {Integer.MAX_VALUE, -100, 0, Integer.MIN_VALUE, 500};
        int[] expected = {Integer.MIN_VALUE, -100, 0, 500, Integer.MAX_VALUE};
        assertArrayEquals(expected, Main.heapSort(arr));
    }

    @Test
    void testLargeRandomArray() {
        Random random = new Random();
        int[] arr = new int[100_000];
        int[] expected = new int[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            int val = random.nextInt();
            arr[i] = val;
            expected[i] = val;
        }
        Arrays.sort(expected);

        assertArrayEquals(expected, Main.heapSort(arr));
    }
}

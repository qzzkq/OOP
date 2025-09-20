package ru.nsu.kiryushin;

/** Class for heap sort. */
public final class SortUtil {
    /**
     * heapSort.
     *
     * @param arr array to sort
     *
     * @return sorted array
     */
    public static int[] heapSort(int[] arr) {
        int n = arr.length;
        // build max-heap
        for (int i = n / 2 - 1; i >= 0; --i) {
            heapify(arr, i, n);
        }
        // extract max to the end
        for (int i = n - 1; i > 0; --i) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, 0, i);
        }
        return arr;
    }

    /**
     * rearranges a heap to maintain the heap property.
     *
     * @param arr input array
     *
     * @param i index
     *
     * @param n heap size
     */
    private static void heapify(int[] arr, int i, int n) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }
        if (largest != i) {
            int temp = arr[largest];
            arr[largest] = arr[i];
            arr[i] = temp;
            heapify(arr, largest, n);
        }
    }

    public static void main(String[] args) {}
}

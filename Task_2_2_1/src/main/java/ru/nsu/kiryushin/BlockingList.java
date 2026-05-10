package ru.nsu.kiryushin;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread-safe bounded list with blocking put and take operations.
 *
 * @param <T> stored element type
 */
public class BlockingList<T> {
    private final List<T> list = new LinkedList<>();
    private final int capacity;
    private boolean closed;

    /**
     * Creates a blocking list with a fixed capacity.
     *
     * @param capacity maximum number of elements stored at once
     */
    public BlockingList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    /**
     * Adds an element, waiting until space becomes available if needed.
     *
     * @param item element to add
     * @return {@code true} if the element was added, {@code false} if the list was closed
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public synchronized boolean put(T item) throws InterruptedException {
        return put(item, null);
    }

    /**
     * Adds an element, optionally invoking a callback once before the thread blocks.
     *
     * @param item element to add
     * @param onWait callback invoked once before waiting for free space
     * @return {@code true} if the element was added, {@code false} if the list was closed
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public synchronized boolean put(T item, Runnable onWait) throws InterruptedException {
        boolean notifiedAboutWait = false;
        while (list.size() >= capacity && !closed) {
            if (!notifiedAboutWait && onWait != null) {
                onWait.run();
                notifiedAboutWait = true;
            }
            wait();
        }

        if (closed) {
            return false;
        }

        list.add(item);
        notifyAll();
        return true;
    }

    /**
     * Removes and returns the first available element.
     *
     * @return the next element, or {@code null} if the list is closed and empty
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public synchronized T take() throws InterruptedException {
        while (list.isEmpty() && !closed) {
            wait();
        }

        if (list.isEmpty()) {
            return null;
        }

        T item = list.remove(0);
        notifyAll();
        return item;
    }

    /**
     * Removes up to {@code max} elements from the head of the list.
     *
     * @param max maximum number of elements to return
     * @return a list containing removed elements, or an empty list if the list is closed and empty
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public synchronized List<T> takeMultiple(int max) throws InterruptedException {
        if (max <= 0) {
            throw new IllegalArgumentException("max must be positive");
        }

        while (list.isEmpty() && !closed) {
            wait();
        }

        if (list.isEmpty()) {
            return List.of();
        }

        List<T> taken = new ArrayList<>();
        while (!list.isEmpty() && taken.size() < max) {
            taken.add(list.remove(0));
        }

        notifyAll();
        return taken;
    }

    /**
     * Closes the list and wakes all waiting threads.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }
}

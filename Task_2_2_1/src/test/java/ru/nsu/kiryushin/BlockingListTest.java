package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

public class BlockingListTest {
    @Test
    void testWrongCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new BlockingList<>(0));
    }

    @Test
    void testPutAndTake() throws InterruptedException {
        BlockingList<Integer> list = new BlockingList<>(3);
        list.put(1);
        list.put(2);

        assertEquals(1, list.take());
        assertEquals(2, list.take());
    }

    @Test
    void testTakeMultiple() throws InterruptedException {
        BlockingList<Integer> list = new BlockingList<>(4);
        list.put(1);
        list.put(2);
        list.put(3);

        assertIterableEquals(List.of(1, 2), list.takeMultiple(2));
        assertEquals(3, list.take());
    }

    @Test
    void testWrongMax() {
        BlockingList<Integer> list = new BlockingList<>(1);
        assertThrows(IllegalArgumentException.class, () -> list.takeMultiple(0));
    }

    @Test
    void testClose() throws InterruptedException {
        BlockingList<Integer> list = new BlockingList<>(2);
        list.close();

        assertFalse(list.put(1));
        assertNull(list.take());
        assertTrue(list.takeMultiple(1).isEmpty());
    }

    @Test
    void testPutWaits() throws InterruptedException {
        BlockingList<Integer> list = new BlockingList<>(1);
        AtomicInteger calls = new AtomicInteger();
        list.put(1);

        Thread thread = new Thread(() -> {
            try {
                list.put(2, calls::incrementAndGet);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(100);

        assertEquals(1, calls.get());
        assertTrue(thread.isAlive());

        assertEquals(1, list.take());
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertEquals(2, list.take());
    }

    @Test
    void testTakeAfterClose() throws InterruptedException {
        BlockingList<Integer> list = new BlockingList<>(1);
        AtomicInteger value = new AtomicInteger(-1);

        Thread thread = new Thread(() -> {
            try {
                Integer result = list.take();
                if (result == null) {
                    value.set(0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(100);
        list.close();
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertEquals(0, value.get());
    }
}

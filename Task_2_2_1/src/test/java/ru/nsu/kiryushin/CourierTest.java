package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CourierTest {
    @Test
    void testRunDeliversAllOrdersAndStopsAfterStorageCloses() throws InterruptedException {
        OrderDepartment orderDepartment = new OrderDepartment(1, 2);

        orderDepartment.placeCookedOrderInStorage(new Order(1, 0, 0));
        orderDepartment.placeCookedOrderInStorage(new Order(2, 0, 0));
        orderDepartment.closeStorage();

        Thread thread = new Thread(new Courier(2, orderDepartment));
        thread.start();
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertTrue(orderDepartment.takeOrdersForDelivery(1).isEmpty());
    }

    @Test
    void testRunStopsImmediatelyWhenStorageClosedAndEmpty() throws InterruptedException {
        OrderDepartment orderDepartment = new OrderDepartment(1, 1);
        orderDepartment.closeStorage();

        Thread thread = new Thread(new Courier(1, orderDepartment));
        thread.start();
        thread.join(1000);

        assertFalse(thread.isAlive());
    }

    @Test
    void testRunRestoresInterruptFlagWhenInterruptedDuringDelivery() throws InterruptedException {
        OrderDepartment orderDepartment = new OrderDepartment(1, 1);
        orderDepartment.placeCookedOrderInStorage(new Order(3, 0, 1000));
        orderDepartment.closeStorage();

        Thread thread = new Thread(new Courier(1, orderDepartment));
        thread.start();

        Thread.sleep(100);
        thread.interrupt();
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertTrue(thread.isInterrupted());
    }
}

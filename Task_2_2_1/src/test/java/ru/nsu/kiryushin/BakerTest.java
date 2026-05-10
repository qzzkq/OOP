package ru.nsu.kiryushin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BakerTest {
    @Test
    void testRunMovesOrdersToStorageAndStopsWhenQueueCloses() throws InterruptedException {
        OrderDepartment orderDepartment = new OrderDepartment(1, 1);
        Order order = new Order(1, 0, 0);

        orderDepartment.submitOrder(order);
        orderDepartment.closeOrders();

        Thread thread = new Thread(new Baker(0, orderDepartment));
        thread.start();
        thread.join(1000);

        assertFalse(thread.isAlive());

        orderDepartment.closeStorage();
        Order storedOrder = orderDepartment.takeOrdersForDelivery(1).stream().findFirst().orElse(null);
        assertNotNull(storedOrder);
        assertEquals(1, storedOrder.getId());
        assertTrue(orderDepartment.takeOrdersForDelivery(1).isEmpty());
    }

    @Test
    void testRunStopsWhenStorageIsAlreadyClosed() throws InterruptedException {
        OrderDepartment orderDepartment = new OrderDepartment(1, 1);

        orderDepartment.closeStorage();
        orderDepartment.submitOrder(new Order(2, 0, 0));
        orderDepartment.closeOrders();

        Thread thread = new Thread(new Baker(0, orderDepartment));
        thread.start();
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertTrue(orderDepartment.takeOrdersForDelivery(1).isEmpty());
    }

    @Test
    void testRunRestoresInterruptFlagWhenInterruptedDuringCooking() throws InterruptedException {
        OrderDepartment orderDepartment = new OrderDepartment(1, 1);

        orderDepartment.submitOrder(new Order(3, 0, 0));

        Thread thread = new Thread(new Baker(1000, orderDepartment));
        thread.start();

        Thread.sleep(100);
        thread.interrupt();
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertTrue(thread.isInterrupted());

        orderDepartment.closeOrders();
        orderDepartment.closeStorage();
    }
}

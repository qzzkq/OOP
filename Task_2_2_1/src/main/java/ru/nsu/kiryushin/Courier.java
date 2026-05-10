package ru.nsu.kiryushin;
import java.util.List;

/**
 * Worker that takes ready orders from storage and delivers them in batches.
 */
public class Courier implements Runnable {
    private final int bagCapacity;
    private final OrderDepartment orderDepartment;

    /**
     * Creates a courier worker.
     *
     * @param bagCapacity maximum number of orders delivered in one trip
     * @param orderDepartment department that provides ready orders
     */
    public Courier(int bagCapacity, OrderDepartment orderDepartment) {
        this.bagCapacity = bagCapacity;
        this.orderDepartment = orderDepartment;
    }

    /**
     * Creates a courier worker from raw storage.
     *
     * @param bagCapacity maximum number of orders delivered in one trip
     * @param storage finished-order storage
     */
    public Courier(int bagCapacity, BlockingList<Order> storage) {
        this(bagCapacity, new OrderDepartment(new BlockingList<>(1), storage));
    }

    /**
     * Continuously loads orders, delivers them, and stops when storage is closed and empty.
     */
    @Override
    public void run() {
        try {
            while (true) {
                List<Order> orders = orderDepartment.takeOrdersForDelivery(bagCapacity);
                if (orders.isEmpty()) {
                    return;
                }

                Thread.sleep(totalDeliveryTime(orders));

                orderDepartment.completeDelivery(orders);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Calculates the total delivery time for a courier trip.
     *
     * @param orders orders loaded into the courier bag
     * @return total delivery time in milliseconds
     */
    private long totalDeliveryTime(List<Order> orders) {
        long total = 0;
        for (Order order : orders) {
            total += order.getDeliveryTimeMs();
        }
        return total;
    }
}

package ru.nsu.kiryushin;
import java.util.List;

/**
 * Worker that takes ready orders from storage and delivers them in batches.
 */
public class Courier implements Runnable {
    private final int bagCapacity;
    private final BlockingList<Order> storage;

    /**
     * Creates a courier worker.
     *
     * @param bagCapacity maximum number of orders delivered in one trip
     * @param storage finished-order storage
     */
    public Courier(int bagCapacity, BlockingList<Order> storage) {
        this.bagCapacity = bagCapacity;
        this.storage = storage;
    }

    /**
     * Continuously loads orders, delivers them, and stops when storage is closed and empty.
     */
    @Override
    public void run() {
        try {
            while (true) {
                List<Order> orders = storage.takeMultiple(bagCapacity);
                if (orders.isEmpty()) {
                    return;
                }

                for (Order order : orders) {
                    System.out.println("[" + order.getId() + "] [LOADED]");
                    System.out.println("[" + order.getId() + "] [DELIVERING]");
                }

                Thread.sleep(totalDeliveryTime(orders));

                for (Order order : orders) {
                    System.out.println("[" + order.getId() + "] [DELIVERED]");
                }
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

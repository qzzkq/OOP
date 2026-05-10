package ru.nsu.kiryushin;

/**
 * Worker that prepares orders and places finished pizzas into storage.
 */
public class Baker implements Runnable {
    private final long speedMs;
    private final OrderDepartment orderDepartment;

    /**
     * Creates a baker worker.
     *
     * @param speedMs cooking time for a single order in milliseconds
     * @param orderDepartment department that provides and stores orders
     */
    public Baker(long speedMs, OrderDepartment orderDepartment) {
        this.speedMs = speedMs;
        this.orderDepartment = orderDepartment;
    }

    /**
     * Creates a baker worker from raw queues.
     *
     * @param speedMs cooking time for a single order in milliseconds
     * @param storage finished-order storage
     * @param orders incoming order queue
     */
    public Baker(long speedMs, BlockingList<Order> storage, BlockingList<Order> orders) {
        this(speedMs, new OrderDepartment(orders, storage));
    }

    /**
     * Continuously cooks incoming orders until the order queue is exhausted or closed.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Order order = orderDepartment.takeOrderForCooking();
                if (order == null) {
                    return;
                }

                Thread.sleep(speedMs);

                boolean stored = orderDepartment.placeCookedOrderInStorage(order);
                if (!stored) {
                    return;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

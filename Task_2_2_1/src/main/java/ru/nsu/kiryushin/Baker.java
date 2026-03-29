package ru.nsu.kiryushin;

/**
 * Worker that prepares orders and places finished pizzas into storage.
 */
public class Baker implements Runnable {
    private final long speedMs;
    private final BlockingList<Order> storage;
    private final BlockingList<Order> orders;

    /**
     * Creates a baker worker.
     *
     * @param speedMs cooking time for a single order in milliseconds
     * @param storage finished-order storage
     * @param orders incoming order queue
     */
    public Baker(long speedMs, BlockingList<Order> storage, BlockingList<Order> orders) {
        this.speedMs = speedMs;
        this.storage = storage;
        this.orders = orders;
    }

    /**
     * Continuously cooks incoming orders until the order queue is exhausted or closed.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Order order = orders.take();
                if (order == null) {
                    return;
                }

                System.out.println("[" + order.getId() + "] [COOKING]");
                Thread.sleep(speedMs);

                boolean stored = storage.put(order, () ->
                        System.out.println("[" + order.getId() + "] [WAITING_FOR_STORAGE]")
                );
                if (!stored) {
                    return;
                }

                System.out.println("[" + order.getId() + "] [IN_STORAGE]");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

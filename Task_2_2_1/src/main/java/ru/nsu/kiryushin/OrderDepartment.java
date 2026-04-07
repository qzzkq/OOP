package ru.nsu.kiryushin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Owns order queues and all order-related status output.
 */
public class OrderDepartment {
    private final PizzeriaConfig config;
    private final BlockingList<Order> orderQueue;
    private final BlockingList<Order> storage;

    /**
     * Creates an order department from the full pizzeria configuration.
     *
     * @param config simulation settings
     */
    public OrderDepartment(PizzeriaConfig config) {
        this(config, Math.max(1, config.getOrders().size()), config.getWarehouseCapacity());
    }

    /**
     * Creates an order department with explicit queue capacities.
     *
     * @param config simulation settings
     * @param orderQueueCapacity maximum number of accepted orders waiting for bakers
     * @param storageCapacity maximum number of finished orders in storage
     */
    public OrderDepartment(PizzeriaConfig config, int orderQueueCapacity, int storageCapacity) {
        this.config = config;
        this.orderQueue = new BlockingList<>(orderQueueCapacity);
        this.storage = new BlockingList<>(storageCapacity);
    }

    OrderDepartment(BlockingList<Order> orderQueue, BlockingList<Order> storage) {
        this.config = null;
        this.orderQueue = orderQueue;
        this.storage = storage;
    }

    /**
     * Creates an order department for tests or standalone worker execution.
     *
     * @param orderQueueCapacity maximum number of accepted orders waiting for bakers
     * @param storageCapacity maximum number of finished orders in storage
     */
    public OrderDepartment(int orderQueueCapacity, int storageCapacity) {
        this(null, orderQueueCapacity, storageCapacity);
    }

    /**
     * Accepts configured orders during working hours and closes the incoming queue afterwards.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void acceptOrdersDuringWorkingHours() throws InterruptedException {
        if (config == null) {
            throw new IllegalStateException("acceptOrdersDuringWorkingHours requires a pizzeria config");
        }

        List<Order> plannedOrders = new ArrayList<>(config.getOrders());
        plannedOrders.sort(Comparator.comparingLong(Order::getArrivalTimeMs));

        long openedAt = System.currentTimeMillis();
        try {
            for (Order order : plannedOrders) {
                if (order.getArrivalTimeMs() > config.getWorkDurationMs()) {
                    break;
                }

                long dueTime = openedAt + order.getArrivalTimeMs();
                long sleepTime = dueTime - System.currentTimeMillis();
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }

                submitOrder(order);
            }
        } finally {
            closeOrders();
        }
    }

    /**
     * Adds a new incoming order and prints its accepted status.
     *
     * @param order accepted order
     * @return {@code true} if the order was queued, {@code false} if the queue was closed
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public boolean submitOrder(Order order) throws InterruptedException {
        boolean accepted = orderQueue.put(order);
        if (accepted) {
            printStatus(order, "RECEIVED");
        }
        return accepted;
    }

    /**
     * Closes the incoming order queue.
     */
    public void closeOrders() {
        orderQueue.close();
    }

    /**
     * Returns the next order for a baker and prints its cooking status.
     *
     * @return the next order, or {@code null} if the queue is closed and empty
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public Order takeOrderForCooking() throws InterruptedException {
        Order order = orderQueue.take();
        if (order != null) {
            printStatus(order, "COOKING");
        }
        return order;
    }

    /**
     * Stores a finished order and prints storage-related statuses.
     *
     * @param order cooked order
     * @return {@code true} if the order was stored, {@code false} if storage was closed
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public boolean placeCookedOrderInStorage(Order order) throws InterruptedException {
        boolean stored = storage.put(order, () -> printStatus(order, "WAITING_FOR_STORAGE"));
        if (stored) {
            printStatus(order, "IN_STORAGE");
        }
        return stored;
    }

    /**
     * Returns orders for a courier trip and prints loading statuses.
     *
     * @param bagCapacity maximum number of orders in one courier trip
     * @return loaded orders, or an empty list if storage is closed and empty
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public List<Order> takeOrdersForDelivery(int bagCapacity) throws InterruptedException {
        List<Order> orders = storage.takeMultiple(bagCapacity);
        for (Order order : orders) {
            printStatus(order, "LOADED");
            printStatus(order, "DELIVERING");
        }
        return orders;
    }

    /**
     * Prints delivered statuses for the completed courier trip.
     *
     * @param orders delivered orders
     */
    public void completeDelivery(List<Order> orders) {
        for (Order order : orders) {
            printStatus(order, "DELIVERED");
        }
    }

    /**
     * Closes finished-order storage.
     */
    public void closeStorage() {
        storage.close();
    }

    private void printStatus(Order order, String status) {
        System.out.println("[" + order.getId() + "] [" + status + "]");
    }
}

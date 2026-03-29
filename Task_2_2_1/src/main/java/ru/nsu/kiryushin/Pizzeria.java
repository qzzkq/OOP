package ru.nsu.kiryushin;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Coordinates a full pizzeria workday simulation.
 */
public class Pizzeria {
    private final PizzeriaConfig config;
    private final BlockingList<Order> orderQueue;
    private final BlockingList<Order> storage;
    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();

    /**
     * Creates a simulation instance from an immutable configuration.
     *
     * @param config simulation settings
     */
    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.orderQueue = new BlockingList<>(Math.max(1, config.getOrders().size()));
        this.storage = new BlockingList<>(config.getWarehouseCapacity());
    }

    /**
     * Runs the pizzeria for one simulated workday.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public void runDay() throws InterruptedException {
        startWorkers();
        acceptOrdersDuringWorkingHours();
        waitForWorkers();
    }

    /**
     * Starts all configured bakers and couriers.
     */
    private void startWorkers() {
        int bakerIndex = 1;
        for (long speedMs : config.getBakerSpeeds()) {
            Thread bakerThread = new Thread(
                    new Baker(speedMs, storage, orderQueue),
                    "Baker-" + bakerIndex++
            );
            bakerThreads.add(bakerThread);
            bakerThread.start();
        }

        int courierIndex = 1;
        for (int bagCapacity : config.getCourierCapacities()) {
            Thread courierThread = new Thread(
                    new Courier(bagCapacity, storage),
                    "Courier-" + courierIndex++
            );
            courierThreads.add(courierThread);
            courierThread.start();
        }
    }

    /**
     * Submits orders according to their scheduled arrival times during working hours.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    private void acceptOrdersDuringWorkingHours() throws InterruptedException {
        List<Order> plannedOrders = new ArrayList<>(config.getOrders());
        plannedOrders.sort(Comparator.comparingLong(Order::getArrivalTimeMs));

        long openedAt = System.currentTimeMillis();
        for (Order order : plannedOrders) {
            if (order.getArrivalTimeMs() > config.getWorkDurationMs()) {
                break;
            }

            long dueTime = openedAt + order.getArrivalTimeMs();
            long sleepTime = dueTime - System.currentTimeMillis();
            if (sleepTime > 0) {
                Thread.sleep(sleepTime);
            }

            orderQueue.put(order);
            System.out.println("[" + order.getId() + "] [RECEIVED]");
        }

        orderQueue.close();
    }

    /**
     * Waits for bakers to finish, closes storage, and then waits for couriers.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    private void waitForWorkers() throws InterruptedException {
        joinAll(bakerThreads);
        storage.close();
        joinAll(courierThreads);
    }

    /**
     * Waits until all threads in the provided list complete.
     *
     * @param threads worker threads to join
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    private void joinAll(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
